package ru.alfabank.dmpr.widget.pilAndCC.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.model.pilAndCC.ReportRow;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseCsvReport;

@Service
public class PilAndCCKpiDetailDataCsvReport extends BaseCsvReport<PILAndCCOptions> {
    @Autowired
    PILAndCCRepository repository;

    public PilAndCCKpiDetailDataCsvReport() {
        super(PILAndCCOptions.class);
    }

    @Override
    protected String getReportName(PILAndCCOptions options) {
        return "KpiDetailDataCsvReport";
    }

    @Override
    protected void configure(CsvBuilder builder, PILAndCCOptions options) {
        ReportRow[] data = repository.getKpiDetailDataСsvReport(options);

        builder.configure(ReportRow.class)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("dealRef").title("ID Кредитной заявки");
                        c.add("loanapplRefParent").title("ID Родительской заявки ");
                        c.add("bpKpiName").title("Наименование KPI/Метрики");
                        c.add("productType").title("Продукт");
                        c.add("channelName").title("Канал");
                        c.add("manualCheckFlag").title("Наличие ручных проверок Y/N");
                        c.add("clientSegmentName").title("Тип Клиента");
                        c.add("bpKpiStartTime").title("Левая граница KPI ").format("dd.MM.yyyy HH:mm:ss");
                        c.add("bpKpiStartYear").title("Год (по левой границе KPI)");
                        c.add("bpKpiStartMonth").title("Месяц (по левой границе KPI)");
                        c.add("bpKpiStartWeek").title("Неделя (по левой границе KPI)");
                        c.add("bpKpiStartDay").title("День (по левой границе KPI)");
                        c.add("bpKpiEndTime").title("Правая граница KPI").format("dd.MM.yyyy HH:mm:ss");
                        c.add("bpKpiEndYear").title("Год (по правой границе KPI)");
                        c.add("bpKpiEndMonth").title("Месяц (по правой границе KPI)");
                        c.add("bpKpiEndWeek").title("Неделя (по правой границе KPI)");
                        c.add("bpKpiEndDay").title("День (по правой границе KPI)");
                        c.add("regionName").title("Регион");
                        c.add("cityName").title("Город");
                        c.add("userSaleAgentName").title("Менеджер");
                        c.add("branchName").title("ДО/ККО");
                        c.add("clientWaitDurCntAsString").title("Время ожидания клиента");
                        c.add("kpiDurCntAsString").title("Фактическое значение KPI");
                        c.add("kpiPlanValueAvgAsString").title("Норматив KPI");
                        c.add("kpiPlanFactValueAvgAsString").title("Отконение от норматива KPI");
                        c.add("kpiInQuotaFlag").title("KPI по заявке выполнен Y/N");
                        c.add("loanapplStatusTypeName").title("Тип заявки (выдана, отказ)");
                        c.add("cardActivateFlag").title("Карта активирована Y/N");
                        c.add("clientSelfRejectFlag").title("Клиент отказался Y/N");
                        c.add("kpiDurationName").title("Категория группы длительности");
                        c.add("moduleName").title("Система-источник");
                    }
                });
    }
}
