package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
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
 * График "Расходы и экономический эффект от проектов ОБ, тыс. долл." (KPIOB~9)<br>
 * Фильтр - квартал, по-умолчанию предыдущий.<br>
 * Вид графика -  гистограмма, на которой выводится попарно 2 КПЭ Расходы (KPIOB~9~1)
 * план и факт и экономический эффект (KPIOB~9~2) план и факт на конец квартала. <br>
 * Вторым графиком для сравнения выводится аналогичная информация на конец предыдущего, относительно выбранного, года.
 */
@Service
public class LeaderBoardKpi9Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi9Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        int year = options.startDate.getYear();

        return new ChartResult[]{
                getChartByQuarter(year - 1, 4, true, options),
                getChartByQuarter(year, options.quarter, false, options)
        };
    }

    private ChartResult getChartByQuarter(int year, int quarter, boolean isPrevYear, LeaderBoardOptions options) {
        int startMonth = (quarter - 1) * 3 + 1;

        options.dateIntervalType = IntervalType.Quarter.toString();
        options.startDate = new DateTime(year, startMonth, 1, 0, 0).toLocalDate();
        options.endDate = options.startDate;

        LinqWrapper<KpiDataItem> data = LinqWrapper.from(repository.getKpiData(options, "KPIOB~9~1", "KPIOB~9~2"));

        Series fact = new Series(isPrevYear ? String.format("Прогноз на %s год", year) : "Прогноз");
        Series plan = new Series(isPrevYear ? String.format("План на %s год", year) : "План");

        String[] categories = data.select(new Selector<KpiDataItem, String>() {
            @Override
            public String select(KpiDataItem item) {
                return item.kpiName;
            }
        }).toArray(String.class);

        fact.data = data.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                return Point.withY(item.currentValue, item.kpiName);
            }
        }).toArray(Point.class);

        plan.data = data.select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                return Point.withY(item.planValue, item.kpiName);
            }
        }).toArray(Point.class);

        String title = "Расходы и экономический эффект по портфелю";
        if(isPrevYear){
            title += String.format(" в %s году", year);
        }

        Map<String, Object> bag = new HashMap<>();
        bag.put("isPrevYear", isPrevYear);
        bag.put("categories", categories);
        bag.put("kpiName", title);

        return new ChartResult(new Series[]{plan, fact}, bag);
    }
}
