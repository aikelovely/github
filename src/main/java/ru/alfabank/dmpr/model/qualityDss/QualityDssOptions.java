package ru.alfabank.dmpr.model.qualityDss;

import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.model.BasePeriodOptions;


/**
 * Набор параметров для получения данных на витрине "Показатели качества ОБ".
 */
public class QualityDssOptions extends BasePeriodOptions implements Cloneable {


    /**
     * Значение фильтра "Подразделение"
     */

    public String[] divisionIds;


    public Integer reportIds;

    public LocalDateTime startDate;
    public LocalDateTime endDate;



    @Override
    protected QualityDssOptions clone() throws CloneNotSupportedException {
        return (QualityDssOptions)super.clone();
    }

    public QualityDssOptions copy() {
        QualityDssOptions result;

        try{
            result = this.clone();
        } catch(CloneNotSupportedException e){
            result = new QualityDssOptions();
        }

        return result;
    }
}
