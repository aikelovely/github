package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.pilAndCC.KpiRating;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Рейтинг "Топ N худших/лучших"
 */
@Service
public class PILAndCCRating extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCRating() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        List<ChartResult> charts = createCharts(options, true);

        charts.addAll(createCharts(options, false));

        return charts.toArray(new ChartResult[charts.size()]);
    }

    private List<ChartResult> createCharts(final PILAndCCOptions options, final boolean isTop){
        final List<ChartResult> charts = new ArrayList<>();
        options.sortOrderId = isTop ? 2 : 1;

        LinqWrapper.from(repository.getRating(options))
                .group(new Selector<KpiRating, Integer>() {
                    @Override
                    public Integer select(KpiRating rating) {
                        return rating.reportTypeId;
                    }
                })
                .each(new Action<Group<Integer, KpiRating>>() {
                    @Override
                    public void act(Group<Integer, KpiRating> data) {
                        ChartResult chart;
                        if (options.valueTypeId == 1) {
                            chart = createAvgDuration(data);
                        } else {
                            chart = createPercent(data);
                        }
                        chart.bag.put("isTop", isTop);
                        charts.add(chart);
                    }
                });

        return charts;
    }

    private ChartResult createPercent(Group<Integer, KpiRating> groupByReportType) {
        LinqWrapper<KpiRating> data = groupByReportType.getItems()
                .sort(new Selector<KpiRating, Comparable>() {
                    @Override
                    public Comparable select(KpiRating rating) {
                        return rating.sortOrder;
                    }
                });

        List<String> categories = data
                .select(new Selector<KpiRating, String>() {
                    @Override
                    public String select(KpiRating rating) {
                        return rating.valueName;
                    }
                }).toList();

        Point[] points = data
                .select(new Selector<KpiRating, Point>() {
                    @Override
                    public Point select(KpiRating rating) {
                        return Point.withY(rating.value);
                    }
                })
                .toArray(Point.class);

        Series series = new Series("", points);

        Map<String, Object> bag = new HashMap<>();
        bag.put("reportTypeId", groupByReportType.getKey());
        bag.put("categories", categories);
        return new ChartResult(new Series[]{series}, bag);
    }

    private ChartResult createAvgDuration(Group<Integer, KpiRating> groupByReportType) {
        final Map<Integer, KpiRating> sortOrderMap = groupByReportType
                .getItems()
                .toMap(new Selector<KpiRating, Integer>() {
                    @Override
                    public Integer select(KpiRating rating) {
                        return rating.sortOrder;
                    }
                });

        final LinqWrapper<Group<Integer, KpiRating>> groupBySortOrder = groupByReportType
                .getItems()
                .group(new Selector<KpiRating, Integer>() {
                    @Override
                    public Integer select(KpiRating rating) {
                        return rating.sortOrder;
                    }
                }).sort(new Selector<Group<Integer, KpiRating>, Comparable>() {
                    @Override
                    public Comparable select(Group<Integer, KpiRating> group) {
                        return group.getKey();
                    }
                });

        List<String> categories = groupBySortOrder
                .select(new Selector<Group<Integer, KpiRating>, String>() {
                    @Override
                    public String select(Group<Integer, KpiRating> group) {
                        return sortOrderMap.get(group.getKey()).valueName;
                    }
                }).toList();

        Series[] series = groupByReportType.getItems()
                .group(new Selector<KpiRating, String>() {
                    @Override
                    public String select(KpiRating rating) {
                        return rating.operationName;
                    }
                })
                .select(new Selector<Group<String, KpiRating>, Series>() {
                    @Override
                    public Series select(final Group<String, KpiRating> groupByOperation) {
                        Point[] points = groupBySortOrder
                                .select(new Selector<Group<Integer, KpiRating>, Point>() {
                                    @Override
                                    public Point select(final Group<Integer, KpiRating> group) {
                                        KpiRating currentValue = groupByOperation.getItems()
                                                .firstOrNull(new Predicate<KpiRating>() {
                                                    @Override
                                                    public boolean check(KpiRating item) {
                                                        return item.sortOrder == group.getKey();
                                                    }
                                                });

                                        if (currentValue == null) return null;

                                        return Point.withY(currentValue.value / 3600);
                                    }
                                })
                                .toArray(Point.class);

                        return new Series(groupByOperation.getKey(), points);
                    }
                })
                .toArray(Series.class);


        Map<String, Object> bag = new HashMap<>();
        bag.put("reportTypeId", groupByReportType.getKey());
        bag.put("categories", categories);
        return new ChartResult(series, bag);
    }
}
