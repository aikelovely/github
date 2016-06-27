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
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDetailsDataItem;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.RatingReportRow;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.repository.cr.CRFilterRepository;
import ru.alfabank.dmpr.repository.cr.ClientTimeRepository;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Выгрузка в Excel
 */
@Service
public class TTYAndTTMRatingReport extends BaseReport<TTYAndTTMOptions> {
    @Autowired
    private TTYAndTTMRepository repository;

    @Autowired
    private CRFilterRepository crFilterRepository;

    public TTYAndTTMRatingReport() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    protected String getReportName(final TTYAndTTMOptions options) {
        return "RatingReport";
    }

    @Override
    protected void configure(ReportBuilder builder, final TTYAndTTMOptions options) {
        RatingReportRow[] data = LinqWrapper.from(repository.getRating(options))
                .select(new Selector<Rating, RatingReportRow>() {
                    @Override
                    public RatingReportRow select(Rating rating) {
                        return new RatingReportRow(rating, options);
                    }
                }).toArray(RatingReportRow.class);

        final BaseEntity systemUnit = LinqWrapper.from(crFilterRepository.getSystemUnits())
                .first(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(BaseEntity item) {
                        return item.id == options.systemUnitId;
                    }
                });

        builder.addWorksheet(RatingReportRow.class)
                .bindTo(data)
                .title("Данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("name").title(systemUnit.name);
                        c.add("processName").title("Процесс");
                        c.add("value").title("Фактическое значение");
                        c.add("planValue").title("Плановое значение");
                    }
                });
    }
}
