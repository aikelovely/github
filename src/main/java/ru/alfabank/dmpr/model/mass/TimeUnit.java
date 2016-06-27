package ru.alfabank.dmpr.model.mass;

import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Единица времени.
 */
public class TimeUnit extends BaseEntity {
    /**
     * Максимальный период в дней, для которого доступна единица времени.
     */
    public int maxDays;
}
