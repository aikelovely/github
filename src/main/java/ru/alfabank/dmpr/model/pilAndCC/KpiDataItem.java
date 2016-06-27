package ru.alfabank.dmpr.model.pilAndCC;

import org.joda.time.LocalDate;

/**
 * Основной класс, который содержит данные для отображения графиков витрины
 * "Персональные кредиты и кредитные карты. Витрина декомпозиции"
 */
public class KpiDataItem {
    public int reportTypeId;
    public LocalDate calcDate;
    public double value;
    public String valueName;
    public Long valueId;
    public double planValue;
    public Double count;
    public Double inQuotaCount;
}

