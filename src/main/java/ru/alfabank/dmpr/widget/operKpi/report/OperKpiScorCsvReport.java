package ru.alfabank.dmpr.widget.operKpi.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.model.operKpi.OperKpiScorDetailValue;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCFilterRepository;
import ru.alfabank.dmpr.widget.BaseCsvReport;

@Service
public class OperKpiScorCsvReport extends BaseCsvReport<OperKpiOptions> {
    @Autowired
    OperKpiRepository repository;

    @Autowired
    PILAndCCFilterRepository filterRepository;

    public OperKpiScorCsvReport() {
        super(OperKpiOptions.class);
    }

    @Override
    protected String getReportName(OperKpiOptions options) {
        return "OperKpiScorReport";
    }

    @Override
    protected void configure(CsvBuilder builder, OperKpiOptions options) {
        options.sevenFieldsCheckCode = filterRepository.getSevenFieldsCheckById(options.sevenFieldsCheckId).code;
        OperKpiScorDetailValue[] data = repository.getOperKpiScorDtlValues(options);

        builder.configure(OperKpiScorDetailValue.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("channelName").title("Канал");
                        c.add("productType").title("Продукт");
                        c.add("bpOperationDurationAsString").title("Длительность");
                        c.add("durGroupName").title("Группа");
                        c.add("kpiLoanCode").title("ASN/Номер_Продукта");
                        c.add("moduleName").title("Система-источник");
                        c.add("opEndTime").title("Дата").format("dd.MM.yyyy HH:mm:ss");
                        c.add("bpKpiName").title("Тип KPI");
                    }
                });
    }
}
