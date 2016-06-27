package ru.alfabank.dmpr.infrastructure.export.csv;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;

import java.lang.reflect.Field;


public class Column<T> {
    private final Field dataField;
    private final CellBuilder cellBuilder;
    private String title;
    private String format;

    public Column(Class dataType, String name) {
        try {
            dataField = dataType.getField(name);
            Class cellType = dataField.getType();
            cellBuilder = createCellBuilder(cellType);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue(T row, String format){
        try {
            return cellBuilder.getValue(row, format);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private CellBuilder createCellBuilder(Class type) {
        // оптимистическая проверка, т.к. большинство данных типа String
        if (type == String.class) {
            return new CellBuilder() {
                @Override
                public String getValue(Object value, String format) throws IllegalAccessException {
                    String currentValue = (String) dataField.get(value);
                    if(currentValue == null) return "";
                    return String.valueOf(currentValue);
                }
            };
        } else if (type == int.class || type == Integer.class ||
                type == long.class || type == Long.class ||
                type == double.class || type == Double.class) {
            return new CellBuilder() {
                @Override
                public String getValue(Object value, String format) throws IllegalAccessException {
                    return String.valueOf(dataField.get(value));
                }
            };
        } else if (type == LocalDate.class) {
            return new CellBuilder() {
                @Override
                public String getValue(Object value, String format) throws IllegalAccessException {
                    LocalDate currentValue = (LocalDate) dataField.get(value);
                    if(currentValue == null) return "";
                    if(format == null){
                        format =  "yyyy-MM-dd HH:mm:ss";
                    }
                    return DateHelper.format(currentValue, format);
                }
            };
        } else if (type == LocalDateTime.class) {
            return new CellBuilder() {
                @Override
                public String getValue(Object value, String format) throws IllegalAccessException {
                    LocalDateTime currentValue = (LocalDateTime) dataField.get(value);
                    if(currentValue == null) return "";
                    if(format == null){
                        format =  "yyyy-MM-dd HH:mm:ss";
                    }
                    return DateHelper.format(currentValue, format);
                }
            };
        }else {
            return new CellBuilder() {
                @Override
                public String getValue(Object value, String format) throws IllegalAccessException {
                    String currentValue = (String) dataField.get(value);
                    if(currentValue == null) return "";
                    return String.valueOf(currentValue);
                }
            };
        }
    }
}
