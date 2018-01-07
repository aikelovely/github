package ru.alfabank.dmpr.filter.qualityDss;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.*;
import ru.alfabank.dmpr.model.qualityDss.QualityDssOptions;
import ru.alfabank.dmpr.repository.qualityDss.QualityDssFilterRepository;

import java.util.List;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин МАСС.
 */
@Service
public class QualityDssFilter2 {
    @Autowired
    QualityDssFilterRepository repository;

    public BaseEntity[] getSystemUnits() {
        return repository.getSystemUnits();
    }

    public SelectOptGroup[] getCities() {
        BaseEntity[] regions = repository.getRegions();
        final ChildEntity[] cities = repository.getCities();

        return LinqWrapper.from(regions).select(new Selector<BaseEntity, SelectOptGroup>() {
            @Override
            public SelectOptGroup select(final BaseEntity region) {
                List<BaseEntity> citiesByRegion = LinqWrapper.from(cities)
                        .filter(new Predicate<ChildEntity>() {
                            @Override
                            public boolean check(ChildEntity city) {
                                return city.parentId == region.id;
                            }
                        })
                        .select(new Selector<ChildEntity, BaseEntity>() {
                            @Override
                            public BaseEntity select(ChildEntity childEntity) {
                                return childEntity.toBaseEntity();
                            }
                        }).toList();

                return new SelectOptGroup(region, citiesByRegion);
            }
        }).sort(new Selector<SelectOptGroup, String>() {
            @Override
            public String select(SelectOptGroup selectOptGroup) {
                return selectOptGroup.name;
            }
        }).toArray(SelectOptGroup.class);
    }

    public BaseEntity[] getTimeUnits() {
        return repository.getTimeUnits();
    }

    public BaseEntity[] getSalesChannel() {
        return repository.getSalesChannel();
    }


    public BaseEntity[] getDivision(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                    @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                    @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("timeUnitId") long timeUnitId) {
        BasePeriodOptions options2 = new BasePeriodOptions();
        options2.startYear = startYear;
        options2.startDateId = (long)startDateId;
        options2.endYear = endYear;
        options2.endDateId = (long)endDateId;
        options2.timeUnitId = Integer.valueOf((int)timeUnitId);
        options2.endDate= endDate;
        options2.startDate=startDate;
        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options2, this.repository.getWeeks());
        QualityDssOptions qualityDssOptions  = new QualityDssOptions();
        qualityDssOptions.reportIds=2;
        qualityDssOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        qualityDssOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);

        return repository.getDivision(qualityDssOptions);
    }
    public BaseEntity[] getEmployee(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                    @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                    @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("timeUnitId") long timeUnitId) {
        BasePeriodOptions options2 = new BasePeriodOptions();
        options2.startYear = startYear;
        options2.startDateId = (long)startDateId;
        options2.endYear = endYear;
        options2.endDateId = (long)endDateId;
        options2.timeUnitId = Integer.valueOf((int)timeUnitId);
        options2.endDate= endDate;
        options2.startDate=startDate;
        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options2, this.repository.getWeeks());
        QualityDssOptions qualityDssOptions  = new QualityDssOptions();
        qualityDssOptions.reportIds=2;
        qualityDssOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        qualityDssOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);

        return repository.getEmployee(qualityDssOptions);
    }
    public BaseEntity[] getOperation(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                    @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                    @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("timeUnitId") long timeUnitId) {
        BasePeriodOptions options2 = new BasePeriodOptions();
        options2.startYear = startYear;
        options2.startDateId = (long)startDateId;
        options2.endYear = endYear;
        options2.endDateId = (long)endDateId;
        options2.timeUnitId = Integer.valueOf((int)timeUnitId);
        options2.endDate= endDate;
        options2.startDate=startDate;
        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options2, this.repository.getWeeks());
        QualityDssOptions qualityDssOptions  = new QualityDssOptions();
        qualityDssOptions.reportIds=2;
        qualityDssOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        qualityDssOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);

        return repository.getOperation(qualityDssOptions);
    }
    public BaseEntity[] getTypeProduct(@Param("startYear") LocalDate startYear, @Param("endYear") LocalDate endYear,
                                    @Param("startDateId") int startDateId, @Param("endDateId") int endDateId,
                                    @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                    @Param("timeUnitId") long timeUnitId) {
        BasePeriodOptions options2 = new BasePeriodOptions();
        options2.startYear = startYear;
        options2.startDateId = (long)startDateId;
        options2.endYear = endYear;
        options2.endDateId = (long)endDateId;
        options2.timeUnitId = Integer.valueOf((int)timeUnitId);
        options2.endDate= endDate;
        options2.startDate=startDate;
        LocalDate[] dates = PeriodSelectHelper.getDatesByBasePeriodOptions(options2, this.repository.getWeeks());
        QualityDssOptions qualityDssOptions  = new QualityDssOptions();
        qualityDssOptions.reportIds=2;
        qualityDssOptions.startDate = dates[0].toLocalDateTime(LocalTime.MIDNIGHT);
        qualityDssOptions.endDate = dates[1].toLocalDateTime(LocalTime.MIDNIGHT);

        return repository.getTypeProduct(qualityDssOptions);
    }
    public BaseEntity[] getDopOffices(@Param("cityIds[]") long[] cityIds) {
        return repository.getDopOffices(cityIds);
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
            Week[] weeks = repository.getWeeks(yearDate.getYear());
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
            Week[] weeks = repository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }

        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }

}
