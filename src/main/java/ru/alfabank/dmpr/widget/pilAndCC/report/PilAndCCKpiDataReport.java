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
public class PilAndCCKpiDataReport extends BaseReport<PILAndCCOptions> {
    @Autowired
    PILAndCCRepository repository;

    public PilAndCCKpiDataReport() {
        super(PILAndCCOptions.class);
    }

    @Override
    protected String getReportName(PILAndCCOptions options) {
        return "KpiDataReport";
    }

    @Override
    protected void configure(ReportBuilder builder, PILAndCCOptions options) {
        ReportRow[] data = repository.getKpiDataReport(options);

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

                        c.add("kpiFactValueAvgAsString").title("Значение метрики по средней " +
                                "длительности в рамках границ показателя").width(40);
                        c.add("kpiFactValuePc").title("Значение метрики по доле заявок в нормативе на " +
                                "среднюю длительность в рамках границ показателя")
                                .width(40);

                        c.add("kpiPlanValueAvgAsString").title("Целевое значение по средней " +
                                "длительности в рамках границ показателя").width(40);
                        c.add("kpiPlanValuePc").title("Целевое значение по доле заявок " +
                                "в нормативе на среднюю длительность в рамках границ показателя")
                                .width(40);

                        c.add("kpiPlanFactValueAvgAsString").title("Отклонение от целевого значения" +
                                " по средней длительности").width(40);
                        c.add("kpiPlanFactValuePc").title("Отклонение от целевого значения по доле")
                                .width(40);
                    }
                });
    }
}

