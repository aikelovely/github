package ru.alfabank.dmpr.model.zp;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Компания. DTO для получения данных из бд.
 */
public class CompanyDto extends BaseItem {
    public String id;
    public String name;
    public String cityIds;
    public String managerId;
    public String inn;
}

