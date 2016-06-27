package ru.alfabank.dmpr.widget.cr.ClientTime.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDataItem;
import ru.alfabank.dmpr.repository.cr.ClientTimeRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Таблица детализация бизнес-процессов/функциольных групп/подразделений.
 */
@Service
public class ClientTimeDetailsTable extends BaseWidget<ClientTimeOptions, List<Map<String, Object>>> {
    @Autowired
    ClientTimeRepository repository;

    public ClientTimeDetailsTable() {
        super(ClientTimeOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(final ClientTimeOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        switch (options.drillDownLevel){
            case 1: options.systemUnitId = 2; break;
            case 2: options.systemUnitId = 5; break;
            case 3: options.systemUnitId = 6; break;
        }

        LinqWrapper<KpiDataItem> data = LinqWrapper.from(repository.getKpiData(options))
                .filter(new Predicate<KpiDataItem>() {
                    @Override
                    public boolean check(KpiDataItem item) {
                        return item.processId == options.processId;
                    }
                });

        List<Map<String, Object>> rows = data
                .group(new Selector<KpiDataItem, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(KpiDataItem item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, KpiDataItem>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, KpiDataItem> unitGroup) {
                        return createRow(unitGroup, dates, options);
                    }
                })
                .sort(new Selector<Map<String, Object>, String>() {
                    @Override
                    public String select(Map<String, Object> row) {
                        return (String) row.get("unitName");
                    }
                })
                .toList();

        return rows;
    }

    private Map<String, Object> createRow(Group<Pair<String, String>, KpiDataItem> unitGroup,
                                          final List<LocalDate> dates,
                                          final ClientTimeOptions options) {
        Pair<String, String> key = unitGroup.getKey();

        final Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitCode", key.getLeft());
        row.put("unitName", key.getRight());

        LinqWrapper<KpiDataItem> items = unitGroup.getItems();
        final Map<LocalDate, KpiDataItem> index = items.toMap(new Selector<KpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(KpiDataItem item) {
                return item.calcDate;
            }
        });

        class RichCell {
            public double value;
            public double quotaInDays;
            public double quotaPercent;

            public RichCell(KpiDataItem item, ParamType paramType) {
                if(paramType == ParamType.AvgDuration){
                    value = MathHelper.safeDivide(item.totalDuration, item.bpCount);
                } else {
                    value = MathHelper.safeDivide(item.inKpiBpCount, item.bpCount) * 100;
                }

                quotaInDays = item.quotaInDays;
                quotaPercent = item.quotaPercent;
            }
        }

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        KpiDataItem item = index.get(date);
                        String columnName = DateHelper.createDateName(date);

                        RichCell cell = null;
                        if (item != null)
                            cell = new RichCell(item, options.paramType);

                        row.put(columnName, cell);
                    }
                });

        final KpiDataItem summation = new KpiDataItem();
        summation.totalDuration = 0d;
        summation.inKpiBpCount = 0;
        summation.bpCount = 0;

        items.each(new Action<KpiDataItem>() {
            @Override
            public void act(KpiDataItem item) {
                summation.totalDuration+= item.totalDuration;
                summation.inKpiBpCount += item.inKpiBpCount;
                summation.bpCount += item.bpCount;
            }
        });

        row.put("totals", new RichCell(summation, options.paramType));

        return row;
    }
}