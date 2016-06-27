package ru.alfabank.dmpr.model.operKpi;

/**
 * Строка таблицы Отчет по KPI на длительность скорингов.
 */
public class OperKpiScorTableRow {
    public long reportTypeId;

    public String durGroupName;

    // TTY Окончательное решение
    public double finalFactValuePIL;
    public double finalFactValueCC;

    // TTY Предварительное решение
    public double preliminarilyFactValuePIL;
    public double preliminarilyFactValueCC;

    /**
     * Строка состоит из 2х частей: TTY окончательное и TTY предварительное.
     * @param finalData
     * @param preliminarilyData
     */
    public OperKpiScorTableRow(OperKpiScorValue finalData,
                               OperKpiScorValue preliminarilyData){
        this.reportTypeId = finalData.reportTypeId;
        this.durGroupName = finalData.durGroupName;

        this.finalFactValueCC = finalData.factValueCC;
        this.finalFactValuePIL = finalData.factValuePIL;


        if(preliminarilyData != null){
            this.preliminarilyFactValueCC = preliminarilyData.factValueCC;
            this.preliminarilyFactValuePIL = preliminarilyData.factValuePIL;
        }
    }
}
