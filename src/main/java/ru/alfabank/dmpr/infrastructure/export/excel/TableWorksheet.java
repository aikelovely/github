package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import ru.alfabank.dmpr.infrastructure.chart.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableWorksheet<T> extends NoDataWorksheet {
    private List<T> data;
    private List<ColumnBase<T>> columns;
    private HashMap<String, XSSFCellStyle> styleHashMap = new HashMap<>();

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<ColumnBase<T>> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnBase<T>> columns) {
        this.columns = columns;
    }

    public TableWorksheet() {
        super();
        this.columns = new ArrayList<>();
    }

    @Override
    public void processWorksheet(Workbook wb) {
        if (data == null || data.size() == 0) {
            super.processWorksheet(wb);
            return;
        }

        Sheet wbSheet = wb.createSheet(this.title);
        int rowIndex = 0;

        createHeaders(wb, wbSheet, rowIndex);
        rowIndex++;

        rowIndex = fillData(wb, wbSheet, rowIndex);

        // Настраиваем ширину колонок
        for (int i = 0; i < columns.size(); i++) {
            ColumnBase column = columns.get(i);

            if (column.getWidth() == null) {
                wbSheet.autoSizeColumn(i);
            } else {
                wbSheet.setColumnWidth(i, column.getWidth() * 256);
            }
        }
    }

    private void createHeaders(Workbook wb, Sheet wbSheet, int rowIndex) {
        XSSFCellStyle style = (XSSFCellStyle)wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setWrapText(true);
        style.setFillForegroundColor(new XSSFColor(Color.LightBlueColor.bytes));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);

        XSSFFont font = (XSSFFont)wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new XSSFColor(Color.WhiteColor.bytes));
        style.setFont(font);

        Row row = wbSheet.createRow(rowIndex);

        for (int i = 0; i < columns.size(); i++) {
            ColumnBase column = columns.get(i);

            Cell cell = row.createCell(i);
            cell.setCellValue(column.getTitle());
            cell.setCellStyle(style);
        }
    }

    private int fillData(Workbook wb, Sheet wbSheet, int rowIndex) {
        CreationHelper createHelper = wb.getCreationHelper();

        for (T dataRow : data) {
            Row row = wbSheet.createRow(rowIndex);

            for (int i = 0; i < columns.size(); i++) {
                ColumnBase column = columns.get(i);

                Cell cell = row.createCell(i);
                column.setValue(cell, dataRow);

                Color bgColor = null;
                String dynamicFormat = null;

                if (column.getDynamicBackground() != null) {
                    Object currentValue = column.getValue(dataRow);
                    bgColor = (Color) column.getDynamicBackground().select(currentValue);
                }

                if (column.getDynamicBackgroundByRow() != null) {
                    bgColor = (Color) column.getDynamicBackgroundByRow().select(dataRow);
                }

                if (column.getDynamicFormat() != null){
                    dynamicFormat = (String) column.getDynamicFormat().select(dataRow);
                }

                String styleKey = getStyleKey(column, bgColor, dynamicFormat);
                XSSFCellStyle style = styleHashMap.get(styleKey);
                if (style == null) {
                    style = (XSSFCellStyle)wb.createCellStyle();
                    if (column.getFormat() != null) {
                        style.setDataFormat(createHelper.createDataFormat().getFormat(column.getFormat()));
                    }
                    if (dynamicFormat != null){
                        style.setDataFormat(createHelper.createDataFormat().getFormat(dynamicFormat));
                    }
                    if(bgColor != null){
                        style.setFillForegroundColor(new XSSFColor(bgColor.bytes));
                        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                    }
                    styleHashMap.put(styleKey, style);
                }

                cell.setCellStyle(style);
            }

            rowIndex++;
        }

        return rowIndex;
    }

    private String getStyleKey(ColumnBase column, Color bgColor, String dynamicFormat) {
        String key = "Style";

        if (column.getFormat() != null) {
            key += column.getFormat();
        }

        if (dynamicFormat != null) {
            key += dynamicFormat;
        }

        if (bgColor != null) {
            key += bgColor.toString();
        }

        return key;
    }
}

