package ru.alfabank.dmpr.filter.leaderBoard;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин КПЭ ОБ.
 */
@Service
public class LeaderBoardFilter {
    @Autowired
    LeaderBoardFilterRepository repository;

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
}
