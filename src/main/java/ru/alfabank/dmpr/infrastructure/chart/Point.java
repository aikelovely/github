package ru.alfabank.dmpr.infrastructure.chart;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;

import java.util.TimeZone;

/**
 * Структура для сериализации данных одной точки серии (линии) диаграммы виджета.
 * Точка имеет две координаты, цвет, имя, а также поле tag, в которое можно поместить
 * дополнительные параметры точки
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Point {
    public Double x;
    public Double y;
    public Color color;
    public String name;
    public Object tag;
    public String customHTMLTooltip;

    public Point() {
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point(LocalDate x, Double y) {
        if (x != null) {
            this.x = DateHelper.toUnixTime(x);
        } else
            this.x = null;

        this.y = y;
    }

    public Point(Double x, String name) {
        this.x = x;
        this.name = name;
    }

    public Point(Double x, Double y, String name) {
        this(x, y);
        this.name = name;
    }

    public Point(LocalDate x, Double y, String name) {
        this(x, y);
        this.name = name;
    }


    public static Point withY(Double y) {
        Point point = new Point();
        point.y = y;
        return point;
    }

    public static Point withY(Double y, String name) {
        Point point = withY(y);
        point.name = name;
        return point;
    }

    public static Point withY(double y, String name, Color color) {
        Point point = withY(y, name);
        point.color = color;
        return point;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", color='" + color + '\'' +
                ", name='" + name + '\'' +
                ", tag=" + tag + '\'' +
                ", customHTMLTooltip='" + customHTMLTooltip + '\'' +
                '}';
    }
}
