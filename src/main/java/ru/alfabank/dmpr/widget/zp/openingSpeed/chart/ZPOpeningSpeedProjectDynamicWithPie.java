package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.zp.*;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseChart;

/**
 * График "Доля компаний с данными CRM".
 */
@Service
public class ZPOpeningSpeedProjectDynamicWithPie extends BaseChart<ZPOpeningSpeedOptions> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedProjectDynamicWithPie() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public ChartResult[] getData(ZPOpeningSpeedOptions options) {
        LinqWrapper<ZPProjectDynamic> data = LinqWrapper.from(repository.getProjectDynamic(options))
                .sort(new Selector<ZPProjectDynamic, Comparable>() {
                    @Override
                    public Comparable select(ZPProjectDynamic item) {
                        return item.calcDate;
                    }
                });

        class RichPoint extends Point {
            public Integer startedOkCount;
            public Integer companyCount;

            public RichPoint(ZPProjectDynamic item) {
                super(item.calcDate, MathHelper.safeDivide(item.startedOkCount, item.companyCount)* 100);

                this.startedOkCount = item.startedOkCount;
                this.companyCount = item.companyCount;
            }
        }

        Point[] columnPoints = data.select(new Selector<ZPProjectDynamic, Point>() {
            @Override
            public Point select(ZPProjectDynamic item) {
                return new RichPoint(item);
            }
        }).toArray(Point.class);

        Series column = new Series("Доля компаний с данными CRM", columnPoints, ChartType.column);

        int totalOkCount = data.sum(new Selector<ZPProjectDynamic, Integer>() {
            @Override
            public Integer select(ZPProjectDynamic item) {
                if(item == null) return 0;
                return item.startedOkCount;
            }
        });

        int totalNotOkCount = data.sum(new Selector<ZPProjectDynamic, Integer>() {
            @Override
            public Integer select(ZPProjectDynamic item) {
                return item.companyCount;
            }
        }) - totalOkCount;

        Series pie = new Series("Pie", new Point[]{
                Point.withY(totalOkCount, "С данными CRM", Color.GreenColor),
                Point.withY(totalNotOkCount, "Без данных CRM", Color.RedColor)
        }, ChartType.pie);

        ChartResult result = new ChartResult(new Series[]{column, pie});
        return new ChartResult[]{result};
    }

}
