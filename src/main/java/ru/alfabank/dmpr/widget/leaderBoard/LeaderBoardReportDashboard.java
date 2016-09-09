package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.model.leaderBoard.ReportDashboardQualityBo;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Created by U_M0U9C on 09.09.2016.
 */
@Service
public class LeaderBoardReportDashboard extends BaseReport<LeaderBoardOptions> {

    @Autowired
    private LeaderBoardRepository repository;

    public LeaderBoardReportDashboard() {
        super(LeaderBoardOptions.class);
    }

    @Override
    protected String getReportName(LeaderBoardOptions options) {
        return "Summary table KPE OB";
    }

    @Override
    protected void configure(ReportBuilder builder, LeaderBoardOptions options) {
        ReportDashboardQualityBo[] data = repository.getqualitybo();

        builder.addWorksheet(ReportDashboardQualityBo.class)
                .bindTo(data)
                .title("Детальные данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("valueDay").title("Дата").format("dd.MM.yyyy");
                        c.add("period").title("Период").width(15);
                        c.add("typePeriod").title("Тип Периода").width(15);
                        c.add("numPeriod").title("№ периода").width(15);
                        c.add("directorate").title("Дирекция").width(15);
                        c.add("nameType").title("Тип").width(15);
                        c.add("codeKpi").title("Код Показатель").width(15);
                        c.add("nameKpi").title("Показатель").width(15);
                        c.add("regions").title("Регион").width(15);
                        c.add("regionCenter").title("Региональный  центр").width(15);
                        c.add("factKpi").title("Факт KPI").width(15);
                        c.add("goalKpi").title("Цель KPI").width(15);
                        c.add("executekpi").title("Выполнение  KPI").width(15);
                    }
                });
    }
}
