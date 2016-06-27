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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График "Реализация проектов: достижение запланированных результатов
 * в срок и бюджет, с заявленным качеством, %" (KPIOB~14) <br>
 * Параметр <i>dateIntervalType</i> - квартал.<br/>
 */
@Service
public class LeaderBoardKpi14Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi14Chart() {
        super(LeaderBoardOptions.class);
    }


    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Quarter.toString();

        String kpiCode = "KPIOB~14~" + options.kpi14Value;

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

        Point[] currentValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.currentValue == null) return null;
                return Point.withY(item.currentValue * 100);
            }
        }).toArray(Point.class);

        Point[] planValues = kpiDataItems.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                if (item.planValue == null) return null;
                return Point.withY(item.planValue * 100);
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