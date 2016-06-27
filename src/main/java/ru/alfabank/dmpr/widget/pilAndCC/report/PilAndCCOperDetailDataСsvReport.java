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
public class PilAndCCOperDetailDataСsvReport extends BaseCsvReport<PILAndCCOptions> {
    @Autowired
    PILAndCCRepository repository;

    public PilAndCCOperDetailDataСsvReport() {
        super(PILAndCCOptions.class);
    }

    @Override
    protected String getReportName(PILAndCCOptions options) {
        return "OperDetailDataReport";
    }

    @Override
    protected void configure(CsvBuilder builder, PILAndCCOptions options) {
        ReportRow[] data = repository.getOperDetailDataCsvReport(options);

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

                        c.add("opStartTime").title("Левая граница этапа").format("dd.MM.yyyy HH:mm:ss");
                        c.add("opStartYear").title("Год (по правой левой этапа)");
                        c.add("opStartMonth").title("Месяц (по левой границе этапа)");
                        c.add("opStartWeek").title("Неделя (по левой границе этапа)");
                        c.add("opStartDay").title("День (по левой границе этапа)");
                        c.add("opEndTime").title("Правая граница этапа").format("dd.MM.yyyy HH:mm:ss");
                        c.add("opEndYear").title("Год (по правой границе этапа)");
                        c.add("opEndMonth").title("Месяц (по правой границе этапа)");
                        c.add("opEndWeek").title("Неделя (по правой границе этапа)");
                        c.add("opEndDay").title("День (по правой границе этапа)");

                        c.add("bpOperationName").title("Наименование этапа");
                        c.add("bpOperationDurCntAsString").title("Фактическая длительность этапа");
                        c.add("manualDurCntAsString").title("Чистое операционное время");
                        c.add("autoDurCntAsString").title("Время автоматических операций");
                        c.add("waitTimeDurCntAsString").title("Время ожиданий в очередях");
                        c.add("waitTimePreDurCntAsString").title("Время ожидания до предыдущего этапа");
                        c.add("operQuota1ValueAsString").title("Норматив 1 на этап");
                        c.add("operQuota2ValueAsString").title("Норматив 2 на этап");
                        c.add("operQuota1DifValueAsString").title("Отклонение от норматива 1");
                        c.add("operQuota2DifValueAsString").title("Отклонение от норматива 2");
                        c.add("operInQuota1Flag").title("Норматив 1 выполнен? Y/N");
                        c.add("operInQuota2Flag").title("Норматив 2 выполнен? Y/N");

                        c.add("regionName").title("Регион");
                        c.add("cityName").title("Город");
                        c.add("userSaleAgentName").title("Менеджер");
                        c.add("branchName").title("ДО/ККО");
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
