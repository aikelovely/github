package ru.alfabank.dmpr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Базовый для всех классов параметров всех витрин
 */
public abstract class BaseOptions {
    public int drillDownLevel;
    public String widgetUrl;

    /**
     * @return имя витрины
     */
    @JsonIgnore
    private String getShowcaseName() {
        String simpleName = getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - "Options".length());
    }

    /**
     * @return имя виджета
     */
    @JsonIgnore
    public String getWidgetName() {
        return widgetUrl == null ? null : widgetUrl.substring(getShowcaseName().length());
    }
}
