package ru.alfabank.dmpr.mapper.nom;

import ru.alfabank.dmpr.model.ChildEntityWithCode;
import ru.alfabank.dmpr.model.nom.NomDetailsReportRow;
import ru.alfabank.dmpr.model.nom.NomKpiDataItem;
import ru.alfabank.dmpr.model.nom.NomQueryOptions;

/**
 * Маппер для фильтров витрины "Количество конечных продуктов для расчета UC ОБ"
 */
public interface NomMapper {
    /**
     * Возращает список конечных продуктов
     * @return
     */
    ChildEntityWithCode[] getInnerEndProducts();

    /**
     * Основная функция для получений данных для графиков
     * @param options Параметры фильтров
     * @return
     */
    NomKpiDataItem[] getKpiData(NomQueryOptions options);

    /**
     * Функция для выгрузки данных в Excel
     * @param options Параметры фильтров
     * @return
     */
    NomDetailsReportRow[] getReport(NomQueryOptions options);
}
