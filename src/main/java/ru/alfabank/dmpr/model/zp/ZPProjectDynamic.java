package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Набор данных для отображения динамики "Доля компаний с данными CRM"
 */
public class ZPProjectDynamic extends BaseItem {
    public String unitCode;
    public String unitName;
    public LocalDate calcDate;
    public Integer companyCount;
    public Integer startedOkCount;
}

