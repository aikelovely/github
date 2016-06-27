package ru.alfabank.dmpr.mapperMock.cards;

import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.dev.MockXlsGenericMapperBean;
import ru.alfabank.dmpr.mapper.cards.CardsCustomersPercentMapper;
import ru.alfabank.dmpr.mapper.cards.CardsDeliveryPeriodMapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;

import java.util.Objects;

@Component
public class MockCardsCustomersPercentMapper implements CardsCustomersPercentMapper {
    private CardsCustomersPercentMapper delegate;

    public MockCardsCustomersPercentMapper() {
        delegate = MockXlsGenericMapperBean.createProxy(CardsCustomersPercentMapper.class);
    }


    @Override
    public CardsCustomersPercentItem[] getKpi2DataItems(CardsCustomersPercentOptions options) {
        if (options.getWidgetName().contains("DetailsTableReport")){
            switch (options.systemUnitId){
                case 0:
                    options.widgetUrl = "CardsCustomersPercentDetailsTableReportByMacroRegion";
                    break;
                case 1:
                    options.widgetUrl = "CardsCustomersPercentDetailsTableReportByRegion";
                    break;
                case 2:
                    options.widgetUrl = "CardsCustomersPercentDetailsTableReportByCity";
                    break;
                case 3:
                    options.widgetUrl = "CardsCustomersPercentDetailsTableReportByDo";
                    break;
            }
        }

        return delegate.getKpi2DataItems(options);
    }

    @Override
    public CardsCustomersPercentDetailsItem[] getKpi2UnTakenDetailsDataItems(CardsCustomersPercentOptions options) {
        return new CardsCustomersPercentDetailsItem[0];
    }

    @Override
    public CardsCustomersPercentDetailsItem[] getKpi2LostCardsDetailsDataItems(CardsCustomersPercentOptions options) {
        return new CardsCustomersPercentDetailsItem[0];
    }
}
