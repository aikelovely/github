package ru.alfabank.dmpr.mapper.qualityDss;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;

/**
 * MyBatis mapper. Используется для загрузки фильтров
 */
public interface QualityDssFilterMapper {
    BaseEntity[] getRegions();

    ChildEntity[] getCities();

    ChildEntity[] getDopOffices();

    BaseEntity[] getSystemUnits();

    BaseEntity[] getBpTypes();

    BaseEntity[] getSalesChannel();

    BaseEntity[] getTimeUnits();
}
