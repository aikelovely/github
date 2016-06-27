package ru.alfabank.dmpr.model.cards.customersPercent;

import ru.alfabank.dmpr.model.cards.CardsCommonOptions;


public class CardsCustomersPercentOptions extends CardsCommonOptions {
    /**
     * Id отделений
     */
    public String[] branchIds;

    /**
     * Значение фильтра "Признак перевыпуска"
     */
    public Integer reissueId;

    /**
     * Значение фильтра "Отображение параметра"
     */
    public CardsCustomersPercentParamType paramType;
}
