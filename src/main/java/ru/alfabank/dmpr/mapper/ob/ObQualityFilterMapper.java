package ru.alfabank.dmpr.mapper.ob;

import org.apache.ibatis.annotations.Param;
import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntityWithInfo;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;

/**
 * Маппер, отвечающий за загрузку фильтров для витрины "Показатели качества ОБ".
 */
public interface ObQualityFilterMapper {
    /**
     * Возвращает данные для фильтра "Тип периода"
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает данные для фильтра "Группа показателей"
     * @return
     */
    BaseEntity[] getKPIKinds();

    /**
     * Возвращает данные для фильтра "Показатель"
     * @param startDate Значение фильтра "Год" + "Период, с"
     * @param endDate Значение фильтра "Год" + "Период, по"
     * @return
     */
    ChildEntityWithInfo[] getKPI(ObQualityOptions obQualityOptions);

    /**
     * Возвращает данные для фильтра "Регион"
     * @return
     */
    BaseEntity[] getRegions(ObQualityOptions obQualityOptions);

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @return
     */
    BaseEntity[] getDirections();

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    Week[] getWeeks();
}
