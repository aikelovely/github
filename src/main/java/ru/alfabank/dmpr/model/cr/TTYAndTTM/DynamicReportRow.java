package ru.alfabank.dmpr.model.cr.TTYAndTTM;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.model.ParamType;

public class DynamicReportRow {
    public String name;
    public String processName;
    public Double value;
    public int dealCount;
    public double ttxDuration;
    public LocalDate calcDate;

    public DynamicReportRow(Dynamic dynamic, final TTYAndTTMOptions options){
        this.name = dynamic.name;
        this.processName = dynamic.processName;
        this.dealCount = dynamic.dealCount;
        this.ttxDuration = dynamic.ttxDuration;
        this.calcDate = dynamic.calcDate;

        if(options.paramType == ParamType.AvgDuration){
            this.value = dynamic.averageValue;
        } else {
            if((dynamic.inKpiCount + dynamic.outOfKpiCount) == 0){
                this.value = null;
            } else {
                this.value = Math.round(MathHelper.safeDivide(dynamic.inKpiCount,
                        dynamic.inKpiCount + dynamic.outOfKpiCount) * 10000) / 100d;
            }
        }
    }
}
