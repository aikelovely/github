package ru.alfabank.dmpr.model.zp;

import ru.alfabank.dmpr.model.BaseItem;

/**
 * Набор данных для отображения таблицы детализированный отчет о качестве заполнения CRM
 */
public class ZPQualityInfo extends BaseItem {
    public String unitCode;
    public String unitName;
    public String companyINN;
    public long subStageId;
    public int companyCount;
}

