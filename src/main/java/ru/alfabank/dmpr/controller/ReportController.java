package ru.alfabank.dmpr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alfabank.dmpr.infrastructure.export.ReportManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Контроллер отвечающий за выгрузку отчетов в формате Excel.
 */
@Controller
public class ReportController {
    @Autowired
    ReportManager reportManager;

    private static final int BUFFER_SIZE = 4096;

    /**
     * Загрузка отчета по Id.
     *
     * @param id         Id отчета, UUID
     * @param response   HttpServletResponse
     * @throws IOException
     */
    @RequestMapping("/excel/getById")
    public void getById(@RequestParam(value = "id") UUID id,
                        HttpServletRequest request,
                        HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");

        String reportPath = reportManager.getReportDirectory() + "/" + id + ReportManager.EXCEL_EXTENSION;
        File downloadFile = new File(reportPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        String reportName = URLDecoder.decode(request.getParameter("reportName"), "UTF-8");
        String fileName = reportName + ReportManager.EXCEL_EXTENSION;

        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentType("application/ms-excel; charset=UTF-8");
        response.setContentLength((int) downloadFile.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s",
                URLEncoder.encode(fileName, "UTF-8").replace("+", "%20"), URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")));

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }

    /**
     * Загрузка отчета в формате CSV по Id.
     *
     * @param id         Id отчета, UUID
     * @param reportName Название файла CSV
     * @param response   HttpServletResponse
     * @throws IOException
     */
    @RequestMapping("/csv/getById")
    public void getCsvById(@RequestParam(value = "id") UUID id,
                        @RequestParam(value = "reportName") String reportName,
                        HttpServletResponse response) throws IOException {

        String reportPath = reportManager.getReportDirectory() + "/" + id + ReportManager.CSV_EXTENSION;
        File downloadFile = new File(reportPath);
        FileInputStream inputStream = new FileInputStream(downloadFile);

        String fileName = reportName + ReportManager.CSV_EXTENSION;

        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "max-age=0");
        response.setContentType("text/csv");
        response.setContentLength((int) downloadFile.length());
        response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"; filename*=UTF-8''%s",
                fileName, URLEncoder.encode(fileName, "UTF-8").replace("+", "%20")));

        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead = -1;

        // write bytes read from the input stream into the output stream
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inputStream.close();
        outStream.close();
    }
}
