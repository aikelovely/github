package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.export.excel.Column;

import java.util.List;

public class ColumnFactory<T> {
    private final List<Column> columns;
    private final Class<?> dataType;

    public ColumnFactory(List<Column> columns, Class<T> dataType) {
        this.columns = columns;
        this.dataType = dataType;
    }

    public ColumnBuilder add(String name){
        Column column = new Column(dataType, name);
        ColumnBuilder builder = new ColumnBuilder(column);
        columns.add(column);

        return builder;
    }

    public ColumnBuilder add(Column column){
        ColumnBuilder builder = new ColumnBuilder(column);
        columns.add(column);

        return builder;
    }
}
