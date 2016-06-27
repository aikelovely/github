package ru.alfabank.dmpr.widget;

import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;

/**
 * Базовый класс для всех виджетов-диаграмм
 *
 * @param <TOptions> тип класса параметров витрины
 */
public abstract class BaseChart<TOptions extends BaseOptions> extends BaseWidget<TOptions, ChartResult[]> {
    public BaseChart(Class<TOptions> tOptionsClass) {
        super(tOptionsClass);
    }
}
