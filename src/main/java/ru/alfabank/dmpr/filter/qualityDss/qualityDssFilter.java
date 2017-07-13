package ru.alfabank.dmpr.filter.qualityDss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.SelectOptGroup;
import ru.alfabank.dmpr.model.mass.TimeUnit;
import ru.alfabank.dmpr.repository.qualityDss.QualityDssFilterRepository;

import java.util.List;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин МАСС.
 */
@Service
public class QualityDssFilter {
    @Autowired
    QualityDssFilterRepository repository;

    public BaseEntity[] getSystemUnits() {
        return repository.getSystemUnits();
    }

    public SelectOptGroup[] getCities() {
        BaseEntity[] regions = repository.getRegions();
        final ChildEntity[] cities = repository.getCities();

        return LinqWrapper.from(regions).select(new Selector<BaseEntity, SelectOptGroup>() {
            @Override
            public SelectOptGroup select(final BaseEntity region) {
                List<BaseEntity> citiesByRegion = LinqWrapper.from(cities)
                        .filter(new Predicate<ChildEntity>() {
                            @Override
                            public boolean check(ChildEntity city) {
                                return city.parentId == region.id;
                            }
                        })
                        .select(new Selector<ChildEntity, BaseEntity>() {
                            @Override
                            public BaseEntity select(ChildEntity childEntity) {
                                return childEntity.toBaseEntity();
                            }
                        }).toList();

                return new SelectOptGroup(region, citiesByRegion);
            }
        }).sort(new Selector<SelectOptGroup, String>() {
            @Override
            public String select(SelectOptGroup selectOptGroup) {
                return selectOptGroup.name;
            }
        }).toArray(SelectOptGroup.class);
    }

    public BaseEntity[] getTimeUnits() {
        return repository.getTimeUnits();
    }

    public BaseEntity[] getSalesChannel() {
        return repository.getSalesChannel();
    }

    public BaseEntity[] getBpTypes() {
        return repository.getBpTypes();
    }

    public BaseEntity[] getDopOffices(@Param("cityIds[]") long[] cityIds) {
        return repository.getDopOffices(cityIds);
    }
}
