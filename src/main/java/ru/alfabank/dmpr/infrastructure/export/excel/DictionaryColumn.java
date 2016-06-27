package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.LocalDate;

import java.util.Map;

public class DictionaryColumn<T extends Map<String, Object>> extends ColumnBase<T> {
    private final String key;
    private final CellBuilder cellBuilder;

    public DictionaryColumn(String key, Class<?> columnType) {
        this.key = key;
        cellBuilder = createCellBuilder(columnType);
    }

    @Override
    public void setValue(Cell cell, T row) {
        try {
            cellBuilder.setValue(cell, row.get(key));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getValue(T row) {
        return row.get(key);
    }

    private CellBuilder createCellBuilder(Class type) {
        // оптимистическая проверка, т.к. большинство данных типа String
        if (type == String.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    String strValue = (String) value;
                    if (strValue != null) cell.setCellValue(strValue);
                }
            };
        } else if (type == int.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    cell.setCellValue((int) value);
                }
            };
        } else if (type == Integer.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    Integer intValue = (Integer) value;
                    if (intValue != null) cell.setCellValue(intValue);
                }
            };
        } else if (type == long.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    cell.setCellValue((long) value);
                }
            };
        } else if (type == Long.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    Long longValue = (Long) value;
                    if (longValue != null) cell.setCellValue(longValue);
                }
            };
        } else if (type == double.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    cell.setCellValue((double) value);
                }
            };
        } else if (type == Double.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    Double doubleValue = (Double) value;
                    if (doubleValue != null) cell.setCellValue(doubleValue);
                }
            };
        } else if (type == LocalDate.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    LocalDate dateValue = (LocalDate) value;
                    if (dateValue != null) cell.setCellValue(dateValue.toDate());
                }
            };
        } else {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) {
                    String strValue = (String) value;
                    if (strValue != null) cell.setCellValue(strValue);
                }
            };
        }
    }
}
