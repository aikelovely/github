package ru.alfabank.dmpr.mapper.ob;

import ru.alfabank.dmpr.model.ob.ObQualityDataItem;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.model.ob.ObReportSummaryQuality;
import ru.alfabank.dmpr.model.ob.ObReportSummaryQualityFilter;

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

}
