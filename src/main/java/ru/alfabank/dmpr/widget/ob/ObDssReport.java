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
public class ObDssReport extends BaseReport<ObQualityOptions> {

    @Autowired
    private ObQualityRepository repository;


    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObDssReport() {
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
        addOptions.detailsMode = options.kpiId == null ? 2 : 1;
        addOptions.doudrFlag = options.doudrFlag;



        ObQualityQueryOptions queryOptions = new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromPeriod, filterRepository.getWeeks());



        ObDss[] data = repository.getObDss(queryOptions);

        builder.addWorksheet(ObDss.class)
                .bindTo(data)
                .title("Детальные данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("division_name").title("Отдел");
                        c.add("user_name").title("ФИО сотрудника").width(15);
                        c.add("loanappl_ref").title("Номер заявки в КК").width(15);
                        c.add("slaoperationsrc_name").title("Операция").width(15);
                        c.add("slaoperationsrc_no").title("ID операции").width(15);
                        c.add("operation_start_time").title("Время начала операции").format("dd.MM.yyyy hh:mi:ss").width(15);
                        c.add("operation_end_time").title("Время завершения операции").format("dd.MM.yyyy hh:mi:ss").width(15);
                        c.add("di").title("Доп.инфо и согласования").width(15);
                        c.add("time2").title("Продолжительность операции").width(15);
                    }
                });
    }
}
