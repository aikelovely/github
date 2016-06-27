package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Пирог по показателю "Эффект от процессного управления, млн руб." (KPIOB~8)
 * Значения параметров следующие: <br/>
 * <i>dateIntervalType</i> - год.<br/>
 * <i>startDate</i> начало выбранного года<br/>
 * <i>endDate</i> - конец выбранного года.<br/>
 * <i>divisionGroupId</i> - 0, т.е. данные нужно отобразить по всем группам подразделений
 */
@Service
public class LeaderBoardKpi8PieChart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi8PieChart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Year.toString();
        options.divisionGroupId = 0L;

        return LinqWrapper.from(repository.getKpiData(options, "KPIOB~8~1", "KPIOB~8~2"))
                .group(new Selector<KpiDataItem, String>() {
                    @Override
                    public String select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.kpiCode;
                    }
                })
                .select(new Selector<Group<String,KpiDataItem>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<String, KpiDataItem> value) {
                        HashMap<String, Object> bag = new HashMap<>();

                        String kpiCode = value.getKey();
                        bag.put("kpiCode", kpiCode);

                        Point[] points = value.getItems()
                                .select(new Selector<KpiDataItem, Point>() {
                                    @Override
                                    public Point select(KpiDataItem item) {
                                        if(item.planValue == null) return null;
                                        Point p = Point.withY(item.planValue, item.divisionGroupCode);
                                        p.tag = item.divisionGroupName;
                                        return p;
                                    }
                                })
                                .filter(new Predicate<Point>() {
                                    @Override
                                    public boolean check(Point item) {
                                        return item != null;
                                    }
                                })
                                .toArray(Point.class);
                        return new ChartResult(new Series[]{new Series("", points)}, bag);
                    }
                }).toArray(ChartResult.class);
    }
}
