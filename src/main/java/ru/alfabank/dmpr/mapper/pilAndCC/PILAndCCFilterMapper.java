package ru.alfabank.dmpr.mapper.pilAndCC;

import org.apache.ibatis.annotations.Param;
import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.pilAndCC.QuotaDescription;

/**
 * Маппер, отвечающий за загрузку фильтров для витрины "Показатели качества ОБ".
 */
public interface PILAndCCFilterMapper {
    /**
     * Возвращает данные для фильтра "Регион"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @return
     */
    BaseEntity[] getRegions(@Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate,
                            @Param("kpiIds") long[] kpiIds);

    /**
     * Возвращает данные для фильтра "Город"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @return
     */
    BaseEntity[] getCities(@Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate,
                           @Param("kpiIds") long[] kpiIds,
                           @Param("regionIds") long[] regionIds);

    /**
     * Возвращает данные для фильтра "ДО/ККО"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @param cityIds Значение фильтра "Город"
     * @return
     */
    BaseEntity[] getBranches(@Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate,
                             @Param("kpiIds") long[] kpiIds,
                             @Param("regionIds") long[] regionIds,
                             @Param("cityIds") long[] cityIds);

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @param cityIds Значение фильтра "Город"
     * @param branchIds Значение фильтра "ДО/ККО"
     * @return
     */
    BaseEntity[] getUsers(@Param("startDate") LocalDate startDate,
                          @Param("endDate") LocalDate endDate,
                          @Param("kpiIds") long[] kpiIds,
                          @Param("regionIds") long[] regionIds,
                          @Param("cityIds") long[] cityIds,
                          @Param("branchIds") long[] branchIds);

    /**
     * Возвращает значения ддя фильтра "Тип продукта" (на витрине называется как "Продукт")
     * @return
     */
    BaseEntity[] getProductTypes();

    /**
     * Возвращает значения ддя фильтра "Продукт"
     * @return
     */
    BaseEntity[] getProducts();

    /**
     * Возвращает значения ддя фильтра "KPI и метрики"
     * @param productTypeIds Значение фильтра "Тип продукта"
     * @param productIds Значение фильтра "Продукт". Не используется в данный момент.
     * @return
     */
    BaseEntity[] getKPIs(@Param("productTypeIds") long[] productTypeIds,
                         @Param("productIds") long[] productIds);

    /**
     * Возвращает данные для фильтра "Период агрегации"
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает данные для фильтра "Уровень агрегации"
     * @return
     */
    BaseEntity[] getDimensions();

    /**
     * Возвращает данные для фильтра "Ручные проверки"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @return
     */
    BaseEntity[] getManualChecks(@Param("kpiIds") long[] kpiIds);

    /**
     * Возвращает данные для фильтра "Клиент"
     * @return
     */
    BaseEntity[] getClientSegments();

    /**
     * Возвращает данные для фильтра "Тип значения"
     * @return
     */
    BaseEntity[] getFactValueTypes();

    /**
     * Возвращает данные для фильтра "Тип заявки"
     * @return
     */
    BaseEntity[] getRequestStatusTypes();

    /**
     * Возвращает данные для фильтра "Сортировка рейтинга"
     * @return
     */
    BaseEntity[] getSortDirections();

    /**
     * Возвращает данные для фильтра "Тип сортировки"
     * @return
     */
    BaseEntity[] getSortTypes();

    /**
     * Возвращает данные для фильтра "Система-источник"
     * @return
     */
    BaseEntity[] getModules();

    /**
     * Возвращает данные для фильтра "Канал продаж"
     * @return
     */
    BaseEntity[] getChannels();

    /**
     * Возвращает описание нормативов.
     * @return
     */
    QuotaDescription[] getQuotaDesc();

    /**
     * Возвращает данные для фильтра "Учет проверок по 7 полям"
     * @return
     */
    BaseEntityWithCode[] getSevenFieldsCheck();
}
