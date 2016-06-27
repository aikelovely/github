package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;

/**
 * Пирог "% незабора карт из отделений"
 */
@Service
public class CardsCustomersPercentNotPickedUpPie extends CardsCustomersPercentPieBase {
    @Override
    protected Selector<CardsCustomersPercentItem, Point[]> getPointSelector() {
        return new Selector<CardsCustomersPercentItem, Point[]>() {
            @Override
            public Point[] select(CardsCustomersPercentItem item) {
                return new Point[]{
                        Point.withY((double) (item.unTakenCardCountDIV - item.unTakenCardCount), "% забранных", Color.GreenColor),
                        Point.withY(item.unTakenCardCount, "% незабранных", Color.RedColor)
                };
            }
        };
    }
}
