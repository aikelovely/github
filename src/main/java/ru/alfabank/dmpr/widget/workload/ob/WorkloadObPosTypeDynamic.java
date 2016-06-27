package ru.alfabank.dmpr.widget.workload.ob;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.ChartType;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.*;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseChart;

@Service
public class WorkloadObPosTypeDynamic extends BaseChart<WorkloadOptions> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObPosTypeDynamic() {
        super(WorkloadOptions.class);
    }

    @Override
    public ChartResult[] getData(WorkloadOptions options) {
        final Week[] weeks = filterRepository.getWeeks();

        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, weeks);
        queryOptions.sliceCode = SliceCode.POSTYPE.toString();
        queryOptions.weekCount = 8;

        LinqWrapper<DistributionInfo> dataWrapper = LinqWrapper.from(repository.getStaffDistribution(queryOptions));

        Series[] columnSeries = dataWrapper.group(new Selector<DistributionInfo, String>() {
            @Override
            public String select(DistributionInfo distributionInfo) {
                return distributionInfo.id;
            }
        }).select(new Selector<Group<String,DistributionInfo>, Series>() {
            @Override
            public Series select(Group<String, DistributionInfo> group) {
                LinqWrapper<DistributionInfo> items = group.getItems();
                DistributionInfo first = items.first();

                Point[] points = items.select(new Selector<DistributionInfo, Point>() {
                    @Override
                    public Point select(DistributionInfo item) {
                        return new WorkloadRichPoint(new Point(item.calcdate, item.staffCountFact), item, weeks);
                    }
                }).toArray(Point.class);

                return new Series(first.unitName, points, ChartType.column);
            }
        }).toArray(Series.class);

        Series lineSeries = new Series("Штатная численность",
                dataWrapper.group(new Selector<DistributionInfo, LocalDate>() {
            @Override
            public LocalDate select(DistributionInfo distributionInfo) {
                return distributionInfo.calcdate;
            }
        }).select(new Selector<Group<LocalDate,DistributionInfo>, Point>() {
            @Override
            public Point select(Group<LocalDate, DistributionInfo> distributionInfos) {
                double value = distributionInfos.getItems().sum(new Selector<DistributionInfo, Double>() {
                    @Override
                    public Double select(DistributionInfo distributionInfo) {
                        return distributionInfo.staffCountFact;
                    }
                });
                return new WorkloadRichPoint(new Point(distributionInfos.getKey(), value), distributionInfos.getItems().first(), weeks);
            }
        }).toArray(Point.class), ChartType.line);

        return new ChartResult[] { new ChartResult(ArrayUtils.addAll(columnSeries, lineSeries)) };
    }
}
