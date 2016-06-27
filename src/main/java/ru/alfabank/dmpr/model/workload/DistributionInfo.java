package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;

public class DistributionInfo extends WorkloadDynamicItem {
    public LocalDate calcdate;
    public String id;
    public String unitName;
    public double staffCountFact;
}
