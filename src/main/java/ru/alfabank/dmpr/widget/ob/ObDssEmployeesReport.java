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
public class ObDssEmployeesReport extends BaseReport<ObQualityOptions> {

    @Autowired
    private ObQualityRepository repository;


    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObDssEmployeesReport() {
        super(ObQualityOptions.class);
    }

  /*  public ObQualityQueryOptions getQueryOptions(ObQualityOptions options) {
        return null;
    }*/

    @Override
    protected String getReportName(ObQualityOptions options) {
        return "ObDss ошибки";
    }

    @Override
    protected void configure(ReportBuilder builder, ObQualityOptions options) {


        /*ObQualityQueryOptions queryOptions = getQueryOptions(options);*/

        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();



        ObQualityQueryOptions queryOptions = new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromPeriod, filterRepository.getWeeks());



        ObDssemployees[] data = repository.getObDssEmployees(queryOptions);

        builder.addWorksheet(ObDssemployees.class)
                .bindTo(data)
                .title("Детальные данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("division_name").title("Отдел");
                        c.add("user_name").title("ФИО сотрудника").width(15);
                        c.add("oper1").title("Завершенные операции").width(15);
                        c.add("goodtime24").title("Своевременно за 24ч, заявки").width(15);
                        c.add("goodtime24p").title("Своевременно за 24ч, %").width(15);
                        c.add("goodtime48").title("Своевременно за 48ч, заявки").format("dd.MM.yyyy hh:mi:ss").width(15);
                        c.add("goodtime48p").title("Своевременно за 48ч, %").format("dd.MM.yyyy hh:mi:ss").width(15);
                        c.add("dop").title("Доп.инфо и согласования").width(15);
                    }
                });
    }
}
