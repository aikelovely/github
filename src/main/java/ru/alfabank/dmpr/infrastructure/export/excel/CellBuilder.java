package ru.alfabank.dmpr.infrastructure.export.excel;


import org.apache.poi.ss.usermodel.Cell;

public interface CellBuilder {
    public void setValue(Cell cell, Object value) throws IllegalAccessException;
}
