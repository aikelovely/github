package ru.alfabank.dmpr.model.cr.TTYAndTTM;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Все необходимые значения для отображения динамики.
 */
public class Dynamic extends BaseEntity {
    public LocalDate calcDate;
    public Double averageValue;
    public double quotaInDays;
    public double quotaPercent;
    public Integer processId;
    public String processName;
    public int dealCount;
    public double ttxDuration;
    public int inKpiCount;
    public int outOfKpiCount;
}
