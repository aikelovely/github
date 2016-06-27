package ru.alfabank.dmpr.infrastructure.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.*;
import org.joda.time.Period;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.*;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * helper для формирования списков месяцев, недель в заданном году, пока используется только в obQuality, в
 * дальнейшем возможно потребуется реализация для других витрин
 */
public class DateArraysHelper {
    private static Locale ruLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();

    /**
     * список месяцев
     */
    public static PeriodEntity[] getMonthsArray(int year){
        String[] months = new DateFormatSymbols(ruLocale).getMonths();
        LocalDateTime date = new LocalDateTime(year, 1, 1, 0, 0, 0, 0);
        PeriodEntity[] result = new PeriodEntity[12];
        for(int i = 0; i < 12; i++) {
            result[i] = new PeriodEntity(i, months[i] + " '" + date.toString("YY"), date, date.plusMonths(1).minusDays(1));;
            date = date.plusMonths(1);
        }
        return result;
    }

    public static PeriodEntity getMonthById(int year, final long id) {
        return LinqWrapper.from(getMonthsArray(year)).firstOrNull(new Predicate<PeriodEntity>() {
            @Override
            public boolean check(PeriodEntity item) {
                return item.id == id;
            }
        });
    }

    /**
     * список недель
     */
    public static PeriodEntity[] getWeeksArray(int year){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Period weekPeriod = new Period().withWeeks(1);

        DateTime startDate = new DateTime(year, 1, 1, 0, 0, 0, 0 );
        DateTime endDate = new DateTime(year + 1, 1, 1, 0, 0, 0, 0 );

        int delta = startDate.dayOfWeek().get() > 4 ? 1 : -1;

        while(startDate.getDayOfWeek() != DateTimeConstants.MONDAY ){
            startDate = startDate.plusDays(delta);
        }

        delta = endDate.dayOfWeek().get() >= 4 ? 1 : -1;
        while( endDate.getDayOfWeek() != DateTimeConstants.MONDAY ){
            endDate = endDate.plusDays(delta);
        }

        ArrayList<BaseEntity> result = new ArrayList();

        int weekNo = 1;
        Interval i = new Interval(startDate, weekPeriod);
        while(i.getStart().isBefore( endDate )){
            PeriodEntity ent = new PeriodEntity(weekNo, "",
                    i.getStart().toDateTime().withTimeAtStartOfDay().toLocalDateTime(),
                    i.getEnd().minusMillis(1).toDateTime().withTimeAtStartOfDay().toLocalDateTime());
            ent.name = "Неделя " + ent.id + " (" + df.format(ent.start.toDate()) + "-" + df.format(ent.end.toDate()) + ")";
            result.add(ent);
            i = new Interval(i.getStart().plus(weekPeriod), weekPeriod);
            weekNo++;
        }
        return result.toArray(new PeriodEntity[result.size()]);
    }

    public static PeriodEntity getWeekById(int year, final long id) {
        return LinqWrapper.from(getWeeksArray(year)).firstOrNull(new Predicate<PeriodEntity>() {
            @Override
            public boolean check(PeriodEntity item) {
                return item.id == id;
            }
        });
    }

    /**
     * список дней
     */
    public static PeriodEntity[] getDaysArray(int year){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        DateTime startDate = new DateTime(year, 1, 1, 0, 0, 0, 0 );
        DateTime endDate = new DateTime(year + 1, 1, 1, 0, 0, 0, 0 );

        ArrayList<BaseEntity> result = new ArrayList();

        while(startDate.compareTo(endDate) < 0){
            PeriodEntity ent = new PeriodEntity(startDate.getDayOfYear(), "", startDate.toLocalDateTime(), startDate.toLocalDateTime());
            ent.name = "День " + ent.id + " (" + df.format(ent.start.toDate()) + ")";
            result.add(ent);
            startDate = startDate.plusDays(1);
        }
        return result.toArray(new PeriodEntity[result.size()]);
    }

    public static PeriodEntity getDayById(int year, final long id) {
        return LinqWrapper.from(getDaysArray(year)).firstOrNull(new Predicate<PeriodEntity>() {
            @Override
            public boolean check(PeriodEntity item) {
                return item.id == id;
            }
        });
    }

    /**
     * список кварталов
     */
    public static PeriodEntity[] getQuartalsArray(int year){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Period quartalPeriod = new Period().withMonths(3);

        DateTime startDate = new DateTime(year, 1, 1, 0, 0, 0, 0 );
        DateTime endDate = new DateTime(year + 1, 1, 1, 0, 0, 0, 0 );

        ArrayList<BaseEntity> result = new ArrayList();

        Interval i = new Interval(startDate, quartalPeriod);
        while(i.getStart().isBefore( endDate )){
            PeriodEntity ent = new PeriodEntity((i.getStart().getMonthOfYear()/3) + 1, "",
                    i.getStart().toDateTime().withTimeAtStartOfDay().toLocalDateTime(),
                    i.getEnd().minusMillis(1).toDateTime().withTimeAtStartOfDay().toLocalDateTime());
            ent.name = "Квартал " + ent.id + " (" + df.format(ent.start.toDate()) + "-" + df.format(ent.end.toDate()) + ")";
            result.add(ent);
            i = new Interval(i.getStart().plus(quartalPeriod), quartalPeriod);
        }
        return result.toArray(new PeriodEntity[result.size()]);
    }

    public static PeriodEntity getQuartalById(int year, final long id) {
        return LinqWrapper.from(getQuartalsArray(year)).firstOrNull(new Predicate<PeriodEntity>() {
            @Override
            public boolean check(PeriodEntity item) {
                return item.id == id;
            }
        });
    }

    /**
     * список годов
     */
    public static PeriodEntity[] getYearsArray(int year){
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

        DateTime startDate = new DateTime(year, 1, 1, 0, 0, 0, 0 );
        DateTime endDate = startDate.plusYears(1).minusDays(1);

        return new PeriodEntity[]{
                new PeriodEntity(startDate.getYear(), "Год " + startDate.getYear(),
                        startDate.toLocalDateTime(), endDate.toLocalDateTime())
        };
    }

    public static PeriodEntity getYearById(int year, final long id) {
        return LinqWrapper.from(getYearsArray(year)).firstOrNull(new Predicate<PeriodEntity>() {
            @Override
            public boolean check(PeriodEntity item) {
                return item.id == id;
            }
        });
    }

    public static PeriodEntity[] getDatesArrayByTimeUnitId(LocalDate yearDate, long timeUnitId) {
        int year = yearDate.getYear();
        switch((int)timeUnitId){
            case 2: return getDaysArray(year);
            case 3: return getWeeksArray(year);
            case 4: return getMonthsArray(year);
            case 5: return getQuartalsArray(year);
            case 6: return getYearsArray(year);
            default: return new PeriodEntity[0];
        }
    }

    public static PeriodEntity[] getPeriodArrayByTimeUnitIdBounded(final LocalDate startDate, final LocalDate endDate, long timeUnitId){
        return LinqWrapper.from(
                ArrayUtils.addAll(getDatesArrayByTimeUnitId(startDate, timeUnitId),
                        getDatesArrayByTimeUnitId(endDate, timeUnitId)))
                .distinctBy(new Selector<PeriodEntity, LocalDateTime>() {
                    @Override
                    public LocalDateTime select(PeriodEntity periodEntity) {
                        return periodEntity.start;
                    }
                })
                .filter(new Predicate<PeriodEntity>() {
                    @Override
                    public boolean check(PeriodEntity item) {
                        LocalDate itemStartDate = item.start.toLocalDate();
                        return itemStartDate.compareTo(startDate) >= 0 && itemStartDate.compareTo(endDate) <= 0;
                    }
                })
                .sort(new Selector<PeriodEntity, LocalDateTime>() {
                    @Override
                    public LocalDateTime select(PeriodEntity periodEntity) {
                        return periodEntity.start;
                    }
                })
                .toArray(PeriodEntity.class);
    }

    public static PeriodEntity[] getDatesByBasePeriodOptions(BasePeriodOptions options){

        PeriodEntity endPeriod = new PeriodEntity();
        PeriodEntity startPeriod = new PeriodEntity();

        switch (options.timeUnitId) {
            case 2:
                endPeriod = getDayById(options.endYear.getYear(), options.endDateId);
                startPeriod = getDayById(options.startYear.getYear(), options.startDateId);
                break;
            case 3:
                endPeriod = getWeekById(options.endYear.getYear(), options.endDateId);
                startPeriod = getWeekById(options.startYear.getYear(), options.startDateId);
                break;
            case 4:
                endPeriod = getMonthById(options.endYear.getYear(), options.endDateId);
                startPeriod = getMonthById(options.startYear.getYear(), options.startDateId);
                break;
            case 5:
                endPeriod = getQuartalById(options.endYear.getYear(), options.endDateId);
                startPeriod = getQuartalById(options.startYear.getYear(), options.startDateId);
                break;
            case 6:
                endPeriod = getYearById(options.endYear.getYear(), options.endDateId);
                startPeriod = getYearById(options.startYear.getYear(), options.startDateId);
                break;
            default:
                break;
        }

        if (endPeriod == null) {
            endPeriod = startPeriod;
        }

        return new PeriodEntity[]{ startPeriod, endPeriod };
    }
}
