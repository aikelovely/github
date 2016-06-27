package ru.alfabank.dmpr.model.nom;

import org.joda.time.LocalDate;

/**
 * Строка таблицы "Детализация ВКП".
 */
public class NomDetailsTableRow {
    /**
     * Id конечного продукта
     */
    public Long id;

    /**
     * Отчетная дата. Последний день периода, за который было расчитано currentValue.
     */
    public LocalDate calcDate;

    /**
     * Номер недели/месяца в году.
     */
    public int periodNum;

    /**
     * Id "родительского" конечного продукта
     */
    public Long parentId;

    /**
     * Id группы подразделений
     */
    public Long divisionGroupId;

    /**
     * Название группы подразделений
     */
    public String divisionGroupName;

    /**
     * Название внутренного конечного продукта.
     */
    public String innerEndProductName;

    /**
     * Количество
     */
    public int factCount;

    /**
     * Признак ручной проверки
     */
    public boolean isManual;

    /**
     * Признак уровня данных (дирекция, КП, подКП)
     */
    public int level;

    public NomDetailsTableRow(
            Long id,
            LocalDate calcDate,
            int periodNum,
            Long parentId,
            Long divisionGroupId,
            String divisionGroupName,
            String innerEndProductName,
            int factCount,
            String isManual,
            int level) {
        this.id = id;
        this.calcDate = calcDate;
        this.periodNum = periodNum;
        this.parentId = parentId;
        this.divisionGroupId = divisionGroupId;
        this.divisionGroupName = divisionGroupName;
        this.innerEndProductName = innerEndProductName;
        this.factCount = factCount;
        this.isManual = isManual.equalsIgnoreCase("Y");
        this.level = level;
    }
}
