package ru.alfabank.dmpr.infrastructure.export.excel;


import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class NoDataWorksheet implements Worksheet {
    protected String title;

    public NoDataWorksheet(){
        title = "Данные";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void processWorksheet(Workbook wb) {
        Sheet wbSheet = wb.createSheet(this.title);

        Row row = wbSheet.createRow(0);
        row.createCell(0).setCellValue("Нет данных");
        wbSheet.autoSizeColumn(0);
    }
}
