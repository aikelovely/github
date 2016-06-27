package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentParamType;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPeriodWidgetHelper;

/**
 * График "Динамика по количеству/проценту карт, не забранных из отделений"
 */
@Service
public class CardsCustomersPercentNotPickedUpDynamic extends CardsCustomersPercentDynamicBase {
    @Autowired
    private CardsCustomersPeriodWidgetHelper widgetHelper;

    @Override
    protected Configuration getConfiguration(final CardsCustomersPercentOptions options) {
        final Configuration result = new Configuration();

        result.normativeSelector = new Selector<CardsCustomersPercentItem, Double>() {
            @Override
            public Double select(CardsCustomersPercentItem item) {
                return options.paramType == CardsCustomersPercentParamType.Percent
                        ? item.unTakenCardKPI * 100
                        : null;
            }
        };

        result.valueSelector = new Selector<CardsCustomersPercentItem, Double>() {
            @Override
            public Double select(CardsCustomersPercentItem item) {
                return options.paramType == CardsCustomersPercentParamType.Percent
                        ? widgetHelper.getUnTakenCardPercent(item) * 100
                        : item.unTakenCardCount;
            }
        };

        result.colorSelector = new Selector<NormativeValuePair, Color>() {
            @Override
            public Color select(NormativeValuePair pair) {
                return pair.Normative == null
                        ? Color.BlueColor
                        : (pair.Value > pair.Normative ? Color.RedColor : Color.GreenColor);
            }
        };

        result.serieName = String.format("%s карт, не забранных из отделений",
                options.paramType == CardsCustomersPercentParamType.Percent
                        ? "Процент"
                        : "Количество");

        return result;
    }
}

