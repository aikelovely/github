package ru.alfabank.dmpr.repository.cr;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.cr.CRFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;

/**
 * Репозиторий для загрузки фильтров витрины "Корпоративный бизнес".
 */
@Repository
public class CRFilterRepository {
    @Autowired
    CRFilterMapper mapper;

    /**
     * Возвращает данные для фильтра "ЦО/РП".
     * @return
     */
    public BaseEntity[] getBLTypes() {
        return mapper.getBLTypes();
    }

    /**
     * Возвращает данные для фильтра "Процесс".
     * @return
     */
    public BaseEntity[] getProcesses() {
        return mapper.getProcesses();
    }

    /**
     * Возвращает данные для фильтра "Уровень клиента".
     * @return
     */
    public BaseEntity[] getClientLevels() {
        return mapper.getClientLevels();
    }

    /**
     * Возвращает данные для фильтра "Тип показателя"
     * @return
     */
    public BaseEntity[] getBPTypes() {
        return mapper.getBPTypes();
    }

    /**
     * Возвращает данные для фильтра "Уполномоченный орган".
     * @return
     */
    public ChildEntity[] getAllCommittees() {
        return mapper.getCommittees();
    }

    /**
     * Возвращает данные для фильтра "Уполномоченный орган".
     * @param processIds Значения фильтра "Процесс"
     * @return
     */
    public BaseEntity[] getCommittees(final long[] processIds) {
        ChildEntity[] result = getAllCommittees();

        if(processIds != null && processIds.length > 0){
            result = LinqWrapper.from(result).filter(new Predicate<ChildEntity>() {
                @Override
                public boolean check(ChildEntity item) {
                    return ArrayUtils.contains(processIds, item.parentId);
                }
            }).toArray(ChildEntity.class);
        }

        return BaseEntity.toBaseEntities(result);
    }

    /**
     * Возвращает данные для фильтра "Доп. офис".
     * @return
     */
    public ChildEntity[] getAllDopOffices() {
        return mapper.getDopOffices();
    }

    /**
     * Возвращает данные для фильтра "Доп. офис".
     * @param blIds Значения фильтра "Куст"
     * @return
     */
    public BaseEntity[] getDopOffices(final long[] blIds) {
        ChildEntity[] result = getAllDopOffices();

        if(blIds != null && blIds.length > 0){
            result = LinqWrapper.from(result).filter(new Predicate<ChildEntity>() {
                @Override
                public boolean check(ChildEntity item) {
                    return ArrayUtils.contains(blIds, item.parentId);
                }
            }).toArray(ChildEntity.class);
        }

        return BaseEntity.toBaseEntities(result);
    }

    /**
     * Возвращает данные для фильтра "Куст".
     * @return
     */
    public ChildEntity[] getAllBLs() {
        return mapper.getBLs();
    }

    /**
     * Возвращает данные для фильтра "Куст".
     * @param blTypeIds Значения фильтра "ЦО/РП"
     * @return
     */
    public BaseEntity[] getBLs(final long[] blTypeIds) {
        ChildEntity[] result = getAllBLs();

        if(blTypeIds != null && blTypeIds.length > 0){
            result = LinqWrapper.from(result).filter(new Predicate<ChildEntity>() {
                @Override
                public boolean check(ChildEntity item) {
                    return ArrayUtils.contains(blTypeIds, item.parentId);
                }
            }).toArray(ChildEntity.class);
        }

        return BaseEntity.toBaseEntities(result);
    }

    public BaseEntity[] getSystemUnits(){
        BaseEntity[] result = new BaseEntity[4];

        result[0] = new BaseEntity(1, "Банк");
        result[1] = new BaseEntity(2, "Регион");
        result[2] = new BaseEntity(3, "Куст");
        result[3] = new BaseEntity(4, "Доп. офис");

        return result;
    }

    public BaseEntity[] getProductTypes(long bpTypeId) {
        return mapper.getProductTypes(bpTypeId);
    }
}
