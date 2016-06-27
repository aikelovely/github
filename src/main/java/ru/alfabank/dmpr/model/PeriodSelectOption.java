package ru.alfabank.dmpr.model;

import org.joda.time.LocalDate;

public class PeriodSelectOption extends BaseEntity {
    public LocalDate startDate;
    public LocalDate endDate;
    public int periodNum;

    public PeriodSelectOption() {
    }

    public PeriodSelectOption(long id, int periodNum, String name, LocalDate startDate, LocalDate endDate){
        super(id, name);
        this.periodNum = periodNum;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
