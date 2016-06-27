package ru.alfabank.dmpr.model.leaderBoard;

import org.joda.time.LocalDate;

/**
 * Содержит все необходимые данные о KPI. Не все поля являются обязательными для заполнения
 */
public class KpiDataItem {
    /**
     * Отчетная дата. Последний день периода, за который было расчитано currentValue.
     */
    public LocalDate calcDate;

    /**
     * Цифровой ID KPI
     */
    public long kpiId;

    /**
     * Код KPI вида "KPIOB-1"
     */
    public String kpiCode;

    /**
     * Название KPI
     */
    public String kpiName;

    /**
     * ID группы подразделений
     */
    public long divisionGroupId;

    /**
     * Код группы подразделений
     */
    public String divisionGroupCode;

    /**
     * Имя группы подразделений
     */
    public String divisionGroupName;

    /**
     * Значение за предыдущий период
     */
    public Double prevValue;

    /**
     * Значение за текущий период
     */
    public Double currentValue;

    /**
     * Плановое значение
     */
    public Double planValue;

    /**
     * Описание. Используется только для KPIOB~9 и KPIOB~10.
     */
    public String description;
}

