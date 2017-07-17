package ru.alfabank.dmpr.mapper.qualityDss;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.qualityDss.QualityDssOptions;

/**
 * MyBatis mapper. Используется для загрузки фильтров
 */
public interface QualityDssFilterMapper {
    BaseEntity[] getRegions();

    ChildEntity[] getCities();

    ChildEntity[] getDopOffices();

    BaseEntity[] getSystemUnits();

    BaseEntity[] getDivision(QualityDssOptions qualityDssOptions);
    BaseEntity[] getEmployee(QualityDssOptions qualityDssOptions);
    BaseEntity[] getOperation(QualityDssOptions qualityDssOptions);
    BaseEntity[] getTypeProduct(QualityDssOptions qualityDssOptions);


    BaseEntity[] getSalesChannel();
    Week[] getWeeks();
    BaseEntity[] getTimeUnits();

}
