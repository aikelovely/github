package ru.alfabank.dmpr.widget.zp.openingSpeed;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;

/**
 * Строка таблицы "Аномальная длительность процесса"
 */
public class EmissionTableResult {
    // Это поле нужно заполнять вручную, не через конструктор
    public String managerName;

    public String companyName;
    public String companyCode;

    public Double avgDuration;

    public LocalDate s1StartDate;
    public Double s1Duration;

    public LocalDate s2StartDate;
    public Double s2Duration;

    public LocalDate s3StartDate;
    public Double s3Duration;

    public String companyINN;

    public EmissionTableResult(ZPKPIDataItem dataItem){
        companyName = dataItem.unitName;
        companyCode = dataItem.companyCode;
        avgDuration = dataItem.avgDuration;
        s1StartDate = dataItem.s1StartDate;
        s1Duration = dataItem.s1Duration;

        s2StartDate = dataItem.s2StartDate;
        s2Duration = dataItem.s2Duration;

        s3StartDate = dataItem.s3StartDate;
        s3Duration = dataItem.s3Duration;

        companyINN = dataItem.companyINN;
    }
}
