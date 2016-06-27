package ru.alfabank.dmpr.model.cr.ClientTime;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.Period;

/**
 * Набор параметров для запросов к БД для получения данных витрины "Декомпозиция процессов".
 */
public class ClientTimeOptions extends BaseOptions implements PeriodOptions {
    /**
     * Значение фильтра "Тип заявки"
     */
    public long kpiId;

    /**
     * Значение фильтра "Отображение параметра"
     */
    public ParamType paramType;

    /**
     * Значение фильтра "Тип продукта"
     */
    public long[] productTypeIds;

    /**
     * Значение фильтра "Этап"
     */
    public long[] stageIds;

    /**
     * Значение фильтра "Бизнес-процесс"
     */
    public long[] bpKindIds;

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
     * Значение фильтра "Кредитное подразделение"
     */
    public long[] creditDepartIds;

    /**
     * Значение фильтра "Подразделение"
     */
    public long[] departIds;

    /**
     * Значение фильтра "Уполномоченный орган"
     */
    public long[] committeeIds;

    /**
     * Значение фильтра "Функциональная группа"
     */
    public long[] funcGroupIds;

    /**
     * Значение фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значение фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значение фильтра "Период агрегации"
     */
    public Integer timeUnitId;

    /**
     * Уровень детализации
     */
    public Integer systemUnitId;

    /**
     * Используется только при дриллдаун
     */
    public Integer processId;

    /**
     * Значение фильтра "Уровень клиента"
     */
    public Long clientLevelId;

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
