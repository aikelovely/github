package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Created by U_M0NCT on 27.07.2016.
 */
public class BpworkWeekReport {
    public String Ccode;
    public String Name;
    public int NumberOfWeek;
    public int YearWeek;
    public LocalDate ValueDay;
    public double WorkminCnt;
    public LocalDateTime DateNow=LocalDateTime.now();
}
