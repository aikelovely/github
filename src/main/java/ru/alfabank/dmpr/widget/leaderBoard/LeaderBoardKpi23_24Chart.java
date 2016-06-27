package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;

@Service
public class LeaderBoardKpi23_24Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi23_24Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Month.toString();

        return LinqWrapper.from(repository.getKpiDataLastFact(options, "KPIOB~23", "KPIOB~24"))
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

                        KpiDataItem item = value.getItems().filter(new Predicate<KpiDataItem>() {
                            @Override
                            public boolean check(KpiDataItem item) {
                                return item.calcDate != null;
                            }
                        }).sortDesc(new Selector<KpiDataItem, LocalDate>() {
                            @Override
                            public LocalDate select(KpiDataItem kpiDataItem) {
                                return kpiDataItem.calcDate;
                            }
                        }).firstOrNull();

                        if(item != null){
                            if(item.currentValue != null){
                                bag.put("value", item.currentValue * 100);
                            }
                            bag.put("calcDate", item.calcDate);
                        }

                        return new ChartResult(null, bag);
                    }
                }).toArray(ChartResult.class);
    }
}
