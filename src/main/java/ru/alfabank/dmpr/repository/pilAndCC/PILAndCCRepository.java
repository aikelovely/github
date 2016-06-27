package ru.alfabank.dmpr.repository.pilAndCC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.mapper.pilAndCC.PILAndCCMapper;
import ru.alfabank.dmpr.model.pilAndCC.*;

/**
 * Репозиторий, отвечающий за загрузку данных для графиков витрины "Показатели качества ОБ".
 */
@Repository
public class PILAndCCRepository {
    @Autowired
    private PILAndCCMapper mapper;

    /**
     * Возвращает данные для верхней динамики
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDataItem[] getAvgDynamic(PILAndCCOptions options) {
        return mapper.getAvgDynamic(options);
    }

    /**
     * Возвращает данные для динамики "Распределение заявок по группам длительности"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDataItem[] getRequestByGroup(PILAndCCOptions options) {
        return mapper.getRequestByGroup(options);
    }

    /**
     * Возвращает данные для рейтинга "Топ N худших/лучших"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiRating[] getRating(PILAndCCOptions options) {
        return mapper.getRating(options);
    }

    /**
     * Возвращает данные для графика "Динамика изменения доли каждого этапа в процессе"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDurationDynamic[] getTimeRatioDynamic(PILAndCCOptions options) {
        return mapper.getTimeRatioDynamic(options);
    }

    /**
     * Возвращает данные для графика "% активированных карт"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDataItem[] getCardActivityDynamic(PILAndCCOptions options){
        return mapper.getCardActivityDynamic(options);
    }

    /**
     * Возвращает данные для кнопок-переключателей выбранного этапа.
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiStageInfo[] getStageInfo(PILAndCCOptions options){
        return mapper.getStageInfo(options);
    }

    /**
     * Возвращает данные для динамики по выбранному этапу
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDynamicByStage[] getDynamicByStage(PILAndCCOptions options){
        return mapper.getDynamicByStage(options);
    }

    /**
     * Возвращает данные для графика "Процент дооформлений УИУК" / "Процент дооформлений (после проверки на конвейере)"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiAfterDrawDynamic[] getAfterDrawDynamic(PILAndCCOptions options){
        return mapper.getAfterDrawDynamic(options);
    }

    /**
     * Возвращает данные для графика "Доля клиентских отказов"
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDataItem[] getRejectDynamic(PILAndCCOptions options){
        return mapper.getRejectDynamic(options);
    }

    /**
     * Возвращает данные для отображения коэффициента повторяемости этапов.
     * @param options Текущие значения фильтров
     * @return
     */
    public KpiDynamicByStage[] getStagesRepeatKoeff(PILAndCCOptions options) {
        return mapper.getStagesRepeatKoeff(options);
    }

    public ReportRow[] getKpiDataReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getKpiDataReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }

    public ReportRow[] getCardActivateReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getCardActivateReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }

    public ReportRow[] getKpiDetailDataСsvReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getKpiDetailDataСsvReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }

    public ReportRow[] getOperDetailDataReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getOperDetailDataReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }

    public ReportRow[] getOperDetailDataCsvReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getOperDetailDataCsvReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }

    public ReportRow[] getAfterCheckReport(PILAndCCOptions options) {
        ReportRow[] rows = mapper.getAfterCheckReport(options);
        LinqWrapper.from(rows).each(new Action<ReportRow>() {
            @Override
            public void act(ReportRow value) {
                value.fillDurationFields();
            }
        });
        return rows;
    }
}
