package ru.alfabank.dmpr.infrastructure.helper;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BasePeriodOptions;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.Week;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PeriodSelectHelper {
    private static Locale ruLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
    private static SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    public static PeriodSelectOption[] getMonths(int year){
        String[] months = new DateFormatSymbols(ruLocale).getMonths();
        LocalDate date = new LocalDate(year, 1, 1);
        PeriodSelectOption[] result = new PeriodSelectOption[12];
        for(int i = 0; i < 12; i++) {
            result[i] = new PeriodSelectOption(i, i,
                    months[i] + " '" + date.toString("YY"), date,
                    date.plusMonths(1).minusDays(1));

            date = date.plusMonths(1);
        }
        return result;
    }

    public static PeriodSelectOption getMonthById(int year, final long id) {
        return LinqWrapper.from(getMonths(year)).firstOrNull(new Predicate<PeriodSelectOption>() {
            @Override
            public boolean check(PeriodSelectOption item) {
                return item.id == id;
            }
        });
    }

    public static PeriodSelectOption getMonthByDate(final LocalDate date) {
        return LinqWrapper.from(getMonths(date.getYear())).firstOrNull(new Predicate<PeriodSelectOption>() {
            @Override
            public boolean check(PeriodSelectOption item) {
                return DateHelper.isBetween(date, item.startDate, item.endDate);
            }
        });
    }

    public static String formatDate(LocalDate date) {
        return df.format(date.toDate());
    }

    public static PeriodSelectOption[] getWeeks(Week[] weeks){
        return LinqWrapper.from(weeks)
                .select(new Selector<Week, PeriodSelectOption>() {
                    @Override
                    public PeriodSelectOption select(Week week) {
                        String name = getWeekName(week);
                        return new PeriodSelectOption(week.id, week.weekNum, name, week.startDate, week.endDate);
                    }
                })
                .toArray(PeriodSelectOption.class);
    }

    public static String getWeekName(Week week) {
        return "Неделя " + week.weekNum + " (" + df.format(week.startDate.toDate())
                + "-" + df.format(week.endDate.toDate()) + ")";
    }

    public static PeriodSelectOption getWeekByDate(final LocalDate date, Week[] weeks) {
        Week week = LinqWrapper.from(weeks).firstOrNull(new Predicate<Week>() {
            @Override
            public boolean check(Week item) {
                return DateHelper.isBetween(date, item.startDate, item.endDate);
            }
        });

        return week != null ? getWeekByYearAndNum(week.year, week.weekNum, weeks) : null;
    }

    public static LocalDate[] getDatesByBasePeriodOptions(BasePeriodOptions options, Week[] weeks){

        LocalDate startDate = new LocalDate();
        LocalDate endDate = new LocalDate();

        switch (options.timeUnitId) {
            case 3:
                Week startWeek = getWeekById(options.startDateId, weeks);
                Week endWeek = getWeekById(options.endDateId, weeks);

                if(startWeek != null) startDate = startWeek.startDate;
                if(endWeek != null) endDate = endWeek.endDate;
                break;
            case 4:
                PeriodSelectOption startMonth = getMonthById(options.startYear.getYear(), options.startDateId);
                PeriodSelectOption endMonth = getMonthById(options.endYear.getYear(), options.endDateId);

                if(startMonth != null) startDate = startMonth.startDate;
                if(endMonth != null) endDate = endMonth.endDate;
                break;
            default:
                break;
        }

        if (endDate == null) {
            endDate = startDate;
        }

        return new LocalDate[]{ startDate, endDate };
    }

    public static Week getWeekById(final long id, Week[] weeks) {
        return LinqWrapper.from(weeks).firstOrNull(new Predicate<Week>() {
            @Override
            public boolean check(Week item) {
                return item.id == id;
            }
        });
    }

    public static PeriodSelectOption getWeekByYearAndNum(final int year, final long num, Week[] weeks) {
        Week week = LinqWrapper.from(weeks).firstOrNull(new Predicate<Week>() {
            @Override
            public boolean check(Week item) {
                return item.year == year && item.weekNum == num;
            }
        });
        return week != null ? new PeriodSelectOption(week.id, week.weekNum, getWeekName(week), week.startDate, week.endDate) : null;
    }
}
