package ru.alfabank.dmpr.model.operKpi;

import org.joda.time.LocalDateTime;

/**
 * Содержит все необходимые данные для формирования выгрузок.
 */
public class OperKpiDetailValue {
    public String channelName;
    public String productType;
    public int bpOperationDuration;
    public String durGroupName;
    public String loanapplRefParent;
    public String dealRef;
    public LocalDateTime opEndTime;

    public String bpOperationDurationAsString;
}

