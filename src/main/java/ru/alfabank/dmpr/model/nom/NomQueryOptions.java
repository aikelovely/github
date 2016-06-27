package ru.alfabank.dmpr.model.nom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.DateArraysHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.PeriodEntity;
import ru.alfabank.dmpr.model.Week;

public class NomQueryOptions extends BaseOptions {
    /**
     * Может содержать значение фильтра "Год"
     * Обязательное поле.
     */
    public LocalDate startDate;

    /**
     * Может содержать значение фильтра "Год"
     * Обязательное поле.
     */
    public LocalDate endDate;

    /**
     * Значение фильтра "Единица времени"
     */
    public Integer timeUnitId;

    /**
     * Список значений фильтра "Группа подразделений"
     */
    public long[] divisionGroupIds;

    /**
     * Список идентификаторов конечных продуктов
     */
    public long[] innerProductIds;

    /**
     * Список идентификаторов  подпродуктов
     */
    public long[] subInnerProductIds;

    /**
     * Уровень детализации
     */
    public int[] levels;

    /**
     * Запрос последнего периода
     */
    public int onlyLastPeriod;

    private String widgetName;

    @JsonIgnore
    @Override
    public String getWidgetName() {
        return widgetName == null ? null : widgetName;
    }

    /**
     * Конструктор
     * @param options - изначально принимаемые с клиента параметры фильтров
     */
    public NomQueryOptions(NomOptions options, Week[] weeks){
        widgetName = options.getWidgetName();
        timeUnitId = options.timeUnitId;
        divisionGroupIds = options.divisionGroupIds;
        innerProductIds = options.innerProductIds;
        subInnerProductIds = options.subInnerProductIds;
        levels = options.levels;
        onlyLastPeriod = options.onlyLastPeriod;

        LocalDate[] periods = PeriodSelectHelper.getDatesByBasePeriodOptions(options, weeks);

        startDate = periods[0];
        endDate = periods[1];
    }
}
