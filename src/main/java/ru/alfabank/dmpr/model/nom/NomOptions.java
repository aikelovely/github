package ru.alfabank.dmpr.model.nom;

import ru.alfabank.dmpr.model.BasePeriodOptions;

/**
 * Параметры фильтров витрины "Количество конечных продуктов для расчета UC ОБ"
 */
public class NomOptions extends BasePeriodOptions {
    /**
     * Список значений фильтра "Группа подразделений"
     */
    public long[] divisionGroupIds;

    /**
     * Список идентификаторов конечных продуктов
     */
    public long[] innerProductIds;

    /**
     * Список идентификаторов  подпродуктов
     */
    public long[] subInnerProductIds;

    /**
     * Уровень детализации
     */
    public int[] levels;

    /**
     * Запрос последнего периода
     */
    public int onlyLastPeriod;
}
