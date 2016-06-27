package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.export.excel.TableWorksheet;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;

import java.util.List;

public class TableWorksheetBuilder<T> {
    private final TableWorksheet<T> worksheet;
    private final Class<T> dataType;

    public TableWorksheetBuilder(TableWorksheet<T> worksheet, Class<T> dataType) {
        this.worksheet = worksheet;
        this.dataType = dataType;
    }

    public TableWorksheetBuilder<T> columns(ColumnFactoryWrapper factory){
        @SuppressWarnings("unchecked")
        ColumnFactory builder = new ColumnFactory(worksheet.getColumns(), dataType);
        factory.createColumns(builder);
        return this;
    }

    public TableWorksheetBuilder<T> title(String title){
        worksheet.setTitle(title);
        return this;
    }

    public TableWorksheetBuilder<T> bindTo(List<T> data){
        worksheet.setData(data);
        return this;
    }

    public TableWorksheetBuilder<T> bindTo(T[] data){
        worksheet.setData(LinqWrapper.from(data).toList());
        return this;
    }
}
