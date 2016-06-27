package ru.alfabank.dmpr.filter.nom;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.DateArraysHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.*;
import ru.alfabank.dmpr.repository.nom.NomFilterRepository;
import ru.alfabank.dmpr.repository.nom.NomRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины Количество конечных продуктов для расчета UC ОБ.
 */
@Service
public class NomFilter {
    @Autowired
    private NomFilterRepository filterRepository;

    @Autowired
    private NomRepository repository;

    /**
     * Значения для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return BaseEntity.toBaseEntities(filterRepository.getTimeUnits());
    }

    /**
     * Значения для фильтра "Группа подразделений"
     * @return
     */
    public BaseEntity[] getDivisionGroups(){
        return BaseEntity.toBaseEntities(filterRepository.getDivisionGroups());
    }

    /**
     * Значения для фильтра "Аналитический разрез"
     * @return
     */
    public BaseEntity[] getSubInnerProductsById(@Param("innerProductId") final long innerProductId){
        return LinqWrapper.from(repository.getInnerEndProducts()).filter(new Predicate<ChildEntityWithCode>() {
            @Override
            public boolean check(ChildEntityWithCode item) {
                return item.parentId == innerProductId;
            }
        }).toArray(ChildEntityWithCode.class);
    }

    /**
     * Возвращает данные для фильтра "Период, с"
     * @param yearDate Значение фильтра "Год"
     * @param timeUnitId Значение фильтра "Тип периода"
     * @return
     */
    public PeriodSelectOption[] getStartDates(@Param("startYear") LocalDate yearDate,
                                              @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = filterRepository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }

        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }

    /**
     * Возвращает данные для фильтра "Период, по"
     * @param yearDate Значение фильтра "Год"
     * @param timeUnitId Значение фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getEndDates(@Param("endYear") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = filterRepository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }

        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }
}
