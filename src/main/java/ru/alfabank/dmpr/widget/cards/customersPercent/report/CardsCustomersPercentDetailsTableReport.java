package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.Column;
import ru.alfabank.dmpr.infrastructure.export.excel.ExcelHelper;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCommonFilterRepository;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseReport;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPercentReportItem;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPeriodWidgetHelper;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Выгрузка "Детализированный отчет"
 */
@Service
public class CardsCustomersPercentDetailsTableReport extends BaseReport<CardsCustomersPercentOptions> {

    // Helper hierarchical class
    class SheetLevelConfig {
        private Integer systemUnitId;
        public ArrayList<Column> columns;

        public CardsCustomersPercentItem[] getFilteredData(final CardsCustomersPercentItem item) {
            return item == null ? data : LinqWrapper.from(data).filter(new Predicate<CardsCustomersPercentItem>() {
                @Override
                public boolean check(CardsCustomersPercentItem ddItem) {
                    switch (systemUnitId) {
                        case 0:
                            return true;
                        case 1:
                            return ddItem.macroRegionId.equals(item.macroRegionId);
                        case 2:
                            return ddItem.regionId.equals(item.regionId);
                        case 3:
                            return ddItem.cityId.equals(item.cityId);
                        default:
                            return false;
                    }
                }
            }).toArray(CardsCustomersPercentItem.class);
        }

        private CardsCustomersPercentItem[] data;

        public void setNextLevel(SheetLevelConfig nextLevel) {
            this.nextLevel = nextLevel;
        }

        public SheetLevelConfig nextLevel;

        public SheetLevelConfig(ArrayList<Column> columns, CardsCustomersPercentItem[] data, Integer systemUnitId) {
            this.columns = columns;
            this.data = data;
            this.systemUnitId = systemUnitId;
        }
    }

    @Autowired
    private CardsCustomersPercentRepository repository;

    @Autowired
    private CardsCommonFilterRepository filterRepository;

    @Autowired
    private CardsCustomersPeriodWidgetHelper widgetHelper;

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "DetailsReport";
        //return "Детализированный отчет";
    }

    private ArrayList<Column> getColumnsBySystemUnitId(Integer systemUnitId) {
        final BaseEntity systemUnit = filterRepository.getSystemUnitById(systemUnitId);

        ColumnFactoryWrapper wrapper = new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("unitName").title(systemUnit != null ? systemUnit.name : "Единица сети");
                c.add("unTakenCardPercent").title("% незабора карт из отделений").format("0.00%");
                c.add("unTakenCardKPI").title("KPI").format("0.00%");
                c.add("unTakenCardDiviation").title("отклонение от KPI").format("0.00%");
                c.add("RCDOCardPercent").title("% клиентов, получивших карту в отделении").format("0.00%");
                c.add("RCDOCardKPI").title("KPI").format("0.00%");
                c.add("destructedCardCount").title("Количество карт со статусом уничтожено");
            }
        };

        ArrayList<Column> columns = new ArrayList();
        ColumnFactory columnFactory = new ColumnFactory(columns, CardsCustomersPercentReportItem.class);
        wrapper.createColumns(columnFactory);

        return columns;
    }

    private void CascadeFill(Sheet sheet, SheetLevelConfig config, AtomicInteger rowIndex, CardsCustomersPercentItem filterItem) {
        int groupStart = rowIndex.get();
        ExcelHelper.createStandardHeaderRow(sheet, config.columns, rowIndex);
        for (CardsCustomersPercentItem item : config.getFilteredData(filterItem)) {
            ExcelHelper.fillRow(sheet, config.columns, rowIndex, widgetHelper.toReportItem(item));
            if (config.nextLevel != null)
                CascadeFill(sheet, config.nextLevel, rowIndex, item);
        }
        sheet.groupRow(groupStart, rowIndex.get());
        if (filterItem != null)
            sheet.setRowGroupCollapsed(groupStart, true);
    }

    @Override
    protected void configure(ReportBuilder builder, final CardsCustomersPercentOptions options) {
        options.timeUnitId = null;
        final CardsCustomersPercentItem[] dbData = repository.getKpi2DataItems(options);

        if (dbData != null && dbData.length != 0) {
            final SheetLevelConfig sheetLevelConfig = new SheetLevelConfig(getColumnsBySystemUnitId(options.systemUnitId), dbData, options.systemUnitId);
            SheetLevelConfig dd = sheetLevelConfig;

            while (filterRepository.getSystemUnitById(options.systemUnitId + 1) != null) {
                options.systemUnitId++;
                SheetLevelConfig nl = new SheetLevelConfig(getColumnsBySystemUnitId(options.systemUnitId), repository.getKpi2DataItems(options), options.systemUnitId);
                dd.setNextLevel(nl);
                dd = dd.nextLevel;
            }

            builder.useStreaming(false)
                    .addCustomWorksheet()
                    .configure(new Action<Sheet>() {
                        @Override
                        public void act(Sheet sheet) {
                            AtomicInteger rowIndex = new AtomicInteger(0);
                            CascadeFill(sheet, sheetLevelConfig, rowIndex, null);
                            sheet.setRowSumsBelow(false);
                            ExcelHelper.resizeColumns(sheet, sheetLevelConfig.columns);
                        }
                    });
        } else {
            builder.addNoDataWorksheet();
        }
    }

    public CardsCustomersPercentDetailsTableReport() {
        super(CardsCustomersPercentOptions.class);
    }
}
