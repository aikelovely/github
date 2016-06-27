package ru.alfabank.dmpr.model.operKpi;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Набор значений фильтров витрин отчет по выполнению KPI.
 */
public class OperKpiOptions extends BaseOptions implements PeriodOptions {
    /**
     * Значеиие фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значеиие фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значеиие фильтра "Система-источник"
     */
    public long[] sourceSystemIds;

    /**
     * Значеиие фильтра "Период агрегации". Используется только на витрине
     * "Отчет по выполнению KPI на этапе «Автоматический скоринг»"
     */
    public int timeUnitId;

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public Integer getTimeUnitId() {
        return timeUnitId;
    }

    /**
     * Значение фильтра "Учет проверок по 7 полям"
     */
    public long sevenFieldsCheckId;

    /**
     * Значение фильтра "Учет проверок по 7 полям" (код)
     */
    public String sevenFieldsCheckCode;
}
