package ru.alfabank.dmpr.widget.cards.deliveryPeriod.report;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.repository.cards.CardsCommonFilterRepository;
import ru.alfabank.dmpr.repository.cards.CardsDeliveryPeriodRepository;
import ru.alfabank.dmpr.widget.BaseReport;
import ru.alfabank.dmpr.widget.cards.deliveryPeriod.CardsDeliveryPeriodWidgetHelper;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnDictionaryFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnDictionaryFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;

import java.util.*;

@Service
public class CardsDeliveryPeriodDistributionDynamicReport extends BaseReport<CardsDeliveryPeriodOptions> {
    @Autowired
    private CardsDeliveryPeriodRepository repository;

    @Autowired
    private CardsCommonFilterRepository filterRepository;

    @Autowired
    private CardsDeliveryPeriodWidgetHelper widgetHelper;

    public CardsDeliveryPeriodDistributionDynamicReport() {
        super(CardsDeliveryPeriodOptions.class);
    }

    @Override
    protected String getReportName(CardsDeliveryPeriodOptions options) {
        return "CardsDeliveryPeriodDynamicDetailsReport";
//        return options.automaticExtensionMode
//                ? "Срок доставки карт в отделение относительно месяца окончания срока действия карты"
//                : "Распределение карт от количества рабочих дней, потраченных на доставку";
    }

    @Override
    protected void configure(final ReportBuilder builder, final CardsDeliveryPeriodOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);
        CardsDeliveryPeriodItem[] data = repository.getDistributionDynamicItems(options);

        if (data == null || data.length == 0) {
            builder.addNoDataWorksheet();
            return;
        }

        LinqWrapper.from(data)
                .group(new Selector<CardsDeliveryPeriodItem, Long>() {
                    @Override
                    public Long select(CardsDeliveryPeriodItem item) {
                        return item.macroRegionId;
                    }
                })
                .each(new Action<Group<Long, CardsDeliveryPeriodItem>>() {
                    @Override
                    public void act(Group<Long, CardsDeliveryPeriodItem> dataByRegion) {
                        createDistributionDynamicWorksheet(
                                builder,
                                options,
                                dataByRegion,
                                dates);
                    }
                });
    }

    private void createDistributionDynamicWorksheet(
            ReportBuilder builder,
            final CardsDeliveryPeriodOptions options,
            final Group<Long, CardsDeliveryPeriodItem> dataByRegion,
            final List<LocalDate> dates) {

        BaseEntity region = filterRepository.getMacroRegionById(dataByRegion.getKey());
        String regionName = region == null ? "" : region.name;

        // Создаем список данных для каждого листа
        final List<HashMap<String, Object>> worksheetCountData = new ArrayList<>();
        final List<HashMap<String, Object>> worksheetPercentData = new ArrayList<>();

        final List<String> columnNames = new ArrayList<>();

        dataByRegion.getItems()
                .group(widgetHelper.getDistributionKeySelector(options))
                .sort(new Selector<Group<Long, CardsDeliveryPeriodItem>, Long>() {
                    @Override
                    public Long select(Group<Long, CardsDeliveryPeriodItem> group) {
                        return group.getKey();
                    }
                })
                .each(new Action<Group<Long, CardsDeliveryPeriodItem>>() {
                    @Override
                    public void act(Group<Long, CardsDeliveryPeriodItem> group) {
                        HashMap<String, Object> rowCount = new HashMap<>(); // worksheetCountData row
                        HashMap<String, Object> rowPercent = new HashMap<>(); // worksheetPercentData row

                        String name = widgetHelper.getSeriesName(group, options);
                        rowCount.put("Name", name);
                        rowPercent.put("Name", name);

                        for (final LocalDate date : dates) {
                            CardsDeliveryPeriodItem dateValue = group.getItems()
                                    .filter(new Predicate<CardsDeliveryPeriodItem>() {
                                        @Override
                                        public boolean check(CardsDeliveryPeriodItem item) {
                                            return item.calcDate.isEqual(date);
                                        }
                                    }).firstOrNull();

                            String columnName = DateHelper.createDateName(date);

                            if (dateValue == null) {
                                rowCount.put(columnName, null);
                                rowPercent.put(columnName, null);
                            } else {
                                rowCount.put(columnName, dateValue.cardCount);

                                Integer totalCardCount = dataByRegion.getItems()
                                        .filter(new Predicate<CardsDeliveryPeriodItem>() {
                                            @Override
                                            public boolean check(CardsDeliveryPeriodItem item) {
                                                return item.calcDate.isEqual(date);
                                            }
                                        }).sum(new Selector<CardsDeliveryPeriodItem, Integer>() {
                                            @Override
                                            public Integer select(CardsDeliveryPeriodItem item) {
                                                return item.cardCount;
                                            }
                                        }, 0);

                                Double percent = MathHelper.safeDivide(dateValue.cardCount, totalCardCount);
                                rowPercent.put(columnName, percent);
                            }

                            columnNames.add(columnName);
                        }

                        worksheetCountData.add(rowCount);
                        worksheetPercentData.add(rowPercent);
                    }
                });

        final String firstColumnTitle = options.automaticExtensionMode
                ? "Дата/категория"
                : "Дата/кол-во дней";

        builder.addWorksheetWithDictionaryData()
                .title(String.format("Распределение количества (%s)", regionName))
                .bindTo(worksheetCountData)
                .columns(new ColumnDictionaryFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnDictionaryFactory c) {
                        c.add("Name", String.class).title(firstColumnTitle);

                        for (int i = 0; i < dates.size(); i++) {
                            LocalDate currentDate = dates.get(i);
                            String title = PeriodHelper.dateAsStringByTimeUnitId(currentDate, options);

                            c.add(columnNames.get(i), Integer.class).title(title);
                        }
                    }
                });

        builder.addWorksheetWithDictionaryData()
                .title(String.format("Распределение процента (%s)", regionName))
                .bindTo(worksheetPercentData)
                .columns(new ColumnDictionaryFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnDictionaryFactory c) {
                        c.add("Name", String.class).title(firstColumnTitle);

                        for (int i = 0; i < dates.size(); i++) {
                            LocalDate currentDate = dates.get(i);
                            String title = PeriodHelper.dateAsStringByTimeUnitId(currentDate, options);

                            c.add(columnNames.get(i), Double.class).title(title).format("0.00%");
                        }
                    }
                });
    }
}
