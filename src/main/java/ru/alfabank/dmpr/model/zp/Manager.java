package ru.alfabank.dmpr.model.zp;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Менеджер.
 */
public class Manager {
    /**
     * Id.
     */
    public String id;

    /**
     * Название
     */
    public String name;

    /**
     * Id городов.
     */
    public long[] cityIds;

    public Manager(String id, String name, long[] cityIds) {
        this.id = id;
        this.name = name;
        this.cityIds = cityIds;
    }
}

