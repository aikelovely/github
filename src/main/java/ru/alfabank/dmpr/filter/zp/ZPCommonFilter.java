package ru.alfabank.dmpr.filter.zp;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.model.*;
import ru.alfabank.dmpr.model.zp.City;
import ru.alfabank.dmpr.model.zp.Company;
import ru.alfabank.dmpr.model.zp.Manager;
import ru.alfabank.dmpr.model.zp.SubProcessStage;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;

import java.util.List;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин Зарплатные проекты.
 */
@Service
public class ZPCommonFilter {
    @Autowired
    ZPFilterRepository filterRepository;

    /**
     * Возвращает данные для фильтра "Куст"
     * @return
     */
    public BaseEntity[] getBushes() {
        return filterRepository.getBushes();
    }

    /**
     * Возвращает данные для фильтра "Операционный офис"
     * @param bushIds Значения фильтра "Куст"
     * @return
     */
    public BaseEntity[] getOperationOffices(@Param("bushIds[]") final long[] bushIds) {
        return filterRepository.getOperationOffices(bushIds);
    }

    /**
     * Возвращает данные для фильтра "Город"
     * @param bushIds Значения фильтра "Куст"
     * @param operationOfficeIds Значения фильтра "Операционный офис"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public SelectOptGroup[] getCities(@Param("bushIds[]") final long[] bushIds,
                            @Param("operationOfficeIds[]") final long[] operationOfficeIds,
                            @Param("openingTypeId") Integer openingTypeId) {
        if(openingTypeId == null){
            openingTypeId = 1;
        }

        return LinqWrapper.from(filterRepository.getCities(bushIds, operationOfficeIds, openingTypeId)).group(new Selector<City, BaseEntity>() {
            @Override
            public BaseEntity select(City city) {
                return new BaseEntity(city.regionName.hashCode(), city.regionName);
            }
        }).select(new Selector<Group<BaseEntity,City>, SelectOptGroup>() {
            @Override
            public SelectOptGroup select(Group<BaseEntity, City> cities) {
                return new SelectOptGroup(cities.getKey(), cities.getItems().select(new Selector<City, BaseEntity>() {
                    @Override
                    public BaseEntity select(City city) {
                        return city;
                    }
                }).toList());
            }
        }).sort(new Selector<SelectOptGroup, String>() {
            @Override
            public String select(SelectOptGroup selectOptGroup) {
                return selectOptGroup.name;
            }
        }).toArray(SelectOptGroup.class);
    }

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @param term Поисковый запрос
     * @param page Номер страницы с результатами поиска
     * @param pageSize Размер страницы с результатами поиска
     * @param bushIds Значение фильтра "Куст"
     * @param operationOfficeIds Значение фильтра "Операционный офис"
     * @param cityIds Значение фильтра "Город"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public Select2QueryResult getManagers(@Param("term") final String term,
                                 @Param("page") final Integer page,
                                 @Param("pageSize") final int pageSize,
                                 @Param("bushIds[]") final long[] bushIds,
                                 @Param("operationOfficeIds[]") final long[] operationOfficeIds,
                                 @Param("cityIds[]") final long[] cityIds,
                                 @Param("openingTypeId") Integer openingTypeId) {
        if(openingTypeId == null){
            openingTypeId = 1;
        }

        LinqWrapper<Manager> managers = LinqWrapper.from(filterRepository.getManagers(bushIds, operationOfficeIds, cityIds, openingTypeId));

        if(term != null && !term.isEmpty()){
            final String searchTerm = term.toLowerCase();
            managers = managers.filter(new Predicate<Manager>() {
                @Override
                public boolean check(Manager item) {
                    return item.name.toLowerCase().contains(searchTerm);
                }
            });
        }
        int actualPage = page == null ? 0 : page - 1;

        List<Select2Option> result = managers.select(new Selector<Manager, Select2Option>() {
            @Override
            public Select2Option select(Manager c) {
                return new Select2Option(c.id, c.name);
            }
        }).toList();

        int fromIndex = actualPage * pageSize;
        int toIndex = fromIndex + pageSize;
        boolean more = result.size() > toIndex;
        if(toIndex > result.size()) {
            toIndex = result.size();
        }

        if(result.size() > 0 && result.size() > fromIndex){
            result = result.subList(fromIndex, toIndex);
        }

        return new Select2QueryResult(more, result);
    }

    /**
     * Возвращает данные для фильтра "Компания"
     * @param term Поисковый запрос
     * @param page Номер страницы с результатами поиска
     * @param pageSize Размер страницы с результатами поиска
     * @param bushIds bushIds Значения фильтра "Куст"
     * @param operationOfficeIds Значения фильтра "Операционный офис"
     * @param cityIds Значение фильтра "Город"
     * @param managerIds Значение фильтра "Менеджер"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public Select2QueryResult getCompanies(@Param("term") final String term,
                                           @Param("page") final Integer page,
                                           @Param("pageSize") final int pageSize,
                                           @Param("bushIds[]") final long[] bushIds,
                                           @Param("operationOfficeIds[]") final long[] operationOfficeIds,
                                           @Param("cityIds[]") final long[] cityIds,
                                           @Param("managerIds[]") final String[] managerIds,
                                           @Param("openingTypeId") Integer openingTypeId) {
        if(openingTypeId == null){
            openingTypeId = 1;
        }

        LinqWrapper<Company> companies = LinqWrapper.from(filterRepository.getCompanies(
                bushIds, operationOfficeIds, cityIds, managerIds, openingTypeId));

        if(term != null && !term.isEmpty()){
            final String searchTerm = term.toLowerCase();
            companies = companies.filter(new Predicate<Company>() {
                @Override
                public boolean check(Company item) {
                    return item.nameToSearch.contains(searchTerm);
                }
            });
        }
        int actualPage = page == null ? 0 : page - 1;

        List<Select2Option> result = companies.select(new Selector<Company, Select2Option>() {
            @Override
            public Select2Option select(Company c) {
                return new Select2Option(c.id, c.nameToDisplay);
            }
        }).toList();

        int fromIndex = actualPage * pageSize;
        int toIndex = fromIndex + pageSize;
        boolean more = result.size() > toIndex;
        if(toIndex > result.size()) {
            toIndex = result.size();
        }

        if(result.size() > 0 && result.size() > fromIndex){
            result = result.subList(fromIndex, toIndex);
        }

        return new Select2QueryResult(more, result);
    }

    /**
     * Возвращает данные для фильтра "Подэтап"
     * @param processStageIds Значение фильтра "Этап"
     * @return
     */
    public ChildEntity[] getSubProcessStages(@Param("processStageIds[]") final long[] processStageIds) {
        return LinqWrapper.from(filterRepository.getSubProcessStages()).filter(new Predicate<SubProcessStage>() {
            @Override
            public boolean check(SubProcessStage item) {
                return ArrayUtils.contains(processStageIds, item.parentId);
            }
        }).toArray(SubProcessStage.class);
    }

    /**
     * Возвращает данные для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return filterRepository.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Детализация"
     * @return
     */
    public BaseEntity[] getSystemUnits() {
        return filterRepository.getSystemUnits();
    }

    /**
     * Возвращает данные для фильтра "Тип схемы"
     * @return
     */
    public BaseEntity[] getSchemaTypes() {
        return filterRepository.getSchemaTypes();
    }

    /**
     * Возвращает данные для фильтра "Тип открытия"
     * @return
     */
    public BaseEntity[] getOpeningTypes() {
        return filterRepository.getOpeningTypes();
    }

    /**
     * Возвращает данные для фильтра "Этап"
     * @return
     */
    public BaseEntity[] getProcessStages() {
        return filterRepository.getProcessStages();
    }
}
