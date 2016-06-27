package ru.alfabank.dmpr.model.cr.ClientTime;

import java.io.Serializable;

/**
 * Иерархия кредитных подразделений.
 */
public class CreditDeptHierarchyDto implements Serializable {
    /**
     * Id доп. офиса.
     */
    public long dopOfficeId;

    /**
     * Название доп. офиса.
     */
    public String dopOfficeName;

    /**
     * Id кредитного подразделения.
     */
    public long creditDepartId;

    /**
     * Название кредитного подразделения.
     */
    public String creditDepartName;
}
