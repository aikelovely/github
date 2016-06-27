package ru.alfabank.dmpr.model.cr.TTYAndTTM;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Все необходимые значения для отображения рейтинга.
 */
public class Rating extends BaseEntity {
    public Double averageValue;
    public double minValue;
    public double maxValue;
    public double quotaInDays;
    public double quotaPercent;
    public Integer processId;
    public String processName;
    public int dealCount;
    public double ttxDuration;
    public int inKpiCount;
    public int outOfKpiCount;
}

