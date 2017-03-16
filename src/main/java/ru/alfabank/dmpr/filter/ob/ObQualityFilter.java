package ru.alfabank.dmpr.filter.ob;

import org.joda.time.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.ArrayHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.*;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрины "Показатели качества ОБ".
 */
@Service
public class ObQualityFilter {
    @Autowired
    ObQualityFilterRepository filterRepository;

    /**
     * Возвращает данные для фильтра "Регион"
     * @return
     */
    public BaseEntity[] getRegions(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                @Param("kpiKindId") final long kpiKindId, @Param("directionIds[]") final String[] directionIds,
                                @Param("regionIds[]") final String[] regionIds,
                                @Param("timeUnitId") long timeUnitId){
        BasePeriodOptions options = new BasePeriodOptions();
        options.startYear = startYear;
        options.startDateId = startDateId;
        options.endYear = endYear;
        options.endDateId = endDateId;
        options.timeUnitId = (int)timeUnitId;
        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options, filterRepository.getWeeks());
        ObQualityOptions obQualityOptions = new ObQualityOptions();
        obQualityOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        obQualityOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);
        obQualityOptions.directionIds = directionIds;
        obQualityOptions.regionIds = regionIds;
        obQualityOptions.kpiKindId = kpiKindId;
        return filterRepository.getRegions(obQualityOptions);
    }

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @return
     */
    public BaseEntity[] getDirections(){
        return filterRepository.getDirections();
    }

    /**
     * Возвращает данные для фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getTimeUnits(){
        return filterRepository.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Группа показателей"
     * @return
     */
    public BaseEntity[] getKPIKinds(){
        return filterRepository.getKPIKinds();
    }

    /**
     * Возвращает данные для фильтра "Показатель"
     * @param startYear Значение фильтра "Год"
     * @param endYear Значение фильтра "Год"
     * @param startDateId Значение фильтра "Период, с"
     * @param endDateId Значение фильтра "Период, по"
     * @param kpiKindId Значение фильтра "Группа показателей"
     * @param directionIds Значение фильтра "Дирекция"
     * @param timeUnitId Значение фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getKPIs(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                @Param("kpiKindId") final long kpiKindId, @Param("directionIds[]") final String[] directionIds,
                                @Param("regionIds[]") final String[] regionIds,
                                @Param("timeUnitId") long timeUnitId){
        BasePeriodOptions options = new BasePeriodOptions();
        options.startYear = startYear;
        options.startDateId = startDateId;
        options.endYear = endYear;
        options.endDateId = endDateId;
        options.timeUnitId = (int)timeUnitId;

        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options, filterRepository.getWeeks());

        ObQualityOptions obQualityOptions = new ObQualityOptions();
        obQualityOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        obQualityOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);
        obQualityOptions.directionIds = directionIds;
        obQualityOptions.regionIds = regionIds;
        obQualityOptions.kpiKindId = kpiKindId;
        return LinqWrapper.from(filterRepository.getKPIs(obQualityOptions
                )).filter(new Predicate<ChildEntityWithInfo>() {
            @Override
            public boolean check(ChildEntityWithInfo item) {
                String[] addInfoArray = item.additionalInfo != null ? item.additionalInfo.split(",") : new String[0];
                return item.parentId == kpiKindId &&
                        (ArrayHelper.IsNullOrEmpty(directionIds) || ArrayHelper.AtLeastOneCommonElement(directionIds, addInfoArray));
            }
        }).toArray(ChildEntityWithInfo.class);
    }

    /**
     * Возвращает данные для фильтра "Период, с"
     * @param yearDate Значение фильтра "Год"
     * @param timeUnitId Значение фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getStartDates(@Param("startYear") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
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
