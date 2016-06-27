package ru.alfabank.dmpr.mapper.pilAndCC;

import ru.alfabank.dmpr.model.pilAndCC.*;

/**
 * Маппер, отвечающий за загрузку данных для графиков витрины "Персональные кредиты и кредитные карты. Витрина декомпозиции".
 */
public interface PILAndCCMapper {
    /**
     * Возвращает данные для верхней динамики
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDataItem[] getAvgDynamic(PILAndCCOptions options);

    /**
     * Возвращает данные для динамики "Распределение заявок по группам длительности"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDataItem[] getRequestByGroup(PILAndCCOptions options);

    /**
     * Возвращает данные для рейтинга "Топ N худших/лучших"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiRating[] getRating(PILAndCCOptions options);

    /**
     * Возвращает данные для графика "Динамика изменения доли каждого этапа в процессе"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDurationDynamic[] getTimeRatioDynamic(PILAndCCOptions options);

    /**
     * Возвращает данные для графика "% активированных карт"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDataItem[] getCardActivityDynamic(PILAndCCOptions options);

    /**
     * Возвращает данные для кнопок-переключателей выбранного этапа.
     * @param options Текущие значения фильтров
     * @return
     */
    KpiStageInfo[] getStageInfo(PILAndCCOptions options);

    /**
     * Возвращает данные для динамики по выбранному этапу
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDynamicByStage[] getDynamicByStage(PILAndCCOptions options);

    /**
     * Возвращает данные для графика "Процент дооформлений УИУК" / "Процент дооформлений (после проверки на конвейере)"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiAfterDrawDynamic[] getAfterDrawDynamic(PILAndCCOptions options);

    /**
     * Возвращает данные для графика "Доля клиентских отказов"
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDataItem[] getRejectDynamic(PILAndCCOptions options);

    /**
     * Возвращает данные для отображения коэффициента повторяемости этапов.
     * @param options Текущие значения фильтров
     * @return
     */
    KpiDynamicByStage[] getStagesRepeatKoeff(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по заявкам (CSV)
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getKpiDetailDataСsvReport(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по этапам (CSV)
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getOperDetailDataCsvReport(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по KPI
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getKpiDataReport(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по этапам (Excel)
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getOperDetailDataReport(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по проценту активации карт
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getCardActivateReport(PILAndCCOptions options);

    /**
     * Возвращает данные для отчета по проценту дооформлений
     * @param options Текущие значения фильтров
     * @return
     */
    ReportRow[] getAfterCheckReport(PILAndCCOptions options);
}
