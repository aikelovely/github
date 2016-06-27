package ru.alfabank.dmpr.model.ctq;

/**
 * Информация о метриках.
 */
public class CTQDashboardMetric {
    public String id;
    public String code;
    public String name;
    public String shortName;

    public Double normativeValue;

    public Double value;
    public Double prevValue;

    public Long numerator;
    public Long denominator;
    public Long prevNumerator;
    public Long prevDenominator;

    public Double qualityLevel;
    public Double qualityLevelNormative;
    public Double prevQualityLevel;

    public String additionalInfo;
}
