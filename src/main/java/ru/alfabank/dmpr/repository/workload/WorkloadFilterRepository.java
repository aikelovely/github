package ru.alfabank.dmpr.repository.workload;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.workload.WorkloadFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.DuodrReg;

@Repository
public class WorkloadFilterRepository {
    @Autowired
    WorkloadFilterMapper mapper;

    public BaseEntity[] getRpTypes() {
        return mapper.getRpTypes();
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    public Week[] getWeeks() {
        return mapper.getWeeks();
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
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    public BaseEntity[] getUCInnerEndProducts(LocalDate endDate) {
        return mapper.getUCInnerEndProducts(endDate);
    }


    public DuodrReg[] getDuodrReg() { return mapper.getDuodrReg(); }
}
