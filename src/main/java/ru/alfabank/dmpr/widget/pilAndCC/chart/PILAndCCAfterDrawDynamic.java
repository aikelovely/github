package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiAfterDrawDynamic;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Процент дооформлений УИУК" / "Процент дооформлений (после проверки на конвейере)"
 */
@Service
public class PILAndCCAfterDrawDynamic extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCAfterDrawDynamic() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiAfterDrawDynamic[] data = repository.getAfterDrawDynamic(options);

        return LinqWrapper.from(data)
                .group(new Selector<KpiAfterDrawDynamic, Integer>() {
                    @Override
                    public Integer select(KpiAfterDrawDynamic kpiAfterDrawDynamic) {
                        return kpiAfterDrawDynamic.reportTypeId;
                    }
                })
                .select(new Selector<Group<Integer, KpiAfterDrawDynamic>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Integer, KpiAfterDrawDynamic> items) {
                        return createChartResult(items);
                    }
                })
                .toArray(ChartResult.class);
    }

    private ChartResult createChartResult(Group<Integer, KpiAfterDrawDynamic> group){
        LinqWrapper<KpiAfterDrawDynamic> items = group.getItems();

        LinqWrapper<Point> columnPoints = items.sort(new Selector<KpiAfterDrawDynamic, Comparable>() {
            @Override
            public Comparable select(KpiAfterDrawDynamic kpiAfterDrawDynamic) {
                return kpiAfterDrawDynamic.calcDate;
            }
        }).select(new Selector<KpiAfterDrawDynamic, Point>() {
            @Override
            public Point select(KpiAfterDrawDynamic kpiAfterDrawDynamic) {
                Point p = new Point(kpiAfterDrawDynamic.calcDate, kpiAfterDrawDynamic.value);
                p.tag = "<b>" + kpiAfterDrawDynamic.value + "%</b><br/>Количество дооформлений: <b>"
                        + Math.round(kpiAfterDrawDynamic.decCount) + "</b><br/>Всего этапов: <b>" +
                        Math.round(kpiAfterDrawDynamic.count) + "</b>";
                return p;
            }
        });

        double fullCount = items.sum(new Selector<KpiAfterDrawDynamic, Double>() {
            @Override
            public Double select(KpiAfterDrawDynamic kpiAfterDrawDynamic) {
                return kpiAfterDrawDynamic.count;
            }
        });

        double decCount = items.sum(new Selector<KpiAfterDrawDynamic, Double>() {
            @Override
            public Double select(KpiAfterDrawDynamic kpiAfterDrawDynamic) {
                return kpiAfterDrawDynamic.decCount;
            }
        });

        Map<String, Object> bag = new HashMap<>();

        bag.put("reportTypeId", group.getKey());

        return new ChartResult(new Series[]{ new Series("Процент дооформлений", columnPoints.toArray(Point.class), ChartType.column),
        new Series("Pie", new Point[]{
                Point.withY(decCount, "Дооформления", Color.RedColor),
                Point.withY(fullCount - decCount, "Остальные этапы", Color.GreenColor)
        }, ChartType.pie)}, bag);
    }
}
