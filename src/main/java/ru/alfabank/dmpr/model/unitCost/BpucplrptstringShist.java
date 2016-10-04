package ru.alfabank.dmpr.model.unitCost;

import org.joda.time.LocalDate;

/**
 * Набор данных для формирования выгрузки в Excel.
 */
public class BpucplrptstringShist {
    public String ncodepl;
    public String namepl;
    public String ncodeupc;
    public String nameupc;
    public LocalDate effectiveFrom;
    public LocalDate effectiveTo;
}