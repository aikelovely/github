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
import ru.alfabank.dmpr.model.zp.ZPProjectDynamic;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Таблица "Количество компаний в АЗОН"
 */
@Service
public class ZPOpeningSpeedProjectDetailsTable extends BaseWidget<ZPOpeningSpeedOptions, List<Map<String, Object>>> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    public ZPOpeningSpeedProjectDetailsTable() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(final ZPOpeningSpeedOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        if (options.systemUnitId == 3) // отображать по городам, если выбрана детализация по городам/компаниям
        {
            options.systemUnitId = 1;
        }

        LinqWrapper<ZPProjectDynamic> data = LinqWrapper.from(repository.getProjectDetails(options));

        List<Map<String, Object>> rows = data
                .group(new Selector<ZPProjectDynamic, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(ZPProjectDynamic item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, ZPProjectDynamic>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, ZPProjectDynamic> unitGroup) {
                        return createRow(unitGroup, dates, options);
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

    private Map<String, Object> createRow(Group<Pair<String, String>, ZPProjectDynamic> unitGroup,
                                          final List<LocalDate> dates,
                                          final ZPOpeningSpeedOptions options) {
        Pair<String, String> key = unitGroup.getKey();
        LinqWrapper<ZPProjectDynamic> items = unitGroup.getItems();

        final Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitCode", key.getLeft());
        row.put("unitName", key.getRight());

        final Map<LocalDate, ZPProjectDynamic> index = items
                .toMap(new Selector<ZPProjectDynamic, LocalDate>() {
                    @Override
                    public LocalDate select(ZPProjectDynamic item) {
                        return item.calcDate;
                    }
                });

        class RichCell {
            public int startedOkCount;
            public int companyCount;

            public RichCell(ZPProjectDynamic item) {
                startedOkCount = item.startedOkCount;
                companyCount = item.companyCount;
            }
        }

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        ZPProjectDynamic item = index.get(date);
                        String columnName = DateHelper.createDateName(date);

                        RichCell cell = null;
                        if (item != null)
                            cell = new RichCell(item);

                        row.put(columnName, cell);
                    }
                });

        final ZPProjectDynamic summation = new ZPProjectDynamic();
        summation.startedOkCount = 0;
        summation.companyCount = 0;

        items.each(new Action<ZPProjectDynamic>() {
            @Override
            public void act(ZPProjectDynamic item) {
                summation.startedOkCount+= item.startedOkCount;
                summation.companyCount += item.companyCount;
            }
        });

        row.put("totals", new RichCell(summation));

        return row;
    }
}