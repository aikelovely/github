package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Набор данных для выгрузки отчета в Excel.
 */
public class ZPReportDetailsItem extends BaseItem {
    public String companyName;
    public String INN;
    public String cityName;
    public String managerName;
    public String bushName;
    public String operationOfficeName;

    // Поля ниже имеют значения только при "p_DimensionID => 3"
    public LocalDateTime s1StartDate;
    public Double s1Duration;

    public LocalDateTime s2StartDate;
    public Double s2Duration;

    public LocalDateTime s3StartDate;
    public String s3StartDateText;
    public Double s3Duration;

    public Double totalDuration;

    // Используются для доотрытия
    public LocalDateTime roStartDate;
    public LocalDateTime roFinishDate;

    public boolean isInKpi;
    public String isInKpiTitle;
}
