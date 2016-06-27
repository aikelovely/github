package ru.alfabank.dmpr.model.mass.openAccount;

import ru.alfabank.dmpr.infrastructure.helper.MathHelper;

public class MassOpenAccountRatingRow {
    public long unitId;
    public String unitName;
    public double percent;
    public double diff;

    public MassOpenAccountRatingRow(KpiDataItem item){
        this.unitId = item.unitId;
        this.unitName = item.unitName;
        this.percent = MathHelper.safeDivide(item.bpCountGrp1, item.bpCount) * 100;
        this.diff = item.diff * 100;
    }
}
