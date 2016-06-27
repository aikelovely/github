package ru.alfabank.dmpr.mapper.zp;

import ru.alfabank.dmpr.model.zp.*;

/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Скорость заведения зарплатного проекта".
 */
public interface ZPOpeningSpeedMapper {
    /**
     * Возвращает данные для графика Динамика средней скорости / доли проектов в пределах KPI.
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getDynamic(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для графика рейтинг Средняя скорость / Доля проектов в пределах в KPI.
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getRating(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для таблицы "Детализированный отчет".
     * @param options
     * @return
     */
    ZPKPIDataItem[] getDetails(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для графика "Динамика средней скорости / доли проектов в пределах KPI" (тип открытия - дооткрытие).
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getRoDynamic(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для графика рейтинг "Средняя скорость" /
     * "Доля проектов в пределах в KPI" (тип открытия - дооткрытие).
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getRoRating(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для таблицы "Детализированный отчет" (тип открытия - дооткрытие).
     * @param options Текущие значения фильтров
     * @return
     */
    ZPKPIDataItem[] getRoDetails(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для графика "Доля компаний с данными CRM".
     * @param options Текущие значения фильтров
     * @return
     */
    ZPProjectDynamic[] getProjectDynamic(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для таблицы "Количество компаний в АЗОН"
     * @param options Текущие значения фильтров
     * @return
     */
    ZPProjectDynamic[] getProjectDetails(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для таблицы детализированный отчет о качестве заполнения CRM
     * @param options Текущие значения фильтров
     * @return
     */
    ZPQualityInfo[] getCrmFillQualityDetails(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для графика "Динамика качества заполнения CRM"
     * @param options Текущие значения фильтров
     * @return
     */
    ZPQualityDynamic[] getCrmFillQualityDynamic(ZPOpeningSpeedOptions options);

    /**
     * Возвращает норматив % проектов в пределах KPI
     * @param options Текущие значения фильтров
     * @return
     */
    double getInKPIThreshold(ZPOpeningSpeedOptions options);

    /**
     * Возвращает длительность открытия проекта, которая считается "выбросом"
     * @param options Текущие значения фильтров
     * @return
     */
    double getRetardedThreshold(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для выгрузки "Детализированный отчет"
     * @param options Текущие значения фильтров
     * @return
     */
    ZPReportDetailsItem[] getReportDetails(ZPOpeningSpeedOptions options);

    /**
     * Возвращает данные для выгрузки "Детализированный отчет" (тип открытия - дооткрытие).
     * @param options Текущие значения фильтров
     * @return
     */
    ZPReportDetailsItem[] getRoReportDetails(ZPOpeningSpeedOptions options);
}

