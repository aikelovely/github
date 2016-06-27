package ru.alfabank.dmpr.repository.cr;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.mapper.cr.ClientTimeFilterMapper;
import ru.alfabank.dmpr.mapper.cr.ClientTimeMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.cr.ClientTime.*;

/**
 * Репозиторий для фильтров витрины "Декомпозиция процессов".
 */
@Repository
public class ClientTimeFilterRepository {
    @Autowired
    ClientTimeFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "Тип заявки".
     * @return
     */
    public BaseEntity[] getKpiMetrics() {
        return mapper.getKpiMetrics();
    }

    private DeptHierarchyDto[] getCreditDeptHierarchy() {
        return mapper.getDeptHierarchy();
    }

    /**
     * Возвращает данные для фильтра "ЦО/РП".
     * @return
     */
    public BaseEntity[] getBLTypes() {
        return LinqWrapper.from(getCreditDeptHierarchy())
                .select(new Selector<DeptHierarchyDto, BaseEntity>() {
                    @Override
                    public BaseEntity select(DeptHierarchyDto dto) {
                        return new BaseEntity(dto.blTypeId, dto.blTypeName);
                    }
                })
                .distinct()
                .toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Куст".
     * @param blTypeIds Значения фильтра "ЦО/РП"
     * @return
     */
    public BaseEntity[] getBLs(final long[] blTypeIds) {
        LinqWrapper<DeptHierarchyDto> result = LinqWrapper.from(getCreditDeptHierarchy());

        if(blTypeIds != null && blTypeIds.length > 0){
            result = result.filter(new Predicate<DeptHierarchyDto>() {
                @Override
                public boolean check(DeptHierarchyDto item) {
                    return ArrayUtils.contains(blTypeIds, item.blTypeId);
                }
            });
        }

        return result.select(new Selector<DeptHierarchyDto, BaseEntity>() {
            @Override
            public BaseEntity select(DeptHierarchyDto dto) {
                return new BaseEntity(dto.blId, dto.blName);
            }
        }).distinct().toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Доп. офис".
     * @param blIds Значения фильтра "Куст".
     * @return
     */
    public BaseEntity[] getDopOffices(final long[] blIds){
        LinqWrapper<DeptHierarchyDto> result = LinqWrapper.from(getCreditDeptHierarchy());

        if(blIds != null && blIds.length > 0){
            result = result.filter(new Predicate<DeptHierarchyDto>() {
                @Override
                public boolean check(DeptHierarchyDto item) {
                    return ArrayUtils.contains(blIds, item.blId);
                }
            });
        }

        return result.select(new Selector<DeptHierarchyDto, BaseEntity>() {
            @Override
            public BaseEntity select(DeptHierarchyDto dto) {
                return new BaseEntity(dto.dopOfficeId, dto.dopOfficeName);
            }
        }).distinct().toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Подразделение".
     * @param dopOfficeIds Значения фильтра "Доп. офис"
     * @return
     */
    public BaseEntity[] getDeparts(final long[] dopOfficeIds){
        LinqWrapper<DeptHierarchyDto> result = LinqWrapper.from(getCreditDeptHierarchy());

        if(dopOfficeIds != null && dopOfficeIds.length > 0){
            result = result.filter(new Predicate<DeptHierarchyDto>() {
                @Override
                public boolean check(DeptHierarchyDto item) {
                    return ArrayUtils.contains(dopOfficeIds, item.dopOfficeId);
                }
            });
        }

        return result.select(new Selector<DeptHierarchyDto, BaseEntity>() {
            @Override
            public BaseEntity select(DeptHierarchyDto dto) {
                return new BaseEntity(dto.departId, dto.departName);
            }
        }).distinct().toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Кредитное подразделение".
     * @param dopOfficeIds Значения фильтра "Доп. офис"
     * @return
     */
    public BaseEntity[] getCreditDeparts(final long[] dopOfficeIds){
        LinqWrapper<CreditDeptHierarchyDto> result = LinqWrapper.from(mapper.getCreditDeptHierarchy());

        if(dopOfficeIds != null && dopOfficeIds.length > 0){
            result = result.filter(new Predicate<CreditDeptHierarchyDto>() {
                @Override
                public boolean check(CreditDeptHierarchyDto item) {
                    return ArrayUtils.contains(dopOfficeIds, item.dopOfficeId);
                }
            });
        }

        return result.select(new Selector<CreditDeptHierarchyDto, BaseEntity>() {
            @Override
            public BaseEntity select(CreditDeptHierarchyDto dto) {
                return new BaseEntity(dto.creditDepartId, dto.creditDepartName);
            }
        }).distinct().toArray(BaseEntity.class);
    }

    private StageHierarchyDto[] getStageHierarchy(LocalDate startDate, LocalDate endDate, long kpiId, long[] stageIds) {
        return mapper.getStageHierarchy(startDate, endDate, kpiId, stageIds);
    }

    /**
     * Возвращает данные для фильтра "Этап".
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "Тип заявки"
     * @return
     */
    public BaseEntity[] getStages(LocalDate startDate, LocalDate endDate, long kpiId){
        return LinqWrapper.from(getStageHierarchy(startDate, endDate, kpiId, null))
                .select(new Selector<StageHierarchyDto, BaseEntity>() {
                    @Override
                    public BaseEntity select(StageHierarchyDto dto) {
                        return new BaseEntity(dto.stageId, dto.stageName);
                    }
                })
                .distinct()
                .toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Бизнес-процесс".
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "Тип заявки"
     * @param stageIds Значения фильтра "Этап"
     * @return
     */
    public BaseEntity[] getBpKinds(LocalDate startDate, LocalDate endDate, long kpiId, long[] stageIds){
        return LinqWrapper.from(getStageHierarchy(startDate, endDate, kpiId, stageIds))
                .select(new Selector<StageHierarchyDto, BaseEntity>() {
                    @Override
                    public BaseEntity select(StageHierarchyDto dto) {
                        return new BaseEntity(dto.bpKindId, dto.bpKindName);
                    }
                })
                .distinct()
                .toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Период агрегации".
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает данные для фильтра "Тип продукта".
     * @return
     */
    public BaseEntity[] getProductTypes(long kpiId) {
        return mapper.getProductTypes(kpiId);
    }

    private DeptHierarchyDto[] getDeptHierarchy() {
        return mapper.getDeptHierarchy();
    }

    /**
     * Возвращает данные для фильтра "Функциональная группа".
     * @return
     */
    public BaseEntity[] getFuncGroups() {
        return mapper.getFuncGroups();
    }
}
