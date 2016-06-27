package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * График "Средняя скорость" / "Доля проектов в пределах в KPI".
 */
@Service
public class ZPOpeningSpeedRating extends BaseChart<ZPOpeningSpeedOptions> {
    private final int MaxRatingRowCount = 400;

    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedRating() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public ChartResult[] getData(ZPOpeningSpeedOptions options) {
        LinqWrapper<ZPKPIDataItem> data = LinqWrapper.from(repository.getRating(options))
                .sort(new Selector<ZPKPIDataItem, Comparable>() {
                    @Override
                    public Comparable select(ZPKPIDataItem dataItem) {
                        return dataItem.calcDate;
                    }
                });

        data.each(new Action<ZPKPIDataItem>() {
            @Override
            public void act(ZPKPIDataItem item) {
                if (item.unitName == null) return;
                item.unitName = item.unitName.replace("'", "\\'");
            }
        });

        ChartResult result = options.paramType == ParamType.AvgDuration
                ? createAvgDuration(data, options)
                : createPercent(data, options);


        return new ChartResult[]{result};
    }

    private String formatCompanyTooltip(ZPKPIDataItem item, ZPOpeningSpeedOptions options) {
        final String unknown = "N/A";

        StringBuilder text = new StringBuilder();

        if (options.processStageIds.length == 0 ||
                ArrayUtils.contains(options.processStageIds, 2)) {
            text.append(String.format("<b>Открытие счета 1-му ФЛ</b>:<br/>дата начала - %s, длительность %s дн.",
                    item.s2StartDate != null ? DateHelper.format(item.s2StartDate, "dd.MM.yy") : unknown,
                    item.s2Duration != null ? item.s2Duration.toString() : unknown));
        }

        if (options.processStageIds.length == 0 ||
                ArrayUtils.contains(options.processStageIds, 3)) {
            if (text.length() > 0) {
                text.append("<br/>");
            }

            text.append(String.format("<b>Выдача карты ФЛ</b>:<br/>дата начала - %s, длительность %s дн.",
                    item.s3StartDate != null ? DateHelper.format(item.s3StartDate, "dd.MM.yy") : unknown,
                    item.s3Duration != null ? item.s3Duration.toString() : unknown));
        }

        return text.toString();
    }

    private ChartResult createAvgDuration(LinqWrapper<ZPKPIDataItem> data,
                                          final ZPOpeningSpeedOptions options) {
        final String unitText = options.subProcessStageId != null ? " ч" : " дн";

        final List<Series> series = new ArrayList<>();

        data = data.sortDesc(new Selector<ZPKPIDataItem, Comparable>() {
            @Override
            public Comparable select(ZPKPIDataItem item) {
                return item.avgDuration;
            }
        });

        data = reduce(data);

        double kpiValue = 0;
        if(data.count() > 0){
            kpiValue = data.first().kpiNorm;
        }

        List<String> categories = data.select(new Selector<ZPKPIDataItem, String>() {
            @Override
            public String select(ZPKPIDataItem item) {
                return item.unitName;
            }
        }).toList();

        Point[] columnPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
            @Override
            public Point select(ZPKPIDataItem item) {
                if (item.unitCode.equals("-1")) return Point.withY(0d, item.unitName);

                String name = options.systemUnitId == 3 && options.openingTypeId == 1
                        ? formatCompanyTooltip(item, options)
                        : item.avgDuration + unitText;

                Color color = item.avgDuration > item.kpiNorm
                        ? Color.RedColor
                        : Color.GreenColor;

                return Point.withY(item.avgDuration, name, color);
            }
        }).toArray(Point.class);

        series.add(new Series("Средняя скорость", columnPoints, ChartType.bar));

        if (options.systemUnitId != 3 && data.count() > 0) { // Если не компания
            double maxAvgDuration = data.first().avgDuration;
            final double offset = maxAvgDuration * 0.08;
            final double minPosition = maxAvgDuration * 0.45;

            series.add(getMarkerSeries(data, options, new Selector<ZPKPIDataItem, Double>() {
                @Override
                public Double select(ZPKPIDataItem item) {
                    double position = item.avgDuration + offset;
                    if(position < minPosition) position = minPosition;
                    return position;
                }
            }));
        }

        Map<String, Object> bag = new HashMap<>();
        bag.put("categories", categories);
        bag.put("kpiValue", kpiValue);

        return new ChartResult(series.toArray(new Series[series.size()]), bag);
    }

    private LinqWrapper<ZPKPIDataItem> reduce(LinqWrapper<ZPKPIDataItem> data) {
        if (data.count() > MaxRatingRowCount) {
            int topCount = MaxRatingRowCount / 2;
            List<ZPKPIDataItem> dataAsList = data.toList();
            List<ZPKPIDataItem> dataToDisplay = dataAsList.subList(0, topCount);

            int skippedCount = dataAsList.size() - MaxRatingRowCount;
            ZPKPIDataItem middleItem = new ZPKPIDataItem();

            middleItem.unitCode = "-1";
            middleItem.unitName = String.format("Пропущенно %d строк", skippedCount);

            dataToDisplay.add(middleItem);
            dataToDisplay.addAll(dataAsList.subList(dataAsList.size() - topCount, dataAsList.size() - 1));

            return LinqWrapper.from(dataToDisplay);
        } else
            return data;
    }

    private Series getMarkerSeries(LinqWrapper<ZPKPIDataItem> data,
                                   ZPOpeningSpeedOptions options,
                                   Selector<ZPKPIDataItem, Double> selector){
        List<Point> markers = new ArrayList<>();

        double durationThreshold = repository.getRetardedThreshold(options);

        List<ZPKPIDataItem> dataAsList = data.toList();
        for (int i = 0; i < data.count(); i++) {
            ZPKPIDataItem item = dataAsList.get(i);
            if (item.retardedCount > 0) {
                Point p = new Point((double) i, selector.select(item));
                p.tag = item.unitCode;
                markers.add(p);
            }
        }

        return new Series(String.format("Есть проекты с длительностью открытия более %.2f дней",
                durationThreshold),
                markers.toArray(new Point[markers.size()]), ChartType.line);
    }

    private ChartResult createPercent(LinqWrapper<ZPKPIDataItem> data,
                                      ZPOpeningSpeedOptions options) {
        final double inKpiThreshold = repository.getInKPIThreshold(options);
        final List<Series> series = new ArrayList<>();

        data = data.sortDesc(new Selector<ZPKPIDataItem, Comparable>() {
            @Override
            public Comparable select(ZPKPIDataItem item) {
                return MathHelper.safeDivide(item.totalCount - item.inKpiCount, item.totalCount);
            }
        });

        data = reduce(data);

        List<String> categories = data.select(new Selector<ZPKPIDataItem, String>() {
            @Override
            public String select(ZPKPIDataItem item) {
                return item.unitName;
            }
        }).toList();

        class RichPoint extends Point {
            public Integer inKpiCount;
            public Integer totalCount;

            public RichPoint(ZPKPIDataItem item) {
                super(item.calcDate, MathHelper.safeDivide(item.inKpiCount, item.totalCount) * 100);

                this.color = this.y >= inKpiThreshold
                        ? Color.GreenColor
                        : this.y >= ZPOpeningSpeedRepository.warningThreshold * 100
                        ? Color.OrangeColor
                        : Color.RedColor;

                this.inKpiCount = item.inKpiCount;
                this.totalCount = item.totalCount;
            }
        }

        Point[] columnPoints = data.select(new Selector<ZPKPIDataItem, Point>() {
            @Override
            public Point select(ZPKPIDataItem item) {
                if (item.unitCode.equals("-1")) return Point.withY(0d, item.unitName);

                return new RichPoint(item);
            }
        }).toArray(Point.class);

        series.add(new Series("В KPI", columnPoints, ChartType.bar));

        if (options.systemUnitId != 3 && data.count() > 0) { // Если не компания
            series.add(getMarkerSeries(data, options, new Selector<ZPKPIDataItem, Double>() {
                @Override
                public Double select(ZPKPIDataItem dataItem) {
                    return 110d;
                }
            }));
        }

        Map<String, Object> bag = new HashMap<>();

        bag.put("categories", categories);
        bag.put("kpiValue", inKpiThreshold);

        return new ChartResult(series.toArray(new Series[series.size()]), bag);
    }
}
