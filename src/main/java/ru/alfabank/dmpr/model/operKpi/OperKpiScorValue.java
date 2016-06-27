package ru.alfabank.dmpr.model.operKpi;

/**
 * Содержит все необходимые данные для формирования отчета по KPI на длительность скорингов за период.
 */
public class OperKpiScorValue {
    public long reportTypeId;
    public String reportTypeName;
    public String bpKpiCode;
    public String bpKpiName;
    public String durGroupName;
    public double factValuePIL;
    public double factValueCC;
    public int sortOrder;
}

