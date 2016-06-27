package ru.alfabank.dmpr.mapper.workload;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;

public interface WorkloadFilterMapper {
    BaseEntity[] getRpTypes();
    Week[] getWeeks();
}
