package ru.alfabank.dmpr.mapper.cards;

import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodDetailsItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;

/**
 * Маппер для графиков витрины "Срок доставки пластиковых карт в отделения"
 */
public interface CardsDeliveryPeriodMapper {
    /**
     * Основная функция для полученния данных
     * @param options
     * @return
     */
    CardsDeliveryPeriodItem[] getKpi1DataItems(CardsDeliveryPeriodOptions options);

    /**
     * Функция для детализированного отчета
     * @param options
     * @return
     */
    CardsDeliveryPeriodDetailsItem[] getKpi1Details(CardsDeliveryPeriodOptions options);
}
