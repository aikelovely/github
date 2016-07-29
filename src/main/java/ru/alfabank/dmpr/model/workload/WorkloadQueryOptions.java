package ru.alfabank.dmpr.model.workload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.BasePeriodOptions;
import ru.alfabank.dmpr.model.Week;

public class WorkloadQueryOptions extends BaseOptions {
    public LocalDate endDate;
    public Long rpTypeId;
    public Long duodrReg;
    public Long divisionGroupId;
    public Long regionId;

    public String sliceCode;
    public int weekCount;
    public String innerEndProductId;
    private String widgetName;

    /*
    * Параметр для выгрузки лучших/худших (таблица ОБ)
    */
    public int topSide;

    @Override
    @JsonIgnore
    public String getWidgetName() {
        return widgetName;
    }

    public WorkloadQueryOptions(WorkloadOptions options, Week[] weeks){
        BasePeriodOptions bpOptions = new BasePeriodOptions();
        bpOptions.startYear = bpOptions.endYear = options.year;
        bpOptions.startDateId = bpOptions.endDateId = options.week;
        bpOptions.timeUnitId = 3;

        LocalDate[] periods = PeriodSelectHelper.getDatesByBasePeriodOptions(bpOptions, weeks);

        this.endDate = periods[1];
        this.rpTypeId = options.rpTypeId;

        this.divisionGroupId = options.divisionGroupId;
        this.regionId = options.regionId;
        this.duodrReg = options.duodrReg;
        this.widgetName = options.getWidgetName();
        this.innerEndProductId= options.innerEndProductId;

    }
}
