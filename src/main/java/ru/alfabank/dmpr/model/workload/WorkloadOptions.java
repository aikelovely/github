package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Содержит значения все фильтров и выбранных параметров для витрины Нагрузка.
 */
public class WorkloadOptions extends BaseOptions {
    public LocalDate year;
    public int week;
    public Long rpTypeId;
    public Long duodrReg;
    public Long divisionGroupId;
    public Long regionId;
    public String innerEndProductId;

}

