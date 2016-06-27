package ru.alfabank.dmpr.repository.cards;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.cards.*;
import ru.alfabank.dmpr.mapper.cards.CardsCommonFilterMapper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;

import java.util.List;

/**
 * Репозиторий фильтров общий для витрин, входящих в субдомен Cards
 */
@Repository
public class CardsCommonFilterRepository {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CardsCommonFilterMapper mapper;

    /**
     * Возвращает список "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает один элемента из списка "Единица времени" по его Id
     * @return
     */
    public BaseEntity getTimeUnitById(long id) {
        return BaseEntity.getById(getTimeUnits(), id);
    }

    /**
     * Возвращает список макро-регионов
     * @return
     */
    public BaseEntity[] getMacroRegions() {
        return mapper.getMacroRegions();
    }

    /**
     * Возвращает список регионов
     * @return
     */
    public ChildEntity[] getRegions() {
        return mapper.getRegions();
    }

    /**
     * Возвращает список отделений
     * @return
     */
    public Branch[] getAllBranches() {
        return mapper.getBranches();
    }

    /**
     * Возвращает список типов отделений
     * @return
     */
    public BaseEntity[] getBranchTypes() {
        return mapper.getBranchTypes();
    }

    /**
     * Возвращает список категорий карты
     * @return
     */
    public BaseEntity[] getCardCategories() {
        return mapper.getCardCategories();
    }

    /**
     * Возвращает список значений для фильтра "Дебет/Кредит"
     * @return
     */
    public BaseEntity[] getDebitOrCredits() {
        return mapper.getDebitOrCredits();
    }

    /**
     * Возвращает список значений для фильтра "Сегмент клиента"
     * @return
     */
    public BaseEntity[] getClientSegments() {
        return mapper.getClientSegments();
    }

    /**
     * Возвращает список значений для фильтра "Единица сети"
     * @return
     */
    public BaseEntity[] getSystemUnits() {
        return mapper.getSystemUnits();
    }

    /**
     * Возвращает список городов
     * @return
     */
    public ChildEntity[] getCities() {
        return mapper.getCities();
    }

    /**
     * Возвращает список значений для фильтра "Признак МВК"
     * @return
     */
    public BaseEntity[] getMvkSigns() {
        return mapper.getMvkSigns();
    }

    /**
     * Возвращает список значений для фильтра "Скорость перевыпуска"
     * @return
     */
    public BaseEntity[] getReissueSpeeds() {
        return mapper.getReissueSpeeds();
    }

    /**
     * Возвращает список регионов, входящих в выбранные макро-регионы
     * @return
     */
    public ChildEntity[] getRegionsByMacroRegionIds(final long[] macroRegionIds) {
        return LinqWrapper.from(getRegions())
                .filter(new Predicate<ChildEntity>() {
                    @Override
                    public boolean check(ChildEntity item) {
                        return macroRegionIds.length == 0 || ArrayUtils.contains(macroRegionIds, item.parentId);
                    }
                })
                .toArray(ChildEntity.class);
    }

    /**
     * Возвращает список городов, входящих в выбранные макро-регионы
     * @return
     */
    public ChildEntity[] getCitiesByMacroRegionIds(final long[] macroRegionIds) {
        final List<Long> regionIds = LinqWrapper.from(getRegionsByMacroRegionIds(macroRegionIds))
                .select(new Selector<ChildEntity, Long>() {
                    @Override
                    public Long select(ChildEntity childEntity) {
                        return childEntity.id;
                    }
                })
                .toList();


        return LinqWrapper.from(getCities())
                .filter(new Predicate<ChildEntity>() {
                    @Override
                    public boolean check(ChildEntity item) {
                        return macroRegionIds.length == 0 || regionIds.contains(item.parentId);
                    }
                })
                .distinct()
                .toArray(ChildEntity.class);
    }

    /**
     * Возвращает список городов, входящих в выбранные регионы
     * @return
     */
    public ChildEntity[] getCitiesByRegionIds(final long[] regionIds) {
        return LinqWrapper.from(getCities())
                .filter(new Predicate<ChildEntity>() {
                    @Override
                    public boolean check(ChildEntity item) {
                        return regionIds.length == 0 || ArrayUtils.contains(regionIds, item.parentId);
                    }
                })
                .distinct()
                .toArray(ChildEntity.class);
    }

    /**
     * Возвращает список отделений, входящих в выбранные макро-регионы, регионы, города и с выбранными типами отделений
     * @return
     */
    public Branch[] getBranches(final long[] macroRegionIds, long[] cityIds, final long[] branchTypeIds) {
        if(cityIds.length == 0){
            cityIds = ArrayUtils.toPrimitive(LinqWrapper.from(getCitiesByMacroRegionIds(macroRegionIds))
                    .select(new Selector<ChildEntity, Long>() {
                        @Override
                        public Long select(ChildEntity city) {
                            return city.id;
                        }
                    }).toArray(Long.class));
        }

        final long[] cityIdsToCheck = cityIds;

        return LinqWrapper.from(getAllBranches())
                .filter(new Predicate<Branch>() {
                    @Override
                    public boolean check(Branch item) {
                        return (cityIdsToCheck.length == 0 || ArrayUtils.contains(cityIdsToCheck, item.cityId)) &&
                                (branchTypeIds.length == 0 || ArrayUtils.contains(branchTypeIds, item.branchTypeId));
                    }
                })
                .distinct()
                .toArray(Branch.class);
    }

    /**
     * Возвращает макро-регион с данным ID
     * @return
     */
    public BaseEntity getMacroRegionById(long id) {
        return BaseEntity.getById(getMacroRegions(), id);
    }

    /**
     * Возвращает регион с данным ID
     * @return
     */
    public ChildEntity getRegionById(long id) {
        return BaseEntity.getById(getRegions(), id);
    }

    /**
     * Возвращает единицу сети с данным ID
     * @return
     */
    public BaseEntity getSystemUnitById(long id) {
        return BaseEntity.getById(getSystemUnits(), id);
    }

    /**
     * Возвращает признак перевыпуска с данным ID
     * @return
     */
    public BaseEntity getReissueSpeedById(long id) {
        return BaseEntity.getById(getReissueSpeeds(), id);
    }

    /**
     * Возвращает список значений для фильтра "Признак ЗП"
     * @return
     */
    public BaseEntity[] getZPSigns() {
        return mapper.getZPSigns();
    }

    /**
     * Возвращает список значений для фильтра "Тип ПУ"
     * @return
     */
    public ChildEntity[] getPYTypes() {
        return mapper.getPYTypes();
    }

    /**
     * Возвращает список значений для фильтра "Тип ПУ" отфильтрованный по "Признак ЗП"
     * @return
     */
    public ChildEntity[] getPYTypesByZPSignIds(final long[] zpSignIds) {
        return LinqWrapper.from(getPYTypes())
                .filter(new Predicate<ChildEntity>() {
                    @Override
                    public boolean check(ChildEntity item) {
                        return zpSignIds.length == 0 || ArrayUtils.contains(zpSignIds, item.parentId);
                    }
                })
                .distinct()
                .toArray(ChildEntity.class);
    }
}
