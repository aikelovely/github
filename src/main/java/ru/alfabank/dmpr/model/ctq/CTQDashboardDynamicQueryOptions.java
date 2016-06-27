package ru.alfabank.dmpr.model.ctq;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.Week;

/**
 * Набор параметров для запросов к БД для получения данных витрины показателей CTQ.
 */
public class CTQDashboardDynamicQueryOptions {
    /**
     * Может содержать значение фильтра "Тип периода"
     * Обязательное поле.
     */
    public int timeUnitId;

    /**
     * Может содержать значение даты начала периода
     * Обязательное поле.
     */
    public LocalDate startDate;

    /**
     * Может содержать значение даты конца периода
     * Обязательное поле.
     */
    public LocalDate endDate;

    /**
     * Может содержать список кодов показателей
     * Необязательное поле.
     */
    public String[] kpiIds;

    /**
     * Конструктор
     * @param options - изначально принимаемые с клиента параметры фильтров
     */
    public CTQDashboardDynamicQueryOptions(CTQDashboardDynamicOptions options, Week[] weeks){
        timeUnitId = options.timeUnitId;
        kpiIds = options.kpiIds;

        options.endYear = options.startYear;
        if (options.endDateId == 0) {
            options.endDateId = options.startDateId;
        }

        LocalDate[] periods = PeriodSelectHelper.getDatesByBasePeriodOptions(options, weeks);

        startDate = periods[0];
        endDate = periods[1];
    }
}
