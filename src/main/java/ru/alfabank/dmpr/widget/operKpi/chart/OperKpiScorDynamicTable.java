package ru.alfabank.dmpr.widget.operKpi.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.model.operKpi.OperKpiScorDynValue;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCFilterRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Таблица, Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
 */
@Service
public class OperKpiScorDynamicTable extends BaseWidget<OperKpiOptions, List<Map<String, Object>>> {
    @Autowired
    OperKpiRepository repository;

    @Autowired
    PILAndCCFilterRepository filterRepository;

    public OperKpiScorDynamicTable() {
        super(OperKpiOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(OperKpiOptions options) {
        options.sevenFieldsCheckCode = filterRepository.getSevenFieldsCheckById(options.sevenFieldsCheckId).code;

        LinqWrapper<OperKpiScorDynValue> data = LinqWrapper.from(repository.getOperKpiScorDynamic(options));

        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        return data
                .group(new Selector<OperKpiScorDynValue, Pair<Long, String>>() {
                    @Override
                    public Pair<Long, String> select(OperKpiScorDynValue item) {
                        return Pair.of(item.reportTypeId, item.durGroupName);
                    }
                })
                .select(new Selector<Group<Pair<Long, String>, OperKpiScorDynValue>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<Long, String>, OperKpiScorDynValue> group) {
                        LinqWrapper<OperKpiScorDynValue> items = group.getItems();
                        Pair<Long, String> key = group.getKey();

                        final Map<LocalDate, OperKpiScorDynValue> index = items
                                .toMap(new Selector<OperKpiScorDynValue, LocalDate>() {
                                    @Override
                                    public LocalDate select(OperKpiScorDynValue item) {
                                        return item.calcDate;
                                    }
                                });

                        OperKpiScorDynValue first = group.getItems().first();

                        final Map<String, Object> row = new LinkedHashMap<>();
                        row.put("reportTypeId", key.getLeft());
                        row.put("durGroupName", key.getRight());
                        row.put("sortOrder", first.sortOrder);

                        LinqWrapper.from(dates)
                                .each(new Action<LocalDate>() {
                                    @Override
                                    public void act(LocalDate date) {
                                        OperKpiScorDynValue item = index.get(date);
                                        if(item == null) return;

                                        String columnName = DateHelper.createDateName(date);
                                        row.put(columnName, item.factValueTotal);
                                    }
                                });

                        return row;
                    }
                })
                .sort(new Selector<Map<String, Object>, Integer>() {
                    @Override
                    public Integer select(Map<String, Object> row) {
                        return (Integer) row.get("sortOrder");
                    }
                })
                .toList();
    }
}
