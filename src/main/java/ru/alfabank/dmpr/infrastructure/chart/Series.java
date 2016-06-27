package ru.alfabank.dmpr.infrastructure.chart;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Map;

/**
 * Структура для сериализации данных одной серии (линии) диаграммы виджета.
 * Серия имеет тип, имя и набор точек
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Series {
    public String name;
    public ChartType type;
    public Point[] data;
    public Color color;

    // набор дополнительных данных
    public Map<String, Object> bag;

    public Series() {
    }

    public Series(String name) {
        this.name = name;
    }

    public Series(Point[] data) {
        this.data = data;
    }

    public Series(String name, Point[] data) {
        this(name);
        this.data = data;
    }

    public Series(String name, Point[] data, ChartType type) {
        this(name, data);
        this.type = type;
    }

    public Series(String name, Point[] data, ChartType type, Color color) {
        this(name, data);
        this.type = type;
        this.color = color;
    }

    public Series(String name, Point[] data, Map<String, Object> bag) {
        this(name, data);
        this.bag = bag;
    }

    @Override
    public String toString() {
        return "Series{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
