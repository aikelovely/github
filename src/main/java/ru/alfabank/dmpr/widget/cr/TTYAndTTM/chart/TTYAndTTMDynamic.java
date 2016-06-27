package ru.alfabank.dmpr.widget.cr.TTYAndTTM.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Dynamic;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График динамике среднего значения/процента в KPI.
 */
@Service
public class TTYAndTTMDynamic extends BaseChart<TTYAndTTMOptions> {
    @Autowired
    TTYAndTTMRepository repository;

    public TTYAndTTMDynamic() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    public ChartResult[] getData(TTYAndTTMOptions options) {
        LinqWrapper<Dynamic> data = LinqWrapper.from(repository.getDynamic(options));

        return options.paramType == ParamType.AvgDuration
                ? createAvgDurationCharts(data)
                : createPercentCharts(data);
    }

    public ChartResult[] createPercentCharts(LinqWrapper<Dynamic> data) {
        return data.group(new Selector<Dynamic, Pair<Integer, String>>() {
            @Override
            public Pair<Integer, String> select(Dynamic dynamic) {
                return Pair.of(dynamic.processId, dynamic.processName);
            }
        }).select(new Selector<Group<Pair<Integer, String>, Dynamic>, ChartResult>() {
            @Override
            public ChartResult select(Group<Pair<Integer, String>, Dynamic> group) {
                Map<String, Object> bag = new HashMap<>();
                Pair<Integer, String> key = group.getKey();

                final Dynamic summation = new Dynamic();
                summation.inKpiCount = 0;
                summation.outOfKpiCount = 0;

                Point[] points = group.getItems().select(new Selector<Dynamic, Point>() {
                    @Override
                    public Point select(Dynamic dynamic) {
                        Double value = null;
                        if (dynamic.inKpiCount + dynamic.outOfKpiCount > 0) {
                            value = MathHelper.safeDivide(dynamic.inKpiCount,
                                    (dynamic.inKpiCount + dynamic.outOfKpiCount)) * 100;

                            summation.inKpiCount += dynamic.inKpiCount;
                            summation.outOfKpiCount += dynamic.outOfKpiCount;
                        }

                        Point point = new Point(dynamic.calcDate, value);
                        if (value != null) {
                            point.color = value > dynamic.quotaPercent ? Color.GreenColor : Color.RedColor;
                        }

                        return point;
                    }
                }).toArray(Point.class);

                Dynamic first = group.getItems().firstOrNull();
                if(first != null){
                    bag.put("quotaInDays", first.quotaInDays);
                    bag.put("quotaPercent", first.quotaPercent);
                }

                Series series = new Series("Показатель", points);

                bag.put("processId", key.getLeft());
                bag.put("average", MathHelper.safeDivide(summation.inKpiCount,
                        summation.inKpiCount + summation.outOfKpiCount) * 100);

                return new ChartResult(new Series[]{series, TrendLine.create(points, 100d)}, bag);
            }
        }).toArray(ChartResult.class);
    }

    public ChartResult[] createAvgDurationCharts(LinqWrapper<Dynamic> data) {
        return data.group(new Selector<Dynamic, Pair<Integer, String>>() {
            @Override
            public Pair<Integer, String> select(Dynamic dynamic) {
                return Pair.of(dynamic.processId, dynamic.processName);
            }
        }).select(new Selector<Group<Pair<Integer, String>, Dynamic>, ChartResult>() {
            @Override
            public ChartResult select(Group<Pair<Integer, String>, Dynamic> group) {
                Map<String, Object> bag = new HashMap<>();
                Pair<Integer, String> key = group.getKey();

                final Dynamic summation = new Dynamic();
                summation.ttxDuration = 0;
                summation.dealCount = 0;

                Point[] points = group.getItems().select(new Selector<Dynamic, Point>() {
                    @Override
                    public Point select(Dynamic dynamic) {
                        Point point = new Point(dynamic.calcDate, dynamic.averageValue);
                        if (dynamic.averageValue != null) {
                            point.color = dynamic.averageValue > dynamic.quotaInDays
                                    ? Color.RedColor : Color.GreenColor;
                        }

                        summation.ttxDuration += dynamic.ttxDuration;
                        summation.dealCount += dynamic.dealCount;

                        return point;
                    }
                }).toArray(Point.class);

                Dynamic first = group.getItems().firstOrNull();
                if (first != null) {
                    bag.put("quotaInDays", first.quotaInDays);
                    bag.put("quotaPercent", first.quotaPercent);
                }

                Series series = new Series("Показатель", points);

                bag.put("processId", key.getLeft());
                bag.put("average", MathHelper.safeDivide(summation.ttxDuration, summation.dealCount));

                return new ChartResult(new Series[]{series, TrendLine.create(points, null)}, bag);
            }
        }).toArray(ChartResult.class);
    }
}
