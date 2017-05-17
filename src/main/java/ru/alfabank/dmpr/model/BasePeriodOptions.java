package ru.alfabank.dmpr.model;

import org.joda.time.LocalDate;

public class BasePeriodOptions extends BaseOptions {
    /**
     * Значение фильтра "Год" используется в связке с фильтром "Период, с"
     * Обязательное поле.
     */
    public LocalDate startDate;
    public LocalDate startYear;

    /**
     * Значение фильтра "Год" используется в связке с фильтром "Период, по"
     * Обязательное поле.
     */
    public LocalDate endYear;

    /**
     * Может содержать значение фильтра "Период, с"
     * Обязательное поле.
     */
    public long startDateId;

    /**
     * Может содержать значение фильтра "Период, по"
     * Обязательное поле.
     */
    public long endDateId;

    /**
     * Может содержать значение фильтра "Тип периода"
     */
    public Integer timeUnitId;
}
