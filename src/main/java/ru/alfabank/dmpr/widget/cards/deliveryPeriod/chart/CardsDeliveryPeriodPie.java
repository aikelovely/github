package ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * График "Круговая диаграмма уложившихся в KPI"
 */
@Service
public class CardsDeliveryPeriodPie extends BaseChart<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;

    public CardsDeliveryPeriodPie() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    public ChartResult[] getData(CardsDeliveryPeriodOptions options) {
        CardsDeliveryPeriodItem[] items = repository.getPieItems(options);
        return LinqWrapper.from(items)
                .select(new Selector<CardsDeliveryPeriodItem, ChartResult>() {
                    @Override
                    public ChartResult select(CardsDeliveryPeriodItem item) {
                        Map<String, Object> bag = new HashMap<>();
                        bag.put("macroRegionId", item.macroRegionId);

                        Series series = createSeries(item);
                        return new ChartResult(new Series[]{series}, bag);
                    }
                })
                .toArray(ChartResult.class);
    }

    private Series createSeries(CardsDeliveryPeriodItem item) {
        Point[] points = {
                Point.withY((double) item.inKPICardCount, "Уложившихся в KPI", Color.GreenColor),
                Point.withY((double) item.cardCount - item.inKPICardCount, "Не уложившихся в KPI", Color.RedColor)
        };
        return new Series(points);
    }
}
