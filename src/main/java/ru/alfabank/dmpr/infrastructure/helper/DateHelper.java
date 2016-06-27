package ru.alfabank.dmpr.infrastructure.helper;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * thread-safe
 */
public class DateHelper {
    /**
     * static helper class
     */
    private DateHelper() {
    }

    private static final TimeZone TIME_ZONE = TimeZone.getDefault();

    //TODO replace with native invoke of LocalDate
    public static String createDateName(LocalDate date) {
        // SimpleDateFormat.format is not thread-safe
        return "D" + new SimpleDateFormat("yyyyMMddHH").format(date.toDate());
    }

    public static double toUnixTime(LocalDate date) {
        DateTime dateTime = date.toDateTimeAtStartOfDay();
        long time = dateTime.getMillis();
        return time + TIME_ZONE.getOffset(time);
    }


    /**
     * Приводит дату к строке с выбранным форматом.
     * Подробное о форматах можно почитать вот тут http://www.joda.org/joda-time/key_format.html
     * @param date LocalDate
     * @param pattern Формат.
     * @return отформатированное значение
     */
    public static String format(LocalDate date, String pattern){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return date.toString(formatter);
    }

    /**
     * Приводит дату и время к строке с выбранным форматом.
     * Подробное о форматах можно почитать вот тут http://www.joda.org/joda-time/key_format.html
     * @param dateTime LocalDateTime
     * @param pattern Формат.
     * @return отформатированное значение
     */
    public static String format(LocalDateTime dateTime, String pattern){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return dateTime.toString(formatter);
    }

    /**
     * Приводит секунды к формату hh.mm.ss;
     * @param value Строка с результатом
     * @return
     */
    public static String secondsAsString(Integer value){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("mm:ss");
        String result = fmt.print(value * 1000);
        int hours = value / 3600;
        if(hours >= 1){
            result = hours + ":" + result;
        } else {
            result = "0:" + result;
        }
        return result;
    }

    /**
     * Приводит часы к формату X дн. Y ч.
     * @param value
     * @return
     */
    public static String hoursToDDHH(double value){
        if(value > 24){
            int days = (int)value / 24;
            int hours = (int)Math.round(value - days * 24);

            return days + " дн. " + hours + " ч.";
        }
        int hours = (int)Math.round(value);
        return  hours + " ч.";
    }

    /**
     * Проверяем находится ли дата в выбранном диапазоне
     * @param value значение для сравнения
     * @param lowBound - нижняя граница периода
     * @param highBound - нижняя граница периода
     * @return признак нахождения
     */
    public static boolean isBetween(LocalDate value, LocalDate lowBound, LocalDate highBound) {
        return (value.isAfter(lowBound) || value.isEqual(lowBound)) && (value.isBefore(highBound) || value.isEqual(highBound));
    }
}
