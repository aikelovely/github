package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseItem;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;

public class ZPOpeningSpeedOptions extends BaseOptions implements PeriodOptions {
    public LocalDate startDate;
    public LocalDate endDate;

    public long[] bushIds;
    public long[] operationOfficeIds;
    public long[] cityIds;
    public String[] managerIds;
    public String[] companyIds;
    public int topCount;
    public long[] schemaTypeIds;
    public int openingTypeId;
    public long[] processStageIds;
    public Long subProcessStageId;
    public ParamType paramType;
    public Integer timeUnitId;
    public int systemUnitId;
    public Integer withClientWait;

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

