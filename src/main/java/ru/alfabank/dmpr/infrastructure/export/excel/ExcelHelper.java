package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import ru.alfabank.dmpr.infrastructure.chart.Color;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelHelper {
    public static void resizeColumns(Sheet sheet, ArrayList<Column> columns){
        for (int i = 0; i < columns.size(); i++) {
            ColumnBase column = columns.get(i);

            if (column.getWidth() == null) {
                sheet.autoSizeColumn(i);
            } else {
                sheet.setColumnWidth(i, column.getWidth() * 256);
            }
        }
    }

    public static void createStandardHeaderRow(Sheet sheet, ArrayList<Column> columns, AtomicInteger rowIndex){
        Workbook book = sheet.getWorkbook();
        XSSFCellStyle style = (XSSFCellStyle)book.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);

        XSSFFont font = (XSSFFont)book.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new XSSFColor(Color.WhiteColor.bytes));
        style.setFont(font);

        style.setFillForegroundColor(new XSSFColor(Color.LightBlueColor.bytes));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        Row row = sheet.createRow(rowIndex.get());

        for (int i = 0; i < columns.size(); i++) {
            ColumnBase column = columns.get(i);

            Cell cell = row.createCell(i);
            cell.setCellValue(column.getTitle());
            cell.setCellStyle(style);
        }

        rowIndex.incrementAndGet();
    }

    public static void fillRow(Sheet sheet, ArrayList<Column> columns, AtomicInteger rowIndex, Object item){
        Workbook book = sheet.getWorkbook();
        CreationHelper createHelper = book.getCreationHelper();
        Row row = sheet.createRow(rowIndex.get());

        for (int i = 0; i < columns.size(); i++) {
            ColumnBase column = columns.get(i);

            Cell cell = row.createCell(i);

            column.setValue(cell, item);

            CellStyle style = book.createCellStyle();

            if (column.getFormat() != null) {
                style.setDataFormat(createHelper.createDataFormat().getFormat(column.getFormat()));
            }

            cell.setCellStyle(style);
        }
        rowIndex.incrementAndGet();
    }
}
