package ru.alfabank.dmpr.mapper.mass;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.mass.TimeUnit;

/**
 * MyBatis mapper. Используется для загрузки фильтров витрин МАСС.
 */
public interface MassFilterMapper {
    BaseEntity[] getRegions();

    ChildEntity[] getCities();

    ChildEntity[] getDopOffices();

    BaseEntity[] getSystemUnits();

    BaseEntity[] getBpTypes();

    BaseEntity[] getSalesChannel();

    TimeUnit[] getTimeUnits();
}
