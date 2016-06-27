package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LeaderBoardQuarterDynamicBase extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;
    private String kpiCode;

    public LeaderBoardQuarterDynamicBase(String kpiCode) {
        super(LeaderBoardOptions.class);
        this.kpiCode = kpiCode;
    }

    protected double getMultiplier(){
        return 100;
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Quarter.toString();

        LinqWrapper<KpiDataItem> kpiDataItems = LinqWrapper
                .from(repository.getKpiData(options, kpiCode))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

        final List<LocalDate> categories = kpiDataItems.select(new Selector<KpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(KpiDataItem item) {
                return item.calcDate;
            }
        }).distinct().toList();
        Collections.sort(categories);

        final double multiplier = getMultiplier();

        Point[] currentValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.currentValue == null) return null;
                return Point.withY(item.currentValue * multiplier);
            }
        }).toArray(Point.class);

        Point[] planValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.planValue == null) return null;
                return Point.withY(item.planValue * multiplier);
            }
        }).toArray(Point.class);

        Series[] series = new Series[]{
                new Series("Факт", currentValues),
                new Series("План", planValues),
        };

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", repository.getKpiByCode(kpiCode).name);
        bag.put("categories", categories);

        return new ChartResult[]{new ChartResult(series, bag)};
    }
}
