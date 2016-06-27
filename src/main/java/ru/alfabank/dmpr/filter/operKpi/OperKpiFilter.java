package ru.alfabank.dmpr.filter.operKpi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCFilterRepository;

/**
 * Сервис, отвечающий за загрузку фильтров для витрин "Отчет по выполнению KPI".
 */
@Service
public class OperKpiFilter {
    @Autowired
    private PILAndCCFilterRepository repository;

    /**
     * Возвращает данные для фильтра "Период агрегации"
     *
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return LinqWrapper.from(repository.getTimeUnits())
                .filter(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(BaseEntity item) {
                        return item.id == 3 || item.id == 4; // неделя или месяц
                    }
                })
                .toArray(BaseEntity.class);
    }

    /**
     * Возвращает данные для фильтра "Система-источник"
     *
     * @return
     */
    public BaseEntity[] getModules() {
        return repository.getModules();
    }

    /**
     * Возвращает данные для фильтра "Учет проверок по 7 полям"
     *
     * @return
     */
    public BaseEntityWithCode[] getSevenFieldsCheck()  {
        return repository.getSevenFieldsCheck();
    }
}
