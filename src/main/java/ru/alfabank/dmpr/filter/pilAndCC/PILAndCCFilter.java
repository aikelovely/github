package ru.alfabank.dmpr.filter.pilAndCC;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.model.pilAndCC.QuotaDescription;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины "Показатели качества ОБ".
 */
@Service
public class PILAndCCFilter {
    @Autowired
    private PILAndCCFilterRepository repository;

    /**
     * Возвращает данные для фильтра "Регион"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @return
     */
    public BaseEntity[] getRegions(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("kpiId") long kpiId) {
        return repository.getRegions(startDate, endDate, kpiId);
    }

    /**
     * Возвращает данные для фильтра "Город"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @return
     */
    public BaseEntity[] getCities(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("kpiId") long kpiId,
                                  @Param("regionIds[]") long[] regionIds) {
        return repository.getCities(startDate, endDate, kpiId, regionIds);
    }

    /**
     * Возвращает данные для фильтра "ДО/ККО"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @param cityIds Значение фильтра "Город"
     * @return
     */
    public BaseEntity[] getBranches(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("kpiId") long kpiId,
                                    @Param("regionIds[]") long[] regionIds,
                                    @Param("cityIds[]") long[] cityIds) {
        return repository.getBranches(startDate, endDate, kpiId, regionIds, cityIds);
    }

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @param cityIds Значение фильтра "Город"
     * @param branchIds Значение фильтра "ДО/ККО"
     * @return
     */
    public BaseEntity[] getUsers(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate,
                                 @Param("kpiId") long kpiId,
                                 @Param("regionIds[]") long[] regionIds,
                                 @Param("cityIds[]") long[] cityIds,
                                 @Param("doKkoIds[]") long[] branchIds) {
        return repository.getUsers(startDate, endDate, kpiId, regionIds, cityIds, branchIds);
    }

    /**
     * Возвращает данные для фильтра "Период агрегации"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return repository.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Канал продаж"
     * @return
     */
    public BaseEntity[] getChannels() {
        return repository.getChannels();
    }

    /**
     * Возвращает данные для фильтра "Тип заявки"
     * @return
     */
    public BaseEntity[] getRequestStatusTypes() {
        return repository.getRequestStatusTypes();
    }

    /**
     * Возвращает данные для фильтра "Уровень агрегации"
     * @return
     */
    public BaseEntity[] getDimensions() {
        return repository.getDimensions();
    }

    /**
     * Возвращает данные для фильтра "Система-источник"
     * @return
     */
    public BaseEntity[] getModules() {
        return repository.getModules();
    }

    /**
     * Возвращает данные для фильтра "Ручные проверки"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @return
     */
    public BaseEntity[] getManualChecks(@Param("kpiIds[]") long[] kpiIds) {
        return repository.getManualChecks(kpiIds);
    }

    /**
     * Возвращает данные для фильтра "Тип значения"
     * @return
     */
    public BaseEntity[] getFactValueTypes() {
        return repository.getFactValueTypes();
    }

    /**
     * Возвращает данные для фильтра "Продукт"
     * @return
     */
    public BaseEntity[] getProducts() {
        return repository.getProducts();
    }

    /**
     * Возвращает данные для фильтра "Клиент"
     * @return
     */
    public BaseEntity[] getClientSegments() {
        return repository.getClientSegments();
    }

    /**
     * Возвращает данные для фильтра "Сортировка рейтинга"
     * @return
     */
    public BaseEntity[] getSortDirections() {
        return repository.getSortDirections();
    }

    /**
     * Возвращает данные для фильтра "Тип сортировки"
     * @return
     */
    public BaseEntity[] getSortTypes() {
        return repository.getSortTypes();
    }

    /**
     * Возвращает значения ддя фильтра "Тип продукта" (на витрине называется как "Продукт")
     * @return
     */
    public BaseEntity[] getProductTypes() {
        return repository.getProductTypes();
    }

    /**
     * Возвращает значения ддя фильтра "KPI и метрики"
     * @param productTypeIds Значение фильтра "Тип продукта"
     * @param productIds Значение фильтра "Продукт". Не используется в данный момент.
     * @return
     */
    public BaseEntity[] getKPIs(@Param("productTypeIds[]") long[] productTypeIds,
                                @Param("productIds[]") long[] productIds) {
        return repository.getKPIs(productTypeIds, productIds);
    }

    /**
     * Возвращает описание нормативов.
     * @return
     */
    public QuotaDescription[] getQuotaDesc() {
        return repository.getQuotaDesc();
    }
}
