package ru.alfabank.dmpr.widget.operKpi.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.operKpi.OperKpiDetailValue;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.widget.BaseCsvReport;

@Service
public class OperKpiHunterCsvReport extends BaseCsvReport<OperKpiOptions> {
    @Autowired
    OperKpiRepository repository;

    public OperKpiHunterCsvReport() {
        super(OperKpiOptions.class);
    }

    @Override
    protected String getReportName(OperKpiOptions options) {
        return "OperKpiHunterReport";
    }

    @Override
    protected void configure(CsvBuilder builder, OperKpiOptions options) {
        OperKpiDetailValue[] data = repository.getOperKpiHunterDtlValues(options);

        builder.configure(OperKpiDetailValue.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("channelName").title("Канал");
                        c.add("productType").title("Продукт");
                        c.add("bpOperationDurationAsString").title("Длительность");
                        c.add("durGroupName").title("Группа");
                        c.add("loanapplRefParent").title("ASN");
                        c.add("dealRef").title("Номер_Продукта");
                        c.add("opEndTime").title("Дата").format("dd.MM.yyyy HH:mm:ss");
                    }
                });
    }
}
