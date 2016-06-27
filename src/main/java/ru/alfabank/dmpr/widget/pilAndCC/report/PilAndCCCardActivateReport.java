package ru.alfabank.dmpr.widget.pilAndCC.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.model.pilAndCC.ReportRow;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseReport;

@Service
public class PilAndCCCardActivateReport extends BaseReport<PILAndCCOptions> {
    @Autowired
    PILAndCCRepository repository;

    public PilAndCCCardActivateReport() {
        super(PILAndCCOptions.class);
    }

    @Override
    protected String getReportName(PILAndCCOptions options) {
        return "CardActivateReport";
    }

    @Override
    protected void configure(ReportBuilder builder, PILAndCCOptions options) {
        ReportRow[] data = repository.getCardActivateReport(options);

        builder.addWorksheet(ReportRow.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("startDate").title("Период, с").format("dd.MM.yyyy").width(15);
                        c.add("endDate").title("Период, по").format("dd.MM.yyyy").width(15);
                        c.add("productType").title("Продукт");
                        c.add("channelName").title("Канал");
                        c.add("moduleName").title("Система-источник");
                        c.add("kpiFactValuePc").title("Знчение метрики \"Процент активации карт\"").width(29);
                        c.add("kpiPlanValuePc").title("Значение норматива");
                        c.add("kpiPlanFactValuePc").title("Отклонение от норматива").width(25);
                    }
                });
    }
}

