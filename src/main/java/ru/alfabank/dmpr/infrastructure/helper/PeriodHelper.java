package ru.alfabank.dmpr.infrastructure.helper;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import ru.alfabank.dmpr.model.Period;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * thread-safe
 */
public class PeriodHelper {
    /**
     * static helper class
     */
    private PeriodHelper() {
    }

    /**
     * Преобразует значение identity Периода в его экземпляр его enum отображения
     * @param timeUnitId identity
     * @return enum
     */
    public static Period getPeriod(Integer timeUnitId) {
        return Period.values()[timeUnitId];
    }


    /**
     * Формирует список временных тиков для данного экземпляра параметров витрины
     * @param options экземпляр параметров витрины
     * @return список временных тиков
     */
    public static List<LocalDate> getTicks(PeriodOptions options) {
        ArrayList<LocalDate> dates = new ArrayList<>();

        LocalDate startDate = options.getStartDate();
        LocalDate endDate = options.getEndDate();

        Period period = getPeriod(options.getTimeUnitId());
        switch (period) {
            case none:
                break;
            case hour:
                throw new NotImplementedException(period.toString());

            case day:
                while (!startDate.isAfter(endDate)) {
                    dates.add(startDate);
                    startDate = startDate.plusDays(1);
                }
                break;
            case week:
                startDate = startDate.withDayOfWeek(1);
                while (!startDate.isAfter(endDate)) {
                    dates.add(startDate);
                    startDate = startDate.plusDays(7);
                }
                break;
            case month:
                startDate = startDate.withDayOfMonth(1);
                while (!startDate.isAfter(endDate)) {
                    dates.add(startDate);
                    startDate = startDate.plusMonths(1);
                }
                break;
            case quarter:
                int currMonth = startDate.monthOfYear().get();
                int quarter = (currMonth - 1) / 3 + 1;
                int startMonth = (quarter - 1) * 3 + 1;
                startDate = startDate.withDayOfMonth(1).withMonthOfYear(startMonth);
                while (!startDate.isAfter(endDate)) {
                    dates.add(startDate);
                    startDate = startDate.plusMonths(3);
                }
                break;
            case year:
                startDate = startDate.withDayOfYear(1);
                while (!startDate.isAfter(endDate)) {
                    dates.add(startDate);
                    startDate = startDate.plusYears(1);
                }
                break;
            case all:
                dates.add(new DateTime(2001, 1, 1, 0, 0).toLocalDate());
                break;
        }

        return dates;
    }

    private static Locale RuLocale = new Locale("ru");
    /**
     * Форматирует строковое представление даты/времени на основании экземпляра параметров витрины
     * @param value дата/время
     * @param options экземпляр параметров витрины
     * @return строка
     */
    public static String dateAsStringByTimeUnitId(LocalDate value, PeriodOptions options) {
        Period period = getPeriod(options.getTimeUnitId());
        switch (period) {
            case hour:
                return DateTimeFormat.forPattern("HH:mm").withLocale(RuLocale).print(value);
            case day:
                return DateTimeFormat.forPattern("dd:MM").withLocale(RuLocale).print(value);
            case week:
                DateTimeFormatter format = DateTimeFormat.forPattern("dd:MM").withLocale(RuLocale);
                return format.print(value) + " - " + format.print(value.plusDays(6));
            case month:
                return DateTimeFormat.forPattern("MMMM, YYYY").withLocale(RuLocale).print(value);
            case quarter:
                int currMonth = value.monthOfYear().get();
                int quarter = (currMonth - 1) / 3 + 1;
                return "Q" + quarter + " '" + DateTimeFormat.forPattern("YY").withLocale(RuLocale).print(value);
            case year:
                return DateTimeFormat.forPattern("YYYY").print(value);
            default:
                DateTimeFormatter defaultFormat = DateTimeFormat.forPattern("dd.MM.YYYY").withLocale(RuLocale);
                return defaultFormat.print(options.getStartDate()) + "- " +
                        defaultFormat.print(options.getEndDate());
        }
    }
}
