package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.model.zp.ZPQualityDynamic;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;

/**
 * График "Динамика качества заполнения CRM"
 */
@Service
public class ZPOpeningSpeedCrmFillQualityDynamic extends BaseChart<ZPOpeningSpeedOptions> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedCrmFillQualityDynamic() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public ChartResult[] getData(ZPOpeningSpeedOptions options) {
        LinqWrapper<ZPQualityDynamic> data = LinqWrapper.from(repository.getCrmFillQualityDynamic(options))
                .sort(new Selector<ZPQualityDynamic, Comparable>() {
                    @Override
                    public Comparable select(ZPQualityDynamic item) {
                        return item.calcDate;
                    }
                });

        if (data.count() == 0) return new ChartResult[]{new ChartResult(null)};

        final ArrayList<Point> firstStagePoints = new ArrayList<>();
        final ArrayList<Point> secondStagePoints = new ArrayList<>();
        final ArrayList<Point> allStagesPoints = new ArrayList<>();

        data.each(new Action<ZPQualityDynamic>() {
            @Override
            public void act(ZPQualityDynamic item) {
                firstStagePoints.add(new Point(item.calcDate, item.firstStageValue));
                secondStagePoints.add(new Point(item.calcDate, item.secondStageValue));
                allStagesPoints.add(new Point(item.calcDate, item.totalValue));
            }
        });

        Series[] series = {
            new Series("Заведение ЮЛ", firstStagePoints.toArray(new Point[firstStagePoints.size()])),
            new Series("Открытие счета 1-му ФЛ", secondStagePoints.toArray(new Point[secondStagePoints.size()])),
            new Series("Все этапы", allStagesPoints.toArray(new Point[allStagesPoints.size()]))
        };

        return new ChartResult[]{new ChartResult(series)};
    }
}
