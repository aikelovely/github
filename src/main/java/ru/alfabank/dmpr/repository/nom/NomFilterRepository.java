package ru.alfabank.dmpr.repository.nom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.nom.NomFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.Week;


/**
 * Репозиторий для фильтров витрины "Количество конечных продуктов для расчета UC ОБ"
 */
@Repository
public class NomFilterRepository {
    @Autowired
    private NomFilterMapper mapper;

    /**
     * Возвращает список значений для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
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

    /**
     * Возвращает один элемент из списка "Единица времени"
     * @param id Id элемента
     * @return
     */
    public BaseEntity getTimeUnitById(long id) {
        return BaseEntity.getById(getTimeUnits(), id);
    }

    /**
     * Возвращает список значений для фильтра "Группа подразделений"
     * @return
     */
    public ChildEntity[] getDivisionGroups(){
        return mapper.getDivisionGroups();
    }
}
