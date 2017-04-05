package ru.alfabank.dmpr.filter.unitCost;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.unitCost.UnitCostPeriodOptions;
import ru.alfabank.dmpr.repository.unitCost.UnitCostFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины "UnitCost".
 */
@Service
public class UnitCostFilter {
    @Autowired
    UnitCostFilterRepository repository;

    /**
     * Возвращает данные для фильтра "ЦО/РОБ"
     * @return
     */
    public BaseEntityWithCode[] getUCBpOrgRegions() {
        return repository.getUCBpOrgRegions();
    }

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    public BaseEntity[] getUCDirections(@Param("endDate") LocalDate endDate) {
        return repository.getUCDirections(endDate);
    }

    /**
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @param directionId Значение фильтра "Дирекция"
     * @return
     */
    public BaseEntity[] getUCInnerEndProducts(@Param("endDate") LocalDate endDate,
                                              @Param("directionId") long directionId) {
        return repository.getUCInnerEndProducts(endDate, directionId);
    }

    /**
     * Возвращает данные для фильтра "Тип расчета"
     * @return
     */
    public BaseEntity[] getUCCalcTypes() {
        return repository.getUCCalcTypes();
    }

    /**
     * Возвращает данные для фильтра "Валюта"
     * @return
     */
    public BaseEntity[] getUCCurrencies() {
        return repository.getUCCurrencies();
    }
    /**
     * Возвращает данные для
     *
     * @return
     */
    public  BaseEntity[] getProfitCenter(@Param("endDate") LocalDate endDate,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("directionId") long directionId,
                                         @Param("bgOrgRegionId") long bgOrgRegionId,
                                         @Param("calcTypeId") int calcTypeId
    ){
        UnitCostPeriodOptions options = new UnitCostPeriodOptions();
        options.endDate=endDate;
        options.startDate=startDate;
        options.directionId=directionId;
        options.bgOrgRegionId=bgOrgRegionId;
        options.calcTypeId=calcTypeId;
        return repository.getProfitCenter(options);
    }
//    BaseEntity[] getProfitCenter(UnitCostPeriodOptions options);
}
