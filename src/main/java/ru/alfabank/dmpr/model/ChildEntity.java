package ru.alfabank.dmpr.model;

/**
 * Базовый для иерархических типов
 */
public class ChildEntity extends BaseEntity {
    public long parentId;

    @Override
    public String toString() {
        return "ChildEntity{" +
                "parentId=" + parentId +
                "} " + super.toString();
    }
}
