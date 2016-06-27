package ru.alfabank.dmpr.model;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class PeriodEntity extends BaseEntity {
    public LocalDateTime start;
    public LocalDateTime end;
    public PeriodEntity() {}
    public PeriodEntity(long id, String name, LocalDateTime start, LocalDateTime end){
        super(id, name);
        this.start = start;
        this.end = end;
    }
}

