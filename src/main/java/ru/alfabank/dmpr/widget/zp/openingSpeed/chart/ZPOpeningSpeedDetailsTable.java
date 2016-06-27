package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Таблица "Детализированный отчет".
 */
@Service
public class ZPOpeningSpeedDetailsTable extends BaseWidget<ZPOpeningSpeedOptions, List<Map<String, Object>>> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    public ZPOpeningSpeedDetailsTable() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(final ZPOpeningSpeedOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        if (options.systemUnitId == 3) // отображать по городам, если выбрана детализация по городам/компаниям
        {
            options.systemUnitId = 1;
        }

        LinqWrapper<ZPKPIDataItem> data = LinqWrapper.from(repository.getDetails(options));

        final double inKpiThreshold = repository.getInKPIThreshold(options);

        List<Map<String, Object>> rows = data
                .group(new Selector<ZPKPIDataItem, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(ZPKPIDataItem item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, ZPKPIDataItem>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, ZPKPIDataItem> unitGroup) {
                        return createRow(unitGroup, dates, inKpiThreshold, options);
                    }
                })
                .sort(new Selector<Map<String, Object>, String>() {
                    @Override
                    public String select(Map<String, Object> row) {
                        return (String) row.get("unitName");
                    }
                })
                .toList();

        return rows;
    }

    private Map<String, Object> createRow(Group<Pair<String, String>, ZPKPIDataItem> unitGroup,
                                          final List<LocalDate> dates,
                                          final double inKpiThreshold,
                                          final ZPOpeningSpeedOptions options) {
        Pair<String, String> key = unitGroup.getKey();

        final Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitCode", key.getLeft());
        row.put("unitName", key.getRight());

        LinqWrapper<ZPKPIDataItem> items = unitGroup.getItems();
        final Map<LocalDate, ZPKPIDataItem> index = items.toMap(new Selector<ZPKPIDataItem, LocalDate>() {
            @Override
            public LocalDate select(ZPKPIDataItem item) {
                return item.calcDate;
            }
        });

        class RichCell {
            public int inKpiCount;
            public int totalCount;
            public Double avgDuration;
            public double inKpiThreshold;
            public double durationThreshold;

            public RichCell(ZPKPIDataItem item, double inKpiThreshold) {
                inKpiCount = item.inKpiCount;
                totalCount = item.totalCount;
                avgDuration = item.avgDuration;
                this.inKpiThreshold = inKpiThreshold;
                durationThreshold = item.kpiNorm;
            }
        }

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        ZPKPIDataItem item = index.get(date);
                        String columnName = DateHelper.createDateName(date);

                        RichCell cell = null;
                        if (item != null)
                            cell = new RichCell(item, inKpiThreshold);

                        row.put(columnName, cell);
                    }
                });

        ZPKPIDataItem first = items.firstOrNull();
        final ZPKPIDataItem summation = new ZPKPIDataItem();
        summation.inKpiCount = 0;
        summation.totalCount = 0;
        summation.totalDuration = 0d;
        summation.kpiNorm = 0d;

        if(first != null){
            summation.kpiNorm = first.kpiNorm;
        }

        items.each(new Action<ZPKPIDataItem>() {
            @Override
            public void act(ZPKPIDataItem item) {
                summation.inKpiCount+= item.inKpiCount;
                summation.totalCount += item.totalCount;
                summation.totalDuration += item.totalDuration;
            }
        });

        summation.avgDuration  = summation.totalDuration / summation.totalCount;

        row.put("totals", new RichCell(summation, inKpiThreshold));

        return row;
    }
}