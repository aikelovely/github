package ru.alfabank.dmpr.repository.ctq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.ctq.CTQMapper;
import ru.alfabank.dmpr.model.ctq.CTQDashboardDynamicQueryOptions;
import ru.alfabank.dmpr.model.ctq.CTQDashboardIndexItem;
import ru.alfabank.dmpr.model.ctq.CTQDashboardOptions;
import ru.alfabank.dmpr.model.ctq.CTQDashboardReportQueryOptions;

/**
 * Репозиторий для графиков витрин Витрина показателей CTQ
 */
@Repository
public class CTQRepository {
    @Autowired
    CTQMapper mapper;

    /**
     * Получает значения показателей для витрины Витрина показателей CTQ
     * @return значения показателей
     */
    public CTQDashboardIndexItem[] getIndexData(CTQDashboardOptions options){
        return mapper.getIndexData(options);
    }

    /**
     * Получает значения показателей для витрины Витрина показателей CTQ
     * @return значения показателей
     */
    public CTQDashboardIndexItem[] getDynamicData(CTQDashboardDynamicQueryOptions options) { return mapper.getDynamicData(options); }

    /**
     * Получает значения показателей для витрины Витрина показателей CTQ"
     * @return значения показателей
     */
    public CTQDashboardIndexItem[] getReportData(CTQDashboardReportQueryOptions options) { return mapper.getReportData(options); }
    /**
     * Получает значения показателей для витрины Витрина показателей CTQ"
     * @return значения показателей
     */
    public CTQDashboardIndexItem[] getReportDataSummary(CTQDashboardReportQueryOptions options){ return mapper.getReportDataSummary(options); }
}
