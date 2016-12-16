package ru.alfabank.dmpr.repository.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.ob.ObQualityMapper;
import ru.alfabank.dmpr.model.ob.*;

/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Показатели качества ОБ".
 */
@Repository
public class ObQualityRepository {
    @Autowired
    private ObQualityMapper mapper;

    /**
     * Возвращает данные для графика рейтинга
     * @param options Текущие значения фильтров
     * @return
     */
    public ObQualityDataItem[] getRatingData(ObQualityQueryOptions options){
        return mapper.getRatingData(options);
    }

    /**
     * Возвращает данные для динамики
     * @param options Текущие значения фильтров
     * @return
     */
    public ObQualityDataItem[] getDynamicData(ObQualityQueryOptions options){
        return mapper.getDynamicData(options);
    }

    /**
     * Возвращает данные для таблицы с детализацией за отчетный период
     * @param options Текущие значения фильтров
     * @return ObReportSummaryQuality[] getsummarykpiob();
     */
    public ObQualityDataItem[] getDetailsData(ObQualityQueryOptions options){
        return mapper.getDetailsData(options);
    }
    public ObReportSummaryQualityFilter[] getsummarykpiob_filter(ObQualityQueryOptions options){return mapper.getsummarykpiob_filter(options);}
    public ObReportSummaryQuality[] getsummarykpiob(){
        return mapper.getsummarykpiob();
    }
    public ObDss[] getObDss(ObQualityQueryOptions options){
        return mapper.getObDss(options);
    }
    public ObDssemployees[] getObDssEmployees(ObQualityQueryOptions options){
        return mapper.getObDssEmployees(options);
    }
}
