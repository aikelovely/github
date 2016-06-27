package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.decomposition.TresholdCode;
import ru.alfabank.dmpr.model.mass.decomposition.TresholdItem;
import ru.alfabank.dmpr.model.mass.decomposition.KpiDataItem;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

@Service
public class MassDecompositionDynamicPercent extends BaseChart<MassDecompositionOptions> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionDynamicPercent() {
        super(MassDecompositionOptions.class);
    }

    @Override
    public ChartResult[] getData(MassDecompositionOptions options) {
        options.systemUnitId = 0;
        options.durationLimitHours = 24d;

        final TresholdItem tresholdItem = repository.getTresholdByCode(TresholdCode.ByDay);

        Point[] points = LinqWrapper.from(repository.getKpiData(options))
                .select(new Selector<KpiDataItem, Point>() {
                    @Override
                    public Point select(KpiDataItem item) {
                        double value = MathHelper.safeDivide(item.inLimitBpCount, item.bpCount);
                        Point p = new Point(item.calcDate, value*100);
                        p.color = value >= tresholdItem.warning ? Color.GreenColor
                                : value <= tresholdItem.error ? Color.RedColor : Color.OrangeColor;
                        return p;
                    }
                }).toArray(Point.class);

        return new ChartResult[]{new ChartResult(new Series[]{new Series("Доля", points)})};
    }
}
