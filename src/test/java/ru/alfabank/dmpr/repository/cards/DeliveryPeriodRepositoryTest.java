package ru.alfabank.dmpr.repository.cards;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.BaseDataContextTests;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;

import static org.junit.Assert.assertTrue;

public class DeliveryPeriodRepositoryTest extends BaseDataContextTests {
    @Autowired
    private CardsDeliveryPeriodRepository repository;

    @Test
    public void testGetDistributionDynamicItemsModeFalse() throws Exception {
        CardsDeliveryPeriodOptions options = getOptions();
        options.automaticExtensionMode = false;
        options.regionIds = new long[]{1, 2, 3};
        CardsDeliveryPeriodItem[] items = repository.getDistributionDynamicItems(options);
        boolean all = LinqWrapper.from(items).all(new Predicate<CardsDeliveryPeriodItem>() {
            @Override
            public boolean check(CardsDeliveryPeriodItem item) {
                return item.dayGroupId != null;
            }
        });
        assertTrue(all);
    }

    @Test
    public void testGetDistributionDynamicItemsModeTrue() throws Exception {
        CardsDeliveryPeriodOptions options = getOptions();
        options.automaticExtensionMode = true;
        CardsDeliveryPeriodItem[] items = repository.getDistributionDynamicItems(options);
        boolean any = LinqWrapper.from(items).any(new Predicate<CardsDeliveryPeriodItem>() {
            @Override
            public boolean check(CardsDeliveryPeriodItem item) {
                return item.reissueSpeedId != null;
            }
        });
        assertTrue(any);
    }

    private CardsDeliveryPeriodOptions getOptions() {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 2, 6);
        options.timeUnitId = 2;
        return options;
    }
}