package ru.alfabank.dmpr.mapper.workload;

import ru.alfabank.dmpr.model.workload.*;

public interface WorkloadMapper {
    DivisionStat[] getDivisionStat(WorkloadQueryOptions options);
    WLDynamicItem[] getWLDynamic(WorkloadQueryOptions options);
    ESUDynamicItem[] getESUDynamic(WorkloadQueryOptions options);
    DistributionInfo[] getStaffDistribution(WorkloadQueryOptions options);
    DivisionStat[] getTopNTable(WorkloadQueryOptions options);
    BpiepnormValue[]  getBpiepnormValue();
    BpworkWeekReport[]  getBpworkWeek();
    ConvertCountReport[]  getConvertCount();
    WorkloadReport[]  getWorkload();
}

