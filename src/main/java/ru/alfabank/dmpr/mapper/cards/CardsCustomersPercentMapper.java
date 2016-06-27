package ru.alfabank.dmpr.mapper.cards;

import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;

/**
 * Маппер для графиков витрины "Доля клиентов, получивших карту в указанном отделении"
 */
public interface CardsCustomersPercentMapper {
    /**
     * Основная функция для полученния данных
     * @param options
     * @return
     */
    CardsCustomersPercentItem[] getKpi2DataItems(CardsCustomersPercentOptions options);

    /**
     * Функция для выгрузки "Детализированный отчет по не забранным картам"
     * @param options
     * @return
     */
    CardsCustomersPercentDetailsItem[] getKpi2UnTakenDetailsDataItems(CardsCustomersPercentOptions options);

    /**
     * Функция для выгрузки "Детализированный отчет по потерянным картам"
     * @param options
     * @return
     */
    CardsCustomersPercentDetailsItem[] getKpi2LostCardsDetailsDataItems(CardsCustomersPercentOptions options);
}
