package ru.alfabank.dmpr.filter.workload;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.workload.DuodrReg;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;

@Service
public class WorkloadFilter {
    @Autowired
    WorkloadFilterRepository repository;

    /**
     * Возвращает данные для фильтра "Неделя"
     * @param yearDate Значение фильтра "Год"
     * @return
     */
    public BaseEntity[] getWeeks(@Param("year") LocalDate yearDate){
        return PeriodSelectHelper.getWeeks(repository.getWeeks(yearDate.getYear()));
    }

    public BaseEntity[] getRpTypes() {
        return repository.getRpTypes();
    }

    public DuodrReg[] getDuodrReg() { return repository.getDuodrReg(); }
}
