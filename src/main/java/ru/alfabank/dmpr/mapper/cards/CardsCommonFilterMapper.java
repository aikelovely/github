package ru.alfabank.dmpr.mapper.cards;

import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.cards.*;

/**
 * Маппер, который используется на обоих витринах Карт ФЛ
 */
public interface CardsCommonFilterMapper {
    /**
     * Возвращает список макро-регионов
     * @return
     */
    BaseEntity[] getMacroRegions();

    /**
     * Возвращает список регионов
     * @return
     */
    ChildEntity[] getRegions();

    /**
     * Возвращает список городов
     * @return
     */
    ChildEntity[] getCities();

    /**
     * Возвращает список отделений
     * @return
     */
    Branch[] getBranches();

    /**
     * Возвращает список типов отделений
     * @return
     */
    BaseEntity[] getBranchTypes();

    /**
     * Возвращает список категорий карты
     * @return
     */
    BaseEntity[] getCardCategories();

    /**
     * Возвращает список значений для фильтра "Дебет/Кредит"
     * @return
     */
    BaseEntity[] getDebitOrCredits();

    /**
     * Возвращает список значений для фильтра "Сегмент клиента"
     * @return
     */
    BaseEntity[] getClientSegments();

    /**
     * Возвращает список значений для фильтра "Единица времени"
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает список значений для фильтра "Единица сети"
     * @return
     */
    BaseEntity[] getSystemUnits();

    /**
     * Возвращает список значений для фильтра "Признак МВК"
     * @return
     */
    BaseEntity[] getMvkSigns();

    /**
     * Возвращает список значений для фильтра "Скорость перевыпуска"
     * @return
     */
    BaseEntity[] getReissueSpeeds();

    /**
     * Возвращает список значений для фильтра "Признак ЗП"
     * @return
     */
    BaseEntity[] getZPSigns();

    /**
     * Возвращает список значений для фильтра "Тип ПУ"
     * @return
     */
    ChildEntity[] getPYTypes();
}
