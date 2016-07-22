package ru.alfabank.dmpr.widget.workload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.ESUDynamicItem;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.model.workload.WorkloadQueryOptions;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.Objects;

@Service
public class WorkloadByProductIdWithDynamic extends BaseWidget<WorkloadOptions, ESUDynamicItem[]> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadByProductIdWithDynamic() {
        super(WorkloadOptions.class);
    }

    @Override
    public ESUDynamicItem[] getData(final WorkloadOptions options) {
        final Week[] weeks = filterRepository.getWeeks();

        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, weeks);
        queryOptions.weekCount = 8;

        ESUDynamicItem[] data = repository.getESUDynamic(queryOptions);

        LinqWrapper.from(data).each(new Action<ESUDynamicItem>() {
            @Override
            public void act(ESUDynamicItem value) {
                PeriodSelectOption psOption = PeriodSelectHelper.getWeekByYearAndNum(value.timeUnitYear, value.timeUnitPrdNum, weeks);
                value.timeUnitName = psOption != null ? psOption.name : "";
            }
        });
        //filter_blev
        return LinqWrapper.from(data)
                .filter(new Predicate<ESUDynamicItem>() {
                            @Override
                            public boolean check(ESUDynamicItem item) {
                                return Objects.equals(item.unitId, options.innerEndProductId);
                            }
                        }
                ).toArray(ESUDynamicItem.class);

    }
}

