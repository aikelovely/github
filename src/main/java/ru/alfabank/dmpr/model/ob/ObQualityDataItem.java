package ru.alfabank.dmpr.model.ob;

import org.joda.time.LocalDate;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Содержит все необходимые данные для отображения графиков витрины "Показатели качества ОБ"
 */
public class ObQualityDataItem {
    public String kpiKindName;
    public String unitCode;
    public String unitName;
    public Long bpDivisionGroupId;
    public String bpDivisionGroupName;
    public Long bpOrgRegionId;
    public String bpOrgRegionName;
    public Long kpiId;
    public String kpiName;
    public LocalDate timeUnitDD;
    public Integer timeUnitYear;
    public Long timeUnitPrdNum;
    public Long sortOrder;
    public Double kpiNorm;
    public Long inKpiCount;
    public Long totalCount;
    public Double kpiRatioAvg;
    public Double kpi2RatioAvg;
    public Double prevKpiRatioAvg;
    public Double prevKpi2RatioAvg;
    public String description;

    public Double getQualityLevel() {
        if (kpi2RatioAvg == null) return null;
        return kpi2RatioAvg;
    }

    public Double getPrevQualityLevel() {
        if (prevKpi2RatioAvg == null) return null;
        return prevKpi2RatioAvg;
    }

    public Double getQualityLevelNormative(){
        return 1d;
    }

    public Double getNormative() {
        if (kpiNorm == null) return 1d;
        return kpiNorm;
    }

    public boolean getIsRealNormativeNull(){
        return kpiNorm == null;
    }

    private String FormatCount(Long value){
        NumberFormat n = NumberFormat.getNumberInstance(Locale.FRENCH);
        return n.format(Math.round(value));
    }

    public String getInKpiCountAsString(){
        return FormatCount(inKpiCount);
    }

    public String getTotalCountAsString(){
        return FormatCount(totalCount);
    }
}
