package ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Action;
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
 * График "Гистограмма: динамика показателя"
 */
@Service
public class CardsDeliveryPeriodDynamic extends BaseChart<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;
    @Autowired
    private CardsDeliveryPeriodWidgetHelper widgetHelper;

    public CardsDeliveryPeriodDynamic() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(final CardsDeliveryPeriodOptions options) {
        CardsDeliveryPeriodItem[] items = repository.getDynamicItems(options);

        return LinqWrapper
                .from(items)
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
                        Map<String, Object> bag = createBag(macroRegionGroup, options);
                        Series series = createSeries(macroRegionGroup, options);

                        return new ChartResult(new Series[]{series}, bag);
                    }
                })
                .toArray(ChartResult.class);
    }

    private Map<String, Object> createBag(Group<Long, CardsDeliveryPeriodItem> macroRegionGroup, CardsDeliveryPeriodOptions options) {
        Map<String, Object> bag = new HashMap<>();
        bag.put("macroRegionId", macroRegionGroup.getKey());

        final CardsDeliveryPeriodItem summer = new CardsDeliveryPeriodItem();
        macroRegionGroup.getItems().each(new Action<CardsDeliveryPeriodItem>() {
            @Override
            public void act(CardsDeliveryPeriodItem item) {
                summer.inKPICardCount += item.inKPICardCount;
                summer.cardCount += item.cardCount;
                summer.totalDuration += item.totalDuration;
            }
        });
        bag.put("averageValue", widgetHelper.getMainValue(summer, options));

        CardsDeliveryPeriodItem first = macroRegionGroup.getItems().first();
        bag.put("durationNormative", first.durationNormative);
        bag.put("percentNormative", first.percentNormative);

        return bag;
    }

    private Series createSeries(Group<Long, CardsDeliveryPeriodItem> regionGroup, final CardsDeliveryPeriodOptions options) {
        class RichPoint extends Point {
            public int cardCount;
            public int inKPICardCount;

            public RichPoint(LocalDate x, Double y) {
                super(x, y);
            }

            @Override
            public String toString() {
                return "RichPoint{" +
                        "cardCount=" + cardCount +
                        ", inKPICardCount=" + inKPICardCount +
                        "} " + super.toString();
            }
        }

        Point[] points = regionGroup.getItems()
                .select(new Selector<CardsDeliveryPeriodItem, Point>() {
                    @Override
                    public Point select(CardsDeliveryPeriodItem item) {
                        double value = widgetHelper.getMainValue(item, options);
                        RichPoint point = new RichPoint(item.calcDate, value);

                        point.color = options.paramType == ParamType.AvgDuration
                                ? (value > item.durationNormative ? Color.RedColor : Color.GreenColor)
                                : (value < item.percentNormative ? Color.RedColor : Color.GreenColor);

                        point.cardCount = item.cardCount;
                        point.inKPICardCount = item.inKPICardCount;
                        return point;
                    }
                })
                .toArray(Point.class);

        Series series = new Series(points);
        series.type = ChartType.column;
        series.name = options.paramType == ParamType.Percent
                ? "Процент карт, уложившихся в KPI"
                : "Средняя длительность";

        return series;
    }
}
