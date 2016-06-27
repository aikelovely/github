package ru.alfabank.dmpr.model.cards;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Отделение.
 */
public class Branch extends BaseEntity {
    /**
     * Название отделения
     */
    public String branchName;

    /**
     * Id города
     */
    public long cityId;

    /**
     * Id типа отделения
     */
    public long branchTypeId;

    @Override
    public BaseEntity toBaseEntity() {
        return new BaseEntity(id, branchName);
    }
}
