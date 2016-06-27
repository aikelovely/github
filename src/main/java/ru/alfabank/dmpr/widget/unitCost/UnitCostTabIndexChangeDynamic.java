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

/**
 * График "Динамика изменения стоимости UnitCost"
 */
@Service
public class UnitCostTabIndexChangeDynamic extends BaseChart<UnitCostPeriodOptions> {
    @Autowired
    UnitCostRepository repository;

    public UnitCostTabIndexChangeDynamic() {
        super(UnitCostPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final UnitCostPeriodOptions options) {
        Point[] points = LinqWrapper.from(repository.getUCIndexChangeDynamic(options))
                .sort(new Selector<UnitCostDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(UnitCostDataItem item) {
                        return item.calcDate;
                    }
                })
                .select(new Selector<UnitCostDataItem, Point>() {
                    @Override
                    public Point select(UnitCostDataItem item) {
                        return new Point(item.calcDate, item.indexChangeFactor);
                    }
                })
                .toArray(Point.class);

        Series series = new Series(points);

        return new ChartResult[]{new ChartResult(new Series[]{series})};
    }
}
