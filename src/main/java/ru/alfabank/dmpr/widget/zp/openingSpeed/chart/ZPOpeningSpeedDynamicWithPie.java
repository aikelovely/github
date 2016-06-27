package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.zp.City;
import ru.alfabank.dmpr.model.zp.ProcessStage;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График Динамика средней скорости / доли проектов в пределах KPI.
 */
@Service
public class ZPOpeningSpeedDynamicWithPie extends BaseChart<ZPOpeningSpeedOptions> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedDynamicWithPie() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public ChartResult[] getData(ZPOpeningSpeedOptions options) {
        LinqWrapper<ZPKPIDataItem> data = LinqWrapper.from(repository.getDynamic(options))
                .sort(new Selector<ZPKPIDataItem, Comparable>() {
                    @Override
                    public Comparable select(ZPKPIDataItem dataItem) {
                        return dataItem.calcDate;
                    }
                });

        Map<String, Object> bag = new HashMap<String, Object>();

        bag.put("title", getKpiTitleString(options, data));

        if (data.count() == 0) return new ChartResult[]{new ChartResult(null, bag)};

        ChartResult result = options.paramType == ParamType.AvgDuration
                ? createAvgDuration(data, options, bag)
                : createPercent(data, options, bag);


        return new ChartResult[]{result};
    }

    private ChartResult createAvgDuration(LinqWrapper<ZPKPIDataItem> data,
                                          ZPOpeningSpeedOptions options,
                                          Map<String, Object> bag) {

        class RichPoint extends Point {
            public Integer totalCwCount;

            public RichPoint(ZPKPIDataItem item) {
                super(item.calcDate, item.avgDuration);

                this.color = this.y >= item.kpiNorm
                        ? Color.RedColor
                        : Color.GreenColor;

                this.totalCwCount = item.totalCwCount;
            }
        }

        Point[] columnPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
            @Override
            public Point select(ZPKPIDataItem item) {
                return new RichPoint(item);
            }
        }).toArray(Point.class);


        // Pie
        int totalInKpi = data.sum(new Selector<ZPKPIDataItem, Integer>() {
            @Override
            public Integer select(ZPKPIDataItem dataItem) {
                return dataItem.inKpiCount;
            }
        });
        int totalNotInKpi = data.sum(new Selector<ZPKPIDataItem, Integer>() {
            @Override
            public Integer select(ZPKPIDataItem dataItem) {
                return dataItem.totalCount;
            }
        }) - totalInKpi;

        double avgDuration = data.sum(new Selector<ZPKPIDataItem, Double>() {
            @Override
            public Double select(ZPKPIDataItem dataItem) {
                return dataItem.avgDuration; // TODO: Тут точно нужна AvgDuration?
            }
        }) / data.count();


        bag.put("kpiValue", data.first().kpiNorm);
        bag.put("averageValue", avgDuration);

        return new ChartResult(new Series[]{
                new Series("Длительность открытия проекта", columnPoints, ChartType.column),
                createPie(totalInKpi, totalNotInKpi),
                TrendLine.create(columnPoints, null)
        }, bag);
    }

    private ChartResult createPercent(LinqWrapper<ZPKPIDataItem> data,
                                      ZPOpeningSpeedOptions options,
                                      Map<String, Object> bag) {
        final double inKpiThreshold = repository.getInKPIThreshold(options);

        class RichPoint extends Point {
            public Integer inKpiCount;
            public Integer outKpiCount;
            public double outKpiPercent;

            public RichPoint(ZPKPIDataItem item) {
                super(item.calcDate, MathHelper.safeDivide(item.inKpiCount, item.totalCount) * 100);

                this.color = this.y >= inKpiThreshold
                        ? Color.GreenColor
                        : this.y >= ZPOpeningSpeedRepository.warningThreshold * 100
                        ? Color.OrangeColor
                        : Color.RedColor;

                this.inKpiCount = item.inKpiCount;
                this.outKpiCount = item.totalCount - item.inKpiCount;
                this.outKpiPercent = MathHelper.safeDivide(this.outKpiCount, item.totalCount) * 100;
            }
        }

        Point[] columnPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
            @Override
            public Point select(ZPKPIDataItem item) {
                return new RichPoint(item);
            }
        }).toArray(Point.class);

        // Pie
        int totalInKpi = data.sum(new Selector<ZPKPIDataItem, Integer>() {
            @Override
            public Integer select(ZPKPIDataItem dataItem) {
                return dataItem.inKpiCount;
            }
        });
        int totalNotInKpi = data.sum(new Selector<ZPKPIDataItem, Integer>() {
            @Override
            public Integer select(ZPKPIDataItem dataItem) {
                return dataItem.totalCount;
            }
        }) - totalInKpi;


        bag.put("kpiValue", inKpiThreshold);
        bag.put("averageValue", data.first().inKpiRatioAvg);

        return new ChartResult(new Series[]{
                new Series("В KPI", columnPoints, ChartType.column),
                createPie(totalInKpi, totalNotInKpi),
                TrendLine.create(columnPoints, 100d)
        }, bag);
    }

    private Series createPie(int totalInKpi, int totalNotInKpi) {
        return new Series("Pie", new Point[]{
                Point.withY(totalInKpi, "В KPI", Color.GreenColor),
                Point.withY(totalNotInKpi, "За пределами KPI", Color.RedColor)
        }, ChartType.pie);
    }



    private String getKpiTitleString(final ZPOpeningSpeedOptions options, LinqWrapper<ZPKPIDataItem> data) {
        if (options.openingTypeId == 2) return "Дооткрытие";

        StringBuilder title = new StringBuilder();
        if (options.processStageIds != null && options.processStageIds.length == 0
                || ArrayUtils.contains(options.processStageIds, filterRepository.getProcessStageIdByCode(1))
                || ArrayUtils.contains(options.processStageIds, filterRepository.getProcessStageIdByCode(2))) {

            title.append("Заведение ЮЛ (от получения комплекта документов для прохождения СЭБ до активации компании в АЗОН) + <br/>" +
                    "Открытие счета 1-му ФЛ (от активации компании в АЗОН до открытия счета 1-му ФЛ)<br/>");
        }

        if (options.processStageIds != null && options.processStageIds.length == 0
                || ArrayUtils.contains(options.processStageIds, filterRepository.getProcessStageIdByCode(3))) {
            boolean useMoscowKpi = false;

            if (options.cityIds.length > 0 && options.cityIds.length > 0) {
                useMoscowKpi = LinqWrapper.from(filterRepository.getAllCities(options.openingTypeId))
                        .any(new Predicate<City>() {
                            @Override
                            public boolean check(final City item) {
                                return ArrayUtils.contains(options.cityIds, item.id) &&
                                        item.regionName.equals("Регионы");
                            }
                        });
            }

            title.append("Выдача карты ФЛ");
        }

        if (options.withClientWait != null && options.withClientWait == 0) // "Без клиентского времени"
        {
            Integer totalCwCount = data.sum(new Selector<ZPKPIDataItem, Integer>() {
                @Override
                public Integer select(ZPKPIDataItem item) {
                    return item.totalCwCount;
                }
            });

            title.append(String.format("<br/>Клиентское время выделено для %d проектов", totalCwCount));
        }

        return title.toString();
    }
}
