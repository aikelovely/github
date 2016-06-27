package ru.alfabank.dmpr.model.pilAndCC;

import org.joda.time.LocalDate;

/**
 * Набор данных для отображения графика "Динамика изменения доли каждого этапа в процессе"
 */
public class KpiDurationDynamic {
    public int reportTypeId;
    public LocalDate calcDate;
    public long operationId;
    public String operationName;
    public double value;
    public double groupDuration;
}
