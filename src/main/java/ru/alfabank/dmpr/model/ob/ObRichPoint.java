package ru.alfabank.dmpr.model.ob;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.chart.Point;

public class ObRichPoint extends Point {
    public long periodId;
    public int periodNum;
    public String periodName;

    public LocalDate dateTick;

    public ObRichPoint(LocalDate calcDate, double y) {
        super(calcDate, y);
        dateTick = calcDate;
    }
}
