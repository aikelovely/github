package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
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
public class WorkloadObWLDynamic extends BaseChart<WorkloadOptions> {
    @Autowired
    WorkloadRepository repository;

    @Autowired
    WorkloadFilterRepository filterRepository;

    public WorkloadObWLDynamic() {
        super(WorkloadOptions.class);
    }

    @Override
    public ChartResult[] getData(WorkloadOptions options) {
        Week[] weeks = filterRepository.getWeeks();

        WorkloadQueryOptions queryOptions = new WorkloadQueryOptions(options, weeks);
        queryOptions.weekCount = 8;

        WLDynamicItem[] data = repository.getWLDynamic(queryOptions);

        ArrayList<Point> staffFactPoints = new ArrayList<>();
        ArrayList<Point> staffCalcPoints = new ArrayList<>();
        ArrayList<Point> workratePoints = new ArrayList<>();

        for (WLDynamicItem item : data) {
            if (item.unitId.equals("0") || item.unitId.equals("0.0")) {
                staffFactPoints.add(new WorkloadRichPoint(new Point(item.calcDate, item.staffCountFact), item, weeks));
                staffCalcPoints.add(new WorkloadRichPoint(new Point(item.calcDate, item.staffCountCalc), item, weeks));
                workratePoints.add(new WorkloadRichPoint(new Point(item.calcDate, item.workloadRate*100), item, weeks));
            }
        }

        Series[] series = new Series[] {
                new Series("Фактическая численность", staffFactPoints.toArray(new Point[staffFactPoints.size()]),
                        ChartType.area, Color.valueOf("#434348")),
                new Series("Расчетная численность", staffCalcPoints.toArray(new Point[staffCalcPoints.size()]),
                        ChartType.area, Color.valueOf("#7cb5ec")),
                new Series("Процент нагрузки", workratePoints.toArray(new Point[workratePoints.size()]),
                        ChartType.line, Color.RedColor)
        };

        return new ChartResult[] { new ChartResult(series)};
    }
}
