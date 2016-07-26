package ru.alfabank.dmpr.model;

import org.joda.time.LocalDate;

import java.io.Serializable;

public class Week implements Serializable{
    private static final long serialVersionUID = -6703578104401918012L;
    public long id;
    public int year;
    public int weekNum;
    public LocalDate startDate;
    public LocalDate endDate;
}
