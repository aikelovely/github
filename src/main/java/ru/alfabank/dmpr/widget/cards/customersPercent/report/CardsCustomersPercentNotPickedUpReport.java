package ru.alfabank.dmpr.widget.cards.customersPercent.report;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentOptions;

/**
 *
 */
@Service
public class CardsCustomersPercentNotPickedUpReport extends CardsCustomersPercentDynamicPieReportBase {
    @Override
    protected ColumnFactoryWrapper getPieColumns() {
        return new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("macroRegionName").title("Регион");
                c.add("unTakenCardCount").title("Количество не забранных карт из отделений");
                c.add("unTakenCardCountDIV").title("Общее количество карт");
                c.add("unTakenCardPercent").title("Доля карт не забранных из отделений").format("0.00%");
            }
        };
    }

    @Override
    protected ColumnFactoryWrapper getDynamicColumns() {
        return new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                c.add("calcDate").title("Дата").format("dd.MM.yyyy HH:mm:ss");
                c.add("unTakenCardCount").title("Количество не забранных карт из отделений");
                c.add("unTakenCardCountDIV").title("Общее количество карт");
                c.add("unTakenCardPercent").title("Доля карт не забранных из отделений").format("0.00%");
            }
        };
    }

    @Override
    protected String getReportName(CardsCustomersPercentOptions options) {
        return "NotPickedUpReport";
        //return "Незабор карт из отделений";
    }
}
