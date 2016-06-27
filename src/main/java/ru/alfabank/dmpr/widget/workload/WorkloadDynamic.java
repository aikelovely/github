package ru.alfabank.dmpr.widget.workload;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.workload.WLDynamicItem;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.model.workload.WorkloadQueryOptions;
import ru.alfabank.dmpr.model.workload.WorkloadRichPoint;
import ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;

@Service
public class WorkloadDynamic extends BaseChart<WorkloadOptions>
{
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadDynamic() {
        super(WorkloadOptions.class);
    }

    @Override
    public ChartResult[] getData(WorkloadOptions options) {
        final Week[] weeks = filterRepository.getWeeks();

        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, weeks);
        queryOptions.weekCount = 8;

        Point[] staffFactPoints = LinqWrapper.from(repository.getWLDynamic(queryOptions))
                .select(new Selector<WLDynamicItem, Point>() {
                    @Override
                    public Point select(WLDynamicItem info) {
                        return new WorkloadRichPoint(new Point(info.calcDate, info.staffCountFact), info, weeks);
                    }
                }).toArray(Point.class);

        ArrayList<Point> staffCalcPoints = new ArrayList<>();
        ArrayList<Point> workratePoints = new ArrayList<>();

        for (WLDynamicItem item : repository.getWLDynamic(new WorkloadQueryOptions(options, weeks))) {
                staffCalcPoints.add(new WorkloadRichPoint(new Point(item.calcDate, item.staffCountCalc), item, weeks));
                workratePoints.add(new WorkloadRichPoint(new Point(item.calcDate, item.workloadRate*100), item, weeks));
        }

        Series[] series = new Series[]{
                new Series("Фактическая численность", staffFactPoints,
                        ChartType.area, Color.valueOf("#434348")),
                new Series("Расчетная численность", staffCalcPoints.toArray(new Point[staffCalcPoints.size()]),
                        ChartType.area, Color.valueOf("#7cb5ec")),
                new Series("Процент нагрузки", workratePoints.toArray(new Point[workratePoints.size()]),
                        ChartType.line, Color.RedColor)
        };

        return new ChartResult[]{new ChartResult(series)};
    }
}


