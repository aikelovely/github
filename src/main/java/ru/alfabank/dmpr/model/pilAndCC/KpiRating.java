package ru.alfabank.dmpr.model.pilAndCC;

/**
 * Набор данных для отображения рейтинга "Топ N худших/лучших"
 */
public class KpiRating {
    public int reportTypeId;
    public double value;
    public String valueName;
    public long valueId;
    public double count;
    public long operationId;
    public String operationName;
    public int sortOrder;
}
