package ru.alfabank.dmpr.infrastructure.export.csv;

import ru.alfabank.dmpr.infrastructure.export.ReportBuilderResult;
import ru.alfabank.dmpr.infrastructure.export.ReportManager;
import ru.alfabank.dmpr.infrastructure.export.csv.fluent.TableConfigurationBuilder;

import java.io.*;
import java.util.UUID;

public class CsvBuilder {
    private String reportName;
    private String reportDirectory;

    private TableConfiguration tableConfiguration;

    public CsvBuilder(String reportName, String reportDirectory) {
        this.reportName = reportName;
        this.reportDirectory = reportDirectory;
    }

    public <T> TableConfigurationBuilder<T> configure(Class<T> type) {
        this.tableConfiguration = new TableConfiguration<>();

        return new TableConfigurationBuilder<T>(this.tableConfiguration, type);
    }

    public ReportBuilderResult build() {
        File file = new File(reportDirectory);
        if (!file.exists()) {
            boolean result = file.mkdir();
            assert result;
        }

        UUID id = UUID.randomUUID();
        String fileName = id + ReportManager.CSV_EXTENSION;
        String filePath = reportDirectory + "/" + fileName;
        try {
            OutputStream os = new FileOutputStream(filePath);
            // This prefix indicates that the content is in 'UTF-8 with BOM' (otherwise it is just 'UTF-8 without BOM').
            os.write(239);
            os.write(187);
            os.write(191);

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"))) {
                tableConfiguration.exportData(writer);

                writer.flush();
                writer.close();

                return new ReportBuilderResult(reportName, id);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
