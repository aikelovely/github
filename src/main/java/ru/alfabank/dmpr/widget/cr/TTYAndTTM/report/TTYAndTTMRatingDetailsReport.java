package ru.alfabank.dmpr.widget.cr.TTYAndTTM.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.TableWorksheetBuilder;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.RatingReportRow;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.repository.cr.CRFilterRepository;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Выгрузка в Excel
 */
@Service
public class TTYAndTTMRatingDetailsReport extends BaseReport<TTYAndTTMOptions> {
    @Autowired
    private TTYAndTTMRepository repository;

    @Autowired
    private CRFilterRepository crFilterRepository;

    public TTYAndTTMRatingDetailsReport() {
        super(TTYAndTTMOptions.class);
    }

    @Override
    protected String getReportName(final TTYAndTTMOptions options) {
        return "RatingDetailsReport";
    }

    @Override
    protected void configure(ReportBuilder builder, final TTYAndTTMOptions options) {
        final boolean withDrillDown = options.processIds.length != 1;

        final List<Rating> data;

        if(withDrillDown) {
            data = new ArrayList<>();
            LinqWrapper.from(repository.getRatingDetails(options))
                    .filter(new Predicate<Rating>() {
                        @Override
                        public boolean check(Rating item) {
                            return item.processId == null;
                        }
                    }).each(new Action<Rating>() {
                @Override
                public void act(final Rating parentRow) {
                    parentRow.processName = "Все";
                    data.add(parentRow);

                    LinqWrapper.from(repository.getRatingDetails(options))
                            .filter(new Predicate<Rating>() {
                                @Override
                                public boolean check(Rating item) {
                                    return item.id == parentRow.id && item.processId != null;
                                }
                            })
                            .each(new Action<Rating>() {
                                @Override
                                public void act(Rating value) {
                                    data.add(value);
                                }
                            });
                }
            });
        } else {
            data = LinqWrapper.from(repository.getRatingDetails(options))
                    .filter(new Predicate<Rating>() {
                        @Override
                        public boolean check(Rating item) {
                            return item.processId != null;
                        }
                    }).toList();
        }

        TableWorksheetBuilder<Rating> worksheetBuilder = builder.addWorksheet(Rating.class)
                .bindTo(data)
                .title("Данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("name").title("Значение");
                        c.add("processName").title("Процесс");
                        c.add("dealCount").title("Количество выдач").width(20);
                        c.add("ttxDuration").title("Время обработки заявок").width(20);
                        c.add("averageValue").title("Отклонение");
                    }
                });
    }
}
