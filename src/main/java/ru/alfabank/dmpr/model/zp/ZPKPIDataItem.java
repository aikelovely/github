package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Набор необходимых данных для отображения большей части графиков.
 */
public class ZPKPIDataItem extends BaseItem {
    public String unitCode;
    public String unitName;
    public LocalDate calcDate;
    public Integer sortOrder;
    public Integer inKpiCount;
    public Integer totalCount;
    public Double avgDuration;
    public Double totalDuration;
    public Integer totalCwCount;
    public Double kpiNorm;
    public Double inKpiRatioAvg;
    public Double loadedFlCount;
    public Integer retardedCount;
    public Integer fullOkCount;

    // Поля ниже имеют значения только при "p_DimensionID => 3"
    public LocalDate s1StartDate;
    public Double s1Duration;

    public LocalDate s2StartDate;
    public Double s2Duration;

    public LocalDate s3StartDate;
    public Double s3Duration;

    public String companyINN;
    public String companyCode;
}

