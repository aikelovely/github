package ru.alfabank.dmpr.model.cr.ClientTime;

import java.io.Serializable;

/**
 * Иерархия подразделений.
 */
public class DeptHierarchyDto implements Serializable {
    /**
     * Id ЦО/РП.
     */
    public long blTypeId;

    /**
     * Название ЦО/РП..
     */
    public String blTypeName;

    /**
     * Id куста.
     */
    public long blId;

    /**
     * Название куста.
     */
    public String blName;

    /**
     * Id доп. офиса.
     */
    public long dopOfficeId;

    /**
     * Название доп. офиса.
     */
    public String dopOfficeName;

    /**
     * Id подразделения.
     */
    public long departId;

    /**
     * Название подразделения.
     */
    public String departName;
}

