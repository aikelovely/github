package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiDataItem;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Верхняя динамика.
 */
@Service
public class PILAndCCAvgDynamic extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCAvgDynamic() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiDataItem[] data = repository.getAvgDynamic(options);

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
                        return options.valueTypeId == 1
                                ? createAvgDuration(items)
                                : createPercent(items);
                    }
                })
                .toArray(ChartResult.class);
    }

    private ChartResult createAvgDuration(Group<Integer, KpiDataItem> items) {
        class RichPoint extends Point {
            public double seconds;

            public RichPoint(LocalDate x, double seconds) {
                super(x, seconds / 3600);
                this.seconds = seconds;
            }
        }

        Series column = new Series("Средняя длительность", items.getItems()
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                })
                .select(new Selector<KpiDataItem, Point>() {
                    @Override
                    public RichPoint select(KpiDataItem item) {
                        return new RichPoint(item.calcDate, item.value);
                    }
                })
                .toArray(Point.class));

        double avgDuration = items.getItems()
                .sum(new Selector<KpiDataItem, Double>() {
                    @Override
                    public Double select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.value;
                    }
                }) / (3600 * items.getItems().count());

        Map<String, Object> bag = new HashMap<>();

        bag.put("reportTypeId", items.getKey());
        bag.put("averageValue", avgDuration);

        return new ChartResult(new Series[]{column}, bag);
    }

    private ChartResult createPercent(Group<Integer, KpiDataItem> items) {
        List<Point> inKpiPoints = new ArrayList<>();
        List<Point> outOfKpiPoints = new ArrayList<>();
        double totalInKpi = 0;
        double totalOutOfKpi = 0;

        LinqWrapper<KpiDataItem> sortedData = items.getItems()
                .sort(new Selector<KpiDataItem, Comparable>() {
                    @Override
                    public Comparable select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

        for (KpiDataItem item : sortedData) {
            totalInKpi += item.inQuotaCount;
            double outOfKPICount = item.count - item.inQuotaCount;
            totalOutOfKpi += outOfKPICount;

            inKpiPoints.add(new Point(item.calcDate, item.inQuotaCount));
            outOfKpiPoints.add(new Point(item.calcDate, outOfKPICount));
        }

        KpiDataItem firstItem = items.getItems().firstOrNull();
        double avgPercent = MathHelper.safeDivide(totalInKpi, totalOutOfKpi) * 100;

        Map<String, Object> bag = new HashMap<>();
        bag.put("reportTypeId", items.getKey());
        bag.put("averageValue", avgPercent);
        bag.put("planValue", firstItem == null ? 0 : firstItem.planValue);

        return new ChartResult(new Series[]{
                new Series("За пределеми KPI",
                        outOfKpiPoints.toArray(new Point[outOfKpiPoints.size()]),
                        ChartType.column, Color.RedColor),
                new Series("В KPI",
                        inKpiPoints.toArray(new Point[inKpiPoints.size()]),
                        ChartType.column, Color.GreenColor),
                new Series("Pie",
                        new Point[]{
                            Point.withY(totalInKpi, "В KPI", Color.GreenColor),
                            Point.withY(totalOutOfKpi, "За пределеми KPI", Color.RedColor),
                        }, ChartType.pie)
        }, bag);
    }
}
