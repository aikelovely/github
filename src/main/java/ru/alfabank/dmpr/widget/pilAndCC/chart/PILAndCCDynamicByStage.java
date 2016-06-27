package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiDynamicByStage;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Динамика по выбранному этапу
 */
@Service
public class PILAndCCDynamicByStage extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCDynamicByStage() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiDynamicByStage[] data = repository.getDynamicByStage(options);

        LinqWrapper<Group<Integer, KpiDynamicByStage>> groups = LinqWrapper.from(data)
                .group(new Selector<KpiDynamicByStage, Integer>() {
                    @Override
                    public Integer select(KpiDynamicByStage kpiDynamicByStage) {
                        return kpiDynamicByStage.reportTypeId;
                    }
                });

        final LinqWrapper<KpiDynamicByStage> stagesRepeatKoeffData = LinqWrapper.from(
                repository.getStagesRepeatKoeff(options));

        if (options.valueTypeId == 1)
            return groups.select(new Selector<Group<Integer, KpiDynamicByStage>, ChartResult>() {
                @Override
                public ChartResult select(Group<Integer, KpiDynamicByStage> items) {
                    final Integer key = items.getKey();
                    return createAvgDuration(items, stagesRepeatKoeffData.filter(new Predicate<KpiDynamicByStage>() {
                        @Override
                        public boolean check(KpiDynamicByStage item) {
                            return item.reportTypeId == key;
                        }
                    }));
                }
            }).toArray(ChartResult.class);

        return groups
                .select(new Selector<Group<Integer, KpiDynamicByStage>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Integer, KpiDynamicByStage> items) {
                        final Integer key = items.getKey();
                        return createPercent(items, stagesRepeatKoeffData.filter(new Predicate<KpiDynamicByStage>() {
                            @Override
                            public boolean check(KpiDynamicByStage item) {
                                return item.reportTypeId == key;
                            }
                        }));
                    }
                })
                .toArray(ChartResult.class);
    }

    public ChartResult createAvgDuration(Group<Integer, KpiDynamicByStage> group, LinqWrapper<KpiDynamicByStage> koeffData) {
        LinqWrapper<KpiDynamicByStage> lqitems = group.getItems().sort(new Selector<KpiDynamicByStage, Comparable>() {
            @Override
            public Comparable select(KpiDynamicByStage kpiDynamicByStage) {
                return kpiDynamicByStage.calcDate;
            }
        }).select(new Selector<KpiDynamicByStage, KpiDynamicByStage>() {
            @Override
            public KpiDynamicByStage select(KpiDynamicByStage kpiDynamicByStage) {
                return kpiDynamicByStage;
            }
        });

        KpiDynamicByStage[] items = lqitems.toArray(KpiDynamicByStage.class);

        Map<String, Object> bag = new HashMap<>();

        bag.put("reportTypeId", group.getKey());
        bag.put("title", "Длительность на этапе " + items[0].operationName);

        Point[] manualPoints = new Point[items.length];
        Point[] autoPoints = new Point[items.length];
        Point[] waitTimePoints = new Point[items.length];

        for (int i = 0; i < items.length; i++) {
            manualPoints[i] = new Point(items[i].calcDate, (double) items[i].manualOperDuration / (60 * 60));
            autoPoints[i] = new Point(items[i].calcDate, (double) items[i].autoOperDuration / (60 * 60));
            waitTimePoints[i] = new Point(items[i].calcDate, (double) items[i].waitTimeOperDuration / (60 * 60));
        }

        LinqWrapper<Point> koeffP = koeffData.sort(new Selector<KpiDynamicByStage, Comparable>() {
            @Override
            public Comparable select(KpiDynamicByStage kpiDynamicByStage) {
                return kpiDynamicByStage.calcDate;
            }
        }).select(new Selector<KpiDynamicByStage, Point>() {
            @Override
            public Point select(KpiDynamicByStage kpiDynamicByStage) {
                return new Point(kpiDynamicByStage.calcDate, kpiDynamicByStage.value);
            }
        });

        return new ChartResult(new Series[]{
                new Series("Чистое операционное время", manualPoints, ChartType.column),
                new Series("Время автоматических операций", autoPoints, ChartType.column),
                new Series("Время ожидания", waitTimePoints, ChartType.column),
                new Series("Коэффициент повторяемости этапов", koeffP.toArray(Point.class), ChartType.line)
        }, bag);
    }

    public ChartResult createPercent(final Group<Integer, KpiDynamicByStage> group, LinqWrapper<KpiDynamicByStage> koeffData) {
        LinqWrapper<KpiDynamicByStage> items = group.getItems();

        final Map<String, Object> bag = new HashMap<>();

        bag.put("reportTypeId", group.getKey());
        bag.put("title", "Выполнение норматива на долю в рамках KPI на этапе " + items.first().operationName);

        bag.put("normativeColor", Color.GreenColor);
        bag.put("normativeColor2", Color.OrangeColor);

        LinqWrapper<Group<Pair<Integer, String>, KpiDynamicByStage>> dataByCategory = group.getItems()
                .group(new Selector<KpiDynamicByStage, Pair<Integer, String>>() {
                    @Override
                    public Pair<Integer, String> select(KpiDynamicByStage item) {
                        return Pair.of(item.quotaCategoryId, item.quotaCategoryName);
                    }
                });

        final int normativeCount = dataByCategory.count();

        class RichPoint extends Point{
            String normativeName;
            double normativeValue;

            public RichPoint(KpiDynamicByStage item){
                super(item.calcDate, (double) item.value);

                this.color = item.value > item.planValue
                        ? Color.GreenColor
                        : Color.RedColor;

                this.normativeName = normativeCount == 1 ? "Норматив" : item.quotaCategoryName;
                this.normativeValue = item.planValue;
            }
        }

        List<Series> series = dataByCategory.select(new Selector<Group<Pair<Integer, String>, KpiDynamicByStage>, Series>() {
            @Override
            public Series select(Group<Pair<Integer, String>, KpiDynamicByStage> group) {
                String name = normativeCount == 1 ? "Значение" : group.getKey().getRight() + ". Значение";
                if(group.getKey().getLeft() == 1){
                    bag.put("normative", group.getItems().first().planValue);
                }
                else {
                    bag.put("normative2", group.getItems().first().planValue);
                }

                LinqWrapper<RichPoint> points = group.getItems().sort(new Selector<KpiDynamicByStage, Comparable>() {
                    @Override
                    public Comparable select(KpiDynamicByStage kpiDynamicByStage) {
                        return kpiDynamicByStage.calcDate;
                    }
                }).select(new Selector<KpiDynamicByStage, RichPoint>() {
                    @Override
                    public RichPoint select(KpiDynamicByStage item) {
                        return new RichPoint(item);
                    }
                });

                return new Series(name, points.toArray(RichPoint.class), ChartType.column);
            }
        }).toList();


        LinqWrapper<Point> koeffP = koeffData.sort(new Selector<KpiDynamicByStage, Comparable>() {
            @Override
            public Comparable select(KpiDynamicByStage kpiDynamicByStage) {
                return kpiDynamicByStage.calcDate;
            }
        }).select(new Selector<KpiDynamicByStage, Point>() {
            @Override
            public Point select(KpiDynamicByStage kpiDynamicByStage) {
                return new Point(kpiDynamicByStage.calcDate, kpiDynamicByStage.value);
            }
        });

        series.add(new Series("Коэффициент повторяемости этапов", koeffP.toArray(Point.class), ChartType.line));

        return new ChartResult(series.toArray(new Series[series.size()]), bag);
    }
}
