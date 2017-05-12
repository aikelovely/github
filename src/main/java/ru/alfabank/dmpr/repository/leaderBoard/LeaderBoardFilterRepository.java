package ru.alfabank.dmpr.repository.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.leaderBoard.LeaderBoardFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.Week;

/**
 * Репозиторий для фильтров витрины КПЭ ОБ.
 */
@Repository
public class LeaderBoardFilterRepository {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private LeaderBoardFilterMapper mapper;

    /**
     * Возвращает спиоск групп подразделений
     * @param startDate Год, месяц или значение фильтра "Период, с"
     * @param endDate Год, месяц или значение фильтра "Период, по"
     * @return Список групп подразделений
     */
    public BaseEntityWithCode[] getDivisionGroups(LocalDate startDate, LocalDate endDate) {
        LocalDate fixedEndDate = endDate != null ? endDate : startDate.plusYears(1).plusDays(-1);


        BaseEntityWithCode[] divisionGroups = mapper.getDivisionGroups(startDate, fixedEndDate);

//      подставляет то что надо послеfilter(new) (ctrl + пробел)   return LinqWrapper.from(divisionGroups).filter(new Predicate<BaseEntityWithCode>() {
        return LinqWrapper.from(divisionGroups).filter(new Predicate<BaseEntityWithCode>() {
            @Override
            public boolean check(BaseEntityWithCode item) {
                return !item.code.equals("ДРиД");
            }
        }).toArray(BaseEntityWithCode.class);


    }

    /**
     * Ищет группу подразделений по дате и коду.
     * @param startDate Год, месяц или значение фильтра "Период, с"
     *                  @param endDate Год, месяц или значение фильтра "Период, по"
     * @param code Код группы подразделений
     * @return Группа подразделений
     */
    public BaseEntityWithCode getDivisionGroupByCode(LocalDate startDate, LocalDate endDate, final String code){
        LocalDate fixedEndDate = endDate != null ? endDate : startDate.plusYears(1).plusDays(-1);
        return LinqWrapper.from(getDivisionGroups(startDate, fixedEndDate))
                .firstOrNull(new Predicate<BaseEntityWithCode>() {
                    @Override
                    public boolean check(final BaseEntityWithCode item) {
                        return item.code.equals(code);
                    }
                });
    }

    /**
     * Возвращает список значений для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    public BaseEntity[] getKpi5() {
        return mapper.getKpi5();
    }

    public Week[] getWeeks(final int year) {
        return LinqWrapper.from(mapper.getWeeks())
                .filter(new Predicate<Week>() {
                    @Override
                    public boolean check(Week item) {
                        return item.year == year;
                    }
                })
                .toArray(Week.class);
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    public Week[] getWeeks() {
        return mapper.getWeeks();
    }

}
