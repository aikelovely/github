package ru.alfabank.dmpr.infrastructure.helper;

import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Интерфейс для классов параметров витрин, имеющих фильтры начала/конца периода, а также
 * единицы агрегации данных во времени (по часам, дням, неделям ...)
 */
public interface PeriodOptions {
    LocalDate getStartDate();

    LocalDate getEndDate();

    Integer getTimeUnitId();
}
