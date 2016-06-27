package ru.alfabank.dmpr.widget.nom.chart;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.ChildEntityWithCode;
import ru.alfabank.dmpr.model.nom.NomKpiDataItem;
import ru.alfabank.dmpr.model.nom.NomOptions;
import ru.alfabank.dmpr.model.nom.NomQueryOptions;
import ru.alfabank.dmpr.repository.nom.NomFilterRepository;
import ru.alfabank.dmpr.repository.nom.NomRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Service
public class NomDynamic extends BaseChart<NomOptions> {
    @Autowired
    private NomFilterRepository filterRepository;

    @Autowired
    private NomRepository repository;

    public NomDynamic() {
        super(NomOptions.class);
    }

    @Override
    public ChartResult[] getData(final NomOptions options) {
        options.levels = new int[]{1};
        LinqWrapper<NomKpiDataItem> data = LinqWrapper.from(repository.getKpiData(new NomQueryOptions(options, filterRepository.getWeeks())));

        class RichPoint extends Point {
            public int periodNum;

            public RichPoint(LocalDate calcDate, Double y) {
                super(calcDate, y);
            }
        }

        LinqWrapper<Point> points = data.group(new Selector<NomKpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(NomKpiDataItem nomKpiDataItem) {
                return nomKpiDataItem.calcDate;
            }
        }).select(new Selector<Group<LocalDate, NomKpiDataItem>, Point>() {
            @Override
            public Point select(Group<LocalDate, NomKpiDataItem> group) {
                RichPoint p = new RichPoint(group.getKey(), group.getItems()
                        .sum(new Selector<NomKpiDataItem, Double>() {
                            @Override
                            public Double select(NomKpiDataItem nomKpiDataItem) {
                                return (double)nomKpiDataItem.factCount;
                            }
                        }));
                p.periodNum = group.getItems().first().periodNum;
                return p;
            }
        }).sort(new Selector<Point, Comparable>() {
            @Override
            public Comparable select(Point point) {
                return point.x;
            }
        });

        return new ChartResult[]{new ChartResult(new Series[]{new Series("Количество", points.toArray(Point.class))})};
    }
}
