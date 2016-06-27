package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.ParamType;

/**
 * Набор значений фильтров витрины "Заведение зарплатного проекта"
 */
public class ZPInstitutionOptions extends BaseOptions implements PeriodOptions {
    /**
     * Значение фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значение фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значение фильтра "Куст"
     */
    public long[] bushIds;

    /**
     * Значение фильтра "Операционный офис"
     */
    public long[] operationOfficeIds;

    /**
     * Значение фильтра "Город"
     */
    public long[] cityIds;

    /**
     * Значение фильтра "Менеджер"
     */
    public String[] managerIds;

    /**
     * Значение фильтра "Компания"
     */
    public String[] companyIds;

    /**
     * Значение фильтра "Тип схемы"
     */
    public long[] schemaTypeIds;

    /**
     * Значение фильтра "Отображение параметра"
     */
    public ParamType paramType;

    /**
     * Значение фильтра "Единица времени"
     */
    public Integer timeUnitId;

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public Integer getTimeUnitId() {
        return timeUnitId;
    }
}

