package ru.alfabank.dmpr.model.ob;

import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Дополнительные параметры витрины "Показатели качества ОБ"
 */
public class ObQualityAdditionalOptions extends BaseOptions {
    /**
     * Код уровня детализации
     */
    public String systemUnitCode;
    public String doudrFlag;

    /**
     * Уровень детализации
     */
    public Integer detailsMode;
}
