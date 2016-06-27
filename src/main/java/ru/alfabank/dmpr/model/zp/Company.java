package ru.alfabank.dmpr.model.zp;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Компания.
 */
public class Company {
    /**
     * Id.
     */
    public String id;

    /**
     * Название.
     */
    public String name;

    /**
     * Id городов.
     */
    public long[] cityIds;

    /**
     * Id менеджера
     */
    public String managerId;

    /**
     * Название компании + ИНН (для отображения в выпадающем списке.
     */
    public String nameToDisplay;

    /**
     * Название компании + ИНН в нижнем регистре (для поиска)
     */
    public String nameToSearch;

    /**
     * ИНН
     */
    public String inn;

    public Company(String id,
                   String name,
                   long[] cityIds,
                   String managerId,
                   String nameToDisplay,
                   String nameToSearch,
                   String inn) {
        this.id = id;
        this.name = name;
        this.cityIds = cityIds;
        this.managerId = managerId;
        this.nameToDisplay = nameToDisplay;
        this.nameToSearch = nameToSearch;
        this.inn = inn;
    }
}


