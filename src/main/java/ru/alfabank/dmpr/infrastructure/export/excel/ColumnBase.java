package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

public abstract class ColumnBase<T> {
    private String title;
    private String format;
    private Integer width;
    private Selector<Object, Color> dynamicBackground;
    private Selector<T, Color> dynamicBackgroundByRow;
    private Selector<T, String> dynamicFormat;

    public abstract void setValue(Cell cell, T row);

    public abstract Object getValue(T row);

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Selector<Object, Color> getDynamicBackground() {
        return dynamicBackground;
    }

    public void setDynamicBackground(Selector<Object, Color> dynamicBackground) {
        this.dynamicBackground = dynamicBackground;
    }

    public Selector<T, Color> getDynamicBackgroundByRow() {
        return dynamicBackgroundByRow;
    }

    public void setDynamicBackgroundByRow(Selector<T, Color> _dynamicBackgroundByRow) {
        this.dynamicBackgroundByRow = _dynamicBackgroundByRow;
    }

    public Selector<T, String> getDynamicFormat() {
        return dynamicFormat;
    }

    public void setDynamicFormat(Selector<T, String> _dynamicFormat) {
        this.dynamicFormat = _dynamicFormat;
    }
}

