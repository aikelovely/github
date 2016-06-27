package ru.alfabank.dmpr.infrastructure.export;

import java.util.UUID;

public class ReportBuilderResult {
    private String reportName;
    private UUID id;

    public ReportBuilderResult(String fileName, UUID id){
        this.reportName = fileName;
        this.id = id;
    }

    public String getReportName() {
        return reportName;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ReportBuilderResult{" +
                "reportName='" + reportName + '\'' +
                ", id=" + id +
                '}';
    }
}
