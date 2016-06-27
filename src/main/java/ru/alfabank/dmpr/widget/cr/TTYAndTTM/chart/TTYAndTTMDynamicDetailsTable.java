package ru.alfabank.dmpr.widget.cr.TTYAndTTM.chart;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Dynamic;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Таблица с детализацией динамики среднего значения/процента в KPI.
 */
@Service
public class TTYAndTTMDynamicDetailsTable extends BaseWidget<TTYAndTTMOptions, List<Map<String, Object>>> {
    @Autowired
    TTYAndTTMRepository repository;

    public TTYAndTTMDynamicDetailsTable() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(final TTYAndTTMOptions options) {
        if(options.floatingYear){
            options.startDate = options.endDate.plusMonths(-12).plusDays(1);
        }

        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        LinqWrapper<Dynamic> data = LinqWrapper.from(repository.getDynamicDetails(options));

        final List<Map<String, Object>> rows = new ArrayList<>();

        if(options.processIds == null || options.processIds.length == 0 || ArrayUtils.contains(options.processIds, 1)){
            LinqWrapper<Dynamic> filteredData = data.filter(new Predicate<Dynamic>() {
                @Override
                public boolean check(Dynamic item) {
                    return item.processId == 1;
                }
            });

            filteredData.group(new Selector<Dynamic, Pair<Long, String>>() {
                @Override
                public Pair<Long, String> select(Dynamic item) {
                    return Pair.of(item.id, item.name);
                }
            }).each(new Action<Group<Pair<Long, String>, Dynamic>>() {
                @Override
                public void act(Group<Pair<Long, String>, Dynamic> group) {
                    rows.add(createRow(group, 1, dates, options));
                }
            });
        }

        if(options.processIds == null || options.processIds.length == 0 || ArrayUtils.contains(options.processIds, 2)){
            LinqWrapper<Dynamic> filteredData = data.filter(new Predicate<Dynamic>() {
                @Override
                public boolean check(Dynamic item) {
                    return item.processId == 2;
                }
            });

            filteredData.group(new Selector<Dynamic, Pair<Long, String>>() {
                @Override
                public Pair<Long, String> select(Dynamic item) {
                    return Pair.of(item.id, item.name);
                }
            }).each(new Action<Group<Pair<Long, String>, Dynamic>>() {
                @Override
                public void act(Group<Pair<Long, String>, Dynamic> group) {
                    rows.add(createRow(group, 2, dates, options));
                }
            });
        }

        return LinqWrapper.from(rows).sort(new Selector<Map<String, Object>, Comparable>() {
            @Override
            public Comparable select(Map<String, Object> row) {
                return (String) row.get("name");
            }
        }).toList();
    }

    private Map<String, Object> createRow(Group<Pair<Long, String>, Dynamic> group,
                                          final int processId,
                                          final List<LocalDate> dates,
                                          final TTYAndTTMOptions options) {
        Pair<Long, String> key = group.getKey();
        LinqWrapper<Dynamic> items = group.getItems();

        final Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", key.getLeft());
        row.put("name", key.getRight());
        row.put("processId", processId);

        final Map<LocalDate, Dynamic> index = items.toMap(new Selector<Dynamic, LocalDate>() {
            @Override
            public LocalDate select(Dynamic item) {
                return item.calcDate;
            }
        });

        class RichCell {
            public Double value;
            public double quotaInDays;
            public double quotaPercent;

            public RichCell(Dynamic item) {
                if (options.paramType == ParamType.AvgDuration) {
                    value = item.averageValue;
                } else {
                    value = MathHelper.safeDivide(item.inKpiCount, item.inKpiCount + item.outOfKpiCount) * 100;
                }

                quotaInDays = item.quotaInDays;
                quotaPercent = item.quotaPercent;
            }
        }

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        Dynamic item = index.get(date);
                        String columnName = DateHelper.createDateName(date);

                        RichCell cell = null;
                        if (item != null)
                            cell = new RichCell(item);

                        row.put(columnName, cell);
                    }
                });

        return row;
    }
}