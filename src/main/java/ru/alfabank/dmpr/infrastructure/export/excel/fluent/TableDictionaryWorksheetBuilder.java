package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.export.excel.TableDictionaryWorksheet;

import java.util.List;
import java.util.Map;

public class TableDictionaryWorksheetBuilder<T extends Map<String, Object>> {
    private final TableDictionaryWorksheet<T> worksheet;

    public TableDictionaryWorksheetBuilder(TableDictionaryWorksheet<T> worksheet) {
        this.worksheet = worksheet;
    }

    public TableDictionaryWorksheetBuilder<T> columns(ColumnDictionaryFactoryWrapper factory){
        ColumnDictionaryFactory<T> builder = new ColumnDictionaryFactory<T>(worksheet.getColumns());
        factory.createColumns(builder);
        return this;
    }

    public TableDictionaryWorksheetBuilder<T> title(String title){
        worksheet.setTitle(title);
        return this;
    }

    public TableDictionaryWorksheetBuilder<T> bindTo(List<? extends Map<String, Object>> data){
        worksheet.setData(data);
        return this;
    }
}
