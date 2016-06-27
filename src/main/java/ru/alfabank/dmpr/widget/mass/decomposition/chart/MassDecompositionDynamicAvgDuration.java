package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.decomposition.KpiDataItem;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;
import ru.alfabank.dmpr.model.mass.decomposition.StageInfo;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.Map;

@Service
public class MassDecompositionDynamicAvgDuration extends BaseChart<MassDecompositionOptions> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionDynamicAvgDuration() {
        super(MassDecompositionOptions.class);
    }

    @Override
    public ChartResult[] getData(MassDecompositionOptions options) {
        options.stageDetalization = 1;
        options.systemUnitId = 0;

        final Map<Long, StageInfo> stages = LinqWrapper.from(repository.getStages())
                .toMap(new Selector<StageInfo, Long>() {
                    @Override
                    public Long select(StageInfo stageInfo) {
                        return stageInfo.id;
                    }
                });

        Series[] series = LinqWrapper.from(repository.getKpiData(options))
                .group(new Selector<KpiDataItem, Long>() {
                    @Override
                    public Long select(KpiDataItem item) {
                        return item.stageId;
                    }
                }).filter(new Predicate<Group<Long, KpiDataItem>>() {
                    @Override
                    public boolean check(Group<Long, KpiDataItem> item) {
                        return stages.get(item.getKey()) != null;
                    }
                }).select(new Selector<Group<Long, KpiDataItem>, Series>() {
                    @Override
                    public Series select(Group<Long, KpiDataItem> group) {
                        Point[] points = group.getItems().select(new Selector<KpiDataItem, Point>() {
                            @Override
                            public Point select(KpiDataItem item) {
                                double value = MathHelper.safeDivide(item.totalDuration, item.bpCount);
                                Point p = new Point(item.calcDate, value);
                                p.tag = item.bpCount;
                                return p;
                            }
                        }).toArray(Point.class);

                        StageInfo stage = stages.get(group.getKey());

                        return new Series(stage.name, points);
                    }
                }).toArray(Series.class);

        return new ChartResult[]{new ChartResult(series)};
    }
}

