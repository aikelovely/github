package ru.alfabank.dmpr.model.mass.openAccount;

import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;

public class MassOpenAccountDetailsReportRow {
    public String firstLvlUnitName;
    public String secondLvlUnitName;

    public String clientInn;
    public String clientPin;
    public String branchName;
    public String branchMnemonic;

    public int bpCount;
    public double inCrmPercent;

    public int bpCountGrp1;
    public int bpCountGrp2;
    public int bpCountGrp3;
    public int bpCountGrp4;
    public int bpCountGrp5;

    public double bpGrp1Percent;
    public double bpGrp2Percent;
    public double bpGrp3Percent;
    public double bpGrp4Percent;
    public double bpGrp5Percent;

    public String avgDurationInDays;
    public String maxDurationInDays;

    public MassOpenAccountDetailsReportRow(KpiDataItem item, String firstLvlUnitName, String secondLvlUnitName){
        this.firstLvlUnitName = firstLvlUnitName;
        this.secondLvlUnitName = secondLvlUnitName;


        this.clientInn = item.clientInn;
        this.clientPin = item.clientPin;
        this.branchName = item.branchName;
        this.branchMnemonic = item.branchMnemonic;

        this.bpCount = item.bpCount;

        this.bpCountGrp1 = item.bpCountGrp1;
        this.bpCountGrp2 = item.bpCountGrp2;
        this.bpCountGrp3 = item.bpCountGrp3;
        this.bpCountGrp4 = item.bpCountGrp4;
        this.bpCountGrp5 = item.bpCountGrp5;

        this.bpGrp1Percent = MathHelper.safeDivide(item.bpCountGrp1, item.bpCount) * 100;
        this.bpGrp2Percent = MathHelper.safeDivide(item.bpCountGrp2, item.bpCount) * 100;
        this.bpGrp3Percent = MathHelper.safeDivide(item.bpCountGrp3, item.bpCount) * 100;
        this.bpGrp4Percent = MathHelper.safeDivide(item.bpCountGrp4, item.bpCount) * 100;
        this.bpGrp5Percent = MathHelper.safeDivide(item.bpCountGrp5, item.bpCount) * 100;

        this.avgDurationInDays = DateHelper.hoursToDDHH(MathHelper.safeDivide(item.sumDurationInHours, item.bpCount));
        this.maxDurationInDays = DateHelper.hoursToDDHH(item.maxDurationInHours);
    }
}
