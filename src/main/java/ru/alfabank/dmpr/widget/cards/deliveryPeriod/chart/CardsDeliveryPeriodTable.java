package ru.alfabank.dmpr.widget.cards.deliveryPeriod.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.CardsDeliveryPeriodWidgetHelper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;

import java.util.*;

/**
 * Таблица "Детализированный отчет"
 */
@Service
public class CardsDeliveryPeriodTable extends BaseWidget<CardsDeliveryPeriodOptions, List<Map<String, Object>>> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;
    @Autowired
    private CardsDeliveryPeriodWidgetHelper widgetHelper;

    public CardsDeliveryPeriodTable() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    public List<Map<String, Object>> getData(final CardsDeliveryPeriodOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);
        CardsDeliveryPeriodItem[] items = repository.getTableItems(options);

        LinqWrapper<CardsDeliveryPeriodItem> sortedItems = LinqWrapper.from(items)
                .sort(new Selector<CardsDeliveryPeriodItem, LocalDate>() {
                    @Override
                    public LocalDate select(CardsDeliveryPeriodItem item) {
                        return item.calcDate;
                    }
                });

        List<Map<String, Object>> rows = sortedItems
                .group(new Selector<CardsDeliveryPeriodItem, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(CardsDeliveryPeriodItem item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, CardsDeliveryPeriodItem>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, CardsDeliveryPeriodItem> unitGroup) {
                        return createUnitRow(unitGroup, dates, options);
                    }
                })
                .sort(new Selector<Map<String, Object>, String>() {
                    @Override
                    public String select(Map<String, Object> row) {
                        return (String)row.get("unitName");
                    }
                })
                .toList();

        LinqWrapper<Group<LocalDate, CardsDeliveryPeriodItem>> dateGroups = sortedItems.group(new Selector<CardsDeliveryPeriodItem, LocalDate>() {
            @Override
            public LocalDate select(CardsDeliveryPeriodItem item) {
                return item.calcDate;
            }
        });
        rows.add(createTotalsRow(dateGroups, dates, options));

        return rows;
    }

    // row - строка с данными для одного подразделения (unit)
    private Map<String, Object> createUnitRow(
            Group<Pair<String, String>, CardsDeliveryPeriodItem> unitGroup, List<LocalDate> dates, final CardsDeliveryPeriodOptions options) {
        Pair<String, String> key = unitGroup.getKey();
        final Map<String, Object> row = createEmptyRow(key.getLeft(), key.getRight());

        LinqWrapper<CardsDeliveryPeriodItem> items = unitGroup.getItems();
        final Map<LocalDate, CardsDeliveryPeriodItem> index = items.toMap(new Selector<CardsDeliveryPeriodItem, LocalDate>() {
            @Override
            public LocalDate select(CardsDeliveryPeriodItem item) {
                return item.calcDate;
            }
        });

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        CardsDeliveryPeriodItem item = index.get(date);
                        String columnName = DateHelper.createDateName(date);
                        addNewCell(row, columnName, item, options);
                    }
                });

        final CardsDeliveryPeriodItem summer = new CardsDeliveryPeriodItem();
        items.each(new Action<CardsDeliveryPeriodItem>() {
            @Override
            public void act(CardsDeliveryPeriodItem item) {
                summer.cardCount += item.cardCount;
                summer.inKPICardCount += item.inKPICardCount;
                summer.totalDuration += item.totalDuration;
            }
        });

        final CardsDeliveryPeriodItem first = items.first();
        summer.percentNormative = first.percentNormative;
        summer.durationNormative = first.durationNormative;

        int size = items.count();
        summer.cardCount = summer.cardCount / size;
        summer.inKPICardCount = summer.inKPICardCount / size;
        summer.totalDuration = summer.totalDuration / size;

        addNewCell(row, "average", summer, options);

        return row;
    }

    private Map<String, Object> createTotalsRow(
            LinqWrapper<Group<LocalDate, CardsDeliveryPeriodItem>> dateGroups, List<LocalDate> dates, CardsDeliveryPeriodOptions options) {
        Map<LocalDate, CardsDeliveryPeriodItem> index = new HashMap<>();
        for (Group<LocalDate, CardsDeliveryPeriodItem> dateGroup : dateGroups) {
            CardsDeliveryPeriodItem summer = new CardsDeliveryPeriodItem();
            final List<CardsDeliveryPeriodItem> items = dateGroup.getItems().toList();
            for (CardsDeliveryPeriodItem item : items) {
                summer.cardCount += item.cardCount;
                summer.inKPICardCount += item.inKPICardCount;
                summer.totalDuration += item.totalDuration;
            }

            int size = items.size();
            summer.cardCount = summer.cardCount / size;
            summer.inKPICardCount = summer.inKPICardCount / size;
            summer.totalDuration = summer.totalDuration / size;

            index.put(dateGroup.getKey(), summer);
        }

        Map<String, Object> row = createEmptyRow("totals", "totals");
        for (LocalDate date : dates) {
            CardsDeliveryPeriodItem item = index.get(date);

            String columnName = DateHelper.createDateName(date);
            addNewCell(row, columnName, item, options);
        }

        addNewCell(row, "average", null, options);
        return row;
    }

    private void addNewCell(Map<String, Object> row, String columnName, CardsDeliveryPeriodItem item, CardsDeliveryPeriodOptions options) {
        class RichCell {
            public double mainValue;
            public int cardCount;
            public int inKPICardCount;
            public double totalDuration;
            public double durationNormative;
            public double percentNormative;

            public RichCell(CardsDeliveryPeriodItem item, CardsDeliveryPeriodOptions options) {
                mainValue = widgetHelper.getMainValue(item, options);
                cardCount = item.cardCount;
                inKPICardCount = item.inKPICardCount;
                totalDuration = item.totalDuration;
                durationNormative = item.durationNormative;
                percentNormative = item.percentNormative;
            }
        }

        RichCell cell = null;
        if (item != null)
            cell = new RichCell(item, options);

        row.put(columnName, cell);
    }

    private Map<String, Object> createEmptyRow(String unitCode, Object unitName) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitCode", unitCode);
        row.put("unitName", unitName);
        return row;
    }
}
