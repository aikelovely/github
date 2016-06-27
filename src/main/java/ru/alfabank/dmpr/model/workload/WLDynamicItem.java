package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;

public class WLDynamicItem extends WorkloadDynamicItem {
    public LocalDate calcDate;
    public String unitId;
    public double workloadRate;
    public double staffCountCalc;
    public double staffCountFact;
}

