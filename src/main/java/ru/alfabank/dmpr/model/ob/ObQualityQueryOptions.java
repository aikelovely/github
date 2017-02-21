package ru.alfabank.dmpr.model.ob;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;

/**
 * Набор параметров, который используется для запросов к бд.
 * Формируется на основании ObQualityOptions и ObQualityAdditionalOptions.
 */
public class ObQualityQueryOptions {

    public long kpiKindId;
    public String kpiId;
    public String[] directionIds;
    public String[] regionIds;
    public LocalDate startDate;
    public LocalDate endDate;
    public LocalDate startDateFirst;
    public LocalDate endDateSecond;
    public int timeUnitId;
    public String systemUnitCode;
    public String doudrFlag;
    public Integer detailsMode;

    public ObQualityQueryOptions(ObQualityOptions options, ObQualityAdditionalOptions addOptions,
                                 ObQualityFilterRepository rep, ObQualityQueryOptionsGenerationType genType, Week[] weeks){

        kpiKindId = options.kpiKindId;
        kpiId = options.kpiId;
        directionIds = options.directionIds;
        regionIds = options.regionIds;
        timeUnitId = options.timeUnitId;
        doudrFlag = options.doudrFlag;

        if (addOptions != null) {
            systemUnitCode = addOptions.systemUnitCode;
            doudrFlag = addOptions.doudrFlag;
            detailsMode = addOptions.detailsMode;
        }

        ObQualityOptions newOptions = options.copy();

        LocalDate[] periods;

        switch (genType) {
            case FromEndDatePeriod:
                periods = PeriodSelectHelper.getDatesByBasePeriodOptions(newOptions, weeks);
                startDateFirst = periods[0];
                endDateSecond = periods[1];
                newOptions.startDateId = newOptions.endDateId;
                newOptions.startYear = newOptions.endYear;
                periods = PeriodSelectHelper.getDatesByBasePeriodOptions(newOptions, weeks);
                startDate = periods[0];
                endDate = periods[1];
                break;
            case FromPeriod:
                periods = PeriodSelectHelper.getDatesByBasePeriodOptions(newOptions, weeks);
                startDate = periods[0];
                endDate = periods[1];
                break;
            case FromYear:
                startDate = options.endYear;
                switch (timeUnitId) {
                    case 3:
                        endDate = options.endYear.plusDays(6);
                        break;
                    case 4:
                        endDate = options.endYear.plusMonths(1).minusDays(1);
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}