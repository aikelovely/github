package ru.alfabank.dmpr.widget.cr.TTYAndTTM.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.*;
import ru.alfabank.dmpr.repository.cr.CRFilterRepository;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Выгрузка в Excel
 */
@Service
public class TTYAndTTMDynamicReport extends BaseReport<TTYAndTTMOptions> {
    @Autowired
    private TTYAndTTMRepository repository;

    @Autowired
    private CRFilterRepository crFilterRepository;

    public TTYAndTTMDynamicReport() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    protected String getReportName(final TTYAndTTMOptions options) {
        return "DynamicReport";
    }

    @Override
    protected void configure(ReportBuilder builder, final TTYAndTTMOptions options) {
        DynamicReportRow[] data = LinqWrapper.from(repository.getDynamic(options))
                .select(new Selector<Dynamic, DynamicReportRow>() {
                    @Override
                    public DynamicReportRow select(Dynamic rating) {
                        return new DynamicReportRow(rating, options);
                    }
                }).toArray(DynamicReportRow.class);


        builder.addWorksheet(DynamicReportRow.class)
                .bindTo(data)
                .title("Данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("processName").title("Процесс");
                        c.add("value").title(options.paramType == ParamType.AvgDuration
                                ? "Среднее значение"
                                : "Процент в KPI");
                        c.add("dealCount").title("Количество выдач").width(20);
                        c.add("ttxDuration").title("Время обработки заявок").width(20);
                        c.add("calcDate").title("Дата").format("dd.MM.yyyy");
                    }
                });
    }
}
