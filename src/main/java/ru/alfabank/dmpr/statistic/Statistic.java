package ru.alfabank.dmpr.statistic;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class Statistic {
    private final String user;
    private final String page;
    private final LocalDateTime localDateTime;


    public Statistic(String user, String page, LocalDateTime localDateTime) {
        this.user = user;
        this.page = page;
        this.localDateTime = localDateTime;
    }

    public String getUser() {
        return user;
    }

    public String getPage() {
        return page;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }
}
