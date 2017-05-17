package ru.alfabank.dmpr.mapper.leaderBoard;

import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.leaderBoard.*;

/**
 * MyBatis mapper. Используется для загрузки данных для графиков витрины КПЭ ОБ.
 */
public interface LeaderBoardMapper {
    /**
     * Получает описание всех KPI
     * @return список KPI
     */
    BaseEntityWithCode[] getKPIs();

    /**
     * Основная функция. Возвращает данные для графиков.
     * @param options Список параметров
     * @return KpiDataItem[]
     */
    KpiDataItem[] getKpiData(LeaderBoardOptions options);
    Kpi5DescriptionData[] getKpi5DescriptionData(LeaderBoardOptions options);
    /**
     * Возвращает данные за последний месяц. Используется для kpi 23 и 24
     * @param options Список параметров
     * @return
     */
    KpiDataItem[] getKpiDataLastFact(LeaderBoardOptions options);

    ChartMetaData[] getLayout(LeaderBoardOptions options);

    ReportDashboardQualityBo[] getqualitybo();

}
