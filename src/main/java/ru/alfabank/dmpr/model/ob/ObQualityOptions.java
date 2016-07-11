package ru.alfabank.dmpr.model.ob;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.model.BasePeriodOptions;


/**
 * Набор параметров для получения данных на витрине "Показатели качества ОБ".
 */
public class ObQualityOptions extends BasePeriodOptions implements Cloneable {
    /**
     * Значение фильтра "Группа показателей"
     */
    public long kpiKindId;

    /**
     * Значение фильтра "Показатель"
     */
    public String kpiId;

    /**
     * Значение фильтра "Дирекция"
     */
    public String[] directionIds;

    /**
     * Значение фильтра "Регион"
     */
    public String[] regionIds;


    public String doudrFlag;

    public LocalDateTime startDate;
    public LocalDateTime endDate;

    @Override
    protected ObQualityOptions clone() throws CloneNotSupportedException {
        return (ObQualityOptions)super.clone();
    }

    public ObQualityOptions copy() {
        ObQualityOptions result;

        try{
            result = this.clone();
        } catch(CloneNotSupportedException e){
            result = new ObQualityOptions();
        }

        return result;
    }
}
