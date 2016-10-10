package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;

public class ESUDynamicItem extends WorkloadDynamicItem {
    public LocalDate calcDate;
    public String unitId;
    public String parentUnitId;
    public String unitName;

    public long divisionGroupId;
    public long regionId;
    public double iepRate;
    public int iepCount;
    public double staffCountCalc;

}
