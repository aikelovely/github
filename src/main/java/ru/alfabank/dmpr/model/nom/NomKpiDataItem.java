package ru.alfabank.dmpr.model.nom;

import org.joda.time.LocalDate;

/**
 * Содержит все необходимые данные о KPI. Не все поля являются обязательными для заполнения
 */
public class NomKpiDataItem {
    /**
     * Отчетная дата. Последний день периода, за который было расчитано currentValue.
     */
    public LocalDate calcDate;

    /**
     * Номер недели/месяца в году.
     */
    public int periodNum;

    /**
     * Id группы подразделений
     */
    public long divisionGroupId;

    /**
     * Id конечного продукта
     */
    public long innerEndProductId;

    /**
     * Количество КП
     */
    public int factCount;

    /**
     * Признак ручной проверки
     */
    public String isManual; // из базы возвращает "Y" or "N"
}
