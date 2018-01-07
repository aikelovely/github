package ru.alfabank.dmpr.repository.unitCost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.unitCost.UnitCostMapper;
import ru.alfabank.dmpr.model.unitCost.*;

/**
 * Репозиторий, отвечающий за загрузку данных для графиков витрины "Unit Cost".
 */
@Repository
public class UnitCostRepository {
    @Autowired
    UnitCostMapper mapper;

    /**
     * Возвращает данные для графика "Динамика изменения стоимости UnitCost"
     *
     * @param options Текущие значения фильтров
     * @return
     */
    public UnitCostDataItem[] getUCIndexChangeDynamic(UnitCostPeriodOptions options) {
        return mapper.getUCIndexChangeDynamic(options);
    }

    /**
     * Возвращает данные для графика "Распределение расходов UnitCost"
     *
     * @param options Текущие значения фильтров
     * @return
     */
    public UnitCostDataItem[] getUCCostDistribution(UnitCostPeriodOptions options) {
        return mapper.getUCCostDistribution(options);
    }

    /**
     * Возвращает данные для графика "Распределение расходов"
     *
     * @param options
     * @return
     */
    public UnitCostDataItem[] getUCDistribution(UnitCostPeriodOptions options) {
        return mapper.getUCDistribution(options);
    }

    /**
     * Возвращает данные для графика "Распределение ФОТ по категории расхода"
     *
     * @param options Текущие значения фильтров
     * @return
     */
    public UnitCostDataItem[] getUCFotDistribution(UnitCostPeriodOptions options) {
        return mapper.getUCFotDistribution(options);
    }

    /**
     * Возвращает данные для графика динамики на второй вкладке
     *
     * @param options Текущие значения фильтров
     * @return
     */
    public UnitCostDataItem[] getUCUnitCostDynamic(UnitCostPeriodOptions options) {
        return mapper.getUCUnitCostDynamic(options);
    }

    public UnitCost2[] getunitCost(UnitCostPeriodOptions options) {
        return mapper.getUnitCost(options);
    }
    public Pl[] getPl(UnitCostPeriodOptions options) {
        return mapper.getPl(options);
    }
    public DetailreportUc[] getDetailreportUc(UnitCostPeriodOptions options) {
        return mapper.getDetailreportUc(options);
    }

    public Bpiep2profitcenterShist[] getBpiep2profitcenterShist(UnitCostPeriodOptions options) {
        return mapper.getBpiep2profitcenterShist(options);
    }
    public UcBpplallocrulesShist[] getBpplallocrulesShist(UnitCostPeriodOptions options) {
        return mapper.getBpplallocrulesShist(options);
    }
    public BpucplrptstringShist[] getBpucplrptstringShist(UnitCostPeriodOptions options) {
        return mapper.getBpucplrptstringShist(options);
    }

    public UnitCostReportCurRate[] getCurrRate(UnitCostPeriodOptions options){
        return mapper.getCurrRate(options);
    }

}
