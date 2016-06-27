package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.export.excel.ColumnBase;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

public class ColumnBuilder {
    private final ColumnBase column;

    public ColumnBuilder(ColumnBase column){
        this.column = column;
    }

    public ColumnBuilder title(String title){
        column.setTitle(title);
        return this;
    }

    public ColumnBuilder width(Integer width){
        column.setWidth(width);
        return this;
    }

    public ColumnBuilder format(String format){
        column.setFormat(format);
        return this;
    }

    public ColumnBuilder dynamicBackground(Selector<Object, Color> dynamicBackground){
        column.setDynamicBackground(dynamicBackground);
        return this;
    }

    public ColumnBuilder dynamicBackgroundByRow(Selector<Object, Color> dynamicBackground){
        column.setDynamicBackgroundByRow(dynamicBackground);
        return this;
    }

    public ColumnBuilder dynamicFormat(Selector<Object, String> dynamicFormat){
        column.setDynamicFormat(dynamicFormat);
        return this;
    }
}

