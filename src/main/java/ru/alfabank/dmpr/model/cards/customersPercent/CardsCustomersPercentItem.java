package ru.alfabank.dmpr.model.cards.customersPercent;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Класс, который содержит все необходимые данные для графиков.
 */
public class CardsCustomersPercentItem extends BaseItem {
    public LocalDate calcDate;
    public String unitCode;
    public String unitName;
    public Long macroRegionId;
    public Long regionId;
    public Long cityId;
    public String branchId;
    public int takenCardCount;
    public int unTakenCardCount;
    public int destructedCardCount;
    public int lostCardCount;
    public int startCardCount;
    public int finishCardCount;
    public int unTakenCardCountDIV;
    public Double unTakenCardKPI;
    public int RCDOCardCount;
    public int RCDOCardCountDIV;
    public Double RCDOCardKPI;

    @Override
    public String toString() {
        return "CardsCustomersPercentItem{" +
                "calcDate=" + calcDate +
                ", unitCode='" + unitCode + '\'' +
                ", unitName='" + unitName + '\'' +
                ", macroRegionId=" + macroRegionId +
                ", regionId=" + regionId +
                ", cityId=" + cityId +
                ", branchId='" + branchId + '\'' +
                ", takenCardCount=" + takenCardCount +
                ", unTakenCardCount=" + unTakenCardCount +
                ", destructedCardCount=" + destructedCardCount +
                ", lostCardCount=" + lostCardCount +
                ", startCardCount=" + startCardCount +
                ", finishCardCount=" + finishCardCount +
                ", unTakenCardCountDIV=" + unTakenCardCountDIV +
                ", unTakenCardKPI=" + unTakenCardKPI +
                ", RCDOCardCount=" + RCDOCardCount +
                ", RCDOCardCountDIV=" + RCDOCardCountDIV +
                ", RCDOCardKPI=" + RCDOCardKPI +
                '}';
    }
}
