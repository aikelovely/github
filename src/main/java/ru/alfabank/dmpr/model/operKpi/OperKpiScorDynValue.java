package ru.alfabank.dmpr.model.operKpi;

import org.joda.time.LocalDate;

/**
 * Содержит все необходимые данные для формирования графики Динамика KPI на длительность скорингов.
 */
public class OperKpiScorDynValue {
    public long reportTypeId;
    public String reportTypeName;
    public LocalDate calcDate;
    public String durGroupName;
    public double factValueTotal;
    public int sortOrder;
}
