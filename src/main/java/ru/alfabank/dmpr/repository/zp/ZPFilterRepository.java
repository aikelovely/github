package ru.alfabank.dmpr.repository.zp;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.mapper.zp.ZPFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.zp.*;

import java.util.List;

/**
 * Репозиторий, отвечающий за загрузку данных для фильтров витрин "Зарплатные проекты".
 */
@Repository
public class ZPFilterRepository {
    @Autowired
    ZPFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "Куст"
     * @return
     */
    public BaseEntity[] getBushes() {
        return mapper.getBushes();
    }

    /**
     * Возвращает данные для фильтра "Операционный офис"
     * @return
     */
    public BaseEntity[] getOperationOffices(final long[] bushIds) {
        BaseEntity[] result = mapper.getOperationOffices();

        if (bushIds == null || bushIds.length == 0) return result;

        final LinqWrapper<BaseEntity> operationOffices = LinqWrapper.from(result);
        final City[] cities = mapper.getCities();

        return LinqWrapper.from(cities).filter(new Predicate<City>() {
            @Override
            public boolean check(City item) {
                return item.bushId != null && ArrayUtils.contains(bushIds, item.bushId);
            }
        }).select(new Selector<City, BaseEntity>() {
            @Override
            public BaseEntity select(final City city) {
                return operationOffices.first(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(final BaseEntity item) {
                        return item.id == city.operationOfficeId;
                    }
                });
            }
        }).distinctBy(new Selector<BaseEntity, Long>() {
            @Override
            public Long select(BaseEntity baseEntity) {
                return baseEntity.id;
            }
        }).toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Тип открытия"
     * @return
     */
    public BaseEntity[] getOpeningTypes() {
        return mapper.getOpeningTypes();
    }

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @return
     */
    public List<Manager> getAllManagers(long[] bushIds, long[] operationOfficeIds, final long[] cityIds, int openingTypeId) {
        return LinqWrapper.from(mapper.getManagers(bushIds, operationOfficeIds, cityIds, openingTypeId))
                .select(new Selector<ManagerDto, Manager>() {
                    @Override
                    public Manager select(ManagerDto dto) {
                        long[] cityIds = parseCityIds(dto.cityIds);
                        return new Manager(dto.id, dto.name, cityIds);
                    }
                }).toList();
    }

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @param cityIds Значение фильтра "Город"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public Manager[] getManagers(final long[] bushIds, final long[] operationOfficeIds, final long[] cityIds, int openingTypeId) {
        return LinqWrapper.from(getAllManagers(bushIds, operationOfficeIds, cityIds, openingTypeId)).toArray(Manager.class);
    }

    /**
     * Возвращает данные для фильтра "Город"
     * @return
     */
    public City[] getAllCities(int openingTypeId) {
        if (openingTypeId == 1) return mapper.getCities();
        return mapper.getRoCities();
    }

    /**
     * Возвращает данные для фильтра "Город"
     * @param bushIds Значение фильтра "Куст"
     * @param operationOfficeIds Значение фильтра "Операционный офис"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public City[] getCities(final long[] bushIds,
                            final long[] operationOfficeIds,
                            int openingTypeId) {
        LinqWrapper<City> cities = LinqWrapper.from(getAllCities(openingTypeId));

        if (bushIds != null && bushIds.length > 0) {
            cities = cities.filter(new Predicate<City>() {
                @Override
                public boolean check(City item) {
                    return item.bushId != null && ArrayUtils.contains(bushIds, item.bushId);
                }
            });
        }

        if (operationOfficeIds != null && operationOfficeIds.length > 0) {
            cities = cities.filter(new Predicate<City>() {
                @Override
                public boolean check(City item) {
                    return item.operationOfficeId != null
                            && ArrayUtils.contains(operationOfficeIds, item.operationOfficeId);
                }
            });
        }

        return cities.toArray(City.class);
    }

    /**
     * Возвращает данные для фильтра "Подэтап"
     * @return
     */
    public SubProcessStage[] getSubProcessStages() {
        return mapper.getSubProcessStages();
    }

    /**
     * Возвращает данные для фильтра "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Детализация"
     * @return
     */
    public BaseEntity[] getSystemUnits() {
        return mapper.getSystemUnits();
    }

    /**
     * Возвращает данные для фильтра "Компания"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public List<Company> getAllCompanies(long[] bushIds,
                                         long[] operationOfficeIds,
                                         long[] cityIds,
                                         int openingTypeId) {
        LinqWrapper<Company> companies = LinqWrapper.from(mapper.getCompanies(bushIds, operationOfficeIds, cityIds, openingTypeId))
                .select(new Selector<CompanyDto, Company>() {
                    @Override
                    public Company select(CompanyDto dto) {
                        long[] cityIds = parseCityIds(dto.cityIds);
                        String nameToDisplay = String.format("%s (%s)", dto.name, dto.inn);
                        String nameToSearch = String.format("%s (%s)", dto.name, dto.inn).toLowerCase();
                        return new Company(dto.id,
                                dto.name,
                                cityIds,
                                dto.managerId,
                                nameToDisplay,
                                nameToSearch,
                                dto.inn);
                    }
                });

        return companies.toList();
    }

    /**
     * Возвращает данные для фильтра "Компания"
     * @param bushIds Значение фильтра "Куст"
     * @param operationOfficeIds Значение фильтра "Операционный офис"
     * @param cityIds Значение фильтра "Город"
     * @param managerIds Значение фильтра "Менеджер"
     * @param openingTypeId Значение фильтра "Тип открытия"
     * @return
     */
    public List<Company> getCompanies(final long[] bushIds,
                                      final long[] operationOfficeIds,
                                      final long[] cityIds,
                                      final String[] managerIds,
                                      int openingTypeId) {
        LinqWrapper<Company> companies = LinqWrapper.from(getAllCompanies(bushIds, operationOfficeIds, cityIds, openingTypeId));

        if (managerIds != null && managerIds.length > 0) {
            companies = companies.filter(new Predicate<Company>() {
                @Override
                public boolean check(Company item) {
                    return item.managerId != null && ArrayUtils.contains(managerIds, item.managerId);
                }
            });
        }

        return companies.toList();
    }

    private long[] parseCityIds(String cityIds) {
        String[] parts = cityIds.split(",");
        Long[] cityIdsAsLong = LinqWrapper.from(parts).select(new Selector<String, Long>() {
            @Override
            public Long select(String s) {
                try {
                    return Long.parseLong(s);
                } catch (NumberFormatException ex) {
                    return Double.valueOf(s).longValue();
                }
            }
        }).toArray(Long.class);

        return ArrayUtils.toPrimitive(cityIdsAsLong);
    }

    /**
     * Возвращает данные для фильтра "Тип схемы"
     * @return
     */
    public BaseEntity[] getSchemaTypes() {
        return mapper.getSchemaTypes();
    }

    /**
     * Возвращает данные для фильтра "Этап"
     * @return
     */
    public ProcessStage[] getProcessStages() {
        return mapper.getProcessStages();
    }

    public long getProcessStageIdByCode(final int code) {
        ProcessStage stage = LinqWrapper.from(mapper.getProcessStages()).firstOrNull(new Predicate<ProcessStage>() {
            @Override
            public boolean check(ProcessStage item) {
                return item.code == code;
            }
        });

        return stage != null ? stage.id : 0l;
    }
}
