package ru.alfabank.dmpr.mapper.cr;

import ru.alfabank.dmpr.model.cr.TTYAndTTM.Dynamic;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;

/**
 * Маппер, для загрузки графиков витрины "Корпоративный бизнес".
 */
public interface TTYAndTTMMapper {
    /**
     * Метод для загрузки рейтинга.
      * @param options Текущие значения фильтров
     * @return
     */
    Rating[] getRating(TTYAndTTMOptions options);

    /**
     * Метод для загрузки рейтинга в виде таблицы.
     * @param options Текущие значения фильтров
     * @return
     */
    Rating[] getRatingDetails(TTYAndTTMOptions options);

    /**
     * Метод для загрузки динамики.
     * @param options Текущие значения фильтров
     * @return
     */
    Dynamic[] getDynamic(TTYAndTTMOptions options);

    /**
     * Метод для загрузки динамики в виде таблицы
     * @param options Текущие значения фильтров
     * @return
     */
    Dynamic[] getDynamicDetails(TTYAndTTMOptions options);
}
