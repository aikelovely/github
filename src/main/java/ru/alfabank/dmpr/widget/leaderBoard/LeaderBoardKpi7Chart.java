package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LeaderBoardKpi7Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi7Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        LinqWrapper<ChartResult> result = LinqWrapper
                .from(repository.getKpiData(options, "KPIOB~7~1", "KPIOB~7~2", "KPIOB~7~3",
                        "KPIOB~7~11", "KPIOB~7~12", "KPIOB~7~13", "KPIOB~7~14",
                        "KPIOB~7~14", "KPIOB~7~15", "KPIOB~7~16", "KPIOB~7~17",
                        "KPIOB~7~18", "KPIOB~7~19", "KPIOB~7~26"))
                .group(new Selector<KpiDataItem, String>() {
                    @Override
                    public String select(KpiDataItem item) {
                        return item.kpiCode;
                    }
                })
                .select(new Selector<Group<String, KpiDataItem>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<String, KpiDataItem> groupByKpi) {
                        final List<LocalDate> categories = new ArrayList<>();
                        Point[] currentValues = groupByKpi.getItems()
                                .sort(new Selector<KpiDataItem, LocalDate>() {
                                    @Override
                                    public LocalDate select(KpiDataItem item) {
                                        return item.calcDate;
                                    }
                                })
                                .select(new Selector<KpiDataItem, Point>() {
                                    @Override
                                    public Point select(KpiDataItem item) {
                                        categories.add(item.calcDate);

                                        if (item.currentValue == null) return null;
                                        Point p = Point.withY(item.currentValue * 100);
                                        if(item.planValue == null){
                                            item.planValue = 1d;
                                        }

                                        if(item.planValue < 0.1){
                                            p.color = item.currentValue <= item.planValue
                                                    ? Color.DarkGreenColor
                                                    : Color.SuperRedColor;
                                        }
                                        else {
                                            p.color = item.currentValue >= item.planValue
                                                    ? Color.DarkGreenColor
                                                    : Color.SuperRedColor;
                                        }


                                        return p;
                                    }
                                }).toArray(Point.class);

                        Series[] series = new Series[]{new Series("Факт", currentValues)};

                        Map<String, Object> bag = new HashMap<>();

                        KpiDataItem first = groupByKpi.getItems().firstOrNull();

                        double planValue = 0;
                        if (first != null && first.planValue != null) {
                            planValue = first.planValue * 100;
                        }

                        String kpiCode = groupByKpi.getKey();
                        bag.put("kpiCode", kpiCode);
                        bag.put("planValue", planValue);
                        bag.put("kpiName", repository.getKpiByCode(kpiCode).name);
                        bag.put("categories", categories);

                        return new ChartResult(series, bag);
                    }
                });

        return result.toArray(ChartResult.class);
    }
}
