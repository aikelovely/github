package ru.alfabank.dmpr.infrastructure.export.csv.fluent;


import ru.alfabank.dmpr.infrastructure.export.csv.Column;

public class ColumnBuilder {
    private final Column column;

    public ColumnBuilder(Column column){
        this.column = column;
    }

    public ColumnBuilder title(String title){
        column.setTitle(title);
        return this;
    }

    public ColumnBuilder format(String format){
        column.setFormat(format);
        return this;
    }
}

