package ru.alfabank.dmpr.repository.pilAndCC;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.pilAndCC.PILAndCCFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.pilAndCC.QuotaDescription;

@Repository
public class PILAndCCFilterRepository {
    @Autowired
    private PILAndCCFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "Регион"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @return
     */
    public BaseEntity[] getRegions(LocalDate startDate, LocalDate endDate, long kpiId) {
        return mapper.getRegions(startDate, endDate, new long[]{kpiId});
    }

    /**
     * Возвращает данные для фильтра "Город"
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "KPI и метрики"
     * @param regionIds Значение фильтра "Регион"
     * @return
     */
    public BaseEntity[] getCities(LocalDate startDate,
                                  LocalDate endDate,
                                  long kpiId,
                                  long[] regionIds) {
        return mapper.getCities(startDate, endDate, new long[]{kpiId}, regionIds);
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
    public BaseEntity[] getBranches(LocalDate startDate,
                                    LocalDate endDate,
                                    long kpiId,
                                    long[] regionIds,
                                    long[] cityIds) {
        return mapper.getBranches(startDate, endDate, new long[]{kpiId}, regionIds, cityIds);
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
    public BaseEntity[] getUsers(LocalDate startDate,
                                 LocalDate endDate,
                                 long kpiId,
                                 long[] regionIds,
                                 long[] cityIds,
                                 long[] branchIds) {
        return mapper.getUsers(startDate, endDate, new long[]{kpiId}, regionIds, cityIds, branchIds);
    }

    /**
     * Возвращает данные для фильтра "Канал продаж"
     * @return
     */
    public BaseEntity[] getChannels() {
        return mapper.getChannels();
    }

    /**
     * Возвращает данные для фильтра "Уровень агрегации"
     * @return
     */
    public BaseEntity[] getDimensions() {
        return mapper.getDimensions();
    }

    /**
     * Возвращает данные для фильтра "Система-источник"
     * @return
     */
    public BaseEntity[] getModules() {
        return mapper.getModules();
    }

    /**
     * Возвращает данные для фильтра "Ручные проверки"
     * @param kpiIds Значение фильтра "KPI и метрики"
     * @return
     */
    public BaseEntity[] getManualChecks(long[] kpiIds) {
        return mapper.getManualChecks(kpiIds);
    }

    /**
     * Возвращает значения ддя фильтра "Продукт"
     * @return
     */
    public BaseEntity[] getProducts() {
        return mapper.getProducts();
    }

    /**
     * Возвращает данные для фильтра "Тип значения"
     * @return
     */
    public BaseEntity[] getFactValueTypes() {
        return mapper.getFactValueTypes();
    }

    /**
     * Возвращает значения ддя фильтра "Тип продукта" (на витрине называется как "Продукт")
     * @return
     */
    public BaseEntity[] getProductTypes() {
        return mapper.getProductTypes();
    }

    /**
     * Возвращает данные для фильтра "Тип сортировки"
     * @return
     */
    public BaseEntity[] getSortTypes() {
        return mapper.getSortTypes();
    }

    /**
     * Возвращает значения ддя фильтра "KPI и метрики"
     * @param productTypeIds Значение фильтра "Тип продукта"
     * @param productIds Значение фильтра "Продукт". Не используется в данный момент.
     * @return
     */
    public BaseEntity[] getKPIs(long[] productTypeIds, long[] productIds) {
        return mapper.getKPIs(productTypeIds, productIds);
    }

    /**
     * Возвращает данные для фильтра "Период агрегации"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Клиент"
     * @return
     */
    public BaseEntity[] getClientSegments() {
        return mapper.getClientSegments();
    }

    /**
     * Возвращает данные для фильтра "Сортировка рейтинга"
     * @return
     */
    public BaseEntity[] getSortDirections() {
        return mapper.getSortDirections();
    }

    /**
     * Возвращает данные для фильтра "Тип заявки"
     * @return
     */
    public BaseEntity[] getRequestStatusTypes() {
        return mapper.getRequestStatusTypes();
    }

    /**
     * Возвращает описание нормативов.
     * @return
     */
    public QuotaDescription[] getQuotaDesc() {
        return mapper.getQuotaDesc();
    }

    /**
     * Возвращает данные для фильтра "Учет проверок по 7 полям"
     * @return
     */
    public BaseEntityWithCode[] getSevenFieldsCheck() {
        return mapper.getSevenFieldsCheck();
    }

    public BaseEntityWithCode getSevenFieldsCheckById(final long id) {
        return LinqWrapper.from(getSevenFieldsCheck()).first(new Predicate<BaseEntityWithCode>() {
            @Override
            public boolean check(BaseEntityWithCode item) {
                return id == item.id;
            }
        });
    }
}
