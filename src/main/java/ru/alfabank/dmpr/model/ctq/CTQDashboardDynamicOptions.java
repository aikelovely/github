package ru.alfabank.dmpr.model.ctq;

import ru.alfabank.dmpr.model.BasePeriodOptions;

/**
 * Набор параметров для получений данных о KPI витрины показателей CTQ.
 */
public class CTQDashboardDynamicOptions extends BasePeriodOptions {
    /**
     * Может содержать значение фильтра "Факт/Ур. качества"
     * Обязательное поле.
     */
    public CTQDashboardParamType paramType;

    /**
     * Может содержать список кодов показателей
     * Необязательное поле.
     */
    public String[] kpiIds;
}
