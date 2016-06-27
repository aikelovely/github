package ru.alfabank.dmpr.model.cr.TTYAndTTM;

import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.model.ParamType;

public class RatingReportRow {
    public String name;
    public String processName;
    public Double value;
    public double planValue;

    public RatingReportRow(Rating rating, final TTYAndTTMOptions options){
        this.name = rating.name;
        this.processName = rating.processName;

        if(options.paramType == ParamType.AvgDuration){
            this.value = rating.averageValue;
            this.planValue = rating.quotaInDays;
        } else {
            if((rating.inKpiCount + rating.outOfKpiCount) == 0){
                this.value = null;
            } else {
                this.value = Math.round(MathHelper.safeDivide(rating.inKpiCount,
                        rating.inKpiCount + rating.outOfKpiCount) * 10000) / 100d;
            }
            this.planValue = rating.quotaPercent;
        }
    }
}
