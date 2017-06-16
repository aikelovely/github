package ru.alfabank.dmpr.model.leaderBoard;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BasePeriodOptions;

/**
 * Набор параметров для получений данных о KPI. Не поля обязательны для заполнения.
 * @see ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository
 */

public class LeaderBoardOptions extends BasePeriodOptions implements Cloneable {
    /**
     * Может содержать значение фильтра "Год", "Месяц" или "Период, с" в зависимости от графика/витрины.
     * Обязательное поле.
     */
//    public LocalDate startDate;

    /**
     * Может содержать значение фильтра "Период, по", либо значение, рассчитаное автоматически на основе startDate,
     * quarter и dateIntervalType
     */
    public LocalDate endDate;

    /**
     * Список KPI Id. Обязательное поле.
     */
    public long[] kpiIds;

    /**
     * Период агрегации. Обязательное поле. Конвертируется в/из enum IntervalType.
     * TODO: Добавить custom MyBatis handler для автоматической конвертации, если IntervalType будет использоваться где-нибудь еще.
     * @see IntervalType
     */
    public String dateIntervalType;
    public String  kpiId;
    /**
     * ID группы подразделения. Обязательное поле, может содержать значение 0, в случае, если необходимо получить данные
     * по всем дирекциям.
     */
    public Long divisionGroupId;

    /**
     * Квартал. Вспомогательное поле, используется для расчета endDate
     */
    public int quarter;
//    public int timeUnitId;
    public int kpi14Value;

    /**
     * Вспомогательное поле. Используется для отрисовки block chart
     */
    public long kpiIdForBlockChart;
 //   public LocalDate startYear;

    /**
     * Значение фильтра "Год" используется в связке с фильтром "Период, по"
     * Обязательное поле.
     */
    public LocalDate endYear;

    /**
     * Может содержать значение фильтра "Период, с"
     * Обязательное поле.
     */
  //  public long startDateId;

    /**
     * Может содержать значение фильтра "Период, по"
     * Обязательное поле.
     */
   // public long endDateId;


    @Override
    protected BasePeriodOptions clone() throws CloneNotSupportedException {
        return (LeaderBoardOptions)super.clone();
    }

    public BasePeriodOptions copy() {
        BasePeriodOptions result;

        try{
            result = this.clone();
        } catch(CloneNotSupportedException e){
            result = new LeaderBoardOptions();
        }

        return result;
    }
}
