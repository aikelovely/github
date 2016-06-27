package ru.alfabank.dmpr.model.cards.deliveryPeriod;

import ru.alfabank.dmpr.model.cards.CardsCommonOptions;

/**
 * Значения фильтров витрины "Срок доставки пластиковых карт в отделения"
 */
public class CardsDeliveryPeriodOptions extends CardsCommonOptions {
    /**
     * Значение фильтра "Признак МВК"
     */
    public Long mvkSignId;

    /**
     * Вспомогательный параметр, используется для построения динамики.
     */
    public Integer dayGrouped;

    /**
     * Значение фильтра N дней для Детализированный отчета по картам
     */
    public Integer daysLowerBound;

    /**
     * Значение фильтра "С автоматическим продлением"
     */
    public boolean automaticExtensionMode;
}
