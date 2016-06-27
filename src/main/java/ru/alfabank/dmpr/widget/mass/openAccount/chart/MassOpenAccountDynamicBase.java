package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;
import ru.alfabank.dmpr.repository.mass.MassOpenAccountRepository;
import ru.alfabank.dmpr.widget.BaseChart;

public abstract class MassOpenAccountDynamicBase extends BaseChart<MassOpenAccountOptions> {
    @Autowired
    MassOpenAccountRepository repository;

    private String hourInterval;

    public MassOpenAccountDynamicBase(String hourInterval) {
        super(MassOpenAccountOptions.class);
        this.hourInterval = hourInterval;
    }

    @Override
    public ChartResult[] getData(MassOpenAccountOptions options) {
        options.hourIntervals = this.hourInterval;

        KpiDataItem[] data = repository.getDynamic(options);

        Point[] points = LinqWrapper.from(data).select(new Selector<KpiDataItem, Point>() {
            @Override
            public Point select(KpiDataItem item) {
                double percent = MathHelper.safeDivide(item.bpCountGrp1, item.bpCount) * 100;
                return new Point(item.calcDate, percent);
            }
        }).toArray(Point.class);

        ChartResult result = new ChartResult(new Series[]{new Series("Доля", points)});

        return new ChartResult[]{result};
    }
}

