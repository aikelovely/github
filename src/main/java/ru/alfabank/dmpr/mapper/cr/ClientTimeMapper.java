package ru.alfabank.dmpr.mapper.cr;

import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDataItem;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDetailsDataItem;

/**
 * Маппер, для загрузки графиков витрины "Декомпозиция процессов"
 */
public interface ClientTimeMapper {
    /**
     * Основной метод, возвращающий все необходимые данные.
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDataItem[] getKpiData(ClientTimeOptions options);

    /**
     * Дополнительный метод, возвращающий необходимые данные для дет. выгрузки
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDetailsDataItem[] getKpiDetailsData(ClientTimeOptions options);
}
