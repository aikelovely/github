package ru.alfabank.dmpr.model.cr.ClientTime;

import org.joda.time.LocalDate;

/**
 * Набор всех необходимых значения для отображения графиков.
 */
public class KpiDataItem {
    public String unitCode;
    public String unitName;
    public int processId;
    public LocalDate calcDate;
    public double totalDuration;
    public int bpCount;
    public int inKpiBpCount;
    public double quotaInDays;
    public double quotaPercent;
}
