package ru.alfabank.dmpr.model.ctq;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.chart.Point;

public class CTQRichPoint extends Point {
    public long periodId;
    public int periodNum;
    public String periodName;

    public CTQRichPoint(LocalDate calcDate, double y) {
        super(calcDate, y);
    }
}
