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
 * График "Динамика портфеля проектов" (KPIOB~18) <br>
 * Фильтры: "Период, с", "Период, по" и "Группа подразделений".
 * Отбираются фактические данные за заданный период и заданную группу подразделений данные с KPIOB в списке
 * – KPIOB~18~1 (кол-во завершенных проектов),
 * KPIOB~18~2 (кол-во реализуемых проектов),
 * KPIOB~18~3  (кол-во не стартовавших проектов). <br>
 * Классическая столбиковая гистограмма. Столбик представляет собой сумму по всем 3ем КПЭ.
 * КПЭ располагаются строго снизу вверх по возрастанию своего номера.
 */
@Service
public class LeaderBoardKpi18Chart extends BaseChart<LeaderBoardOptions> {
    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardKpi18Chart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public ChartResult[] getData(LeaderBoardOptions options) {
        options.dateIntervalType = IntervalType.Week.toString();

        Map<String, Object> bag = new HashMap<>();
        bag.put("kpiName", "Динамика портфеля проектов");

        List<KpiDataItem> data = LinqWrapper
                .from(repository.getKpiData(options,
                        "KPIOB~18~1",
                        "KPIOB~18~2",
                        "KPIOB~18~3"))
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                }).toList();

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
                .sort(new Selector<Group<String, KpiDataItem>, String>() {
                    @Override
                    public String select(Group<String, KpiDataItem> kpiDataItems) {
                        return kpiDataItems.getKey();
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


                        for (LocalDate date = minDate; date.compareTo(maxDate) <= 0; date = date.plusWeeks(1)) {
                            // Пропускаекм 1ю неделю
                            if (date.getWeekOfWeekyear() == 1) {
                                date = date.plusWeeks(1);
                            }

                            final LocalDate currentDate = date;
                            KpiDataItem item = group.getItems().firstOrNull(new Predicate<KpiDataItem>() {
                                @Override
                                public boolean check(KpiDataItem item) {
                                    return item.calcDate.compareTo(currentDate) == 0;
                                }
                            });

                            if (!categories.contains(currentDate)) {
                                categories.add(currentDate);
                            }

                            if (item == null || item.currentValue == null) {
                                currentValues.add(null);
                            } else {
                                currentValues.add(Point.withY(item.currentValue));
                            }
                        }

                        series.add(new Series(
                                repository.getKpiByCode(kpiCode).name,
                                currentValues.toArray(new Point[currentValues.size()]), bag));
                    }
                });

        Collections.sort(categories);
        bag.put("categories", categories);
        return new ChartResult[]{new ChartResult(series.toArray(new Series[series.size()]), bag)};
    }
}
