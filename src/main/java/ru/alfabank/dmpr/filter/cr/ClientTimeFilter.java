package ru.alfabank.dmpr.filter.cr;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.repository.cr.ClientTimeFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины "Декомпозиция процессов".
 */
@Service
public class ClientTimeFilter {
    @Autowired
    ClientTimeFilterRepository repository;

    /**
     * Возвращает данные для фильтра "Тип заявки".
     * @return
     */
    public BaseEntity[] getKpiMetrics() {
        return repository.getKpiMetrics();
    }

    /**
     * Возвращает данные для фильтра "Куст".
     * @param blTypeIds Значения фильтра "ЦО/РП"
     * @return
     */
    public BaseEntity[] getBLs(@Param("blTypeIds[]") long[] blTypeIds) {
        return repository.getBLs(blTypeIds);
    }

    /**
     * Возвращает данные для фильтра "Доп. офис".
     * @param blIds Значения фильтра "Куст"
     * @return
     */
    public BaseEntity[] getDopOffices(@Param("blIds[]") long[] blIds) {
        return repository.getDopOffices(blIds);
    }

    /**
     * Возвращает данные для фильтра "Кредитное подразделение".
     * @param dopOfficeIds Значения фильтра "Доп. офис"
     * @return
     */
    public BaseEntity[] getCreditDeparts(@Param("dopOfficeIds[]") long[] dopOfficeIds) {
        return repository.getCreditDeparts(dopOfficeIds);
    }

    /**
     * Возвращает данные для фильтра "Бизнес-процесс".
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "Тип заявки"
     * @param stageIds Значения фильтра "Этап"
     * @return
     */
    public BaseEntity[] getBpKinds(@Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate,
                                   @Param("kpiId") long kpiId,
                                   @Param("stageIds") long[] stageIds) {
        return repository.getBpKinds(startDate, endDate, kpiId, stageIds);
    }

    /**
     * Возвращает данные для фильтра "Подразделение".
     * @param dopOfficeIds Значения фильтра "Доп. офис"
     * @return
     */
    public BaseEntity[] getDeparts(@Param("dopOfficeIds[]") long[] dopOfficeIds) {
        return repository.getDeparts(dopOfficeIds);
    }

    /**
     * Возвращает данные для фильтра "Этап".
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "Тип заявки"
     * @return
     */
    public BaseEntity[] getStages(@Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate,
                                  @Param("kpiId") long kpiId) {
        return repository.getStages(startDate, endDate, kpiId);
    }

    /**
     * Возвращает данные для фильтра "Тип продукта".
     * @return
     */
    public BaseEntity[] getProductTypes(@Param("kpiId") long kpiId) {
        return repository.getProductTypes(kpiId);
    }

    /**
     * Возвращает данные для фильтра "Функциональная группа".
     * @return
     */
    public BaseEntity[] getFuncGroups() {
        return repository.getFuncGroups();
    }

    /**
     * Возвращает данные для фильтра "ЦО/РП".
     * @return
     */
    public BaseEntity[] getBLTypes() {
        return repository.getBLTypes();
    }

    /**
     * Возвращает данные для фильтра "Период агрегации".
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return repository.getTimeUnits();
    }
}
