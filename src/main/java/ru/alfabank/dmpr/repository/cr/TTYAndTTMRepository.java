package ru.alfabank.dmpr.repository.cr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.cr.TTYAndTTMMapper;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Dynamic;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Rating;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;

/**
 * Репозиторий, для загрузки графиков витрины "Корпоративный бизнес".
 */
@Repository
public class TTYAndTTMRepository {
    @Autowired
    TTYAndTTMMapper mapper;

    /**
     * Метод для загрузки рейтинга.
     * @param options Текущие значения фильтров
     * @return
     */
    public Rating[] getRating(TTYAndTTMOptions options) {
        return mapper.getRating(options);
    }

    /**
     * Метод для загрузки рейтинга в виде таблицы.
     * @param options Текущие значения фильтров
     * @return
     */
    public Rating[] getRatingDetails(TTYAndTTMOptions options) {
        return mapper.getRatingDetails(options);
    }

    /**
     * Метод для загрузки динамики.
     * @param options Текущие значения фильтров
     * @return
     */
    public Dynamic[] getDynamic(TTYAndTTMOptions options) {
        return mapper.getDynamic(options);
    }

    /**
     * Метод для загрузки динамики в виде таблицы
     * @param options Текущие значения фильтров
     * @return
     */
    public Dynamic[] getDynamicDetails(TTYAndTTMOptions options) {
        return mapper.getDynamicDetails(options);
    }
}
