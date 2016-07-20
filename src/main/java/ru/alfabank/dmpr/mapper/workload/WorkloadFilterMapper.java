package ru.alfabank.dmpr.mapper.workload;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.DuodrReg;

public interface WorkloadFilterMapper {
    BaseEntity[] getRpTypes();
    Week[] getWeeks();

    DuodrReg[] getDuodrReg();


    /**
     * Возвращает данные для фильтра "Внутренний конечный продукт"
     * @param endDate Значение фильтра "Период, по"
     * @return
     */
    BaseEntity[] getUCInnerEndProducts(LocalDate endDate);


}
