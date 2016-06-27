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
 * График "Распределение заявок по группам длительности"
 */
@Service
public class PILAndCCRequestByGroup extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCRequestByGroup() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiDataItem[] data = repository.getRequestByGroup(options);

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
                        return createChart(items);
                    }
                })
                .toArray(ChartResult.class);
    }

    public ChartResult createChart(Group<Integer, KpiDataItem> items) {
        Map<String, Object> bag = new HashMap<>();

        final Map<Long, KpiDataItem> valueMap = items.getItems()
                .toMap(new Selector<KpiDataItem, Long>() {
                    @Override
                    public Long select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.valueId;
                    }
                });

        final List<Point> piePoints = new ArrayList<>();

        List<Series> series = items.getItems()
                .group(new Selector<KpiDataItem, Long>() {
                    @Override
                    public Long select(KpiDataItem item) {
                        return item.valueId;
                    }
                })
                .sort(new Selector<Group<Long, KpiDataItem>, Long>() {
                    @Override
                    public Long select(Group<Long, KpiDataItem> group) {
                        return group.getKey();
                    }
                })
                .select(new Selector<Group<Long, KpiDataItem>, Series>() {
                    @Override
                    public Series select(Group<Long, KpiDataItem> group) {
                        Point[] points = group.getItems()
                                .sort(new Selector<KpiDataItem, LocalDate>() {
                                    @Override
                                    public LocalDate select(KpiDataItem kpiDataItem) {
                                        return kpiDataItem.calcDate;
                                    }
                                })
                                .select(new Selector<KpiDataItem, Point>() {
                                    @Override
                                    public Point select(KpiDataItem item) {
                                        return new Point(item.calcDate, item.count);
                                    }
                                })
                                .toArray(Point.class);

                        String valueName = valueMap.get(group.getKey()).valueName;

                        double countByGroup = group.getItems()
                                .sum(new Selector<KpiDataItem, Double>() {
                                    @Override
                                    public Double select(KpiDataItem kpiDataItem) {
                                        return kpiDataItem.count;
                                    }
                                });

                        piePoints.add(Point.withY(countByGroup, valueName));

                        return new Series(valueName, points, ChartType.column);
                    }
                })
                .toList();

        series.add(new Series("Pie",
                piePoints.toArray(new Point[piePoints.size()]),
                ChartType.pie));

        bag.put("reportTypeId", items.getKey());
        return new ChartResult(series.toArray(new Series[series.size()]), bag);
    }
}
