package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//
//
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardFilterRepository;
import ru.alfabank.dmpr.widget.BaseChart;
//import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.model.leaderBoard.*;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import java.util.HashMap;
import java.util.Map;
@Service
public class leaderBoardDescriptionTable extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;
    @Autowired
    protected LeaderBoardFilterRepository filterRepository;
    public leaderBoardDescriptionTable() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(final LeaderBoardOptions options) {
//        if(options.systemUnitIds != null && options.systemUnitIds.length > 0){
//            options.rcbUnitId = options.systemUnitIds[0];
//        } else {
//            options.rcbUnitId = 2;
//        }

        LeaderBoardQueryOptions queryOptions = new LeaderBoardQueryOptions(options,filterRepository.getWeeks());
        Kpi5DescriptionData[] items = repository.getKpi5DescriptionData(queryOptions);

        final Kpi5DescriptionData summation = new Kpi5DescriptionData();

//        List<Kpi5DescriptionData> rows = LinqWrapper.from(data)
//                .select(new Selector<Kpi5DescriptionData, Kpi5DescriptionData>() {
//                    @Override
//                    public Kpi5DescriptionData select(Kpi5DescriptionData item) {
//
//                        return new Kpi5DescriptionData();
//                    }
//                })
//                .sort(new Selector<Kpi5DescriptionData, Comparable>() {
//                    @Override
//                    public Comparable select(Kpi5DescriptionData row) {
//                        return row.dgName;
//                    }
//                })
//                .toList();
//
//        rows.add(new Kpi5DescriptionData());


        Map<String, Object> bag = new HashMap<>();
        bag.put("data", items);

        return new ChartResult[]{new ChartResult(null, bag)};
       // return data;
    }

}
