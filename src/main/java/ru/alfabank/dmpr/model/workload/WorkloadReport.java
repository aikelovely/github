package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Created by U_M0NCT on 27.07.2016.
 */
public class WorkloadReport {
    public LocalDate ValueDay;
    public String BpdivisiongroupNameLvl1;
    public String BpdivisiongroupNameLvl2;
    public String BpdivisiongroupNameLvl3;
    public String BpDivisionGroupCcode;
    public String BporgRegionCcodeLvl2;
    public String BporgRegionCcodeLvl3;
    public double TotalfactvalueCnt;
    public double SlafactvalueCnt;
    public double SlafactvaluedoubleCnt;
    public double SlafactvacancytempCnt;
    public double NoslafactvalueCnt;
    public double NoslafactvaluedoubleCnt;
    public double NoslafactvacancyCnt;
    public double NoslafactvacancytempCnt;
    public double EstimatedstaffunitW8Cnt;
    public double Quartile1W8Cnt;
    public double Quartile3W8Cnt;
    public double LowlimitW8Cnt;
    public double HighlimitW8Cnt;
    public double CriteriaW8Cnt;
    public double Quartile1W4Cnt;
    public double Quartile3W4Cnt;
    public double LowlimitW4Cnt;
    public double HighlimitW4Cnt;
    public double CriteriaW4Cnt;
    public double TotalcriteriaCnt;
    public double WorkloadFactor;
    public LocalDateTime DateNow= LocalDateTime.now();

}
