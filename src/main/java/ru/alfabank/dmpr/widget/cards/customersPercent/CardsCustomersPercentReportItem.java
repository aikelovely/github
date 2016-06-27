package ru.alfabank.dmpr.widget.cards.customersPercent;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;

import java.util.Date;

/**
 * Строка для детализированного отчета
 */
public class CardsCustomersPercentReportItem {

    public String unitName;
    public String macroRegionName;
    public LocalDate calcDate;
    public Integer unTakenCardCount;
    public Integer unTakenCardCountDIV;
    public Double unTakenCardPercent;
    public Integer RCDOCardCount;
    public Integer RCDOCardCountDIV;
    public Double RCDOCardPercent;
    public Double unTakenCardKPI;
    public Double unTakenCardDiviation;
    public Double RCDOCardKPI;
    public Integer destructedCardCount;

    public CardsCustomersPercentReportItem(
            CardsCustomersPercentItem item,
            String macroRegionName,
            Double unTakenCardPercent,
            Double RCDOCardPercent) {
        this.unitName = item.unitName;
        this.macroRegionName = macroRegionName;
        this.calcDate = item.calcDate;
        this.unTakenCardCount = item.unTakenCardCount;
        this.unTakenCardCountDIV = item.unTakenCardCountDIV;
        this.unTakenCardPercent = unTakenCardPercent;
        this.RCDOCardCount = item.RCDOCardCount;
        this.RCDOCardCountDIV = item.RCDOCardCountDIV;
        this.RCDOCardPercent = RCDOCardPercent;
        this.unTakenCardKPI = item.unTakenCardKPI;
        this.unTakenCardDiviation = Math.abs(unTakenCardPercent - item.unTakenCardKPI);
        this.RCDOCardKPI = item.RCDOCardKPI;
        this.destructedCardCount = item.destructedCardCount;
    }
}
