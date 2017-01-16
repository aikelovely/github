package ru.alfabank.dmpr.model.ob;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.format.datetime.joda.LocalDateTimeParser;

/**
 * Created by U_M0NCT on 14.12.2016.
 */
public class ObDss {
    public String division_name;
    public String loanappl_ref;
    public String user_name;
    public String slaoperationsrc_name;
    public String slaoperationsrc_no;
    public LocalDateTime operation_start_time;
    public LocalDateTime operation_end_time;
    public String di;
    public String time2;
}
