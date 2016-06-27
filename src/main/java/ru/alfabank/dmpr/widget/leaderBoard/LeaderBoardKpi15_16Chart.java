package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.*;

/**
 * Два показателя на одной графике: <br>
 * <ol>
 *     <li>"Минимизация операционного риска, %" (KPIOB~15)</li>
 *     <li>"Полнота предоставления данных об операционных рисках, %" (KPIOB~16)</li>
 * </ol>
 * Фильтры: "Период, с", "Период, по" и "Группа подразделений".
 * Показывается в разрезе месяцев совместно с показателем (KPIOB~16) на графике вида
 * <a href="http://www.highcharts.com/demo/bar-negative-stack">http://www.highcharts.com/demo/bar-negative-stack</a>
 * показывая слева столбиками факт операционного риск и линией план,
 * а справа аналогично по полноте предоставления данных об операционных рисках
 */
@Service
public class LeaderBoardKpi15_16Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi15_16Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Month.toString();

        List<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options,
                        "KPIOB~15",
                        "KPIOB~16"))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                }).toList();

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", "Минимизация операционного риска");

        if (data.size() == 0) {
            return new ChartResult[]{new ChartResult(new Series[0], bag)};
        }

        final LocalDate minDate = data.get(0).calcDate;
        final LocalDate maxDate = data.get(data.size() - 1).calcDate;

        final List<LocalDate> categories = new ArrayList<>();
        final List<Series> series = new ArrayList<>();


        LinqWrapper.from(data)
                .group(new Selector<KpiDataItem, String>() {
                    @Override
                    public String select(KpiDataItem item) {
                        return item.kpiCode;
                    }
                })
                .each(new Action<Group<String, KpiDataItem>>() {
                    @Override
                    public void act(Group<String, KpiDataItem> group) {
                        List<Point> currentValues = new ArrayList<>();
                        List<Point> planValues = new ArrayList<>();

                        final String kpiCode = group.getKey();
                        HashMap<String, Object> bag = new HashMap<>();
                        bag.put("kpiCode", kpiCode);


                        for (LocalDate date = minDate; date.compareTo(maxDate) <= 0;
                             date = date.plusMonths(1).dayOfMonth().withMaximumValue()) {

                            final LocalDate currentDate = date;
                            KpiDataItem item = group.getItems().firstOrNull(new Predicate<KpiDataItem>() {
                                @Override
                                public boolean check(KpiDataItem item) {
                                    return item.calcDate.isEqual(currentDate);
                                }
                            });

                            if (!categories.contains(currentDate)) {
                                categories.add(currentDate);
                            }

                            double multiplier = 100;
                            if (kpiCode.equals("KPIOB~15")) {
                                multiplier = -100;
                            }

                            if (item == null || item.currentValue == null) {
                                currentValues.add(null);
                            } else {
                                currentValues.add(Point.withY(item.currentValue * multiplier));
                            }

                            if (item == null || item.planValue == null) {
                                planValues.add(null);
                            } else {
                                if (kpiCode.equals("KPIOB~15")) {
                                    planValues.add(Point.withY(item.planValue * multiplier));
                                }
                                else {
                                    planValues.add(Point.withY(item.planValue * multiplier));
                                }
                            }
                        }

                        series.add(new Series(
                                repository.getKpiByCode(kpiCode).name,
                                currentValues.toArray(new Point[currentValues.size()]),
                                (HashMap) bag.clone()));

                        if (planValues.size() > 0) {
                            String seriesName = kpiCode.equals("KPIOB~15")
                                    ? "План минимизации операционных рисков"
                                    : "План полноты предоставления информации об операционных рисках";

                            bag.put("isPlanValue", true);
                            series.add(new Series(seriesName, planValues.toArray(new Point[planValues.size()]), bag));
                        }
                    }
                });

        Collections.sort(categories);
        bag.put("categories", categories);
        return new ChartResult[]{new ChartResult(series.toArray(new Series[series.size()]), bag)};
    }
}
