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
public class PilAndCCOperDetailDataReport extends BaseReport<PILAndCCOptions> {
    @Autowired
    PILAndCCRepository repository;

    public PilAndCCOperDetailDataReport() {
        super(PILAndCCOptions.class);
    }

    @Override
    protected String getReportName(PILAndCCOptions options) {
        return "OperDetailDataReport";
    }

    @Override
    protected void configure(ReportBuilder builder, PILAndCCOptions options) {
        ReportRow[] data = repository.getOperDetailDataReport(options);

        builder.addWorksheet(ReportRow.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("startDate").title("Период, с").format("dd.MM.yyyy").width(15);
                        c.add("endDate").title("Период, по").format("dd.MM.yyyy").width(15);
                        c.add("bpKpiName").title("Показатель");
                        c.add("productType").title("Продукт");
                        c.add("channelName").title("Канал");
                        c.add("bpOperationName").title("Этап");
                        c.add("moduleName").title("Система-источник");
                        c.add("opManualAvgAsString").title("Среднее чистое операционное время на этапе").width(29);
                        c.add("opAutoAvgAsString").title("Среднее время автоматических операций").width(29);
                        c.add("opWaitAvgAsString").title("Среднее время ожидания внутри этапа").width(29);
                        c.add("opWaitTimePreAvgAsString").title("Среднее время ожидания до предыдущего этапа").width(29);
                        c.add("opLenAvgAsString").title("Средняя длительность этапа").width(15);
                        c.add("opInQuotaAvg").title("Значение метрики по доле " +
                                "этапов в нормативе на среднюю длительность этапа").width(40);
                        c.add("operQuotaName").title("Тип норматива").width(15);
                        c.add("opPlanValueAvgAsString").title("Норматив на этап").width(15);
                        c.add("opPlanFactValueAvgAsString").title("Отклонение от норматива на этап").width(29);
                        c.add("opRepeatRatio").title("Коэффициент повторямости этапа").width(29);
                    }
                });
    }
}

