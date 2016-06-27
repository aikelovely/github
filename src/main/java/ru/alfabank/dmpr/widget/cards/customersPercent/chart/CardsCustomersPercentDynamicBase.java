package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовый класс для реализации графика "динамика".
 */
public abstract class CardsCustomersPercentDynamicBase extends BaseChart<CardsCustomersPercentOptions> {
    protected class NormativeValuePair {
        public Double Normative;
        public Double Value;

        public NormativeValuePair(Double normative, Double value) {
            Normative = normative;
            Value = value;
        }
    }

    protected class Configuration {
        public Selector<CardsCustomersPercentItem, Double> normativeSelector;
        public Selector<CardsCustomersPercentItem, Double> valueSelector;
        public Selector<NormativeValuePair, Color> colorSelector;
        public String serieName;
    }

    @Autowired
    private CardsCustomersPercentRepository repository;

    public CardsCustomersPercentDynamicBase() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    public ChartResult[] getData(CardsCustomersPercentOptions options) {
        options.systemUnitId = null;
        CardsCustomersPercentItem[] items = repository.getKpi2DataItems(options);

        List<Group<Long, CardsCustomersPercentItem>> macroRegionGroups = LinqWrapper
                .from(items)
                .group(new Selector<CardsCustomersPercentItem, Long>() {
                    @Override
                    public Long select(CardsCustomersPercentItem item) {
                        return item.macroRegionId;
                    }
                })
                .toList();

        ChartResult[] charts = new ChartResult[macroRegionGroups.size()];
        Configuration configuration = getConfiguration(options);

        for (Group<Long, CardsCustomersPercentItem> macroRegionGroup : macroRegionGroups) {
            Series series = new Series();
            Map<String, Object> bag = new HashMap<>();
            bag.put("macroRegionId", macroRegionGroup.getKey());

            Double normative = null;

            List<CardsCustomersPercentItem> macroRegionItems = macroRegionGroup.getItems()
                    .sort(new Selector<CardsCustomersPercentItem, LocalDate>() {
                        @Override
                        public LocalDate select(CardsCustomersPercentItem item) {
                            return item.calcDate;
                        }
                    }).toList();

            Point[] points = new Point[macroRegionItems.size()];

            for (int i = 0; i < macroRegionItems.size(); i++) {
                CardsCustomersPercentItem item = macroRegionItems.get(i);

                if (i == 0) {
                    normative = configuration.normativeSelector.select(item);
                }

                double value = configuration.valueSelector.select(item);

                Point point = new Point(item.calcDate, value);

                point.color = configuration.colorSelector.select(new NormativeValuePair(normative, value));

                points[i] = point;
            }

            bag.put("normative", normative);

            series.data = points;
            series.type = ChartType.column;
            series.name = configuration.serieName;

            charts[macroRegionGroup.getIndex()] = new ChartResult(new Series[]{series}, bag);
        }

        return charts;
    }

    protected abstract Configuration getConfiguration(CardsCustomersPercentOptions options);
}

