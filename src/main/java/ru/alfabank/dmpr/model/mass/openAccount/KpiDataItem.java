package ru.alfabank.dmpr.model.mass.openAccount;

import org.joda.time.LocalDate;

public class KpiDataItem {
    public long unitId;
    public String unitName;

    public LocalDate calcDate;

    public int bpCount;
    public int bpCountGrp1;
    public int bpCountGrp2;
    public int bpCountGrp3;
    public int bpCountGrp4;
    public int bpCountGrp5;
    public double sumDurationInHours;
    public double maxDurationInHours;
    public double diff;

    public String clientInn;
    public String clientPin;
    public String branchName;
    public String branchMnemonic;
}
