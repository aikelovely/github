package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.widget.zp.openingSpeed.ZPOpeningSpeedTopOrWorstNCountResult;

import java.util.*;

/**
 * График "Топ N лучших/худших".
 */
@Service
public class ZPOpeningSpeedTopOrWorstNCount extends BaseWidget<ZPOpeningSpeedOptions,
        ZPOpeningSpeedTopOrWorstNCountResult[]> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    public ZPOpeningSpeedTopOrWorstNCount() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public ZPOpeningSpeedTopOrWorstNCountResult[] getData(ZPOpeningSpeedOptions options) {
        ZPKPIDataItem[] data = repository.getRating(options);
        List<ZPKPIDataItem> sortedData = Arrays.asList(data);

        if (options.paramType == ParamType.AvgDuration) {
            Collections.sort(sortedData, new Comparator<ZPKPIDataItem>() {
                @Override
                public int compare(ZPKPIDataItem i1, ZPKPIDataItem i2) {
                    return i1.avgDuration.compareTo(i2.avgDuration);
                }
            });
        } else {
            Collections.sort(sortedData, new Comparator<ZPKPIDataItem>() {
                @Override
                public int compare(ZPKPIDataItem i1, ZPKPIDataItem i2) {
                    int result = -i1.inKpiRatioAvg.compareTo(i2.inKpiRatioAvg);

                    if (result != 0) return result;

                    return i1.avgDuration.compareTo(i2.avgDuration);
                }
            });
        }

        ZPOpeningSpeedTopOrWorstNCountResult top = new ZPOpeningSpeedTopOrWorstNCountResult(
                sortedData.size() >= options.topCount ? sortedData.subList(0, options.topCount) : sortedData,
                true
        );

        Collections.reverse(sortedData);

        ZPOpeningSpeedTopOrWorstNCountResult worst = new ZPOpeningSpeedTopOrWorstNCountResult(
                sortedData.size() >= options.topCount ? sortedData.subList(0, options.topCount) : sortedData,
                false
        );

        return new ZPOpeningSpeedTopOrWorstNCountResult[]{
            top, worst
        };
    }
}
