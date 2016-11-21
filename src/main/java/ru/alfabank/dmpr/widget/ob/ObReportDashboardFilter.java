package ru.alfabank.dmpr.widget.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.ob.*;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;
import ru.alfabank.dmpr.repository.ob.ObQualityRepository;
import ru.alfabank.dmpr.widget.BaseReport;


/**
 * Created by U_M0U9C on 09.11.2016.
 */

@Service
public class ObReportDashboardFilter extends BaseReport<ObQualityOptions> {

    @Autowired
    private ObQualityRepository repository;


    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObReportDashboardFilter() {
        super(ObQualityOptions.class);
    }

  /*  public ObQualityQueryOptions getQueryOptions(ObQualityOptions options) {
        return null;
    }*/

    @Override
    protected String getReportName(ObQualityOptions options) {
        return "Summary table of quality OB(Filter)";
    }

    @Override
    protected void configure(ReportBuilder builder, ObQualityOptions options) {


        /*ObQualityQueryOptions queryOptions = getQueryOptions(options);*/

        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        addOptions.detailsMode = options.kpiId == null ? 2 : 1;
        addOptions.doudrFlag = options.doudrFlag;



        ObQualityQueryOptions queryOptions = new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromPeriod, filterRepository.getWeeks());



        ObReportSummaryQualityFilter[] data = repository.getsummarykpiob_filter(queryOptions);

        builder.addWorksheet(ObReportSummaryQualityFilter.class)
                .bindTo(data)
                .title("Детальные данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("valueday").title("Дата").format("dd.MM.yyyy");
                        c.add("period").title("Период").width(15);
                        c.add("typeperiod").title("Тип Периода").width(15);
                        c.add("numperiod").title("№ периода").width(15);
                        c.add("directorate").title("Дирекция").width(15);
                        c.add("nametype").title("Тип").width(15);
                        c.add("codekpi").title("Код Показатель").width(15);
                        c.add("namekpi").title("Показатель").width(15);
                        c.add("regions").title("Регион").width(15);
                        c.add("regioncenter").title("Региональный  центр").width(15);
                        c.add("direction").title("Направление").width(15);
                        c.add("summary").title("Всего").width(15);
                        c.add("successfully").title("Успешно").width(15);
                        c.add("factkpi").title("Факт KPI").format("0.00%");
                        c.add("goatkpi").title("Цель KPI").format("0.00%");
                        c.add("executekpi").title("Выполнение  KPI").format("0.00%");
                        c.add("descript").title("Комментарий").width(15);
                        c.add("duodr_flag").title("ДУОДР").width(15);
                    }
                });
    }
}
