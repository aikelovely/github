package ru.alfabank.dmpr.model.workload;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.Week;

public class WorkloadRichPoint extends Point {
    public long periodId;
    public int periodNum;
    public String periodName;

    public WorkloadRichPoint(LocalDate calcDate, double y) {
        super(calcDate, y);
    }

    public WorkloadRichPoint(Point p, WorkloadDynamicItem item, Week[] weeks) {
        super(p.x, p.y);
        PeriodSelectOption pSOptions = PeriodSelectHelper.getWeekByYearAndNum(item.timeUnitYear, item.timeUnitPrdNum, weeks);
        periodName = pSOptions.name;
        periodNum = pSOptions.periodNum;
        periodId = pSOptions.id;
    }
}
