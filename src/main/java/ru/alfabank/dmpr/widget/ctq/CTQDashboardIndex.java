package ru.alfabank.dmpr.widget.ctq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ctq.*;
import ru.alfabank.dmpr.repository.ctq.CTQFilterRepository;
import ru.alfabank.dmpr.repository.ctq.CTQRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Список метрик для отображения.
 */
@Service
public class CTQDashboardIndex extends BaseChart<CTQDashboardOptions> {
    @Autowired
    private CTQRepository repository;

    @Autowired
    protected CTQFilterRepository filterRepository;

    @Autowired
    private CTQWidgetHelper widgetHelper;

    public CTQDashboardIndex() {
        super(CTQDashboardOptions.class);
    }

    @Override
    public ChartResult[] getData(final CTQDashboardOptions options) {
        CTQDashboardIndexItem[] data = repository.getIndexData(options);

        final ArrayList<Series> seriesArrayList = new ArrayList<>();

        Map<String, Object> bag = new HashMap<>();

        CTQDashboardMetric[] metrics = LinqWrapper.from(data).group(new Selector<CTQDashboardIndexItem, String>() {
            @Override
            public String select(CTQDashboardIndexItem ctqDashboardIndexItem) {
                return ctqDashboardIndexItem.kpiid;
            }
        }).select(new Selector<Group<String, CTQDashboardIndexItem>, CTQDashboardMetric>() {
            @Override
            public CTQDashboardMetric select(Group<String, CTQDashboardIndexItem> ctqDashboardIndexItems) {
                CTQDashboardKPIEntity kpiEntity = filterRepository.getKPIbyId(ctqDashboardIndexItems.getKey());

                CTQDashboardIndexItem currentItem = ctqDashboardIndexItems.getItems().first(new Predicate<CTQDashboardIndexItem>() {
                    @Override
                    public boolean check(CTQDashboardIndexItem item) {
                        return item.periodOrd == 0;
                    }
                });

                CTQDashboardIndexItem prevItem = ctqDashboardIndexItems.getItems().firstOrNull(new Predicate<CTQDashboardIndexItem>() {
                    @Override
                    public boolean check(CTQDashboardIndexItem item) {
                        return item.periodOrd == -1;
                    }
                });

                CTQDashboardMetric metric = new CTQDashboardMetric();

                metric.id = kpiEntity.id;
                metric.code = kpiEntity.code;
                metric.name = kpiEntity.name;
                metric.shortName = kpiEntity.name;
                metric.normativeValue = currentItem.kpiNorm == null ? 1d : currentItem.kpiNorm;
                metric.value = currentItem.kpiRatioAvg;
                metric.numerator = currentItem.inKpiCount;
                metric.denominator = currentItem.totalCount;
                metric.qualityLevel = currentItem.kpi2RatioAvg;
                metric.qualityLevelNormative = 1d;
                metric.additionalInfo = kpiEntity.additionalInfo;

                ArrayList<Point> points = new ArrayList<>();
                points.add(widgetHelper.createCTQRichPoint(options.timeUnitId, currentItem, CTQDashboardParamType.Fact));

                if (prevItem != null) {
                    metric.prevValue = prevItem.kpiRatioAvg;
                    metric.prevQualityLevel = prevItem.kpi2RatioAvg;
                    metric.prevNumerator = prevItem.inKpiCount;
                    metric.prevDenominator = prevItem.totalCount;
                    points.add(widgetHelper.createCTQRichPoint(options.timeUnitId, prevItem, CTQDashboardParamType.Fact));
                }

                Series series = new Series(kpiEntity.code, points.toArray(new Point[points.size()]));

                Map<String, Object> seriesBag = new HashMap<>();
                seriesBag.put("kpiId", kpiEntity.id);
                series.bag = seriesBag;

                seriesArrayList.add(series);

                return metric;
            }
        }).toArray(CTQDashboardMetric.class);

        bag.put("metrics", metrics);

        return new ChartResult[]{new ChartResult(seriesArrayList.toArray(new Series[seriesArrayList.size()]), bag)};
    }
}

