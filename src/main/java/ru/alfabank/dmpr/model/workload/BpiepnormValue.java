package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

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
    public LocalDateTime EffectifFrom;
    public LocalDateTime EffectifTo;
    public LocalDateTime DateNow= LocalDateTime.now();
}
