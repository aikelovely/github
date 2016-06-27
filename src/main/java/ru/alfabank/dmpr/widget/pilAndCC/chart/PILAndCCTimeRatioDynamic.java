package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.ChartType;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiDurationDynamic;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График "Динамика изменения доли каждого этапа в процессе"
 */
@Service
public class PILAndCCTimeRatioDynamic extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCTimeRatioDynamic() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(PILAndCCOptions options) {
        KpiDurationDynamic[] data = repository.getTimeRatioDynamic(options);

        return LinqWrapper.from(data)
                .group(new Selector<KpiDurationDynamic, Integer>() {
                    @Override
                    public Integer select(KpiDurationDynamic kpiDataItem) {
                        return kpiDataItem.reportTypeId;
                    }
                })
                .select(new Selector<Group<Integer, KpiDurationDynamic>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Integer, KpiDurationDynamic> items) {
                        return createTimeRatioDuration(items);
                    }
                })
                .toArray(ChartResult.class);
    }

    private ChartResult createTimeRatioDuration(Group<Integer, KpiDurationDynamic> items) {

        List<Series> series = items.getItems().group(new Selector<KpiDurationDynamic, Long>() {
            @Override
            public Long select(KpiDurationDynamic kpiDataItem) {
                return kpiDataItem.operationId;
            }
        }).select(new Selector<Group<Long, KpiDurationDynamic>, Series>() {
            @Override
            public Series select(Group<Long, KpiDurationDynamic> items) {
                return createColumnSeries(items.getItems());
            }
        }).toList();

        series.add(createPieSeries(items.getItems()));

        Map<String, Object> bag = new HashMap<>();

        bag.put("reportTypeId", items.getKey());

        return new ChartResult(series.toArray(new Series[]{}), bag);
    }

    private Series createColumnSeries(LinqWrapper<KpiDurationDynamic> items) {
        LinqWrapper<Point> data = items.sort(new Selector<KpiDurationDynamic, Comparable>() {
            @Override
            public Comparable select(KpiDurationDynamic item) {
                return item.calcDate;
            }
        }).select(new Selector<KpiDurationDynamic, Point>() {
            @Override
            public Point select(KpiDurationDynamic kDD) {
                return new Point(kDD.calcDate, kDD.value);
            }
        });

        String name = items.first().operationName;

        return new Series(name, data.toArray(Point.class), ChartType.column);
    }

    private Series createPieSeries(LinqWrapper<KpiDurationDynamic> items){

        final double fullDuration = items.group(new Selector<KpiDurationDynamic, LocalDate>() {
            @Override
            public LocalDate select(KpiDurationDynamic kpiDurationDynamic) {
                return kpiDurationDynamic.calcDate;
            }
        }).sum(new Selector<Group<LocalDate, KpiDurationDynamic>, Double>() {
            @Override
            public Double select(Group<LocalDate, KpiDurationDynamic> kpiDurationDynamics) {
                return kpiDurationDynamics.getItems().first().groupDuration;
            }
        });

        Point[] data = items.group(new Selector<KpiDurationDynamic, Long>() {
            @Override
            public Long select(KpiDurationDynamic kpiDataItem) {
                return kpiDataItem.operationId;
            }
        }).select(new Selector<Group<Long,KpiDurationDynamic>, Point>() {
            @Override
            public Point select(Group<Long, KpiDurationDynamic> kpiDurationDynamics) {
                double sum = kpiDurationDynamics.getItems().sum(new Selector<KpiDurationDynamic, Double>() {
                    @Override
                    public Double select(KpiDurationDynamic kpiDurationDynamic) {
                        return kpiDurationDynamic.groupDuration*(kpiDurationDynamic.value/100);
                    }
                });

                return Point.withY((sum/fullDuration)*100, kpiDurationDynamics.getItems().first().operationName);
            }
        }).toArray(Point.class);

        return new Series("Pie", data, ChartType.pie);
    }
}