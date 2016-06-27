package ru.alfabank.dmpr.mapper.cr;

import org.apache.ibatis.annotations.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;

/**
 * Маппер для загрузки фильтров витрины "Корпоративный бизнес"
 */
public interface CRFilterMapper {
    /**
     * Возвращает данные для фильтра "ЦО/РП".
     * @return
     */
    BaseEntity[] getBLTypes();

    /**
     * Возвращает данные для фильтра "Куст".
     * @return
     */
    ChildEntity[] getBLs();

    /**
     * Возвращает данные для фильтра "Процесс".
     * @return
     */
    BaseEntity[] getProcesses();

    /**
     * Возвращает данные для фильтра "Уровень клиента".
     * @return
     */
    BaseEntity[] getClientLevels();

    /**
     * Возвращает данные для фильтра "Тип показателя".
     * @return
     */
    BaseEntity[] getBPTypes();

    /**
     * Возвращает данные для фильтра "Уполномоченный орган".
     * @return
     */
    ChildEntity[] getCommittees();

    /**
     * Возвращает данные для фильтра "Доп. офис".
     * @return
     */
    ChildEntity[] getDopOffices();

    /**
     * Возвращает данные для фильтра "Тип продукта".
     * @param bpTypeId
     * @return
     */
    BaseEntity[] getProductTypes(@Param("bpTypeId") long bpTypeId);
}
