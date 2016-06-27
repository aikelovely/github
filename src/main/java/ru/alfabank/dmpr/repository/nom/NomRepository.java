package ru.alfabank.dmpr.repository.nom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.nom.NomMapper;
import ru.alfabank.dmpr.model.ChildEntityWithCode;
import ru.alfabank.dmpr.model.nom.NomDetailsReportRow;
import ru.alfabank.dmpr.model.nom.NomKpiDataItem;
import ru.alfabank.dmpr.model.nom.NomQueryOptions;

/**
 * Репозиторий для фильтров витрины "Количество конечных продуктов для расчета UC ОБ"
 */
@Repository
public class NomRepository {
    @Autowired
    private NomMapper mapper;

    /*
     * Возращает список конечных продуктов
     * @return
     */
    public ChildEntityWithCode[] getInnerEndProducts() {
        return mapper.getInnerEndProducts();
    }

    /*
     * Функция для получений данных для выгрузки
     * @param options Параметры фильтров
     * @return
     */
    public NomDetailsReportRow[] getReport(NomQueryOptions options) {
        return mapper.getReport(options);
    }

    /*
     * Функция для получения данных для графиков
     * @param options Параметры фильтров
     * @return
     */
    public NomKpiDataItem[] getKpiData(NomQueryOptions options) {
        return mapper.getKpiData(options);
    }
}
