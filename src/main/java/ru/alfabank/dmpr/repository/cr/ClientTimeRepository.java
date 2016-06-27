package ru.alfabank.dmpr.repository.cr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.cr.ClientTimeMapper;
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDataItem;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDetailsDataItem;

/**
 * Репозиторий для отображения графиков витрины "Декомпозиция процессов"
 */
@Repository
public class ClientTimeRepository {
    @Autowired
    ClientTimeMapper mapper;

    /**
     * Основной метод, возвращающий все необходимые данные.
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDataItem[] getKpiData(ClientTimeOptions options) {
        return mapper.getKpiData(options);
    }

    /**
     * Дополнительный метод, возвращающий необходимые данные для дет. выгрузки
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDetailsDataItem[] getKpiDetailsData(ClientTimeOptions options) {
        return mapper.getKpiDetailsData(options);
    }
}
