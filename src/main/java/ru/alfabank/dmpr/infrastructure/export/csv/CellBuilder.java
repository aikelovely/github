package ru.alfabank.dmpr.infrastructure.export.csv;

public interface CellBuilder {
    public String getValue(Object value, String format) throws IllegalAccessException;
}
