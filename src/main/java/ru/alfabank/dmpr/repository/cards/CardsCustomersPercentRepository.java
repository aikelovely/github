package ru.alfabank.dmpr.repository.cards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.cards.CardsCustomersPercentMapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentDetailsItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;

/**
 * Репозиторий для графиков витрины "Доля клиентов, получивших карту в указанном отделении"
 */
@Repository
public class CardsCustomersPercentRepository {
    @Autowired
    private CardsCustomersPercentMapper mapper;

    /**
     * Основная функция для полученния данных
     * @param options
     * @return
     */
    public CardsCustomersPercentItem[] getKpi2DataItems(CardsCustomersPercentOptions options) {
        return mapper.getKpi2DataItems(options);
    }

    /**
     * Функция для выгрузки "Детализированный отчет по не забранным картам"
     * @param options
     * @return
     */
    public CardsCustomersPercentDetailsItem[] getKpi2UnTakenDetailsDataItems(CardsCustomersPercentOptions options) {
        return mapper.getKpi2UnTakenDetailsDataItems(options);
    }

    /**
     * Функция для выгрузки "Детализированный отчет по потерянным картам"
     * @param options
     * @return
     */
    public CardsCustomersPercentDetailsItem[] getKpi2LostCardsDetailsDataItems(CardsCustomersPercentOptions options) {
        return mapper.getKpi2LostCardsDetailsDataItems(options);
    }
}
