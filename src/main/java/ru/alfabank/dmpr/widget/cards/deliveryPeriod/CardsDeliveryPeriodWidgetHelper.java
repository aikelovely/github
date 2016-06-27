package ru.alfabank.dmpr.widget.cards.deliveryPeriod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodItem;
import ru.alfabank.dmpr.model.cards.deliveryPeriod.CardsDeliveryPeriodOptions;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.repository.cards.CardsCommonFilterRepository;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;

/**
 * Вспомогательный класс, который выполняет преобразование объектов из одного типа в другой и другие полезные действия.
 */
@Component
public class CardsDeliveryPeriodWidgetHelper {
    @Autowired
    private CardsCommonFilterRepository filterRepository;

    public Selector<CardsDeliveryPeriodItem, Long> getDistributionKeySelector(final CardsDeliveryPeriodOptions options) {
        if (options.automaticExtensionMode)
            return new Selector<CardsDeliveryPeriodItem, Long>() {
                @Override
                public Long select(CardsDeliveryPeriodItem item) {
                    return item.reissueSpeedId;
                }
            };
        else
            return new Selector<CardsDeliveryPeriodItem, Long>() {
                @Override
                public Long select(CardsDeliveryPeriodItem item) {
                    return item.dayGroupId;
                }
            };
    }

    public String getSeriesName(Group<Long, CardsDeliveryPeriodItem> seriesGroup, CardsDeliveryPeriodOptions options) {
        Long key = seriesGroup.getKey();
        if (options.automaticExtensionMode) {
            if (key != null) {
                BaseEntity entity = filterRepository.getReissueSpeedById(key);
                if (entity != null)
                    return entity.name;
            }
            return "";
        } else {
            CardsDeliveryPeriodItem item = seriesGroup.getItems().first();
            return item.dayGroupName;//getDayGroupName(item);
        }
    }

    public double getMainValue(CardsDeliveryPeriodItem item, CardsDeliveryPeriodOptions options) {
        return options.paramType == ParamType.Percent
                ? MathHelper.safeDivide(item.inKPICardCount, item.cardCount) * 100
                : Math.round(MathHelper.safeDivide(item.totalDuration, item.cardCount));
    }

    private String getDayGroupName(CardsDeliveryPeriodItem item) {
        String suffix = item.dayGroupId == 1
                ? "день"
                : (item.dayGroupId < 1 || item.dayGroupId > 4)
                ? " дней"
                : " дня";

        return item.dayGroupName + " " + suffix;
    }
}
