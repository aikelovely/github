package ru.alfabank.dmpr.widget.cr.TTYAndTTM.chart;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График рейтинг среднего времени/процента в KPI
 */
@Service
public class TTYAndTTMRating extends BaseChart<TTYAndTTMOptions> {
    @Autowired
    TTYAndTTMRepository repository;

    public TTYAndTTMRating() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    public ChartResult[] getData(TTYAndTTMOptions options) {
        LinqWrapper<Rating> data = LinqWrapper.from(repository.getRating(options));

        ChartResult result = options.paramType == ParamType.AvgDuration
                ? createAvgDurationChart(data)
                : createPercentChart(data);

        return new ChartResult[]{result};
    }

    public ChartResult createPercentChart(LinqWrapper<Rating> data){
        Map<String, Object> bag = new HashMap<>();

        List<String> categories = data.select(new Selector<Rating, String>() {
            @Override
            public String select(Rating rating) {
                return rating.name;
            }
        }).distinct().toList();

        List<Series> series = data.group(new Selector<Rating, String>() {
            @Override
            public String select(Rating rating) {
                return rating.processName;
            }
        }).select(new Selector<Group<String, Rating>, Series>() {
            @Override
            public Series select(Group<String, Rating> group) {
                Point[] points = group.getItems().select(new Selector<Rating, Point>() {
                    @Override
                    public Point select(Rating rating) {
                        Double value = null;
                        if (rating.inKpiCount + rating.outOfKpiCount > 0) {
                            value = MathHelper.safeDivide(rating.inKpiCount,
                                    (rating.inKpiCount + rating.outOfKpiCount)) * 100;
                        }

                        Point point = Point.withY(value, rating.name);
                        if (value != null) {
                            point.color = value > rating.quotaPercent ? Color.GreenColor : Color.RedColor;
                        }
                        return point;
                    }
                }).toArray(Point.class);

                return new Series(group.getKey(), points);
            }
        }).toList();

        Rating first = data.firstOrNull();
        if(first != null){
            bag.put("quotaInDays", first.quotaInDays);
            bag.put("quotaPercent", first.quotaPercent);
        }

        bag.put("categories", categories);

        return new ChartResult(series.toArray(new Series[series.size()]), bag);
    }

    public ChartResult createAvgDurationChart(LinqWrapper<Rating> data){
        Map<String, Object> bag = new HashMap<>();
        final List<Map<String, Object>> kpis = new ArrayList<>();

        List<String> categories = data.select(new Selector<Rating, String>() {
            @Override
            public String select(Rating rating) {
                return rating.name;
            }
        }).distinct().toList();

        Series[] series = data.group(new Selector<Rating, String>() {
            @Override
            public String select(Rating rating) {
                return rating.processName;
            }
        }).select(new Selector<Group<String, Rating>, Series>() {
            @Override
            public Series select(Group<String, Rating> group) {
                Point[] points = group.getItems().select(new Selector<Rating, Point>() {
                    @Override
                    public Point select(Rating rating) {
                        Double value = rating.averageValue;
                        Point point = Point.withY(value, rating.name);
                        if (value != null) {
                            point.color = value > rating.quotaInDays ? Color.RedColor : Color.GreenColor;
                        }
                        return point;
                    }
                }).toArray(Point.class);

                double quotaInDays = group.getItems().first().quotaInDays;

                Map<String, Object> kpi = new HashMap<>();
                kpi.put("name", group.getKey());
                kpi.put("value", quotaInDays);

                kpis.add(kpi);
                return new Series(group.getKey(), points);
            }
        }).toArray(Series.class);

        bag.put("kpis", kpis);
        bag.put("categories", categories);

        return new ChartResult(series, bag);
    }
}
