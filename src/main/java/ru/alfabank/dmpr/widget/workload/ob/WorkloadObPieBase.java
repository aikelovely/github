package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.workload.DistributionInfo;
import ru.alfabank.dmpr.model.workload.SliceCode;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.model.workload.WorkloadQueryOptions;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseChart;

public abstract class WorkloadObPieBase extends BaseChart<WorkloadOptions> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObPieBase() {
        super(WorkloadOptions.class);
    }

    protected abstract SliceCode getSliceCode();

    protected abstract int getWeekCount();

    @Override
    public ChartResult[] getData(WorkloadOptions options) {
        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, filterRepository.getWeeks());
        queryOptions.sliceCode = getSliceCode().toString();
        queryOptions.weekCount = getWeekCount();


        Point[] points = LinqWrapper.from(repository.getStaffDistribution(queryOptions))
                .select(new Selector<DistributionInfo, Point>() {
                    @Override
                    public Point select(DistributionInfo info) {
                        return Point.withY(info.staffCountFact, info.unitName);
                    }
                }).toArray(Point.class);

        Series[] series = new Series[]{new Series("Распределение ш.ч.", points)};

        return new ChartResult[]{new ChartResult(series)};
    }
}
