package ru.alfabank.dmpr.model.mass.decomposition;

import org.joda.time.LocalDate;

public class KpiDataItem {
    public long unitId;
    public String unitName;

    public LocalDate calcDate;

    public int bpCount;
    public long stageId;
    public int inLimitBpCount;
    public double totalDuration;
}
