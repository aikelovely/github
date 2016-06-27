package ru.alfabank.dmpr.widget.zp.institution.chart;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.zp.*;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPInstitutionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.*;

/**
 * График - динамика выбранного параметра.
 */
@Service
public class ZPInstitutionDynamic extends BaseChart<ZPInstitutionOptions> {
    @Autowired
    ZPInstitutionRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPInstitutionDynamic() {
        super(ZPInstitutionOptions.class);
    }

    @Override
    public ChartResult[] getData(ZPInstitutionOptions options) {
        LinqWrapper<ZPKPIDataItem> data = LinqWrapper.from(repository.getDynamic(options))
                .sort(new Selector<ZPKPIDataItem, Comparable>() {
                    @Override
                    public Comparable select(ZPKPIDataItem item) {
                        return item.calcDate;
                    }
                });

        if (data.count() == 0) return new ChartResult[]{new ChartResult(null)};

        Map<String, Object> countBag = new HashMap<String, Object>();
        int totalCount = data.sum(new Selector<ZPKPIDataItem, Integer>() {
            @Override
            public Integer select(ZPKPIDataItem item) {
                return item.totalCount;
            }
        });
        countBag.put("total", totalCount);

        Point[] countPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
            @Override
            public Point select(ZPKPIDataItem item) {
                return new Point(item.calcDate, (double)item.totalCount);
            }
        }).toArray(Point.class);

        Point[] secondChartPoints;

        Map<String, Object> secondChartBag = new HashMap<String, Object>();
        String seriesName;
        if(options.paramType == ParamType.AvgDuration){
            secondChartPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
                @Override
                public Point select(ZPKPIDataItem item) {
                    return new Point(item.calcDate, item.avgDuration);
                }
            }).toArray(Point.class);

            double totalDuration  = data.sum(new Selector<ZPKPIDataItem, Double>() {
                @Override
                public Double select(ZPKPIDataItem item) {
                    return item.totalDuration;
                }
            });
            secondChartBag.put("total", MathHelper.safeDivide(totalDuration, totalCount));
            seriesName = "Среднее время заведения проекта";
        }
        else {
            secondChartPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
                @Override
                public Point select(ZPKPIDataItem item) {
                    return new Point(item.calcDate, MathHelper.safeDivide(item.inKpiCount, item.totalCount) * 100);
                }
            }).toArray(Point.class);

            int totalInKpiCount  = data.sum(new Selector<ZPKPIDataItem, Integer>() {
                @Override
                public Integer select(ZPKPIDataItem item) {
                    return item.inKpiCount;
                }
            });
            secondChartBag.put("total", MathHelper.safeDivide(totalInKpiCount, totalCount) * 100);
            seriesName = "% компаний, заведенных в пределах KPI";
        }

        return new ChartResult[]{
                new ChartResult(new Series[]{new Series("Общее количество проектов", countPoints)}, countBag),
                new ChartResult(new Series[]{new Series(seriesName, secondChartPoints)}, secondChartBag)
        };
    }
}
