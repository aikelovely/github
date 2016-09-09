package ru.alfabank.dmpr.widget.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.mapper.ob.ObQualityMapper;
import ru.alfabank.dmpr.model.ctq.CTQDashboardReportOptions;
import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;
import ru.alfabank.dmpr.model.ob.ObReportSummaryQuality;
import ru.alfabank.dmpr.repository.ob.ObQualityRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Created by U_M0U9C on 09.09.2016.
 */
@Service
public class ObReportDashboard extends BaseReport<ObQualityAdditionalOptions> {

    @Autowired
    private ObQualityRepository repository;

    public ObReportDashboard() {
        super(ObQualityAdditionalOptions.class);
    }


    @Override
    protected String getReportName(ObQualityAdditionalOptions options) {
        return "Summary table of quality OB";
    }

    @Override
    protected void configure(ReportBuilder builder, ObQualityAdditionalOptions options) {

   ObReportSummaryQuality[] data = repository.getsummarykpiob();

   builder.addWorksheet(ObReportSummaryQuality.class)
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
                        c.add("factkpi").title("Факт KPI").width(15);
                        c.add("goalkpi").title("Цель KPI").width(15);
                        c.add("executekpi").title("Выполнение  KPI").width(15);
                        c.add("descript").title("Комментарий").width(15);
                        c.add("duodr_flag").title("ДУОДР").width(15);
                    }
                });
    }
}


