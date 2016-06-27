package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Workbook;

public interface Worksheet {
    void processWorksheet(Workbook wb);
}
