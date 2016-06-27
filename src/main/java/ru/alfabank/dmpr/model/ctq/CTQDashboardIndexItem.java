package ru.alfabank.dmpr.model.ctq;

import org.joda.time.LocalDate;

/**
 * Содержит все необходимые данные для отображения графиков витрины показателей CTQ.
 */
public class CTQDashboardIndexItem {
    public String kpiid;
    public String kpiname;
    public String kpishortname;
    public LocalDate timeUnitDD;
    public Integer timeUnitYear;
    public Long timeUnitPrdNum;
    public Long sortOrder;
    public Double kpiNorm;
    public Long inKpiCount;
    public Long totalCount;
    public Double kpiRatioAvg;
    public Double kpi2RatioAvg;
    public String description;
    public Long  periodOrd;
}
