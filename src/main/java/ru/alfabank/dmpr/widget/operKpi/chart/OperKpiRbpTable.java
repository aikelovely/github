package ru.alfabank.dmpr.widget.operKpi.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.model.operKpi.OperKpiValue;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

/**
 * Таблица, Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
 */
@Service
public class OperKpiRbpTable extends BaseWidget<OperKpiOptions, OperKpiValue[]> {
    @Autowired
    OperKpiRepository repository;

    public OperKpiRbpTable() {
        super(OperKpiOptions.class);
    }

    @Override
    public OperKpiValue[] getData(OperKpiOptions options) {
        OperKpiValue[] data = repository.getOperKpiRbpValues(options);

        return data;
    }
}
