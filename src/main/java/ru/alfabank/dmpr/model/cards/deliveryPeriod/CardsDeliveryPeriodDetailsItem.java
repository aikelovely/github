package ru.alfabank.dmpr.model.cards.deliveryPeriod;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Класс, который содержит все необходимые данные для выгрузок.
 */
public class CardsDeliveryPeriodDetailsItem extends BaseItem {
    public String accountOwnerCode;
    public String cardHolderCode;
    public String cardNo;
    public String reportINN;
    public LocalDate validThru;
    public LocalDate creationDate;
    public LocalDate acceptDate;
    public Double duration;
    public String branchId;
    public String branchName;
    public String cityName;
    public String regionName;
    public Double reissueSpeedDays;

    public String cardContractCode;
    public String cardContractName;
    public String sRVPack;
    public String cardType;
    public String cardKindName;
    public String cardCategory;
    public String debetOrCredit;
}
