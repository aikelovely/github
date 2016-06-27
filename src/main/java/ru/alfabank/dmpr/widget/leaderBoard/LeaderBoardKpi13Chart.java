package ru.alfabank.dmpr.widget.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.ChartType;
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
 * График "Эффективность взыскания" (KPIOB~13) <br>
 * Фильтры: "Период, с" и "Период, по". По-умолчанию с начала года по текущий месяц.<br>
 * По горизонтальной оси недели выбранного периода, по вертикальной оси в % показатель.<br>
 * В виде графиков выводятся отдельно фактические и плановые показатели: <br>
 * KPIOB~13~1 - Доля договоров, выведенных на стадии 1-20 день <br>
 * KPIOB~13~2 - Доля договоров, выведенных на стадии 1-60 день <br>
 * KPIOB~13~3 - Доля договоров, выведенных на стадии 1-90 день <br>
 * KPIOB~13~4 - Доля договоров, выведенных на стадии 1-90 день накопительным итогом (только факт) <br>
 * В виде гистограммы: KPIOB~13~5 - Новые договора, поступившие в работу Collection.<br>
 */
@Service
public class LeaderBoardKpi13Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi13Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Week.toString();

        List<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options,
                        "KPIOB~13~1",
                        "KPIOB~13~2",
                        "KPIOB~13~3",
                        "KPIOB~13~4",
                        "KPIOB~13~5"))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                }).toList();

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", "% взыскания задолженности по кредитам РБ");

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

                        if (kpiCode.equals("KPIOB~13~5")) {
                            bag.put("isMainValue", true);
                        }

                        for (LocalDate date = minDate; date.compareTo(maxDate) <= 0; date = date.plusWeeks(1)) {
                            // Пропускаекм 1ю неделю
                            if(date.getWeekOfWeekyear() == 1){
                                date = date.plusWeeks(1);
                            }

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

                            if (item == null || item.currentValue == null) {
                                currentValues.add(null);
                            } else if (kpiCode.equals("KPIOB~13~5")) {
                                currentValues.add(Point.withY(item.currentValue));
                            } else {
                                currentValues.add(Point.withY(item.currentValue * 100));
                            }

                            if (!kpiCode.equals("KPIOB~13~5") &&
                                    !kpiCode.equals("KPIOB~13~4")) {
                                if (item == null || item.planValue == null) {
                                    planValues.add(null);
                                } else {
                                    planValues.add(Point.withY(item.planValue * 100));
                                }
                            }
                        }

                        series.add(new Series(
                                repository.getKpiByCode(kpiCode).name,
                                currentValues.toArray(new Point[currentValues.size()]),
                                (HashMap) bag.clone()));

                        if (planValues.size() > 0) {
                            bag.put("isPlanValue", true);

                            String seriesName = "";
                            switch (kpiCode) {
                                case "KPIOB~13~1":
                                    seriesName = "Цель на 20 дней";
                                    break;

                                case "KPIOB~13~2":
                                    seriesName = "Цель на 60 дней";
                                    break;

                                case "KPIOB~13~3":
                                    seriesName = "Цель на 90 дней";
                                    break;
                            }

                            series.add(new Series(seriesName, planValues.toArray(new Point[planValues.size()]), bag));
                        }
                    }
                });

        Collections.sort(categories);
        bag.put("categories", categories);

        return new ChartResult[]{new ChartResult(series.toArray(new Series[series.size()]), bag)};
    }
}
