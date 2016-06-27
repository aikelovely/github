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

@Service
public class WorkloadObCustomTable extends BaseWidget<WorkloadOptions, DivisionStat> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObCustomTable() {
        super(WorkloadOptions.class);
    }

    @Override
    public DivisionStat getData(final WorkloadOptions options) {
        return LinqWrapper.from(repository.getDivisionStat(new WorkloadQueryOptions(options, filterRepository.getWeeks())))
                .firstOrNull(new Predicate<DivisionStat>() {
                    @Override
                    public boolean check(DivisionStat item) {
                        return item.lvl == 0;
                    }
                });
    }
}
