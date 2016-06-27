package ru.alfabank.dmpr.model.operKpi;

import org.joda.time.LocalDateTime;

/**
 * Содержит все необходимые данные для формирования выгрузки.
 */
public class OperKpiScorDetailValue {
    public String channelName;
    public String productType;
    public int bpOperationDuration;
    public String durGroupName;
    public String kpiLoanCode;
    public String moduleName;
    public LocalDateTime opEndTime;
    public String bpKpiName;

    public String bpOperationDurationAsString;
}
