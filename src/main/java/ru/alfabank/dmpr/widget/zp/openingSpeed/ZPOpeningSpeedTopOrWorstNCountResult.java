package ru.alfabank.dmpr.widget.zp.openingSpeed;


import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Данные для отображения рейтинга лучших/худших.
 */
public class ZPOpeningSpeedTopOrWorstNCountResult {
    public List<ZPKPIDataItem> rows;
    public boolean isTop;

    public ZPOpeningSpeedTopOrWorstNCountResult(List<ZPKPIDataItem> rows, boolean isTop) {
        this.rows = new ArrayList<>(rows);
        this.isTop = isTop;
    }
}
