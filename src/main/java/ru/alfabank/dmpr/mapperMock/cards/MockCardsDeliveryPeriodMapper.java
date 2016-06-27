package ru.alfabank.dmpr.mapperMock.cards;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.dev.MockXlsGenericMapperBean;
import ru.alfabank.dmpr.mapper.cards.CardsCustomersPercentMapper;
import ru.alfabank.dmpr.mapper.cards.CardsDeliveryPeriodMapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;

import java.util.Objects;

@Component
public class MockCardsDeliveryPeriodMapper implements CardsDeliveryPeriodMapper {
    private CardsDeliveryPeriodMapper delegate;

    public MockCardsDeliveryPeriodMapper() {
        delegate = MockXlsGenericMapperBean.createProxy(CardsDeliveryPeriodMapper.class);
    }

    @Override
    public CardsDeliveryPeriodItem[] getKpi1DataItems(CardsDeliveryPeriodOptions options) {
        if (Objects.equals(options.getWidgetName(), "Table"))
            options.widgetUrl += options.systemUnitId == 1 ? "_byRegion"
                    : options.systemUnitId == 2 ? "_byCity"
                    : "";

        return delegate.getKpi1DataItems(options);
    }

    @Override
    public CardsDeliveryPeriodDetailsItem[] getKpi1Details(CardsDeliveryPeriodOptions options) {
        return delegate.getKpi1Details(options);
    }
}

