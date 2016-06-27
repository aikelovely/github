package ru.alfabank.dmpr.repository.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.cards.CardsDeliveryPeriodMapper;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;

/**
 * Репозиторий для графиков витрины "Срок доставки пластиковых карт в отделения"
 */
@Repository
public class CardsDeliveryPeriodRepository {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private CardsDeliveryPeriodMapper mapper;

    /**
     * Функция, которая возвращает данные для пирога
     * @param options
     * @return
     */
    public CardsDeliveryPeriodItem[] getPieItems(CardsDeliveryPeriodOptions options) {
        options.systemUnitId = null;
        options.timeUnitId = null;
        return mapper.getKpi1DataItems(options);
    }

    /**
     * Функция, которая возвращает данные для динамики
     * @param options
     * @return
     */
    public CardsDeliveryPeriodItem[] getDynamicItems(CardsDeliveryPeriodOptions options) {
        options.systemUnitId = null;
        return mapper.getKpi1DataItems(options);
    }

    /**
     * Функция, которая возвращает данные для динамики распределения количества/процета карт
     * @param options
     * @return
     */
    public CardsDeliveryPeriodItem[] getDistributionDynamicItems(CardsDeliveryPeriodOptions options) {
        options.systemUnitId = null;
        if (!options.automaticExtensionMode)
            options.dayGrouped = 1;

        return mapper.getKpi1DataItems(options);
    }

    /**
     * Функция, которая возвращает данные для таблицы
     * @param options
     * @return
     */
    public CardsDeliveryPeriodItem[] getTableItems(CardsDeliveryPeriodOptions options) {
        return mapper.getKpi1DataItems(options);
    }

    /**
     * Функция, которая возвращает данные для детализированного отчета
     * @param options
     * @return
     */
    public CardsDeliveryPeriodDetailsItem[] getDetailsReportItems(CardsDeliveryPeriodOptions options) {
        return mapper.getKpi1Details(options);
    }
}
