package ru.alfabank.dmpr.widget.cr.ClientTime.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDataItem;
import ru.alfabank.dmpr.repository.cr.ClientTimeRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График "Группировка заявок по длительности"
 */
@Service
public class ClientTimeDynamicRequestByGroup extends BaseChart<ClientTimeOptions> {
    @Autowired
    ClientTimeRepository repository;

    public ClientTimeDynamicRequestByGroup() {
        super(ClientTimeOptions.class);
    }

    @Override
    public ChartResult[] getData(final ClientTimeOptions options) {
        options.systemUnitId = 4;

        class RichPoint extends Point {
            public int bpCount;

            public RichPoint(KpiDataItem item) {
                super(item.calcDate, MathHelper.safeDivide(item.totalDuration, item.bpCount));
                this.bpCount = item.bpCount;
            }
        }

        final List<Point> piePoints = new ArrayList<>();

        List<Series> series = LinqWrapper.from(repository.getKpiData(options))
                .filter(new Predicate<KpiDataItem>() {
                    @Override
                    public boolean check(KpiDataItem item) {
                        return item.processId == options.processId;
                    }
                })
                .group(new Selector<KpiDataItem, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(KpiDataItem item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, KpiDataItem>, Series>() {
                    @Override
                    public Series select(Group<Pair<String, String>, KpiDataItem> groupByCode) {
                        final Pair<String, String> key = groupByCode.getKey();

                        final KpiDataItem summation = new KpiDataItem();
                        summation.totalDuration = 0;
                        summation.bpCount = 0;

                        Point[] points = groupByCode.getItems().select(new Selector<KpiDataItem, Point>() {
                            @Override
                            public Point select(KpiDataItem item) {
                                summation.totalDuration += item.totalDuration;
                                summation.bpCount += item.bpCount;

                                return new RichPoint(item);
                            }
                        }).toArray(Point.class);

                        Point piePoint = Point.withY(
                                MathHelper.safeDivide(summation.totalDuration, summation.bpCount),
                                key.getRight(),
                                getColorByUnitCode(key.getLeft()));
                        piePoints.add(piePoint);

                        return new Series(key.getRight(), points, ChartType.column, getColorByUnitCode(key.getLeft()));
                    }
                })
                .toList();

        Map<String, Object> bag = new HashMap<>();

        series.add(new Series("Pie", piePoints.toArray(new Point[piePoints.size()]), ChartType.pie));

        return new ChartResult[]{new ChartResult(series.toArray(new Series[series.size()]), bag)};
    }

    private Color getColorByUnitCode(String unitCode) {
        switch (unitCode) {
            case "3":
                return Color.valueOf(144, 237, 125);
            case "7":
                return Color.OrangeColor;
            case "0":
                return Color.RedColor;
            default:
                return null;
        }
    }
}
