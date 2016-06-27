package ru.alfabank.dmpr.mapper.unitCost;

import ru.alfabank.dmpr.model.unitCost.UnitCostDataItem;
import ru.alfabank.dmpr.model.unitCost.UnitCostPeriodOptions;
import ru.alfabank.dmpr.model.unitCost.UnitCostReportDataItem;

/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Unit Cost".
 */
public interface UnitCostMapper {
    /**
     * Возвращает данные для графика "Распределение расходов"
     * @param options
     * @return
     */
    UnitCostDataItem[] getUCDistribution(UnitCostPeriodOptions options);

    /**
     * Возвращает данные для графика "Распределение расходов UnitCost"
     * @param options Текущие значения фильтров
     * @return
     */
    UnitCostDataItem[] getUCCostDistribution(UnitCostPeriodOptions options);

    /**
     * Возвращает данные для графика "Динамика изменения стоимости UnitCost"
     * @param options Текущие значения фильтров
     * @return
     */
    UnitCostDataItem[] getUCIndexChangeDynamic(UnitCostPeriodOptions options);

    /**
     * Возвращает данные для графика "Распределение ФОТ по категории расхода"
     * @param options Текущие значения фильтров
     * @return
     */
    UnitCostDataItem[] getUCFotDistribution(UnitCostPeriodOptions options);

    /**
     * Возвращает данные для графика динамики на второй вкладке
     * @param options Текущие значения фильтров
     * @return
     */
    UnitCostDataItem[] getUCUnitCostDynamic(UnitCostPeriodOptions options);
}
