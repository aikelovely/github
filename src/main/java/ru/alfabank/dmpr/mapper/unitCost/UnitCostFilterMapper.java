package ru.alfabank.dmpr.mapper.unitCost;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.ChildEntity;

/**
 * Маппер, отвечающий за загрузку данных для фильтров витрины "Unit Cost".
 */
public interface UnitCostFilterMapper {
    /**
     * Возвращает данные для фильтра "ЦО/РОБ"
     * @return
     */
    BaseEntityWithCode[] getUCBpOrgRegions();

    /**
     * Возвращает данные для фильтра "Тип расчета"
     * @return
     */
    BaseEntity[] getUCCalcTypes();

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    BaseEntity[] getUCDirections(LocalDate endDate);

    /**
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    ChildEntity[] getUCInnerEndProducts(LocalDate endDate);

    /**
     * Возвращает данные для фильтра "Валюта"
     * @return
     */
    BaseEntity[] getUCCurrencies();
}
