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
        ObDss[] data2 = repository.getObDss(queryOptions);
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
                }     );


        builder.addWorksheet(ObDss.class)
                .bindTo(data2)
                .title("Dss ошибки")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("division_name").title("Отдел");
                        c.add("user_name").title("ФИО сотрудника").width(24);
                        c.add("loanappl_ref").title("Номер заявки в КК").width(24);
                        c.add("slaoperationsrc_name").title("Операция").width(24);
                        c.add("slaoperationsrc_no").title("ID операции").width(24);
                        c.add("operation_start_time").title("Время начала операции").format("dd.MM.yyyy hh:mm:ss").width(24);
                        c.add("operation_end_time").title("Время завершения операции").format("dd.MM.yyyy HH:mm:ss ").width(24);
                        c.add("di").title("Доп.инфо и согласования").width(24);
                        c.add("time2").title("Продолжительность операции").width(24);
                    }
                });

        ObDssemployees[] data3 = repository.getObDssEmployees(queryOptions);

        builder.addWorksheet(ObDssemployees.class)
                .bindTo(data3)
                .title("Dss сотрудники")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("division_name").title("Отдел");
                        c.add("user_name").title("ФИО сотрудника").width(15);
                        c.add("oper1").title("Завершенные операции").width(15);
                        c.add("goodtime24").title("Своевременно за 24ч, заявки").width(15);
                        c.add("goodtime24p").title("Своевременно за 24ч, %").width(15).format("0.00%").width(15);
                        c.add("goodtime48").title("Своевременно за 48ч, заявки").width(15);
                        c.add("goodtime48p").title("Своевременно за 48ч, %").format("0.00%").width(15);
                        c.add("dop").title("Доп.инфо и согласования").width(15);
                    }
                });


    }
}
