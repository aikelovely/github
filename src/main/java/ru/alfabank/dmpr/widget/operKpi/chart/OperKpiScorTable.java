package ru.alfabank.dmpr.widget.operKpi.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.model.operKpi.OperKpiScorTableRow;
import ru.alfabank.dmpr.model.operKpi.OperKpiScorValue;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCFilterRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.Map;

/**
 * Таблица, Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
 */
@Service
public class OperKpiScorTable extends BaseWidget<OperKpiOptions, OperKpiScorTableRow[]> {
    @Autowired
    OperKpiRepository repository;

    @Autowired
    PILAndCCFilterRepository filterRepository;

    public OperKpiScorTable() {
        super(OperKpiOptions.class);
    }

    @Override
    public OperKpiScorTableRow[] getData(OperKpiOptions options) {
        options.sevenFieldsCheckCode = filterRepository.getSevenFieldsCheckById(options.sevenFieldsCheckId).code;
        OperKpiScorValue[] data = repository.getOperKpiScorValues(options);

        // Создаем для данных с кодом "TTY предварительное" словарь вида {durGroupCode}_{reportTypeId}
        final Map<String, OperKpiScorValue> preliminarilyData = LinqWrapper.from(data)
                .filter(new Predicate<OperKpiScorValue>() {
                    @Override
                    public boolean check(OperKpiScorValue item) {
                        return item.bpKpiCode.equals("PILCC~TTYP");
                    }
                }).toMap(new Selector<OperKpiScorValue, String>() {
                    @Override
                    public String select(OperKpiScorValue item) {
                        return item.durGroupName + "_" + item.reportTypeId;
                    }
                });

        LinqWrapper<OperKpiScorTableRow> rows = LinqWrapper.from(data)
                .filter(new Predicate<OperKpiScorValue>() {
                    @Override
                    public boolean check(OperKpiScorValue item) {
                        return item.bpKpiCode.equals("PILCC~TTYF");
                    }
                }).sort(new Selector<OperKpiScorValue, Comparable>() {
                    @Override
                    public Comparable select(OperKpiScorValue item) {
                        return item.sortOrder;
                    }
                }).select(new Selector<OperKpiScorValue, OperKpiScorTableRow>() {
                    @Override
                    public OperKpiScorTableRow select(OperKpiScorValue item) {
                        // Ищем данные о "TTY предварительное"
                        String key = item.durGroupName + "_" + item.reportTypeId;
                        OperKpiScorValue preliminarilyItem = preliminarilyData.get(key);

                        return new OperKpiScorTableRow(item, preliminarilyItem);
                    }
                });

        return rows.toArray(OperKpiScorTableRow.class);
    }
}
