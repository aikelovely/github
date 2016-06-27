package ru.alfabank.dmpr.mapper.cards;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.BaseDataContextTests;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;

import static org.junit.Assert.*;

public class CardsDeliveryPeriodMapperTest extends BaseDataContextTests {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CardsDeliveryPeriodMapper mapper;

    @Test
    public void testGetKpi1DataItems_Pie() throws Exception {
        CardsDeliveryPeriodItem[] items = getKpi1DataItems("CardsDeliveryPeriodPie");
        assertNotEquals(0, items.length);
    }

    @Test
    public void testGetKpi1DataItems_Dynamic() throws Exception {
        CardsDeliveryPeriodItem[] items = getKpi1DataItems("CardsDeliveryPeriodDynamic");
        assertNotEquals(0, items.length);
    }

    @Test
    public void testGetKpi1DataItems_DistributionDynamic() throws Exception {
        CardsDeliveryPeriodItem[] items = getKpi1DataItems("CardsDeliveryPeriodDistributionDynamic");
        assertNotEquals(0, items.length);
    }

    private CardsDeliveryPeriodItem[] getKpi1DataItems(String url) {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.widgetUrl = url;
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 2, 6);
        options.timeUnitId = 2;
        return mapper.getKpi1DataItems(options);
    }


    @Test
    public void testGetKpi1DataItems_Table_byRegion() throws Exception {
        CardsDeliveryPeriodItem[] items = getKpi1DataItems_Table(1);
        assertNotEquals(0, items.length);
    }

    @Test
    public void testGetKpi1DataItems_Table_byCity() throws Exception {
        CardsDeliveryPeriodItem[] items = getKpi1DataItems_Table(2);
        assertNotEquals(0, items.length);
    }

    private CardsDeliveryPeriodItem[] getKpi1DataItems_Table(int systemUnitId) {
        CardsDeliveryPeriodOptions options = new CardsDeliveryPeriodOptions();
        options.widgetUrl = "CardsDeliveryPeriodTable";
        options.systemUnitId = systemUnitId;
        options.startDate = new LocalDate(2014, 1, 28);
        options.endDate = new LocalDate(2014, 2, 6);
        options.timeUnitId = 2;
        return mapper.getKpi1DataItems(options);
    }
}