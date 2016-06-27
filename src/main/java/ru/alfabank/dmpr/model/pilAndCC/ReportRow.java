package ru.alfabank.dmpr.model.pilAndCC;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;

/**
 * Содержит все необходимые поля для всех выгрузок на витрине PIL&CC
 */
public class ReportRow {
    public LocalDate startDate;
    public LocalDate endDate;

    public String dealRef;
    public String loanapplRefParent;
    public String bpKpiName;
    public String productType;
    public String channelName;
    public String manualCheckFlag;
    public String clientSegmentName;

    public LocalDateTime bpKpiStartTime;
    public int bpKpiStartYear;
    public int bpKpiStartMonth;
    public int bpKpiStartWeek;
    public int bpKpiStartDay;

    public LocalDateTime bpKpiEndTime;
    public int bpKpiEndYear;
    public int bpKpiEndMonth;
    public int bpKpiEndWeek;
    public int bpKpiEndDay;

    public LocalDateTime opStartTime;
    public int opStartYear;
    public int opStartMonth;
    public int opStartWeek;
    public int opStartDay;

    public LocalDateTime opEndTime;
    public int opEndYear;
    public int opEndMonth;
    public int opEndWeek;
    public int opEndDay;

    public int opManualAvg;
    public int opAutoAvg;
    public int opWaitAvg;
    public int opWaitTimePreAvg;
    public int opLenAvg;
    public int opInQuotaAvg;
    public String operQuotaName;
    public int opPlanValueAvg;
    public int opPlanFactValueAvg;
    public double opRepeatRatio;


    public String bpOperationName;
    public int bpOperationDurCnt;
    public int manualDurCnt;
    public int autoDurCnt;
    public int waitTimeDurCnt;
    public int waitTimePreDurCnt;
    public int operQuota1Value;
    public int operQuota2Value;
    public int operQuota1DifValue;
    public int operQuota2DifValue;
    public String operInQuota1Flag;
    public String operInQuota2Flag;

    public String regionName;
    public String cityName;
    public String userSaleAgentName;
    public String branchName;
    public String loanapplStatusTypeName;
    public String cardActivateFlag;
    public String clientSelfRejectFlag;
    public String kpiDurationName;
    public String moduleName;

    public int clientWaitDurCnt;
    public int kpiDurCnt;
    public String kpiInQuotaFlag;

    public int kpiFactValueAvg;
    public double kpiFactValuePc;

    public int kpiPlanValueAvg;
    public double kpiPlanValuePc;

    public int kpiPlanFactValueAvg;
    public double kpiPlanFactValuePc;

    // Длительность в формате [HH].mm.ss. Расчитывается не в репозитории.
    public String opManualAvgAsString;
    public String opAutoAvgAsString;
    public String opWaitAvgAsString;
    public String opWaitTimePreAvgAsString;
    public String opLenAvgAsString;
    public String opPlanValueAvgAsString;
    public String opPlanFactValueAvgAsString;

    public String bpOperationDurCntAsString;
    public String manualDurCntAsString;
    public String autoDurCntAsString;
    public String waitTimeDurCntAsString;
    public String waitTimePreDurCntAsString;
    public String operQuota1ValueAsString;
    public String operQuota2ValueAsString;
    public String operQuota1DifValueAsString;
    public String operQuota2DifValueAsString;

    public String clientWaitDurCntAsString;
    public String kpiDurCntAsString;

    public String kpiFactValueAvgAsString;
    public String kpiPlanValueAvgAsString;
    public String kpiPlanFactValueAvgAsString;

    public void fillDurationFields(){
        opManualAvgAsString = DateHelper.secondsAsString(opManualAvg);
        opAutoAvgAsString = DateHelper.secondsAsString(opAutoAvg);
        opWaitAvgAsString = DateHelper.secondsAsString(opWaitAvg);
        opWaitTimePreAvgAsString = DateHelper.secondsAsString(opManualAvg);
        opLenAvgAsString = DateHelper.secondsAsString(opLenAvg);
        opPlanValueAvgAsString = DateHelper.secondsAsString(opPlanValueAvg);
        opPlanFactValueAvgAsString = DateHelper.secondsAsString(opPlanFactValueAvg);

        bpOperationDurCntAsString = DateHelper.secondsAsString(bpOperationDurCnt);
        manualDurCntAsString = DateHelper.secondsAsString(manualDurCnt);
        autoDurCntAsString = DateHelper.secondsAsString(autoDurCnt);
        waitTimeDurCntAsString = DateHelper.secondsAsString(waitTimeDurCnt);
        waitTimePreDurCntAsString = DateHelper.secondsAsString(waitTimePreDurCnt);
        operQuota1ValueAsString = DateHelper.secondsAsString(operQuota1Value);
        operQuota2ValueAsString = DateHelper.secondsAsString(operQuota2Value);
        operQuota1DifValueAsString = DateHelper.secondsAsString(operQuota1DifValue);
        operQuota2DifValueAsString = DateHelper.secondsAsString(operQuota2DifValue);

        clientWaitDurCntAsString = DateHelper.secondsAsString(clientWaitDurCnt);
        kpiDurCntAsString = DateHelper.secondsAsString(kpiDurCnt);

        kpiFactValueAvgAsString = DateHelper.secondsAsString(kpiFactValueAvg);
        kpiPlanValueAvgAsString = DateHelper.secondsAsString(kpiPlanValueAvg);
        kpiPlanFactValueAvgAsString = DateHelper.secondsAsString(kpiPlanFactValueAvg);
    }
}
