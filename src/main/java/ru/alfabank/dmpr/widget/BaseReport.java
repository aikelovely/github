package ru.alfabank.dmpr.widget;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.ReportBuilderResult;
import ru.alfabank.dmpr.infrastructure.export.ReportManager;
import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Базовый класс для всех виджетов, создающих выгрузку в Excel
 * @param <TOptions> тип класса параметров витрины
 */
public abstract class BaseReport<TOptions  extends BaseOptions> extends BaseWidget<TOptions, ReportBuilderResult> {
    public BaseReport(Class<TOptions> tOptionsClass) {
        super(tOptionsClass);
    }

    @Autowired
    protected ReportManager reportManager;

    protected abstract String getReportName(final TOptions options);

    protected abstract void configure(ReportBuilder builder, final TOptions options);

    @Override
    public ReportBuilderResult getData(TOptions options) {
        String reportName = getReportName(options);
        ReportBuilder builder = reportManager.createBuilder(reportName);

        configure(builder, options);

        return builder.build();
    }
}
