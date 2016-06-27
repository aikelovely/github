package ru.alfabank.dmpr.widget.leaderBoard;

import org.apache.poi.util.ArrayUtil;
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
import java.util.Map;

/**
 * Динамика по показателю "Эффект от процессного управления, млн руб." (KPIOB~8) <br/>
 * Значения параметров следующие: <br/>
 * <i>dateIntervalType</i> - месяц.<br/>
 * <i>startDate</i> начало выбранного месяца<br/>
 * <i>endDate</i> - конец выбранного месяца.<br/>
 * <i>divisionGroupId</i> - 0, т.е. данные нужно отобразить по всем группам подразделений
 */
@Service
public class LeaderBoardKpi8DynamicChart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi8DynamicChart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Month.toString();
        options.endDate = options.startDate;
        options.startDate = options.startDate.withDayOfMonth(1);
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

                        // Данные для гистограммы
                        Point[] points = value.getItems()
                                .select(new Selector<KpiDataItem, Point>() {
                                    @Override
                                    public Point select(KpiDataItem item) {
                                        if (item.currentValue == null || item.planValue == null) return null;

                                        Point p = Point.withY(
                                                MathHelper.safeDivide(item.currentValue, item.planValue) * 100,
                                                item.divisionGroupCode
                                        );

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
                        return new ChartResult(new Series[]{new Series("Выполнение плана", points)}, bag);
                    }
                })
                .toArray(ChartResult.class);
    }
}
