package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
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

public abstract class  LeaderBoardByMonthChart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;
    private String kpiCode;

    public LeaderBoardByMonthChart(String kpiCode){
        super(LeaderBoardOptions.class);
        this.kpiCode = kpiCode;
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Month.toString();

        LinqWrapper<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options, this.kpiCode))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

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
                new Series("Факт", currentValues),
                new Series("План", planValues, ChartType.line),
        };

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", repository.getKpiByCode(this.kpiCode).name);

        return new ChartResult[]{new ChartResult(series, bag)};
    }
}
