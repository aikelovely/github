package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Эффективность ОБ" (KPIOB~10) <br>
 * Фильтры: квартал года и подразделение. По умолчанию текущей год и подразделение с кодом «ОБ».<br>
 * Визуализация отражается на 2х графиках – факт и прогноз. <br>
 * 1) График «Эффект, полученный в {N} кв года» <br>
 * В качестве легенды выводится информация из поля комментарий в фактических данных (KPIOB~10~1) <br>
 * 2) График аналогично первому но по данным KPIOB~10~3 и KPIOB~10~4 <br><br/>
 * Значения параметров следующие: <br/>
 * <i>dateIntervalType</i> - квартал.<br/>
 * <i>startDate</i> и <i>endDate</i> первый месяц выбранного квартала.<br/>
 * <i>divisionGroupId</i> - "ОБ"
 */
@Service
public class LeaderBoardKpi10Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi10Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        int year = options.startDate.getYear();

        return new ChartResult[]{
                getChartByQuarter(year, options.quarter, true, options, "KPIOB~10~3", "KPIOB~10~4"),
                getChartByQuarter(year, options.quarter, false, options, "KPIOB~10~1", "KPIOB~10~2")
        };
    }

    private ChartResult getChartByQuarter(
            int year,
            int quarter,
            boolean isPrevYear,
            LeaderBoardOptions options,
            final String kpi1Code,
            final String kpi2Code) {
        int startMonth = (quarter - 1) * 3 + 1;

        options.dateIntervalType = IntervalType.Quarter.toString();
        options.startDate = new DateTime(year, startMonth, 1, 0, 0).toLocalDate();
        options.endDate = options.startDate;

        LinqWrapper<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options, kpi1Code, kpi2Code));

        KpiDataItem kpi1Data = data.firstOrNull(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.kpiCode.equals(kpi1Code);
            }
        });

        KpiDataItem kpi2Data = data.firstOrNull(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.kpiCode.equals(kpi2Code);
            }
        });

        Series calculated = new Series("Расчитан", new Point[]{
                (kpi1Data != null && kpi1Data.planValue != null) ? Point.withY(kpi1Data.planValue) : null,
                (kpi1Data != null && kpi1Data.currentValue != null) ? Point.withY(kpi1Data.currentValue) : null,
        });
        Series forecast = new Series("На оценке", new Point[]{
                null,
                (kpi1Data != null && kpi2Data.currentValue != null) ? Point.withY(kpi2Data.currentValue) : null,
        });

        String[] categories = isPrevYear
                ? new String[]{
                String.format("План 31.12.%d", year),
                String.format("Факт 31.12.%d", year)}
                : new String[]{
                String.format("План %d кв %d года", quarter, year),
                String.format("Факт %d кв %d года", quarter, year)};

        String title = isPrevYear
                ? String.format("Прогноз по эффектам на 31.12.%d (выполнение Плана)", year)
                : String.format("Эффект, полученный в %d кв %d года", quarter, year);

        Map<String, Object> bag = new HashMap<>();
        bag.put("isPrevYear", isPrevYear);
        bag.put("categories", categories);
        bag.put("kpi1Message", kpi1Data != null ? kpi1Data.description : null);
        bag.put("kpi2Message", kpi2Data != null ? kpi2Data.description : null);
        bag.put("kpiName", title);

        return new ChartResult(new Series[]{calculated, forecast}, bag);
    }
}
