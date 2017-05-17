package ru.alfabank.dmpr.repository.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.leaderBoard.LeaderBoardMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.leaderBoard.ChartMetaData;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.model.leaderBoard.ReportDashboardQualityBo;
import ru.alfabank.dmpr.model.leaderBoard.Kpi5DescriptionData;

/**
 * Репозиторий для графиков витрины КПЭ ОБ.
 */
@Repository
public class LeaderBoardRepository {
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private LeaderBoardMapper mapper;

    @Autowired
    private LeaderBoardFilterRepository filterRepository;

    /**
     * Возвращает описание всех KPI
     * @return Список KPI
     */
    public BaseEntityWithCode[] getKPIs() {
        return mapper.getKPIs();
    }

    /**
     * Возвращает описание KPI по его коду
     * @param code Код KPI
     * @return KPI
     */
    public BaseEntityWithCode getKpiByCode(final String code) {
        BaseEntityWithCode kpi = LinqWrapper.from(getKPIs()).firstOrNull(new Predicate<BaseEntityWithCode>() {
            @Override
            public boolean check(BaseEntityWithCode item) {
                return item.code.equalsIgnoreCase(code);
            }
        });

        return kpi != null ? kpi : new BaseEntityWithCode();
    }

    /**
     * Основная функция. Возвращает данные для графиков.
     * @param options Список параметров
     * @param kpiCodes Список кодов KPI
     * @return KpiDataItem[]
     */
    public KpiDataItem[] getKpiData(LeaderBoardOptions options, String... kpiCodes){
        setOptions(options, kpiCodes);
        return mapper.getKpiData(options);
    }

    /**
     * Возвращает данные за последний месяц. Используется для kpi 23 и 24
     * @param options Список параметров
     * @param kpiCodes Список кодов KPI
     * @return
     */
    public KpiDataItem[] getKpiDataLastFact(LeaderBoardOptions options, String... kpiCodes){
        setOptions(options, kpiCodes);
        return mapper.getKpiDataLastFact(options);
    }

    public KpiDataItem[] getKpiDataForBlockChart(LeaderBoardOptions options){
        return mapper.getKpiData(options);
    }

    public Kpi5DescriptionData[] getKpi5DescriptionData(LeaderBoardOptions options)
    {
        return mapper.getKpi5DescriptionData(options);
    }
    public ChartMetaData[] getLayout(LeaderBoardOptions options) {
        return mapper.getLayout(options);
    }
    public ReportDashboardQualityBo[] getqualitybo() {return mapper.getqualitybo();}
    private void setOptions(LeaderBoardOptions options, String... kpiCodes) {
        if(options.divisionGroupId == null){
            BaseEntity allDivision = filterRepository.getDivisionGroupByCode(options.startDate, options.endDate, "ОБ");
            options.divisionGroupId = allDivision == null ? null : allDivision.id;
        }

        if(options.endDate == null){
            options.endDate = options.startDate.plusYears(1).plusDays(-1);
        }

        options.kpiIds = new long[kpiCodes.length];
        for(int i = 0; i < kpiCodes.length; i++){
            options.kpiIds[i] = getKpiByCode(kpiCodes[i]).id;
        }
    }
}
