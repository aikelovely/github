package ru.alfabank.dmpr.repository.mass;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.mass.MassFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.mass.TimeUnit;

@Repository
public class MassFilterRepository {
    @Autowired
    MassFilterMapper mapper;

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

    public BaseEntity[] getBpTypes() {
        return mapper.getBpTypes();
    }

    public BaseEntity[] getSalesChannel() {
        return mapper.getSalesChannel();
    }

    public TimeUnit[] getTimeUnits() {
        return mapper.getTimeUnits();
    }
}
