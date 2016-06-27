package ru.alfabank.dmpr.filter.cr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.repository.cr.CRFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины "Корпоративный бизнес".
 */
@Service
public class CRFilter {
    @Autowired
    CRFilterRepository repository;

    /**
     * Возвращает данные для фильтра "ЦО/РП".
     * @return
     */
    public BaseEntity[] getBLTypes() {
        return repository.getBLTypes();
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
     * Возвращает данные для фильтра "Процесс".
     * @return
     */
    public BaseEntity[] getProcesses() {
        return repository.getProcesses();
    }

    /**
     * Возвращает данные для фильтра "Уровень клиента".
     * @return
     */
    public BaseEntity[] getClientLevels() {
        return repository.getClientLevels();
    }

    /**
     * Возвращает данные для фильтра "Тип показателя"
     * @return
     */
    public BaseEntity[] getBPTypes() {
        return repository.getBPTypes();
    }

    /**
     * Возвращает данные для фильтра "Уполномоченный орган".
     * @param processIds Значения фильтра "Процесс"
     * @return
     */
    public BaseEntity[] getCommittees(@Param("processIds[]") long[] processIds) {
        return repository.getCommittees(processIds);
    }

    public BaseEntity[] getSystemUnits() {
        return repository.getSystemUnits();
    }

    public BaseEntity[] getProductTypes(@Param("bpTypeId") long bpTypeId) {
        return repository.getProductTypes(bpTypeId);
    }
}
