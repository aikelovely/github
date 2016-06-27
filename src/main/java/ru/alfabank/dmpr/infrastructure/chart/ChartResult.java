package ru.alfabank.dmpr.infrastructure.chart;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Arrays;
import java.util.Map;

/**
 * Структура для сериализации данных одной диаграммы виджета.
 * Содержит набор серий (линий) диаграммы, а также bag с дополнительными параметрами данной диаграммы
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartResult {
    // набор дополнительных данных
    public Map<String, Object> bag;
    // массив серий
    public Series[] series;

    public ChartResult(Series[] series) {
        this.series = series;
    }

    public ChartResult(Series[] series, Map<String, Object> bag) {
        this(series);
        this.bag = bag;
    }

    @Override
    public String toString() {
        return "ChartResult{" +
                "bag=" + bag +
                ", series=" + Arrays.toString(series) +
                '}';
    }
}
