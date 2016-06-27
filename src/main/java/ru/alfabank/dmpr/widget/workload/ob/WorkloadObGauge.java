package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.workload.DivisionStat;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.model.workload.WorkloadQueryOptions;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class WorkloadObGauge extends BaseWidget<WorkloadOptions, Map<String, Object>> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObGauge() {
        super(WorkloadOptions.class);
    }

    @Override
    public Map<String, Object> getData(final WorkloadOptions options) {
        DivisionStat stat = LinqWrapper.from(repository.getDivisionStat(new WorkloadQueryOptions(options, filterRepository.getWeeks())))
                .firstOrNull(new Predicate<DivisionStat>() {
                    @Override
                    public boolean check(DivisionStat item) {
                        return item.lvl == 0;
                    }
                });

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("value", stat != null ? stat.workloadRate : null);

        return result;
    }
}
