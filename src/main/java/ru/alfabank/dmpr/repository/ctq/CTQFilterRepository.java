package ru.alfabank.dmpr.repository.ctq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.ctq.CTQFilterMapper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.ctq.CTQDashboardKPIEntity;
import ru.alfabank.dmpr.model.ctq.CTQLayoutItem;

/**
 * Репозиторий для фильтров витрин CTQDashboard
 */
@Repository
public class CTQFilterRepository {
    @Autowired
    CTQFilterMapper mapper;

    /**
     * Возвращает список значений для фильтра "Тип периода"
     * @return Список значений
     */
    public BaseEntity[] getTimeUnits(){
        return mapper.getTimeUnits();
    }

    /**
     * Возвращает список показателей
     * @return Список показателей
     */
    public CTQDashboardKPIEntity[] getKPIs(){
        return mapper.getKPIs();
    }

    /**
     * Возвращает сущность показателя по коду
     * @param code код показателя
     * @return Сущность показателя
     */
    public CTQDashboardKPIEntity getKPIbyCode(final String code){
        return LinqWrapper.from(getKPIs()).firstOrNull(new Predicate<CTQDashboardKPIEntity>() {
            @Override
            public boolean check(CTQDashboardKPIEntity item) {
                return item.code.equals(code);
            }
        });
    }

    /**
     * Возвращает сущность показателя по id
     * @param id - идентификатор показателя
     * @return Сущность показателя
     */
    public CTQDashboardKPIEntity getKPIbyId(final String id){
        return LinqWrapper.from(getKPIs()).firstOrNull(new Predicate<CTQDashboardKPIEntity>() {
            @Override
            public boolean check(CTQDashboardKPIEntity item) {
                return item.id.equals(id);
            }
        });
    }

    public CTQLayoutItem[] getLayout(){
        return mapper.getLayout();
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return Week[]
     */
    public Week[] getWeeks(final int year) {
        return LinqWrapper.from(mapper.getWeeks())
                .filter(new Predicate<Week>() {
                    @Override
                    public boolean check(Week item) {
                        return item.year == year;
                    }
                })
                .toArray(Week.class);
    }

    /**
     * Возвращает список значений для фильтра "Период, с", "Период, по" для единицы времени Неделя
     * @return
     */
    public Week[] getWeeks() {
        return mapper.getWeeks();
    }
}
