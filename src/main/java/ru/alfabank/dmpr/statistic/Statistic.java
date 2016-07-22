package ru.alfabank.dmpr.statistic;


import org.joda.time.DateTime;

public class Statistic {
    private final String user;
    private final String page;
    private final DateTime localDateTime;


    public Statistic(String user, String page, DateTime localDateTime) {
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

    public DateTime getLocalDateTime() {
        return localDateTime;
    }
}
