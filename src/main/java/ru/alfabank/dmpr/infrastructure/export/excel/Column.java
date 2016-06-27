package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.lang.reflect.Field;

public class Column<T> extends ColumnBase<T> {
    private final Field dataField;
    private final CellBuilder cellBuilder;

    public Column(Class dataType, String name) {
        try {
            dataField = dataType.getField(name);
            Class cellType = dataField.getType();
            cellBuilder = createCellBuilder(cellType);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setValue(Cell cell, T row) {
        try {
            cellBuilder.setValue(cell, row);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getValue(T row) {
        try {
            return dataField.get(row);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private CellBuilder createCellBuilder(Class type) {
        // оптимистическая проверка, т.к. большинство данных типа String
        if (type == String.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    String currentValue = (String) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        } else if (type == int.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    cell.setCellValue(dataField.getInt(value));
                }
            };
        } else if (type == Integer.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    Integer currentValue = (Integer) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        } else if (type == long.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    cell.setCellValue(dataField.getLong(value));
                }
            };
        } else if (type == Long.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    Long currentValue = (Long) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        } else if (type == Integer.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    Long currentValue = (Long) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        } else if (type == double.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    cell.setCellValue(dataField.getDouble(value));
                }
            };
        } else if (type == Double.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    Double currentValue = (Double) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        } else if (type == LocalDate.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    LocalDate currentValue = (LocalDate) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue.toDate());
                }
            };
        } else if (type == LocalDateTime.class) {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    LocalDateTime currentValue = (LocalDateTime) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue.toDate());
                }
            };
        } else {
            return new CellBuilder() {
                @Override
                public void setValue(Cell cell, Object value) throws IllegalAccessException {
                    String currentValue = (String) dataField.get(value);
                    if (currentValue != null) cell.setCellValue(currentValue);
                }
            };
        }
    }
}
