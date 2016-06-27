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
import ru.alfabank.dmpr.model.mass.decomposition.*;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

@Service
public class MassDecompositionDynamicByStagePercent extends BaseChart<MassDecompositionOptions> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionDynamicByStagePercent() {
        super(MassDecompositionOptions.class);
    }

    @Override
    public ChartResult[] getData(final MassDecompositionOptions options) {
        options.stageDetalization = 1;
        options.systemUnitId = 0;
        options.durationLimitHours = 24d;

        final StageInfo stage = LinqWrapper.from(repository.getStages()).firstOrNull(new Predicate<StageInfo>() {
            @Override
            public boolean check(StageInfo item) {
                return item.id == options.stageId;
            }
        });

        String title = "Доля на этапе \"" + stage.name + "\"";

        final TresholdItem tresholdItem = repository.getTresholdByCode(TresholdCode.ByDay);

        Point[] points = LinqWrapper.from(repository.getKpiData(options)).filter(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.stageId == options.stageId;
            }
        }).select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem kpiDataItem) {
                double value = MathHelper.safeDivide(kpiDataItem.inLimitBpCount, kpiDataItem.bpCount);
                Point p = new Point(kpiDataItem.calcDate, value*100);
                p.color = value >= tresholdItem.warning ? Color.GreenColor : value <= tresholdItem.error ? Color.RedColor : Color.OrangeColor;
                return p;
            }
        }).toArray(Point.class);

        Series[] series = new Series[]{new Series("Доля", points)};

        Map<String, Object> bag = new HashMap<>();
        bag.put("stage", stage);
        bag.put("title", title);
        bag.put("treshold", tresholdItem);

        return new ChartResult[]{ new ChartResult(series, bag) };
    }
}
