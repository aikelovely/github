package ru.alfabank.dmpr.mapper.operKpi;

import ru.alfabank.dmpr.model.operKpi.*;

/**
 * Маппер, отвечающий за загрузку данных для витрин отчет по выполнению KPI.
 */
public interface OperKpiMapper {
    /**
     * Возвращает данные для таблицы Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiValue[] getOperKpiHunterValues(OperKpiOptions options);

    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiDetailValue[] getOperKpiHunterDtlValues(OperKpiOptions options);

    /**
     * Возвращает данные для таблицы Отчет по выполнению KPI на этапе «Урегулирование КП»
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiValue[] getOperKpiRbpValues(OperKpiOptions options);


    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на этапе «Урегулирование КП»
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiDetailValue[] getOperKpiRbpDtlValues(OperKpiOptions options);

    /**
     * Возвращает данные для отчета по KPI на длительность скорингов за период
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiScorValue[] getOperKpiScorValues(OperKpiOptions options);

    /**
     * Возвращает данные для динамики KPI на длительность скорингов
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiScorDynValue[] getOperKpiScorDynamic(OperKpiOptions options);

    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на длительность скорингов
     * @param options Текущие значения фильтров
     * @return
     */
    OperKpiScorDetailValue[] getOperKpiScorDtlValues(OperKpiOptions options);
}
