package ru.alfabank.dmpr.mapper.workload;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.DuodrReg;

public interface WorkloadFilterMapper {
    BaseEntity[] getRpTypes();
    Week[] getWeeks();

    DuodrReg[] getDuodrReg();
}
