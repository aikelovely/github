package ru.alfabank.dmpr.model.unitCost;

import org.joda.time.LocalDate;

/**
 * Набор данных для формирования выгрузки в Excel.
 */
public class UnitCostReportDataItem {
    public String directionName;
    public String calcType;
    public LocalDate calcDate;
    public String innerEndProductCode;
    public String innerEndProductName;
    public String regionCode;
    public String profitCenterCode;
    public Double totalLaborCnt;
    public Double totalBpiepQuantityCnt;
    public Double unitCostRurSum;
    public Double unitCostUsdSum;
    public Double totalRurSum;
    public Double totalUsdSum;
}
