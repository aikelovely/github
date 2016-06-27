package ru.alfabank.dmpr.model.mass.openAccount;

import ru.alfabank.dmpr.infrastructure.helper.MathHelper;

public class MassOpenAccountDetailsTableRow {
    public long unitId;
    public String unitName;

    public int bpCount;
    public double inCrmPercent;

    public double bpGrp1Percent;
    public double bpGrp2Percent;
    public double bpGrp3Percent;
    public double bpGrp4Percent;
    public double bpGrp5Percent;

    public double avgDurationInDays;
    public double maxDurationInDays;

    public MassOpenAccountDetailsTableRow(KpiDataItem item){
        this.unitId = item.unitId;
        this.unitName = item.unitName;

        this.bpCount = item.bpCount;

        this.bpGrp1Percent = MathHelper.safeDivide(item.bpCountGrp1, item.bpCount) * 100;
        this.bpGrp2Percent = MathHelper.safeDivide(item.bpCountGrp2, item.bpCount) * 100;
        this.bpGrp3Percent = MathHelper.safeDivide(item.bpCountGrp3, item.bpCount) * 100;
        this.bpGrp4Percent = MathHelper.safeDivide(item.bpCountGrp4, item.bpCount) * 100;
        this.bpGrp5Percent = MathHelper.safeDivide(item.bpCountGrp5, item.bpCount) * 100;

        this.avgDurationInDays = MathHelper.safeDivide(item.sumDurationInHours, item.bpCount) / 24;
        this.maxDurationInDays = item.maxDurationInHours / 24;
    }
}
