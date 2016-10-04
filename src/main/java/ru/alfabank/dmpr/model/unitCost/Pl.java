package ru.alfabank.dmpr.model.unitCost;

import org.joda.time.LocalDate;

/**
 * Набор данных для формирования выгрузки в Excel.
 */
public class Pl {
    public LocalDate valueDay;
    public String ccode;
    public String rusCcode;
    public String ncodePl;
    public String namePl;
    public String ncodeGr;
    public String nameGr;
    public Double sumRurAmt;
    public Double sumUsdAmt;
}
