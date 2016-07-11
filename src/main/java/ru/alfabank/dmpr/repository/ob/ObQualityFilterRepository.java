package ru.alfabank.dmpr.repository.ob;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.filter.ob.ObQualityFilter;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.ob.ObQualityFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntityWithInfo;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;

/**
 * Репозиторий, отвечающий за загрузку фильтров для витрины "Показатели качества ОБ".
 */
@Repository
public class ObQualityFilterRepository {
    @Autowired
    private ObQualityFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "Тип периода"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Группа показателей"
     * @return
     */
    public BaseEntity[] getKPIKinds() {
        return mapper.getKPIKinds();
    }

    /**
     * Возвращает данные для фильтра "Показатель"
     * @param startDate Значение фильтра "Год" + "Период, с"
     * @param endDate Значение фильтра "Год" + "Период, по"
     * @return
     */
    public ChildEntityWithInfo[] getKPIs(ObQualityOptions obQualityOptions) {
        return mapper.getKPI(obQualityOptions);
    }

    /**
     * Возвращает данные для фильтра "Регион"
     * @return
     */
    public BaseEntity[] getRegions() {
        return mapper.getRegions();
    }

    /**
     * Возвращает данные для фильтра "Дирекция"
     * @return
     */
    public BaseEntity[] getDirections() {
        return mapper.getDirections();
    }

    /**
     * Возвращает группeу показателей по ее ID
     * @return
     */
    public BaseEntity getKPIKindById(final long kpiKindId){
        return LinqWrapper.from(getKPIKinds()).firstOrNull(new Predicate<BaseEntity>() {
            @Override
            public boolean check(BaseEntity item) {
                return item.id == kpiKindId;
            }
        });
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return Week[]
     */
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
