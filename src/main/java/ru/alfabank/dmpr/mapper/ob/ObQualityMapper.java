package ru.alfabank.dmpr.mapper.ob;

import ru.alfabank.dmpr.model.ob.*;
import ru.alfabank.dmpr.model.ob.ObDss;


/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Показатели качества ОБ".
 */
public interface ObQualityMapper {
    /**
     * Возвращает данные для рейтинга
     * @param options Текущие значения фильтров
     * @return
     */
    ObQualityDataItem[] getRatingData(ObQualityQueryOptions options);

    /**
     * Возвращает данные для динамики
     * @param options Текущие значения фильтров
     * @return
     */
    ObQualityDataItem[] getDynamicData(ObQualityQueryOptions options);

    /**
     * Возвращает данные для таблицы с детализацией за отчетный период
     * @param options Текущие значения фильтров
     * @return
     */
    ObQualityDataItem[] getDetailsData(ObQualityQueryOptions options);

    /**
     * Возвращает данные для таблицы с детализацией за отчетный период
     *  Текущие значения фильтров
     * @return
     */
    ObReportSummaryQuality[] getsummarykpiob();

    ObReportSummaryQualityFilter[] getsummarykpiob_filter(ObQualityQueryOptions options);

    ObDss[] getObDss(ObQualityQueryOptions options);
    ObDssemployees[] getObDssEmployees(ObQualityQueryOptions options);


}
