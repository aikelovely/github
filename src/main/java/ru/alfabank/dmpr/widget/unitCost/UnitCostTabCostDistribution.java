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
 * График "Распределение расходов UnitCost"
 */
@Service
public class UnitCostTabCostDistribution extends BaseChart<UnitCostPeriodOptions> {
    @Autowired
    UnitCostRepository repository;

    public UnitCostTabCostDistribution() {
        super(UnitCostPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final UnitCostPeriodOptions options) {
        UnitCostDataItem[] data = repository.getUCCostDistribution(options);

        LinqWrapper<Group<Long, UnitCostDataItem>> groups = LinqWrapper.from(data)
                .sort(new Selector<UnitCostDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(UnitCostDataItem item) {
                        return item.calcDate;
                    }
                })
                .group(new Selector<UnitCostDataItem, Long>() {
                    @Override
                    public Long select(UnitCostDataItem item) {
                        return item.groupId;
                    }
                });

        class RichPoint extends Point{
            public Double totalRurSum;
            public Double totalUsdSum;

            public RichPoint(UnitCostDataItem item){
                super(item.calcDate, options.currencyId == 1 ? item.totalRurSum : item.totalUsdSum);
                this.totalRurSum = item.totalRurSum;
                this.totalUsdSum = item.totalUsdSum;
                this.y = this.y / 1000;
            }
        }

        Series[] series = groups.select(new Selector<Group<Long,UnitCostDataItem>, Series>() {
            @Override
            public Series select(Group<Long, UnitCostDataItem> items) {
                UnitCostDataItem first = items.getItems().first();

                Point[] points = items.getItems().select(new Selector<UnitCostDataItem, Point>() {
                    @Override
                    public Point select(UnitCostDataItem item) {
                        if(item == null) return null;
                        return new RichPoint(item);
                    }
                }).toArray(Point.class);

                String seriesName = first.groupName;
                Series series = new Series(seriesName, points);
                series.color = seriesName.contains("ФОТ") ? Color.DarkBlueColor : Color.DarkRedColor;

                return series;
            }
        }).toArray(Series.class);

        return new ChartResult[]{new ChartResult(series)};
    }
}
