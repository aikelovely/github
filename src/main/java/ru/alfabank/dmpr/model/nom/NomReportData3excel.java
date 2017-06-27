package ru.alfabank.dmpr.model.nom;

import org.joda.time.LocalDate;

/**
 * Набор данных для формирования выгрузки в Excel.
 */
public class NomReportData3excel {
    public String valueDayAsString;
    public String directionName;
    public String calcType;
    public LocalDate bpmanperiodPrdstartDay;
    public LocalDate bpmanperiodValueDay;
    public LocalDate loanapplstatlogValueDay;

    public String timeunitsName;
    public String bpinnerproductCcode;
    public String bpinnerproductName;
    public String loanapplrbDealRef;

    public String salesplaceName;
    public String addressName;
    public String addressCcode;

}
