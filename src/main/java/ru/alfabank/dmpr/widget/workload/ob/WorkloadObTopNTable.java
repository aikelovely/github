package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.workload.DivisionStat;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.model.workload.WorkloadQueryOptions;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorkloadObTopNTable extends BaseWidget<WorkloadOptions, Map<String, DivisionStat[]>> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObTopNTable() {
        super(WorkloadOptions.class);
    }

    private static Selector<DivisionStat, Double> sortSelector = new Selector<DivisionStat, Double>() {
        @Override
        public Double select(DivisionStat divisionStat) {
            return divisionStat.staffCountDeltaCnt;
        }
    };

    @Override
    public Map<String, DivisionStat[]> getData(final WorkloadOptions options) {

        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, filterRepository.getWeeks());

        queryOptions.topSide = 1;
        DivisionStat[] bestOnes = repository.getTopNTable(queryOptions);

        queryOptions.topSide = -1;
        DivisionStat[] worstOnes = repository.getTopNTable(queryOptions);

        Map<String, DivisionStat[]> bag = new HashMap<>();

        bag.put("best", LinqWrapper.from(bestOnes).sortDesc(sortSelector).toArray(DivisionStat.class));
        bag.put("worst", LinqWrapper.from(worstOnes).sort(sortSelector).toArray(DivisionStat.class));

        return bag;
    }
}
