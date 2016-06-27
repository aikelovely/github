package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.ChartType;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Стабильность персонала" KPIOB~1.<br/>
 * На график в разрезе месяцев выводится всегда 12 месяцев без указания года. <br/>
 * В гистограмме выводится 3 столбика: 1) факт за предыдущий год 2) факт за этот год 3) план на этот год. <br/>
 * Параметр <i>divisionGroupId</i> - "ОБ"
 */
@Service
public class LeaderBoardKpi1Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi1Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Month.toString();

        LinqWrapper<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options, "KPIOB~1"))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

        Point[] prevValues = data.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                if(kpiDataItem.prevValue == null) return null;
                return Point.withY(kpiDataItem.prevValue * 100);
            }
        }).toArray(Point.class);

        Point[] currentValues = data.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                if(kpiDataItem.currentValue == null) return null;
                return Point.withY(kpiDataItem.currentValue * 100);
            }
        }).toArray(Point.class);

        Point[] planValues = data.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                if(kpiDataItem.planValue == null) return null;
                return Point.withY(kpiDataItem.planValue * 100);
            }
        }).toArray(Point.class);

        Series[] series = new Series[]{
                new Series("Факт за предыдущий год", prevValues),
                new Series("Факт", currentValues),
                new Series("План", planValues, ChartType.line),
        };

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", repository.getKpiByCode("KPIOB~1").name);

        return new ChartResult[]{new ChartResult(series, bag)};
    }
}
