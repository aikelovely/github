package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//
//
import ru.alfabank.dmpr.widget.BaseWidget;

import ru.alfabank.dmpr.model.leaderBoard.Kpi5DescriptionData;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;

import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;

@Service
public class leaderBoardDescriptionTable extends BaseWidget<LeaderBoardOptions,Kpi5DescriptionData[]> {
    @Autowired
    LeaderBoardRepository repository;

    public leaderBoardDescriptionTable() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public Kpi5DescriptionData[] getData(LeaderBoardOptions options) {
//        if(options.systemUnitIds != null && options.systemUnitIds.length > 0){
//            options.rcbUnitId = options.systemUnitIds[0];
//        } else {
//            options.rcbUnitId = 2;
//        }

        Kpi5DescriptionData[] data = repository.getKpi5DescriptionData(options);

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

        return data;
    }

}
