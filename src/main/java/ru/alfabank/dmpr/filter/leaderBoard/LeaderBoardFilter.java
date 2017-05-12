package ru.alfabank.dmpr.filter.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин КПЭ ОБ.
 */
@Service
public class LeaderBoardFilter {
    @Autowired
    LeaderBoardFilterRepository repository;

    /**
     * Значения для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return BaseEntity.toBaseEntities(repository.getTimeUnits());
    }
    public BaseEntity[] getKpi5() {
        return BaseEntity.toBaseEntities(repository.getKpi5());
    }
    /**
     * Возвращает данные для фильтра "Группа подразделений".
     * @param startDate Может содержать значение фильтра "Год", "Месяц" либо "Период, с" в зависимости от графика/витрины.
     * @param endDate Может содержать значение фильтра "Год", "Месяц" либо "Период, по" в зависимости от графика/витрины.
     * @return Список групп подразделений
     */
    public BaseEntity[] getDivisionGroups(@Param("startDate") final LocalDate startDate,
                                          @Param("endDate") final LocalDate endDate) {
        return repository.getDivisionGroups(startDate, endDate);
    }

    public BaseEntity[] getStartDates(@Param("startDate") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
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
    public BaseEntity[] getEndDates(@Param("startDate") LocalDate yearDate, @Param("timeUnitId") long timeUnitId){
        if(timeUnitId == Period.week.getValue()){
            Week[] weeks = repository.getWeeks(yearDate.getYear());
            return PeriodSelectHelper.getWeeks(weeks);
        }
        return PeriodSelectHelper.getMonths(yearDate.getYear());
    }


}
