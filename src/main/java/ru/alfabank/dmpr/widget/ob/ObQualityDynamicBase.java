package ru.alfabank.dmpr.widget.ob;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.ob.*;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;
import ru.alfabank.dmpr.repository.ob.ObQualityRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Базовый класс для динамики.
 */
public abstract class ObQualityDynamicBase extends BaseChart<ObQualityOptions> {
    @Autowired
    private ObQualityRepository repository;

    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObQualityDynamicBase() {
        super(ObQualityOptions.class);
    }

    protected String FormatCount(double value){
        NumberFormat n = NumberFormat.getNumberInstance(Locale.FRENCH);
        return n.format(Math.round(value));
    }

    protected String FormatRow(String name, String value){
        return "<tr><td>" + name + "</td><td>" + value + "</td></tr>";
    }

    protected abstract ObQualityQueryOptions getQueryOptions(ObQualityOptions options);

    @Override
    public ChartResult[] getData(final ObQualityOptions options) {
        ObQualityQueryOptions queryOptions = getQueryOptions(options);
        ObQualityDataItem[] items = repository.getDynamicData(queryOptions);

        final Selector<ObQualityDataItem, Double> selectValue = new Selector<ObQualityDataItem, Double>() {
            @Override
            public Double select(ObQualityDataItem item) {
                return options.kpiId != null ? item.kpiRatioAvg : item.getQualityLevel();
            }
        };

        final Selector<ObQualityDataItem, Double> selectNormative = new Selector<ObQualityDataItem, Double>() {
            @Override
            public Double select(ObQualityDataItem item) {
                return options.kpiId != null ? item.getNormative() : item.getQualityLevelNormative();
            }
        };

        Map<String, Object> bag = new HashMap<>();

        ObQualityDataItem first = items.length > 0 ? items[0] : new ObQualityDataItem();
        final double normative = selectNormative.select(first);

        bag.put("normative", normative * 100);

        Point[] points = LinqWrapper.from(items).select(new Selector<ObQualityDataItem, Point>() {
            @Override
            public Point select(ObQualityDataItem item) {
                Double value = selectValue.select(item);
                ObRichPoint p = new ObRichPoint(item.timeUnitDD, value * 100);
                p.color = value >= normative ? Color.DarkGreenColor : Color.SuperRedColor;
                p.customHTMLTooltip = "<table class='dynamic-tooltip-table'>";

                PeriodSelectOption pSOptions;
                if (options.timeUnitId == Period.week.getValue()) {
                    pSOptions = PeriodSelectHelper.getWeekByYearAndNum(item.timeUnitYear, item.timeUnitPrdNum, filterRepository.getWeeks());
                } else {
                    pSOptions = PeriodSelectHelper.getMonthById(item.timeUnitYear, item.timeUnitPrdNum - 1);
                }
                p.periodName = pSOptions.name;
                p.periodNum = pSOptions.periodNum;
                p.periodId = pSOptions.id;

                p.customHTMLTooltip += p.periodName;

                if (options.kpiId == null) {
                    p.customHTMLTooltip += FormatRow("Уровень качества", new DecimalFormat("#.##").format(value * 100) + "%") +
                            FormatRow("Цель", new DecimalFormat("#.##").format(item.getNormative() * 100) + "%");

                } else {

                    p.customHTMLTooltip += FormatRow("Факт", new DecimalFormat("#.##").format(value * 100) + "%") +
                            FormatRow("Общее количество", FormatCount(item.totalCount)) +
                            FormatRow("Количество успешных", FormatCount(item.inKpiCount));
                }
                p.customHTMLTooltip += "</table>";
                return p;
            }
        }).toArray(Point.class);

        return new ChartResult[]{new ChartResult(new Series[]{new Series(points)}, bag)};
    }
}
