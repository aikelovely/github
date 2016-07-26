package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;

/**
 * Created by U_M0NCT on 26.07.2016.
 */
public class BpiepnormValue {
    public String BpinnerendProductCcode;
    public String BpinnerendProductName;
    public String BporgRegionCcode;
    public String BporgRegionName;
    public String BpDivisionGroupCcode;
    public String BpDivisionGroupName;
    public double NormativeMinutes;
    public LocalDate EffectifFrom;
    public LocalDate EffectifTo;
}
