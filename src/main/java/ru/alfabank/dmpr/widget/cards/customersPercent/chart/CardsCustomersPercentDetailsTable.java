package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;
import ru.alfabank.dmpr.repository.cards.CardsCustomersPercentRepository;
import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.widget.cards.customersPercent.CardsCustomersPeriodWidgetHelper;

import java.util.List;

/**
 * Таблица "Детализированный отчет"
 */
@Service
public class CardsCustomersPercentDetailsTable extends BaseWidget<CardsCustomersPercentOptions, List<CardsCustomersPercentDetailsTable.TableItem>> {
    @Autowired
    private CardsCustomersPercentRepository repository;

    @Autowired
    private CardsCustomersPeriodWidgetHelper widgetHelper;

    class TableItem {
        public String unitCode;
        public String unitName;
        public Double unTakenCardPercent;
        public Double unTakenCardKPI;
        public Double unTakenCardDiviation;
        public Double RCDOCardPercent;
        public Double RCDOCardKPI;
        public Integer destructedCardCount;

        TableItem(CardsCustomersPercentItem item){
            this.unitCode = item.unitCode;
            this.unitName = item.unitName;
            this.unTakenCardPercent = widgetHelper.getUnTakenCardPercent(item);
            this.unTakenCardKPI = item.unTakenCardKPI;
            this.unTakenCardDiviation = Math.abs(widgetHelper.getUnTakenCardPercent(item) - unTakenCardKPI);
            this.RCDOCardPercent = widgetHelper.getRCDOCardPercent(item);
            this.RCDOCardKPI = item.RCDOCardKPI;
            this.destructedCardCount = item.destructedCardCount;
        }
    }

    @Override
    public List<TableItem> getData(CardsCustomersPercentOptions options) {
        options.timeUnitId = null;
        return LinqWrapper.from(repository.getKpi2DataItems(options)).select(new Selector<CardsCustomersPercentItem, TableItem>() {
            @Override
            public TableItem select(CardsCustomersPercentItem item) {
                return new TableItem(item);
            }
        }).toList();
    }

    public CardsCustomersPercentDetailsTable() {
        super(CardsCustomersPercentOptions.class);
    }
}
