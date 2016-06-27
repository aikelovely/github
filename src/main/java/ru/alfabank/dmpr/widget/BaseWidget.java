package ru.alfabank.dmpr.widget;

import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Базовый класс для всех типов виджетов
 *
 * @param <TOptions> тип класса параметров витрины
 * @param <TResult>  тип результата, возвращаемого единственным бизнес-методом виджета
 */
public abstract class BaseWidget<TOptions extends BaseOptions, TResult> {
    private final Class<TOptions> optionsClass;

    public BaseWidget(Class<TOptions> optionsClass) {
        this.optionsClass = optionsClass;
    }

    public abstract TResult getData(TOptions options);

    public Class<TOptions> getOptionsClass() {
        return optionsClass;
    }
}
