package ru.alfabank.dmpr.model.mass.openAccount;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;

public class MassOpenAccountOptions extends BaseOptions implements PeriodOptions {
    public long[] cityIds;
    public long[] salesChannelIds;
    public long[] bpTypeIds;
    public long[] dopOfficeIds;

    public LocalDate startDate;
    public LocalDate endDate;
    public Integer timeUnitId;

    /**
     * Содержит значения фильтра "Единица сети"
     */
    public int[] systemUnitIds;
    public boolean includeOnfInRk;
    public boolean includeDecisionInRk;

    /**
     * Содержит текущий уровень детализации. По-умолчанию - 2 (Канал продаж).
     */
    public int rcbUnitId;

    public String hourIntervals;

    public int topCount;

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
}
