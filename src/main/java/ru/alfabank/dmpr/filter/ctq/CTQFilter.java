package ru.alfabank.dmpr.filter.ctq;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.ctq.CTQDashboardKPIEntity;
import ru.alfabank.dmpr.model.ctq.CTQLayoutItem;
import ru.alfabank.dmpr.repository.ctq.CTQFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин CTQDashboard.
 */
@Service
public class CTQFilter {
    @Autowired
    CTQFilterRepository filterRepository;

    /**
     * Возвращает данные для фильтра "Тип периода".
     * @return Список значений фильтра "Тип периода"
     */
    public BaseEntity[] getTimeUnits(){
        return filterRepository.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Показатели".
     * @return Список показателей
     */
    public CTQDashboardKPIEntity[] getKPIs(){
        return filterRepository.getKPIs();
    }

    /**
     * Возвращает данные для фильтра "Период, с".
     * @param yearDate год, значение соответствующего фильтра
     * @param timeUnitId значение фильтра "Тип периода"
     * @return
     */
    public PeriodSelectOption[] getStartDates(@Param("startYear") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = filterRepository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }
        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }

    /**
     * Возвращает данные для фильтра "Период, по".
     * @param yearDate год, значение соответствующего фильтра
     * @param timeUnitId значение фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getEndDates(@Param("endYear") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = filterRepository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }
        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }

    public BaseEntity[] getDates(@Param("startYear") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = filterRepository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }
        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }

    public CTQLayoutItem[] getLayout(){
        return filterRepository.getLayout();
    }

    public Week[] getWeeks(){
        return filterRepository.getWeeks();
    }
}
