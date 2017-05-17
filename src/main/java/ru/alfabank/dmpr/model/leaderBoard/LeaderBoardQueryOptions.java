package ru.alfabank.dmpr.model.leaderBoard;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.BasePeriodOptions;
import ru.alfabank.dmpr.model.Week;
//import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;

//import ru.alfabank.dmpr.model.ob.ObQualityQueryOptionsGenerationType;


/**
 * Набор параметров, который используется для запросов к бд.
 */
public class LeaderBoardQueryOptions {

    public long kpiKindId;
    public long kpiIds;
    public LocalDate startDate;
    public LocalDate endDate;
    public int timeUnitId;
    public String kpiId;
    public long startDateId;
    public long endDateId;
    public LeaderBoardQueryOptions(LeaderBoardOptions options, Week[] weeks) {

//        kpiIds[] = options.kpiIds;
        kpiId= options.kpiId;
        timeUnitId = options.timeUnitId;
        startDateId=options.startDateId;
        endDateId=options.endDateId;



        BasePeriodOptions newOptions = options.copy();

        LocalDate[] periods;
       // newOptions.

               // newOptions.startDateId = newOptions.endDateId;
             //   newOptions.startYear = newOptions.startDate;
            //    newOptions.startDateId = newOptions.endDateId;
                periods = PeriodSelectHelper.getDatesByBasePeriodOptions2(newOptions, weeks);
                startDate = periods[0];
                endDate = periods[1];


        }
    }
