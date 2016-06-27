package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import ru.alfabank.dmpr.infrastructure.export.excel.NoDataWorksheet;


public class NoDataWorksheetBuilder {
    private final NoDataWorksheet worksheet;

    public NoDataWorksheetBuilder(NoDataWorksheet worksheet) {
        this.worksheet = worksheet;
    }

    public NoDataWorksheetBuilder title(String title){
        worksheet.setTitle(title);
        return this;
    }
}
