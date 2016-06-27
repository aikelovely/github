package ru.alfabank.dmpr.widget.unitCost;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.unitCost.UnitCostDataItem;
import ru.alfabank.dmpr.model.unitCost.UnitCostPeriodOptions;
import ru.alfabank.dmpr.repository.unitCost.UnitCostRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График "Распределение ФОТ по категории расхода"
 */
@Service
public class UnitCostTabFotDistribution extends BaseChart<UnitCostPeriodOptions> {
    @Autowired
    UnitCostRepository repository;

    public UnitCostTabFotDistribution() {
        super(UnitCostPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final UnitCostPeriodOptions options) {
        LinqWrapper<UnitCostDataItem> data = LinqWrapper.from(repository.getUCFotDistribution(options));

        class RichPoint extends Point{
            public Double totalRurSum;
            public Double totalUsdSum;

            public RichPoint(UnitCostDataItem item){
                super((Double)null, options.currencyId == 1 ? item.totalRurSum : item.totalUsdSum);
                this.totalRurSum = item.totalRurSum;
                this.totalUsdSum = item.totalUsdSum;
                this.y = this.y / 1000;
                this.name = item.groupName;
            }
        }

        Point[] points = data.select(new Selector<UnitCostDataItem, Point>() {
            @Override
            public Point select(UnitCostDataItem item) {
                return new RichPoint(item);
            }
        }).toArray(Point.class);

        return new ChartResult[]{new ChartResult(new Series[]{new Series(points)})};
    }
}
