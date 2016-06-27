package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;

/**
 * Выгрузка "Клиенты, получившие карту в указанном отделении"
 */
@Service
public class CardsCustomersPercentNotReceivedReport extends CardsCustomersPercentDynamicPieReportBase {
    @Override
    protected ColumnFactoryWrapper getPieColumns() {
        return new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("macroRegionName").title("Регион");
                c.add("RCDOCardCount").title("Количество клиентов получивших карту в указанном отделении");
                c.add("RCDOCardCountDIV").title("Общее количество карт");
                c.add("RCDOCardPercent").title("Доля клиентов, которые получили карту в указанном отделении").format("0.00%");
            }
        };
    }

    @Override
    protected ColumnFactoryWrapper getDynamicColumns() {
        return new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("calcDate").title("Дата").format("dd.MM.yyyy HH:mm:ss");
                c.add("RCDOCardCount").title("Количество клиентов получивших карту в указанном отделении");
                c.add("RCDOCardCountDIV").title("Общее количество карт");
                c.add("RCDOCardPercent").title("Доля клиентов, которые получили карту в указанном отделении").format("0.00%");
            }
        };
    }

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "NotReceivesReport";
        //return "Клиенты, получившие карту в указанном отделении";
    }
}
