package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Created by U_M0NCT on 27.07.2016.
 */
public class ConvertCountReport {
    public LocalDate ValueDay;
    public String BpDivisionGroupCcode;
    public String BpinnerendProductName;
    public String BpinnerendProductCcode;
    public String PortfolioFlag;
    public String BporgRegionCcode;
    public double NormativeMinutes;
    public double TotalbpiepquantityCnt;
    public double TotallaborvalueCnt;
    public double Worktime4staffunitCnt;
    public double EstimatedstaffunitCnt;
    public double NoslafactvalueCnt;
    public LocalDateTime DateNow=LocalDateTime.now();
}
