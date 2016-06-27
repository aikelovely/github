package ru.alfabank.dmpr.mapper.zp;

import ru.alfabank.dmpr.model.zp.ZPInstitutionOptions;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPTile;

/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Заведение зарплатного проекта".
 */
public interface ZPInstitutionMapper {
    /**
     * Возвращает данные для динамики.
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getDynamic(ZPInstitutionOptions options);

    /**
     * Возвращает данные для мини-графиков в виде плиток.
     * @param options Текущие значения фильтров
     * @return
     */
    ZPTile[] getTiles(ZPInstitutionOptions options);
}
