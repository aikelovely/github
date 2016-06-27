package ru.alfabank.dmpr.repository.zp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.zp.ZPInstitutionMapper;
import ru.alfabank.dmpr.mapper.zp.ZPOpeningSpeedMapper;
import ru.alfabank.dmpr.model.zp.*;

/**
 * Репозиторий, отвечающий за загрузку данных для графиков витрины "Заведение зарплатного проекта".
 */
@Repository
public class ZPInstitutionRepository {
    @Autowired
    ZPInstitutionMapper mapper;

    /**
     * Возвращает данные для динамики.
     * @param options Текущие значения фильтров
     * @return
     */
    public ZPKPIDataItem[] getDynamic(ZPInstitutionOptions options) {
        return mapper.getDynamic(options);
    }

    /**
     * Возвращает данные для мини-графиков в виде плиток.
     * @param options Текущие значения фильтров
     * @return
     */
    public ZPTile[] getTiles(ZPInstitutionOptions options) {
        return mapper.getTiles(options);
    }
}
