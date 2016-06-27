package ru.alfabank.dmpr.filter.cards;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.Period;

/**
 * Сервис-фильтр для витрины "Доля клиентов, получивших карту в указанном отделении"
 */
@Service
public class CardsCustomersPercentFilter extends CardsCommonFilter {

    /**
     * Возвращает список "Единица времени"
     * @return
     */
    @Override
    public BaseEntity[] getTimeUnits() {
        return LinqWrapper.from(super.getTimeUnits()).filter(new Predicate<BaseEntity>() {
            @Override
            public boolean check(BaseEntity item) {
                return item.id != Period.day.getValue() && item.id != Period.week.getValue();
            }
        }).toArray(BaseEntity.class);
    }
}
