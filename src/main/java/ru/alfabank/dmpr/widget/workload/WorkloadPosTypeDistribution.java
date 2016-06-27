package ru.alfabank.dmpr.widget.workload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.workload.*;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseChart;

@Service
public class WorkloadPosTypeDistribution extends BaseChart<WorkloadOptions>
{
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadPosTypeDistribution() {
        super(WorkloadOptions.class);
    }

    @Override
    public ChartResult[] getData(WorkloadOptions options) {
        WorkloadQueryOptions queryOptions  = new WorkloadQueryOptions(options, filterRepository.getWeeks());
        queryOptions.sliceCode = SliceCode.POSTYPE.toString();
        queryOptions.weekCount = 1;

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


