package ru.alfabank.dmpr.model.cr.TTYAndTTM;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.Period;

/**
 * Набор параметров для запросов к БД для получения данных витрины "Корпоративный бизнес".
 */
public class TTYAndTTMOptions extends BaseOptions implements PeriodOptions  {
    /**
     * Значение фильтра "ЦО/РП"
     */
    public long[] blTypeIds;

    /**
     * Значение фильтра "Куст"
     */
    public long[] blIds;

    /**
     * Значение фильтра "Доп. офис"
     */
    public long[] dopOfficeIds;

    /**
     * Значение фильтра "Процесс"
     */
    public long[] processIds;

    /**
     * Значение фильтра "Тип показателя"
     */
    public long bpTypeId;

    /**
     * Значение фильтра "Тип продукта"
     */
    public long[] productTypeIds;

    /**
     * Значение фильтра "Уполномоченный орган"
     */
    public long[] committeeIds;

    /**
     * Значение фильтра "Уровень агрегации"
     */
    public int systemUnitId;

    /**
     * Значение фильтра "Учитывать плавающий год"
     */
    public boolean floatingYear;

    /**
     * Значение фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значение фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значение фильтра "Отображение параметра"
     */
    public ParamType paramType;

    /**
     * Значение фильтра "Уровень клиента"
     */
    public Long clientLevelId;

    /**
     * Значение фильтра "Использовать плавающий год при расчёте динамики"
     */
    public boolean isSmoothMetric;

    /**
     * Значение фильтра "Сортировка"
     */
    public int orderBy;

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
        return Period.month.getValue();
    }
}
