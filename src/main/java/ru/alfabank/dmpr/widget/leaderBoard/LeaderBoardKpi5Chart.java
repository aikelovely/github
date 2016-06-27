package ru.alfabank.dmpr.widget.leaderBoard;

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
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Уровень зрелости управления знаниями ОБ" (KPIOB~5)<br/>
 * Значения параметров следующие: <br/>
 * <i>dateIntervalType</i> - квартал, т.к. показатель выводится нарастающим итогом поквартально.<br/>
 * <i>startDate</i> расчитывается как (начало выбранного года) - (3 месяца).<br/>
 * <i>endDate</i> - конец выбранного года.<br/>
 * <i>divisionGroupId</i> - "ОБ"
 */
@Service
public class LeaderBoardKpi5Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi5Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Quarter.toString();
        options.endDate = options.startDate.plusYears(1).plusDays(-1);
        options.startDate = options.startDate.plusMonths(-3);

        LinqWrapper<KpiDataItem> kpiDataItems = LinqWrapper
                .from(repository.getKpiData(options, "KPIOB~5"))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

        Point[] currentValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.currentValue == null) return null;
                return Point.withY(item.currentValue);
            }
        }).toArray(Point.class);

        Point[] planValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.planValue == null) return null;
                return Point.withY(item.planValue);
            }
        }).toArray(Point.class);

        Series[] series = new Series[]{
                new Series("Факт", currentValues),
                new Series("План", planValues, ChartType.spline),
        };

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", repository.getKpiByCode("KPIOB~5").name);

        return new ChartResult[]{new ChartResult(series, bag)};
    }
}
