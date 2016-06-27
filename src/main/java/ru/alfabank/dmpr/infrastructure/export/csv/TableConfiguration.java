package ru.alfabank.dmpr.infrastructure.export.csv;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class TableConfiguration<T> {
    private static final char ColumnSeparator = ';';
    private static final String LineSeparator = "\r\n";

    private List<T> data;
    private List<Column<T>> columns;

    public List<Column<T>> getColumns() {
        return columns;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public TableConfiguration() {
        this.columns = new ArrayList<>();
    }

    public void exportData(Writer writer) throws IOException {
        if (data == null || data.size() == 0) {
            writer.append("Нет данных");
            return;
        }

        // добавляем заголовок
        for (int i = 0; i < columns.size(); i++) {
            String title = columns.get(i).getTitle();

            writer.append(title);

            if (i != columns.size() - 1) {
                writer.append(ColumnSeparator);
            } else {
                writer.append(LineSeparator);
            }
        }

        // добавляем данные
        for (T dataRow : data) {
            for (int i = 0; i < columns.size(); i++) {
                Column column = columns.get(i);

                String value = column.getValue(dataRow, column.getFormat());

                writer.append("\"" + value + "\"");

                if (i != columns.size() - 1) {
                    writer.append(ColumnSeparator);
                } else {
                    writer.append(LineSeparator);
                }
            }
        }
    }
}
