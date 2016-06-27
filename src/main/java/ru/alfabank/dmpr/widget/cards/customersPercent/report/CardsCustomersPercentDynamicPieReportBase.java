package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseReport;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPercentReportItem;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPeriodWidgetHelper;

/**
 * Базовый класс для выгрузки данных с графика "пирог".
 */
public abstract class CardsCustomersPercentDynamicPieReportBase extends BaseReport<CardsCustomersPercentOptions> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    @Autowired
    private CardsCustomersPeriodWidgetHelper widgetHelper;

    public CardsCustomersPercentDynamicPieReportBase() {
        super(CardsCustomersPercentOptions.class);
    }

    @Override
    protected void configure(final ReportBuilder builder, CardsCustomersPercentOptions options) {
        options.systemUnitId = null;
        final CardsCustomersPercentItem[] dbDynamicData = repository.getKpi2DataItems(options);
        options.timeUnitId = null;
        final CardsCustomersPercentItem[] dbPieData = repository.getKpi2DataItems(options);
        boolean haveAnyData = false;

        if (dbPieData != null && dbPieData.length != 0) {
            haveAnyData = true;
            CardsCustomersPercentReportItem[] pieData = widgetHelper.toReportItems(dbPieData);
            builder.addWorksheet(CardsCustomersPercentReportItem.class)
                    .title("Пирог")
                    .bindTo(pieData)
                    .columns(getPieColumns());
        }

        if (dbDynamicData != null && dbDynamicData.length != 0) {
            haveAnyData = true;
            CardsCustomersPercentReportItem[] dynamicData = widgetHelper.toReportItems(dbDynamicData);

            LinqWrapper.from(dynamicData)
                    .group(new Selector<CardsCustomersPercentReportItem, String>() {
                        @Override
                        public String select(CardsCustomersPercentReportItem item) {
                            return item.macroRegionName;
                        }
                    })
                    .each(new Action<Group<String, CardsCustomersPercentReportItem>>() {
                        @Override
                        public void act(Group<String, CardsCustomersPercentReportItem> dataByRegion) {
                            builder.addWorksheet(CardsCustomersPercentReportItem.class)
                                    .title(String.format("Динамика (%s)", dataByRegion.getKey()))
                                    .bindTo(dataByRegion.getItems()
                                            .toArray(CardsCustomersPercentReportItem.class))
                                    .columns(getDynamicColumns());
                        }
                    });
        }

        if (!haveAnyData) {
            builder.addNoDataWorksheet();
        }
    }

    protected abstract ColumnFactoryWrapper getPieColumns();

    protected abstract ColumnFactoryWrapper getDynamicColumns();
}
