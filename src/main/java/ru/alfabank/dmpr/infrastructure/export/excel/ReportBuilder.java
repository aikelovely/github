package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.alfabank.dmpr.infrastructure.export.ReportBuilderResult;
import ru.alfabank.dmpr.infrastructure.export.ReportManager;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.CustomTableWorksheetBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.NoDataWorksheetBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.TableDictionaryWorksheetBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.TableWorksheetBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReportBuilder {
    private String reportName;
    private String reportDirectory;
    private List<Worksheet> worksheets = new ArrayList<>();
    private boolean useStreaming = true;

    public ReportBuilder(String reportName, String reportDirectory) {
        this.reportName = reportName;
        this.reportDirectory = reportDirectory;
    }

    public <T> TableWorksheetBuilder<T> addWorksheet(Class<T> type) {
        TableWorksheet<T> sheet = new TableWorksheet<>();

        TableWorksheetBuilder<T> builder = new TableWorksheetBuilder<T>(sheet, type);
        worksheets.add(sheet);

        return builder;
    }

    public <T extends Map<String, Object>> TableDictionaryWorksheetBuilder<T> addWorksheetWithDictionaryData() {
        TableDictionaryWorksheet<T> sheet = new TableDictionaryWorksheet<>();

        TableDictionaryWorksheetBuilder<T> builder = new TableDictionaryWorksheetBuilder<T>(sheet);
        worksheets.add(sheet);

        return builder;
    }

    public NoDataWorksheetBuilder addNoDataWorksheet(){
        NoDataWorksheet sheet = new NoDataWorksheet();

        NoDataWorksheetBuilder builder = new NoDataWorksheetBuilder(sheet);
        worksheets.add(sheet);

        return builder;
    }

    public CustomTableWorksheetBuilder addCustomWorksheet(){
        CustomTableWorksheet sheet = new CustomTableWorksheet();

        CustomTableWorksheetBuilder builder = new CustomTableWorksheetBuilder(sheet);
        worksheets.add(sheet);

        return builder;
    }

    public ReportBuilder useStreaming(boolean useStreaming){
        this.useStreaming = useStreaming;

        return this;
    }

    public ReportBuilderResult build() {
        Workbook wb = useStreaming
                ? new SXSSFWorkbook() // keep 100 rows in memory, exceeding rows will be flushed to disk
                : new XSSFWorkbook(); // keep all rows in memory

        for (Worksheet worksheet : worksheets) {
            worksheet.processWorksheet(wb);
        }

        UUID id = saveReport(wb);
        if(useStreaming){
            ((SXSSFWorkbook) wb).dispose();
        }

        return new ReportBuilderResult(reportName, id);
    }

    private UUID saveReport(Workbook wb) {
        File file = new File(reportDirectory);
        if (!file.exists()) {
            boolean result = file.mkdir();
            assert result;
        }

        UUID id = UUID.randomUUID();
        String fileName = id + ReportManager.EXCEL_EXTENSION;
        String filePath = reportDirectory + "/" + fileName;
        try {
            try (FileOutputStream stream = new FileOutputStream(filePath)) {
                wb.write(stream);
                wb.close();
                return id;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
