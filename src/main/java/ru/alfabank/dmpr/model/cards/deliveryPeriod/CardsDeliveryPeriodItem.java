package ru.alfabank.dmpr.model.cards.deliveryPeriod;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

import java.util.Date;

/**
 * Класс, который содержит все необходимые данные для графиков.
 */
public class CardsDeliveryPeriodItem extends BaseItem {
    public LocalDate calcDate;
    public Long macroRegionId;
    public Long regionId;
    public Long cityId;
    public Long salePlaceId;
    public String unitCode;
    public String unitName;
    public Long dayGroupId;
    public String dayGroupName;
    public int cardCount;
    public int inKPICardCount;
    public double totalDuration;
    public Long reissueSpeedId;
    public double reissueSpeedDays;
    public double durationNormative;
    public double percentNormative;

    @Override
    public String toString() {
        return "Item{" +
                "calcDate=" + calcDate +
                ", macroRegionId=" + macroRegionId +
                ", regionId=" + regionId +
                ", cityId=" + cityId +
                ", salePlaceId=" + salePlaceId +
                ", unitCode='" + unitCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", dayGroupId=" + dayGroupId +
                ", dayGroupName='" + dayGroupName + '\'' +
                ", cardCount=" + cardCount +
                ", inKPICardCount=" + inKPICardCount +
                ", totalDuration=" + totalDuration +
                ", reissueSpeedId=" + reissueSpeedId +
                ", reissueSpeedDays=" + reissueSpeedDays +
                ", durationNormative=" + durationNormative +
                ", percentNormative=" + percentNormative +
                "} " + super.toString();
    }
}
