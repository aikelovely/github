package ru.alfabank.dmpr.model.cr.ClientTime;

import org.joda.time.LocalDateTime;

/**
 * Набор всех необходимых значения для дет. выгрузки.
 */
public class KpiDetailsDataItem {
    public Double rootRequestId;
    public Double lmId;
    public Double lmDealState;
    public String requestTypeName;
    public String comitteName;
    public LocalDateTime firstFsDateTime;
    public LocalDateTime lastFsDateTime;
    public String blTypeName;
    public String blName;
    public String doName;
    public String deptName;
    public String creditDeptName;
    public String stageName;
    public String bpName;
    public String dpName;
    public Double dpRequestId;
    public LocalDateTime minStartDateTime;
    public LocalDateTime maxFinishDateTime;
    public LocalDateTime requestStartDateTime;
    public LocalDateTime requestFinishDateTime;
    public String actionName;
    public LocalDateTime actionDateTime;
    public Double actionDurationDays;
    public Double actionDurationHours;
    public String roleGroupName;
    public String roleName;
    public Double userId;
    public String userFio;
    public String cityName;
    public String blockName;
    public String departmentName;
    public String subDep1;
    public String subDep2;
    public String subDep3;
    public String subDep4;
    public String lastDep;
    public Double rkDepId;
    public String rkDepName;
    public String rkParentDepName;
    public String windowsLogin;
    public String businessLine;
    public String sFun;
    public String processTypeName;
    public String prodTpName;
    public Double sortOrder;
}
