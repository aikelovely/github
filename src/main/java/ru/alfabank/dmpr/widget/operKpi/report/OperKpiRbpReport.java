package ru.alfabank.dmpr.widget.operKpi.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.operKpi.OperKpiDetailValue;
import ru.alfabank.dmpr.model.operKpi.OperKpiOptions;
import ru.alfabank.dmpr.repository.operKpi.OperKpiRepository;
import ru.alfabank.dmpr.widget.BaseReport;

@Service
public class OperKpiRbpReport extends BaseReport<OperKpiOptions> {
    @Autowired
    OperKpiRepository repository;

    public OperKpiRbpReport() {
        super(OperKpiOptions.class);
    }

    @Override
    protected String getReportName(OperKpiOptions options) {
        return "OperKpiRbpReport";
    }

    @Override
    protected void configure(ReportBuilder builder, OperKpiOptions options) {
        OperKpiDetailValue[] data = repository.getOperKpiRbpDtlValues(options);

        builder.addWorksheet(OperKpiDetailValue.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("channelName").title("Канал");
                        c.add("productType").title("Продукт");
                        c.add("bpOperationDurationAsString").title("Длительность").width(20);
                        c.add("durGroupName").title("Группа");
                        c.add("loanapplRefParent").title("ASN");
                        c.add("dealRef").title("Номер_Продукта");
                        c.add("opEndTime").title("Дата").format("dd.MM.yyyy HH:mm:ss").width(19);
                    }
                });
    }
}
