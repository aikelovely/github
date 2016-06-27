package ru.alfabank.dmpr.model.pilAndCC;

import org.joda.time.LocalDate;

/**
 * Набор данных для отображения динамики по выбранному этапу.
 */
public class KpiDynamicByStage {
    public int reportTypeId;
    public LocalDate calcDate;
    public long operationId;
    public String operationName;
    public long autoOperDuration;
    public long manualOperDuration;
    public long waitTimeOperDuration;
    public double value;
    public double planValue;
    public long count;
    public int quotaCategoryId;
    public String quotaCategoryName;
}
