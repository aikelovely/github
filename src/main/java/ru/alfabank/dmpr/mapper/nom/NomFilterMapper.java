package ru.alfabank.dmpr.mapper.nom;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.Week;

/**
 * Маппер для фильтров витрины "Количество конечных продуктов для расчета UC ОБ"
 */
public interface NomFilterMapper {
    /**
     * Возвращает список значений для фильтра "Единица времени"
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    Week[] getWeeks();

    /**
     * Возвращает список значений для фильтра "Группа подразделений"
     * @return
     */
    ChildEntity[] getDivisionGroups();
}

