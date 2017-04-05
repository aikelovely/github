package ru.alfabank.dmpr.model.unitCost;

import org.joda.time.LocalDate;

/**
 * Набор необходимых данных для отображения графиков витрины "UnitCost".
 */
public class UnitCostDataItem {
    public LocalDate calcDate;
    public long groupId;
    public String groupCode;
    public String groupName;
    public long innerEndProductId;
    public String innerEndProductCode;
    public String innerEndProductName;
    public Double totalRurSum;
    public Double totalUsdSum;
    public Double indexChangeFactor;
    public Double unitCostRurSum;
    public Double unitCostUsdSum;
    public Double unitCostRurSumCumulative;
    public Double unitCostUsdSumCumulative;
    public Double totalLaborValueCnt;
    public Double quantityCnt;
    public String topbyField;
    public int orderColumnFot;
}
