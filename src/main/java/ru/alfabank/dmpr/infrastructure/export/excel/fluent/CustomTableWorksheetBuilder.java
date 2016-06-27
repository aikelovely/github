package ru.alfabank.dmpr.infrastructure.export.excel.fluent;

import org.apache.poi.ss.usermodel.Sheet;
import ru.alfabank.dmpr.infrastructure.export.excel.CustomTableWorksheet;
import ru.alfabank.dmpr.infrastructure.linq.Action;

public class CustomTableWorksheetBuilder {
    private final CustomTableWorksheet worksheet;

    public CustomTableWorksheetBuilder(CustomTableWorksheet worksheet) {
        this.worksheet = worksheet;
    }

    public CustomTableWorksheetBuilder title(String title){
        worksheet.setTitle(title);
        return this;
    }

    public CustomTableWorksheetBuilder configure(Action<Sheet> worksheetAction){
        worksheet.setSheetAction(worksheetAction);
        return this;
    }
}
