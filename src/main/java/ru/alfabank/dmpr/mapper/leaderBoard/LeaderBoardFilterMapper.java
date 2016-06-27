package ru.alfabank.dmpr.mapper.leaderBoard;

import org.joda.time.LocalDate;
import org.apache.ibatis.annotations.Param;
import ru.alfabank.dmpr.model.BaseEntityWithCode;

/**
 * MyBatis mapper. Используется для загрузки фильтров витрины КПЭ ОБ.
 */
public interface LeaderBoardFilterMapper {
    /**
     * Возвращает список дирекций
     * @param startDate Год, месяц или значение фильтра "Период, с"
     * @param endDate Год, месяц или значение фильтра "Период, по"
     * @return Список дирекций
     */
    BaseEntityWithCode[] getDivisionGroups(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);
}
