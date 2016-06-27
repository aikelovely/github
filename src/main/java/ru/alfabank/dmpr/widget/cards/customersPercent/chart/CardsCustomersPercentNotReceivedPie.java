package ru.alfabank.dmpr.widget.cards.customersPercent.chart;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.cards.customersPercent.CardsCustomersPercentItem;

/**
 * Пирог "% клиентов, которые получили карту"
 */
@Service
public class CardsCustomersPercentNotReceivedPie extends CardsCustomersPercentPieBase {
    @Override
    protected Selector<CardsCustomersPercentItem, Point[]> getPointSelector() {
        return new Selector<CardsCustomersPercentItem, Point[]>() {
            @Override
            public Point[] select(CardsCustomersPercentItem item) {
                return new Point[]{
                        Point.withY(item.RCDOCardCount, "получили", Color.GreenColor),
                        Point.withY((double) (item.RCDOCardCountDIV - item.RCDOCardCount), "не получили", Color.RedColor)
                };
            }
        };
    }
}