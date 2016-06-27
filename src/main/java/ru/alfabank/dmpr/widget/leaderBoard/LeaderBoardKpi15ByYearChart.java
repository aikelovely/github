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

@Service
public class LeaderBoardKpi15ByYearChart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi15ByYearChart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Year.toString();

        LinqWrapper<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options, "KPIOB~15"))
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

        Series[] series = new Series[]{
                new Series("Факт", currentValues)
        };

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", repository.getKpiByCode("KPIOB~15").name);

        if(data.count() > 0){
            KpiDataItem item = data.first();
            if(item.planValue == null){
                item.planValue = 0d;
            }
            bag.put("planValue", item.planValue * 100);
        }

        return new ChartResult[]{new ChartResult(series, bag)};
    }
}
