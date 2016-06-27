package ru.alfabank.dmpr.model.cards;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.ParamType;

/**
 * Набор значений общих фильтров для витрин Карты ФЛ.
 */
public abstract class CardsCommonOptions extends BaseOptions implements PeriodOptions {
    /**
     * Значение фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значение фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значение фильтра "Макро Регион"
     */
    public long[] macroRegionIds;

    /**
     * Значение фильтра "Регион"
     */
    public long[] regionIds;

    /**
     * Значение фильтра "Город"
     */
    public long[] cityIds;

    /**
     * Значение фильтра "Тип отделения"
     */
    public long[] branchTypeIds;

    /**
     * Значение фильтра "Отделение"
     */
    public long[] salePlaceIds;

    /**
     * Значение фильтра "Признак ЗП"
     */
    public long[] zpSignIds;

    /**
     * Значение фильтра "Тип ПУ"
     */
    public long[] pyTypeIds;

    /**
     * Значение фильтра "Категория карты"
     */
    public long[] cardCategoryIds;

    /**
     * Значение фильтра "Дебет/Кредит"
     */
    public long[] debitOrCreditIds;

    /**
     * Значение фильтра "Сегмент клиента"
     */
    public long[] clientSegmentIds;

    /**
     * Значение фильтра "Единица времени"
     */
    public Integer timeUnitId;

    /**
     * Значение фильтра "Единица сети"
     */
    public Integer systemUnitId;

    /**
     * Значение фильтра "Отображение параметра"
     */
    public ParamType paramType;

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
