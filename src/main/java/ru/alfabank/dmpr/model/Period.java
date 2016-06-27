package ru.alfabank.dmpr.model;

public enum Period {
    none(0), hour(1), day(2), week(3), month(4), quarter(5), year(6), all(7);

    private final int value;

    Period(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
