package ru.alfabank.dmpr.model.unitCost;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Набор значений фильтров витрины "UnitCost".
 */
public class UnitCostPeriodOptions extends BaseOptions {
    /**
     * Значеиие фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значеиие фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значеиие фильтра "ЦО/РОБ"
     */
    public Long bgOrgRegionId;

    /**
     * Значеиие фильтра "Конечный продукт"
     */
    public Long innerEndProductId;

    /**
     * Значеиие фильтра "Тип расчета"
     */
    public int calcTypeId;

    /**
     * Значеиие фильтра "Дирекция"
     */
    public long directionId;

    /**
     * Значеиие фильтра "Валюта"
     */
    public Integer currencyId;

    // Заполняется при дриллдаун
    public Long groupId;
    public String profitCenter;
}
