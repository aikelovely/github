package ru.alfabank.dmpr.repository.ctq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.ctq.CTQMapper;
import ru.alfabank.dmpr.model.ctq.*;

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
    public CTQDashboardSumData[] getReportDataSummary(){return mapper.getReportDataSummary();};
}
