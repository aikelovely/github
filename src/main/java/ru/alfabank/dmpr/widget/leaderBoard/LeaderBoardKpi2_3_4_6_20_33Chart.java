package ru.alfabank.dmpr.widget.leaderBoard;

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
 * Сервис, который строит несколько однотипных графиков:<br/>
 * <ol>
 * <li>"Уровень вовлеченности персонала" (KPIOB~2)
 * <li>"Уровень знаний ОБ" (KPIOB~3)</li>
 * <li>"Следование ценностям" (KPIOB~4)</li>
 * <li>"Следование модели лидерских компетенций" (KPIOB~6)</li>
 * <li>"Уровень компетенций руководителей проектов Банка, %" (KPIOB~20)</li>
 * </ol>
 * На гистограмме отображается выбранный в фильтре год и значение показателя за предыдущий год,
 * линией выводится план на текущий год. <br/>
 * Отдельно отображается % прироста или падения показателя относительно предыдущего  года. <br/>
 * Данные в графике выводятся по всему операционному блоку.
 */
@Service
public class LeaderBoardKpi2_3_4_6_20_33Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi2_3_4_6_20_33Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Year.toString();

        return LinqWrapper.from(repository.getKpiData(options,
                "KPIOB~2",
                "KPIOB~3",
                "KPIOB~4",
                "KPIOB~6",
                "KPIOB~20",
                "KPIOB~33"))
                .group(new Selector<KpiDataItem, String>() {
                    @Override
                    public String select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.kpiCode;
                    }
                })
                .select(new Selector<Group<String, KpiDataItem>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<String, KpiDataItem> kpiDataItems) {
                        Map<String, Object> bag = new HashMap<>();

                        String kpiCode = kpiDataItems.getKey();
                        bag.put("kpiCode", kpiCode);
                        bag.put("kpiName", repository.getKpiByCode(kpiCode).name);

                        KpiDataItem item = kpiDataItems.getItems().first();
                        bag.put("planValue", item.planValue == null ? null : item.planValue * 100);

                        Point[] points = new Point[]{
                                item.prevValue == null ? null : Point.withY(item.prevValue * 100),
                                item.currentValue == null ? null : Point.withY(item.currentValue * 100),
                        };

                        return new ChartResult(new Series[]{new Series("Факт", points)}, bag);
                    }
                }).toArray(ChartResult.class);
    }
}
