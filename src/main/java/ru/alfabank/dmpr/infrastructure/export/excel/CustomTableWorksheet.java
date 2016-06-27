package ru.alfabank.dmpr.infrastructure.export.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import ru.alfabank.dmpr.infrastructure.linq.Action;

public class CustomTableWorksheet extends NoDataWorksheet {
    public Action<Sheet> getSheetAction() {
        return sheetAction;
    }

    public void setSheetAction(Action<Sheet> sheetAction) {
        this.sheetAction = sheetAction;
    }

    private Action<Sheet> sheetAction;

    @Override
    public void processWorksheet(Workbook wb) {
        Sheet wbSheet = wb.createSheet(this.title);
        sheetAction.act(wbSheet);
    }
}
