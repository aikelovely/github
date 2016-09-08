package ru.alfabank.dmpr.mapper.ctq;

import ru.alfabank.dmpr.model.ctq.CTQDashboardDynamicQueryOptions;
import ru.alfabank.dmpr.model.ctq.CTQDashboardIndexItem;
import ru.alfabank.dmpr.model.ctq.CTQDashboardOptions;
import ru.alfabank.dmpr.model.ctq.CTQDashboardReportQueryOptions;

/**
 * MyBatis mapper. Используется для загрузки данных для графиков витрин CTQDashboard*
 */
public interface CTQMapper {
    /**
     * Получает значения показателей для витрины CTQDashboard
     * @return значения показателей
     */
    CTQDashboardIndexItem[] getIndexData(CTQDashboardOptions options);

    /**
     * Получает значения показателей для витрины CTQDashboardDynamic
     * @return значения показателей
     */
    CTQDashboardIndexItem[] getDynamicData(CTQDashboardDynamicQueryOptions options);

    /**
     * Получает значения показателей для витрины CTQDashboardReport
     * @return значения показателей
     */
    CTQDashboardIndexItem[] getReportData(CTQDashboardReportQueryOptions options);

    /**
     * Получает значения для выгрузки в  Excel
     * @return значения показателей
     */
    CTQDashboardIndexItem[] getReportDataSummary(CTQDashboardReportQueryOptions options);

}
