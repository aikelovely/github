package ru.alfabank.dmpr.model.zp;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Город.
 */
public class City extends BaseEntity {
    /**
     * Название Куст.
     */
    public String regionName;

    /**
     * Id Куста
     */
    public Long bushId;

    /**
     * Id операционного офиса
     */
    public Long operationOfficeId;
}
