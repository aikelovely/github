package ru.alfabank.dmpr.model.cards.customersPercent;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Класс, который содержит все необходимые данные для детализированного отчета.
 */
public class CardsCustomersPercentDetailsItem extends BaseItem {
    public String accountOwnerCode;
    public String cardHolderCode;
    public String cardNo;
    public String reportINN;
    public String cardType;
    public String cardKindName;
    public String cardCategory;
    public LocalDate validFrom;
    public LocalDate validThru;
    public Integer cardActivityDuration;
    public LocalDate arrivalDate;
    public String actionWithCard;
    public LocalDate acceptDoDate;
    public String debetOrCredit;
    public String sRVPack;
    public LocalDate orderDate;
    public LocalDate cardReadyDate;
    public String cityName;
    public String branchId;
    public String branchName;
    public String deliverCityName;
    public String deliverBranchId;
    public String deliverBranchName;
    public String requestCreator;
    public String appUserName;
    public String cardContractCode;
    public String cardContractName;

    @Override
    public String toString() {
        return "CardsCustomersPercentDetailsItem{" +
                "accountOwnerCode='" + accountOwnerCode + '\'' +
                ", cardHolderCode='" + cardHolderCode + '\'' +
                ", cardNo='" + cardNo + '\'' +
                ", reportINN='" + reportINN + '\'' +
                ", cardType='" + cardType + '\'' +
                ", cardKindName='" + cardKindName + '\'' +
                ", cardCategory='" + cardCategory + '\'' +
                ", validFrom=" + validFrom +
                ", validThru=" + validThru +
                ", cardActivityDuration=" + cardActivityDuration +
                ", arrivalDate=" + arrivalDate +
                ", actionWithCard='" + actionWithCard + '\'' +
                ", acceptDoDate=" + acceptDoDate +
                ", debetOrCredit='" + debetOrCredit + '\'' +
                ", sRVPack='" + sRVPack + '\'' +
                ", orderDate=" + orderDate +
                ", cardReadyDate=" + cardReadyDate +
                ", cityName='" + cityName + '\'' +
                ", branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", deliverCityName='" + deliverCityName + '\'' +
                ", deliverBranchId='" + deliverBranchId + '\'' +
                ", deliverBranchName='" + deliverBranchName + '\'' +
                ", requestCreator='" + requestCreator + '\'' +
                ", appUserName='" + appUserName + '\'' +
                ", cardContractCode='" + cardContractCode + '\'' +
                ", cardContractName='" + cardContractName + '\'' +
                '}';
    }
}
