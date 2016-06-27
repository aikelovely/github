package ru.alfabank.dmpr.widget.unitCost;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.unitCost.UnitCostDataItem;
import ru.alfabank.dmpr.model.unitCost.UnitCostPeriodOptions;
import ru.alfabank.dmpr.repository.unitCost.UnitCostRepository;
import ru.alfabank.dmpr.widget.BaseChart;

/**
 * График динамики на второй вкладке
 */
@Service
public class UnitCostDynamic extends BaseChart<UnitCostPeriodOptions> {
    @Autowired
    UnitCostRepository repository;

    public UnitCostDynamic() {
        super(UnitCostPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final UnitCostPeriodOptions options) {
        LinqWrapper<UnitCostDataItem> data = LinqWrapper.from(repository.getUCUnitCostDynamic(options))
                .filter(new Predicate<UnitCostDataItem>() {
                    @Override
                    public boolean check(UnitCostDataItem item) {
                        if(options.currencyId == 1){
                            return item.unitCostRurSum != null;
                        }
                        return item.unitCostUsdSum != null;
                    }
                })
                .sort(new Selector<UnitCostDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(UnitCostDataItem item) {
                        return item.calcDate;
                    }
                });

        Series[] unitCostSeries = data
                .group(new Selector<UnitCostDataItem, String>() {
                    @Override
                    public String select(UnitCostDataItem item) {
                        return item.groupName;
                    }
                })
                .select(new Selector<Group<String, UnitCostDataItem>, Series>() {
                    @Override
                    public Series select(Group<String, UnitCostDataItem> group) {
                        boolean isAccumulative = group.getKey().equals("Год");
                        String seriesName = isAccumulative ? "UnitCost, накопленным итогом" : "UnitCost, помесячно";

                        Point[] points = group.getItems()
                                .sort(new Selector<UnitCostDataItem, LocalDate>() {
                                    @Override
                                    public LocalDate select(UnitCostDataItem item) {
                                        return item.calcDate;
                                    }
                                })
                                .select(new Selector<UnitCostDataItem, Point>() {
                                    @Override
                                    public Point select(UnitCostDataItem item) {
                                        Double value = options.currencyId == 1 ? item.unitCostRurSum : item.unitCostUsdSum;

                                        return new Point(item.calcDate, value);
                                    }
                                })
                                .toArray(Point.class);

                        Series series = new Series(seriesName, points,
                                isAccumulative ? ChartType.column : ChartType.line);

                        if (!isAccumulative) {
                            series.color = Color.DarkOrangeColor;
                        }

                        return series;
                    }
                })
                .toArray(Series.class);

        String currencyText = options.currencyId == 1 ? "руб." : "$";

        data = data.filter(new Predicate<UnitCostDataItem>() {
            @Override
            public boolean check(UnitCostDataItem item) {
                return item.groupName.equals("Месяц");
            }
        });

        Series innerEndProductSeries = new Series("Количество КП",
                data.select(new Selector<UnitCostDataItem, Point>() {
                    @Override
                    public Point select(UnitCostDataItem item) {
                        return new Point(item.calcDate, item.totalLaborValueCnt);
                    }
                }).toArray(Point.class), ChartType.line, Color.DarkOrangeColor);

        Series totalCostSeries = new Series("TotalCost, тыс. " + currencyText,
                data.select(new Selector<UnitCostDataItem, Point>() {
                    @Override
                    public Point select(UnitCostDataItem item) {
                        Double value = options.currencyId == 1 ? item.totalRurSum : item.totalUsdSum;

                        if (value != null) {
                            value = value / 1000;
                        }

                        return new Point(item.calcDate, value);
                    }
                }).toArray(Point.class), ChartType.column);

        return new ChartResult[]{
                new ChartResult(unitCostSeries),
                new ChartResult(new Series[]{innerEndProductSeries, totalCostSeries})
        };
    }
}
