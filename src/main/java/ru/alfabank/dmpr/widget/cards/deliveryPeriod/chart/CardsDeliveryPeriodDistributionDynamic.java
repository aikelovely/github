package ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseChart;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.CardsDeliveryPeriodWidgetHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Распределение количества/процента карт от количества рабочих дней, потраченных на доставку"
 */
@Service
public class CardsDeliveryPeriodDistributionDynamic extends BaseChart<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;
    @Autowired
    private CardsDeliveryPeriodWidgetHelper widgetHelper;

    public CardsDeliveryPeriodDistributionDynamic() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final CardsDeliveryPeriodOptions options) {
        CardsDeliveryPeriodItem[] items = repository.getDistributionDynamicItems(options);

        final Selector<CardsDeliveryPeriodItem, Long> seriesKeySelector = widgetHelper.getDistributionKeySelector(options);

        return LinqWrapper.from(items)
                .sort(new Selector<CardsDeliveryPeriodItem, LocalDate>() {
                    @Override
                    public LocalDate select(CardsDeliveryPeriodItem item) {
                        return item.calcDate;
                    }
                })
                .group(new Selector<CardsDeliveryPeriodItem, Long>() {
                    @Override
                    public Long select(CardsDeliveryPeriodItem item) {
                        return item.macroRegionId;
                    }
                })
                .select(new Selector<Group<Long, CardsDeliveryPeriodItem>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Long, CardsDeliveryPeriodItem> macroRegionGroup) {
                        Map<String, Object> bag = new HashMap<>();
                        bag.put("macroRegionId", macroRegionGroup.getKey());

                        final Map<LocalDate, Integer> cardCountTotals = getCardCountTotals(macroRegionGroup);

                        Series[] seriesArray = macroRegionGroup.getItems()
                                .group(seriesKeySelector)
                                .sort(new Selector<Group<Long, CardsDeliveryPeriodItem>, Long>() {
                                    @Override
                                    public Long select(Group<Long, CardsDeliveryPeriodItem> group) {
                                        return group.getKey();
                                    }
                                })
                                .select(new Selector<Group<Long, CardsDeliveryPeriodItem>, Series>() {
                                    @Override
                                    public Series select(Group<Long, CardsDeliveryPeriodItem> seriesGroup) {
                                        return createSeries(seriesGroup, cardCountTotals, options);
                                    }
                                })
                                .toArray(Series.class);

                        return new ChartResult(seriesArray, bag);
                    }
                })
                .toArray(ChartResult.class);
    }

    private Map<LocalDate, Integer> getCardCountTotals(Group<Long, CardsDeliveryPeriodItem> regionGroup) {
        return regionGroup.getItems()
                .group(new Selector<CardsDeliveryPeriodItem, LocalDate>() {
                    @Override
                    public LocalDate select(CardsDeliveryPeriodItem item) {
                        return item.calcDate;
                    }
                })
                .select(new Selector<Group<LocalDate, CardsDeliveryPeriodItem>, Pair<LocalDate, Integer>>() {
                    @Override
                    public Pair<LocalDate, Integer> select(Group<LocalDate, CardsDeliveryPeriodItem> group) {
                        return Pair.of(group.getKey(), group.getItems().sum(new Selector<CardsDeliveryPeriodItem, Integer>() {
                            @Override
                            public Integer select(CardsDeliveryPeriodItem item) {
                                return item.cardCount;
                            }
                        }, 0));
                    }
                })
                .toMap(new Selector<Pair<LocalDate, Integer>, LocalDate>() {
                    @Override
                    public LocalDate select(Pair<LocalDate, Integer> pair) {
                        return pair.getKey();
                    }
                }, new Selector<Pair<LocalDate, Integer>, Integer>() {
                    @Override
                    public Integer select(Pair<LocalDate, Integer> pair) {
                        return pair.getValue();
                    }
                });
    }

    private Series createSeries(Group<Long, CardsDeliveryPeriodItem> group,
                                final Map<LocalDate, Integer> cardCountTotals, final CardsDeliveryPeriodOptions options) {
        class RichPoint extends Point {
            public int cardCount;
            public double valuePercent;

            public RichPoint(LocalDate x, Double y) {
                super(x, y);
            }
        }

        RichPoint[] points = group.getItems().select(new Selector<CardsDeliveryPeriodItem, RichPoint>() {
            @Override
            public RichPoint select(CardsDeliveryPeriodItem item) {
                double valuePercent = MathHelper.safeDivide(
                        item.cardCount,
                        (double) cardCountTotals.get(item.calcDate)) * 100;
                double y = options.paramType == ParamType.Percent ? valuePercent : item.cardCount;

                RichPoint point = new RichPoint(item.calcDate, y);
                point.cardCount = item.cardCount;
                point.valuePercent = valuePercent;
                return point;
            }
        }).toArray(RichPoint.class);

        String name = widgetHelper.getSeriesName(group, options);
        return new Series(name, points);
    }
}
