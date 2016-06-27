package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.export.excel.ColumnBase;
import ru.alfabank.dmpr.infrastructure.export.excel.DictionaryColumn;

import java.util.List;
import java.util.Map;

public class ColumnDictionaryFactory<T extends Map<String, Object>> {
    private final List<ColumnBase<T>> columns;

    public ColumnDictionaryFactory(List<ColumnBase<T>> columns) {
        this.columns = columns;
    }

    public ColumnBuilder add(String name, Class type){
        DictionaryColumn<T> column = new DictionaryColumn<T>(name, type);
        ColumnBuilder builder = new ColumnBuilder(column);
        columns.add(column);

        return builder;
    }
}
