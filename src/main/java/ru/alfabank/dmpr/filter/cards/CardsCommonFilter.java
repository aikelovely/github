package ru.alfabank.dmpr.filter.cards;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.spring.Param;
import ru.alfabank.dmpr.repository.cards.CardsCommonFilterRepository;
import ru.alfabank.dmpr.model.BaseEntity;

/**
 * Базовый класс для сервисов-фильтров витрин Карт ФЛ
 */
public abstract class CardsCommonFilter {
    @Autowired
    private CardsCommonFilterRepository repository;

    /**
     * Возвращает список макро-регионов
     * @return
     */
    public BaseEntity[] getMacroRegions() {
        return repository.getMacroRegions();
    }

    /**
     * Возвращает список "Единица времени"
     * @return
     */
    public BaseEntity[] getTimeUnits() {
        return BaseEntity.toBaseEntities(repository.getTimeUnits());
    }

    /**
     * Возвращает список "Единица сети"
     * @return
     */
    public BaseEntity[] getSystemUnits() {
        return repository.getSystemUnits();
    }

    /**
     * Возвращает список типов отделений
     * @return
     */
    public BaseEntity[] getBranchTypes() {
        return repository.getBranchTypes();
    }

    /**
     * Возвращает список регионов
     * @return
     */
    public BaseEntity[] getRegions() {
        return BaseEntity.toBaseEntities(repository.getRegions());
    }

    /**
     * Возвращает список городов
     * @return
     */
    public BaseEntity[] getCities() {
        return BaseEntity.toBaseEntities(repository.getCities());
    }

    /**
     * Возвращает список категорий карты
     * @return
     */
    public BaseEntity[] getCardCategories() {
        return repository.getCardCategories();
    }

    /**
     * Возвращает список "Дебет/Кредит"
     * @return
     */
    public BaseEntity[] getDebitOrCredits() {
        return repository.getDebitOrCredits();
    }

    /**
     * Возвращает список "Сегмент клиента"
     * @return
     */
    public BaseEntity[] getClientSegments() {
        return repository.getClientSegments();
    }

    /**
     * Возвращает список "Скорость перевыпуска"
     * @return
     */
    public BaseEntity[] getReissueSpeeds() {
        return repository.getReissueSpeeds();
    }

    /**
     * Возвращает список "Признак МВК"
     * @return
     */
    public BaseEntity[] getMvkSigns() {
        return repository.getMvkSigns();
    }

    /**
     *
     *
     *
     *
     *
     *
     * Возвращает список "Признак ЗП"
     * @return
     */
    public BaseEntity[] getZPSigns() {
        return repository.getZPSigns();
    }

    /**
     * Возвращает список "Тип ПУ"
     * @return
     */
    public BaseEntity[] getPYTypes() {
        return BaseEntity.toBaseEntities(repository.getPYTypes());
    }

    /**
     * Возвращает список "Тип ПУ" отфильтрованный по "Признак ЗП"
     * @return
     */
    public BaseEntity[] getPYTypesByZPSignIds(@Param("zpSignIds[]") final long[] zpSignIds) {
        return BaseEntity.toBaseEntities(repository.getPYTypesByZPSignIds(zpSignIds));
    }

    /**
     * Возвращает список городов, входящих в выбранные макро-регионы
     * @return
     */
    public BaseEntity[] getCitiesByMacroRegionIds(@Param("macroRegionIds[]") final long[] macroRegionIds) {
        return BaseEntity.toBaseEntities(repository.getCitiesByMacroRegionIds(macroRegionIds));
    }

    /**
     * Возвращает список городов, входящих в выбранные регионы
     * @return
     */
    public BaseEntity[] getCitiesByRegionIds(@Param("regionIds[]") final long[] regionIds) {
        return BaseEntity.toBaseEntities(repository.getCitiesByRegionIds(regionIds));
    }

    /**
     * Возвращает список отделений, входящих в выбранные макро-регионы, регионы, города и с выбранными типами отделений
     * @return
     */
    public BaseEntity[] getBranches(
            @Param("macroRegionIds[]") final long[] macroRegionIds,
            @Param("cityIds[]") final long[] cityIds,
            @Param("branchTypeIds[]") final long[] branchTypeIds) {
        return BaseEntity.toBaseEntities(repository.getBranches(macroRegionIds, cityIds, branchTypeIds));
    }
}
