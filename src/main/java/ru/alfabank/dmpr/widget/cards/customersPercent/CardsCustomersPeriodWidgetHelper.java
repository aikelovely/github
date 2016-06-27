package ru.alfabank.dmpr.widget.cards.customersPercent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;
import ru.alfabank.dmpr.repository.cards.CardsCommonFilterRepository;

/**
 * Вспомогательный класс, который выполняет преобразование объектов из одного типа в другой и другие полезные действия.
 */
@Component
public class CardsCustomersPeriodWidgetHelper {
    @Autowired
    private CardsCommonFilterRepository filterRepository;

    /**
     * Функция, которая выполняет расчет процента незабранный карт.
     * @param item
     * @return
     */
    public double getUnTakenCardPercent(CardsCustomersPercentItem item) {
        return MathHelper.safeDivide(item.unTakenCardCount, item.unTakenCardCountDIV);
    }

    /**
     * Функция, которая выполняет расчет процента RCDO.
     * @param item
     * @return
     */
    public double getRCDOCardPercent(CardsCustomersPercentItem item) {
        return MathHelper.safeDivide(item.RCDOCardCount, item.RCDOCardCountDIV);
    }

    /**
     * Преобразует CardsCustomersPercentItem в CardsCustomersPercentReportItem
     * @param item
     * @return
     */
    public CardsCustomersPercentReportItem toReportItem(CardsCustomersPercentItem item){
        BaseEntity macroReegion = filterRepository.getMacroRegionById(item.macroRegionId);
        String macroRegionName = macroReegion != null ? macroReegion.name : "";

        return new CardsCustomersPercentReportItem(item, macroRegionName, getUnTakenCardPercent(item), getRCDOCardPercent(item));
    }

    /**
     * Преобразует список CardsCustomersPercentItem[] в CardsCustomersPercentReportItem[]
     * @param items
     * @return
     */
    public CardsCustomersPercentReportItem[] toReportItems(CardsCustomersPercentItem[] items) {
        return LinqWrapper
                .from(items)
                .select(new Selector<CardsCustomersPercentItem, CardsCustomersPercentReportItem>() {
                    @Override
                    public CardsCustomersPercentReportItem select(CardsCustomersPercentItem item) {
                        return toReportItem(item);
                    }
                })
                .toArray(CardsCustomersPercentReportItem.class);
    }
}
