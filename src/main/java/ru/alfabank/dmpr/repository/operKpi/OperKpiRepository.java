package ru.alfabank.dmpr.repository.operKpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.mapper.operKpi.OperKpiMapper;
import ru.alfabank.dmpr.model.operKpi.*;

/**
 * Репозиторий для витрин Отчет по выполнению KPI
 */
@Repository
public class OperKpiRepository {
    @Autowired
    OperKpiMapper mapper;

    /**
     * Возвращает данные для таблицы Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiValue[] getOperKpiHunterValues(OperKpiOptions options) {
        return mapper.getOperKpiHunterValues(options);
    }

    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на этапе «Hunter – ручные проверки»
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiDetailValue[] getOperKpiHunterDtlValues(OperKpiOptions options) {
        OperKpiDetailValue[] result = mapper.getOperKpiHunterDtlValues(options);
        LinqWrapper.from(result).each(new Action<OperKpiDetailValue>() {
            @Override
            public void act(OperKpiDetailValue value) {
                value.bpOperationDurationAsString = DateHelper.secondsAsString(value.bpOperationDuration);
            }
        });
        return result;
    }

    /**
     * Возвращает данные для таблицы Отчет по выполнению KPI на этапе «Урегулирование КП»
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiValue[] getOperKpiRbpValues(OperKpiOptions options) {
        return mapper.getOperKpiRbpValues(options);
    }

    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на этапе «Урегулирование КП»
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiDetailValue[] getOperKpiRbpDtlValues(OperKpiOptions options) {
        OperKpiDetailValue[] result = mapper.getOperKpiRbpDtlValues(options);
        LinqWrapper.from(result).each(new Action<OperKpiDetailValue>() {
            @Override
            public void act(OperKpiDetailValue value) {
                value.bpOperationDurationAsString = DateHelper.secondsAsString(value.bpOperationDuration);
            }
        });
        return result;
    }

    /**
     * Возвращает данные для отчета по KPI на длительность скорингов за период
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiScorValue[] getOperKpiScorValues(OperKpiOptions options) {
        return mapper.getOperKpiScorValues(options);
    }

    /**
     * Возвращает данные для динамики KPI на длительность скорингов
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiScorDynValue[] getOperKpiScorDynamic(OperKpiOptions options) {
        return mapper.getOperKpiScorDynamic(options);
    }

    /**
     * Возвращает данные для выгрузки Отчет по выполнению KPI на длительность скорингов
     * @param options Текущие значения фильтров
     * @return
     */
    public OperKpiScorDetailValue[] getOperKpiScorDtlValues(OperKpiOptions options) {
        OperKpiScorDetailValue[] result = mapper.getOperKpiScorDtlValues(options);
        LinqWrapper.from(result).each(new Action<OperKpiScorDetailValue>() {
            @Override
            public void act(OperKpiScorDetailValue value) {
                value.bpOperationDurationAsString = DateHelper.secondsAsString(value.bpOperationDuration);
            }
        });
        return result;
    }
}
