package ru.alfabank.dmpr.widget.cr.ClientTime.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDetailsDataItem;
import ru.alfabank.dmpr.repository.cr.ClientTimeRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Выгрузка в Excel
 */
@Service
public class ClientTimeDetails extends BaseReport<ClientTimeOptions> {
    @Autowired
    private ClientTimeRepository repository;

    public ClientTimeDetails() {
        super(ClientTimeOptions.class);
    }

    @Override
    protected String getReportName(final ClientTimeOptions options) {
        return "DetailsReport1";
    }

    @Override
    protected void configure(ReportBuilder builder, final ClientTimeOptions options) {
        KpiDetailsDataItem[] data = repository.getKpiDetailsData(options);

        builder.addWorksheet(KpiDetailsDataItem.class)
                .bindTo(data)
                .title("Данные")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("rootRequestId").title("ROOT_REQUEST_ID");
                        c.add("lmId").title("LM_ID");
                        c.add("lmDealState").title("LM_dealstate");
                        c.add("requestTypeName").title("Тип заявки");
                        c.add("comitteName").title("Уполномоченный орган");
                        c.add("firstFsDateTime").title("FIRST_FS_DATETIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("lastFsDateTime").title("LAST_FS_DATETIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("blTypeName").title("BL_TYPE_NAME");
                        c.add("blName").title("BL_NAME");
                        c.add("doName").title("DO_NAME");
                        c.add("deptName").title("DEPT_NAME");
                        c.add("creditDeptName").title("CREDIT_DEPT_NAME");
                        c.add("stageName").title("STAGE_NAME");
                        c.add("bpName").title("BP_NAME");
                        c.add("dpName").title("DP_NAME");
                        c.add("dpRequestId").title("DP_REQUEST_ID");
                        c.add("minStartDateTime").title("MIN_START_DATETIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("maxFinishDateTime").title("MAX_FINISH_DATETIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("requestStartDateTime").title("REQUEST_START_TIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("requestFinishDateTime").title("REQUEST_FINISH_TIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("actionName").title("ACTION_NAME");
                        c.add("actionDateTime").title("ACTIONDATETIME").format("dd.MM.yyyy HH:mm:ss");
                        c.add("actionDurationDays").title("ACTION_DURATION_DAYS");
                        c.add("actionDurationHours").title("ACTION_DURATION_HOURS");
                        c.add("roleGroupName").title("ROLEGROUP_NAME");
                        c.add("roleName").title("ROLENAME");
                        c.add("userId").title("USERID");
                        c.add("userFio").title("USER_FIO");
                        c.add("cityName").title("CITY");
                        c.add("blockName").title("BLOCK");
                        c.add("departmentName").title("DEPARTMENT_NAME");
                        c.add("subDep1").title("SUBDEP1");
                        c.add("subDep2").title("SUBDEP2");
                        c.add("subDep3").title("SUBDEP3");
                        c.add("subDep4").title("SUBDEP4");
                        c.add("lastDep").title("LASTDEPT");
                        c.add("rkDepId").title("RK_DEPT_ID");
                        c.add("rkDepName").title("RK_DEPT_NAME");
                        c.add("rkParentDepName").title("RK_PARENT_DEPT_NAME");
                        c.add("windowsLogin").title("WINDOWSLOGIN");
                        c.add("businessLine").title("BUSINESS_LINE");
                        c.add("sFun").title("S_FUN");
                        c.add("processTypeName").title("PROCESSTYPE_NAME");
                        c.add("prodTpName").title("PRODTP_NAME");
                    }
                });
    }
}
