package ru.alfabank.dmpr.repository.workload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.workload.WorkloadMapper;
import ru.alfabank.dmpr.model.workload.*;

@Repository
public class WorkloadRepository {
    @Autowired
    WorkloadMapper mapper;

    public DivisionStat[] getDivisionStat(WorkloadQueryOptions options) {
        return mapper.getDivisionStat(options);
    }

    public WLDynamicItem[] getWLDynamic(WorkloadQueryOptions options) {
        return mapper.getWLDynamic(options);
    }

    public ESUDynamicItem[] getESUDynamic(WorkloadQueryOptions options) {
        return mapper.getESUDynamic(options);
    }

    public DistributionInfo[] getStaffDistribution(WorkloadQueryOptions options) { return mapper.getStaffDistribution(options); }

    public DivisionStat[] getTopNTable(WorkloadQueryOptions options) { return mapper.getTopNTable(options); }


}
