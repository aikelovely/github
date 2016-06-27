package ru.alfabank.dmpr.mapper.ctq;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.ctq.CTQDashboardKPIEntity;
import ru.alfabank.dmpr.model.ctq.CTQLayoutItem;

/**
 * MyBatis mapper. Используется для загрузки фильтров витрин CTQDashboard*.
 */
public interface CTQFilterMapper {
    /**
     * Возвращает список сущностей для фильтра "Тип периода"
     * @return Список сущностей
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает список показателей
     * @return Список показателей
     */
    CTQDashboardKPIEntity[] getKPIs();

    /**
     * Возвращает информацию о отображении показателей
     * @return Список показателей
     */
    CTQLayoutItem[] getLayout();

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    Week[] getWeeks();
}
