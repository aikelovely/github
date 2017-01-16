package ru.alfabank.dmpr.infrastructure.export;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.export.csv.CsvBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;

import javax.servlet.ServletContext;
import java.util.UUID;

@Component
public class ReportManager {

    @Autowired
    private ServletContext servletContext;

    public static final String EXCEL_EXTENSION = ".xlsx";
    public static final String CSV_EXTENSION = ".csv";

    public ReportBuilder createBuilder(String reportName) {
        return new ReportBuilder(reportName, getReportDirectory());
    }

    public CsvBuilder createCsvBuilder(String reportName) {
        return new CsvBuilder(reportName, getReportDirectory());
    }

    public String getReportPathById(UUID id) {
        return getReportDirectory() + "/" + id + EXCEL_EXTENSION;
    }


//ДЛя 8 томкат палку / добавить blev
//    return servletContext.getRealPath("/report");
    public String getReportDirectory() {

        return servletContext.getRealPath("report");
    }
}
