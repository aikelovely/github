package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс для реализации графика "пирог"
 */
public abstract class CardsCustomersPercentPieBase extends BaseChart<CardsCustomersPercentOptions> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    public CardsCustomersPercentPieBase() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    public ChartResult[] getData(CardsCustomersPercentOptions options) {
        options.systemUnitId = null;
        options.timeUnitId = null;
        CardsCustomersPercentItem[] items = repository.getKpi2DataItems(options);

        ChartResult[] charts = new ChartResult[items.length];

        for (int i = 0; i < items.length; i++) {
            CardsCustomersPercentItem item = items[i];
            Series series = new Series();
            Map<String, Object> bag = new HashMap<>();
            bag.put("macroRegionId", item.macroRegionId);

            series.data = getPointSelector().select(item);

            charts[i] = new ChartResult(new Series[]{series}, bag);
        }

        return charts;
    }

    protected abstract Selector<CardsCustomersPercentItem, Point[]> getPointSelector();
}
