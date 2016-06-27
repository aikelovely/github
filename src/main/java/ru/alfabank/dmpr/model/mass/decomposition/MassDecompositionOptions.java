package ru.alfabank.dmpr.model.mass.decomposition;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;

public class MassDecompositionOptions extends BaseOptions implements PeriodOptions {
    public long[] cityIds;
    public long[] salesChannelIds;
    public long[] dopOfficeIds;

    public LocalDate startDate;
    public LocalDate endDate;
    public Integer timeUnitId;

    public int systemUnitId;

    public int stageDetalization;

    public Integer stageId;

    public Double durationLimitHours;

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
