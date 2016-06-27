package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.decomposition.KpiDataItem;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;
import ru.alfabank.dmpr.model.mass.decomposition.StageInfo;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

@Service
public class MassDecompositionDynamicByStageAvgDuration extends BaseChart<MassDecompositionOptions> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionDynamicByStageAvgDuration() {
        super(MassDecompositionOptions.class);
    }

    @Override
    public ChartResult[] getData(final MassDecompositionOptions options) {
        options.stageDetalization = 1;
        options.systemUnitId = 0;

        final StageInfo stage = LinqWrapper.from(repository.getStages()).firstOrNull(new Predicate<StageInfo>() {
            @Override
            public boolean check(StageInfo item) {
                return item.id == options.stageId;
            }
        });

        String title = "Среднее время на этапе \"" + stage.name + "\"";

        Point [] points = LinqWrapper.from(repository.getKpiData(options)).filter(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.stageId == options.stageId;
            }
        }).select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                double value = MathHelper.safeDivide(kpiDataItem.totalDuration, kpiDataItem.bpCount);
                Point p = new Point(kpiDataItem.calcDate, value);
                p.color = value >= stage.normative ? Color.RedColor : Color.GreenColor;
                p.tag = "Открыто счетов: <b>" + kpiDataItem.bpCount + "</b>";
                return p;
            }
        }).toArray(Point.class);

        Series[] series = new Series[]{new Series("Среднее время", points)};

        Map<String, Object> bag = new HashMap<>();
        bag.put("stage", stage);
        bag.put("title", title);

        return new ChartResult[]{ new ChartResult(series, bag) };
    }
}
