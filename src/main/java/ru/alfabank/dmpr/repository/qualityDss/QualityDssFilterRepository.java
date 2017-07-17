package ru.alfabank.dmpr.repository.qualityDss;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.qualityDss.QualityDssFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.mass.TimeUnit;
import ru.alfabank.dmpr.model.qualityDss.QualityDssOptions;

@Repository
public class QualityDssFilterRepository {
    @Autowired
    QualityDssFilterMapper mapper;

    public BaseEntity[] getRegions() {
        return mapper.getRegions();
    }

    public BaseEntity[] getDopOffices(final long[] cityIds) {
        ChildEntity[] result = mapper.getDopOffices();
        if(cityIds != null && cityIds.length > 0){
            result = LinqWrapper.from(result).filter(new Predicate<ChildEntity>() {
                @Override
                public boolean check(ChildEntity item) {
                    return ArrayUtils.contains(cityIds, item.parentId);
                }
            }).toArray(ChildEntity.class);
        }

        return BaseEntity.toBaseEntities(result);
    }

    public BaseEntity[] getSystemUnits() {
        return mapper.getSystemUnits();
    }

    public ChildEntity[] getCities() {
        return mapper.getCities();
    }


    public BaseEntity[] getDivision(QualityDssOptions qualityDssOptions) {
        return mapper.getDivision(qualityDssOptions);
    }

    public BaseEntity[] getEmployee(QualityDssOptions qualityDssOptions) {
        return mapper.getEmployee(qualityDssOptions);
    }
    public BaseEntity[] getOperation(QualityDssOptions qualityDssOptions) {
        return mapper.getOperation(qualityDssOptions);
    }
    public BaseEntity[] getTypeProduct(QualityDssOptions qualityDssOptions) {
        return mapper.getTypeProduct(qualityDssOptions);
    }
    public BaseEntity[] getSalesChannel() {
        return mapper.getSalesChannel();
    }

    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }
    public Week[] getWeeks(final int year) {
        return LinqWrapper.from(mapper.getWeeks())
                .filter(new Predicate<Week>() {
                    @Override
                    public boolean check(Week item) {
                        return item.year == year;
                    }
                })
                .toArray(Week.class);
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    public Week[] getWeeks() {
        return mapper.getWeeks();
    }
}
