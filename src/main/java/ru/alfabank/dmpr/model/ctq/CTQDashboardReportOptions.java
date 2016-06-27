package ru.alfabank.dmpr.model.ctq;

import ru.alfabank.dmpr.model.BasePeriodOptions;

/**
 * Набор параметров для получений данных о KPI витрины CTQDashboardReport.
 */
public class CTQDashboardReportOptions extends BasePeriodOptions {
    /**
     * Может содержать список идентификаторов показателей
     * Необязательное поле.
     */
    public String[] kpiIds;
}
