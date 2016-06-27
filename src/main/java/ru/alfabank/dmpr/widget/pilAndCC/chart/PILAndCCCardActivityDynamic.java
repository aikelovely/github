package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiDataItem;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "% активированных карт"
 */
@Service
public class PILAndCCCardActivityDynamic extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCCardActivityDynamic() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiDataItem[] data = repository.getCardActivityDynamic(options);

        return LinqWrapper.from(data)
                .group(new Selector<KpiDataItem, Integer>() {
                    @Override
                    public Integer select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.reportTypeId;
                    }
                })
                .select(new Selector<Group<Integer, KpiDataItem>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Integer, KpiDataItem> items) {
                        return createCardActivityDynamic(items);
                    }
                })
                .toArray(ChartResult.class);
    }

    private ChartResult createCardActivityDynamic(Group<Integer, KpiDataItem> items) {

        Series[] series = new Series[]{ createColumnSeries(items.getItems()), createPieSeries(items.getItems()) };

        Map<String, Object> bag = new HashMap<>();

        bag.put("normative", items.getItems().first().planValue);
        bag.put("reportTypeId", items.getKey());

        return new ChartResult(series, bag);
    }

    private Series createColumnSeries(LinqWrapper<KpiDataItem> items){
        return new Series("Процент активированных карт", items.sort(new Selector<KpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(KpiDataItem item) {
                return item.calcDate;
            }
        }).select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                Point p = new Point(kpiDataItem.calcDate, kpiDataItem.value);

                p.color = kpiDataItem.value > kpiDataItem.planValue ? Color.GreenColor : Color.RedColor;
                p.tag = "Количество активированных карт: <b>" + Math.round(kpiDataItem.count*(kpiDataItem.value/100)) +
                        "</b><br/>Количество выданных  карт: <b>" + Math.round(kpiDataItem.count) +
                        "</b>";

                return p;
            }
        }).toArray(Point.class), ChartType.column);
    }

    private Series createPieSeries(LinqWrapper<KpiDataItem> items){
        double fullActivatedCount = items.sum(new Selector<KpiDataItem, Double>() {
            @Override
            public Double select(KpiDataItem kpiDataItem) {
                return kpiDataItem.count*(kpiDataItem.value/100);
            }
        });

        double fullCount = items.sum(new Selector<KpiDataItem, Double>() {
            @Override
            public Double select(KpiDataItem kpiDataItem) {
                return kpiDataItem.count;
            }
        });

        return new Series("Pie", new Point[]{
                Point.withY(Math.round(fullActivatedCount), "Активированных карт", Color.GreenColor),
                Point.withY(Math.round(fullCount - fullActivatedCount), "Неактивированных карт", Color.RedColor)
        }, ChartType.pie);
    }
}
