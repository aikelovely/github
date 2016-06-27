package ru.alfabank.dmpr.model.zp;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseItem;

/**
 * Набор данных для отображения графика "Количество компаний в АЗОН"
 */
public class ZPQualityDynamic extends BaseItem {
    public double totalValue;
    public double firstStageValue;
    public double secondStageValue;
    public LocalDate calcDate;
}
