package ru.alfabank.dmpr.widget.cr.ClientTime.chart;

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

import java.util.HashMap;
import java.util.Map;

/**
 * График динамика среднего времеми/доли заявок в KPI.
 */
@Service
public class ClientTimeDynamicWithPie extends BaseChart<ClientTimeOptions> {
    @Autowired
    ClientTimeRepository repository;

    public ClientTimeDynamicWithPie() {
        super(ClientTimeOptions.class);
    }

    @Override
    public ChartResult[] getData(final ClientTimeOptions options) {
        LinqWrapper<KpiDataItem> data = LinqWrapper.from(repository.getKpiData(options));

        // Если processId задан (это только при drillDown), то фильтруем по нему
        if(options.processId != null){
            data = data.filter(new Predicate<KpiDataItem>() {
                @Override
                public boolean check(KpiDataItem item) {
                    return item.processId == options.processId;
                }
            });
        }

        return options.paramType == ParamType.AvgDuration
                ? createAvgDurationCharts(data)
                : createPercentCharts(data);
    }

    public ChartResult[] createPercentCharts(LinqWrapper<KpiDataItem> data) {
        return data.group(new Selector<KpiDataItem, Integer>() {
            @Override
            public Integer select(KpiDataItem item) {
                return item.processId;
            }
        }).select(new Selector<Group<Integer, KpiDataItem>, ChartResult>() {
            @Override
            public ChartResult select(Group<Integer, KpiDataItem> group) {
                Map<String, Object> bag = new HashMap<>();

                final KpiDataItem summation = new KpiDataItem();
                summation.inKpiBpCount = 0;
                summation.bpCount = 0;

                Point[] points = group.getItems().select(new Selector<KpiDataItem, Point>() {
                    @Override
                    public Point select(KpiDataItem item) {
                        summation.inKpiBpCount += item.inKpiBpCount;
                        summation.bpCount += item.bpCount;

                        double value = MathHelper.safeDivide(item.inKpiBpCount, item.bpCount) * 100;
                        Point point = new Point(item.calcDate, value);
                        point.color = value >= item.quotaPercent ? Color.GreenColor : Color.RedColor;

                        return point;
                    }
                }).toArray(Point.class);

                KpiDataItem first = group.getItems().firstOrNull();
                if (first != null) {
                    bag.put("quotaInDays", first.quotaInDays);
                    bag.put("quotaPercent", first.quotaPercent);
                }

                Series series = new Series("Показатель", points);

                bag.put("processId", group.getKey());
                bag.put("average", MathHelper.safeDivide(summation.inKpiBpCount, summation.bpCount) * 100);

                return new ChartResult(new Series[]{
                        series,
                        createPie(summation.inKpiBpCount, summation.bpCount - summation.inKpiBpCount)},
                        bag);
            }
        }).toArray(ChartResult.class);
    }

    public ChartResult[] createAvgDurationCharts(LinqWrapper<KpiDataItem> data) {
        return data.group(new Selector<KpiDataItem, Integer>() {
            @Override
            public Integer select(KpiDataItem item) {
                return item.processId;
            }
        }).select(new Selector<Group<Integer, KpiDataItem>, ChartResult>() {
            @Override
            public ChartResult select(Group<Integer, KpiDataItem> group) {
                Map<String, Object> bag = new HashMap<>();

                final KpiDataItem summation = new KpiDataItem();
                summation.inKpiBpCount = 0;
                summation.bpCount = 0;
                summation.totalDuration = 0;

                Point[] points = group.getItems().select(new Selector<KpiDataItem, Point>() {
                    @Override
                    public Point select(KpiDataItem item) {

                        double value = MathHelper.safeDivide(item.totalDuration, item.bpCount);
                        Point point = new Point(item.calcDate, value);
                        point.color = value > item.quotaInDays ? Color.RedColor : Color.GreenColor;

                        summation.totalDuration += item.totalDuration;
                        summation.inKpiBpCount += item.inKpiBpCount;
                        summation.bpCount += item.bpCount;

                        return point;
                    }
                }).toArray(Point.class);

                KpiDataItem first = group.getItems().firstOrNull();
                if (first != null) {
                    bag.put("quotaInDays", first.quotaInDays);
                    bag.put("quotaPercent", first.quotaPercent);
                }

                Series series = new Series("Средняя длительность", points);

                bag.put("processId", group.getKey());
                bag.put("average", MathHelper.safeDivide(summation.totalDuration, summation.bpCount));

                return new ChartResult(new Series[]{
                        series,
                        createPie(summation.inKpiBpCount, summation.bpCount - summation.inKpiBpCount)},
                        bag);
            }
        }).toArray(ChartResult.class);
    }

    private Series createPie(int totalInKpi, int totalNotInKpi) {
        return new Series("Pie", new Point[]{
                Point.withY(totalInKpi, "В KPI", Color.GreenColor),
                Point.withY(totalNotInKpi, "За пределами KPI", Color.RedColor)
        }, ChartType.pie);
    }
}
