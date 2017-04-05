package ru.alfabank.dmpr.repository.unitCost;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.mapper.unitCost.UnitCostFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.unitCost.UnitCostPeriodOptions;

/**
 * Репозиторий, отвечающий за загрузку данных для фильтров витрины "Unit Cost".
 */
@Repository
public class UnitCostFilterRepository {
    @Autowired
    private UnitCostFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "ЦО/РОБ"
     * @return
     */
    public BaseEntityWithCode[] getUCBpOrgRegions() {
        return mapper.getUCBpOrgRegions();
    }

    /**
     * Возвращает данные для фильтра "Тип расчета"
     * @return
     */
    public BaseEntity[] getUCCalcTypes() {
        return mapper.getUCCalcTypes();
    }

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    public BaseEntity[] getUCDirections(LocalDate endDate) {
        return mapper.getUCDirections(endDate);
    }

    /**
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    public ChildEntity[] getUCInnerEndProducts(LocalDate endDate) {
        return mapper.getUCInnerEndProducts(endDate);
    }

    /**
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @param directionId Значение фильтра "Дирекция"
     * @return
     */
    public BaseEntity[] getUCInnerEndProducts(LocalDate endDate, final long directionId) {
        return LinqWrapper.from(getUCInnerEndProducts(endDate))
                .filter(new Predicate<ChildEntity>() {
                    @Override
                    public boolean check(ChildEntity item) {
                        return Long.valueOf(item.parentId).equals(directionId);
                    }
                })
                .select(new Selector<ChildEntity, BaseEntity>() {
                    @Override
                    public BaseEntity select(ChildEntity childEntity) {
                        return childEntity.toBaseEntity();
                    }
                })
                .toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Валюта"
     * @return
     */
    public BaseEntity[] getUCCurrencies() {
        return mapper.getUCCurrencies();
    }
    /**
     * Возвращает данные для для profitCenter
     *
     * @param options Текущие значения фильтров
     * @return
     */
    public BaseEntity[] getProfitCenter(UnitCostPeriodOptions options) {
        return mapper.getProfitCenter(options);
    }
}
