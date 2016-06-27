package ru.alfabank.dmpr.model.pilAndCC;

import org.joda.time.LocalDate;
import ru.alfabank.dmpr.infrastructure.helper.PeriodOptions;
import ru.alfabank.dmpr.model.BaseOptions;

/**
 * Набор значений фильтров витрины "Персональные кредиты и кредитные карты. Витрина декомпозиции"
 */
public class PILAndCCOptions extends BaseOptions implements PeriodOptions {
    /**
     * Значеиие фильтра "Период, с"
     */
    public LocalDate startDate;

    /**
     * Значеиие фильтра "Период, по"
     */
    public LocalDate endDate;

    /**
     * Значеиие фильтра "KPI и метрики"
     */
    public long kpiId;

    /**
     * Значеиие фильтра "Тип продукта" (на витрине называется как "Продукт"
     */
    public long productTypeId;

    /**
     * Значеиие фильтра "Ручные проверки"
     */
    public Long manualCheckId;

    /**
     * Значеиие фильтра "Клиент"
     */
    public long[] clientSegmentIds;

    /**
     * Значеиие фильтра "Регион"
     */
    public long[] regionIds;

    /**
     * Значеиие фильтра "Город"
     */
    public long[] cityIds;

    /**
     * Значеиие фильтра "ДО/ККО"
     */
    public long[] doKkoIds;

    /**
     * Значеиие фильтра "Менеджер"
     */
    public long[] managerIds;

    /**
     * Значеиие фильтра "Тип значения"
     */
    public long valueTypeId;

    /**
     * Значеиие фильтра "Период агрегации"
     */
    public int timeUnitId;

    /**
     * Значеиие фильтра "Тип заявки"
     */
    public Long requestTypeId;

    /**
     * Значеиие фильтра "Уровень агрегации"
     */
    public int systemUnitId;

    /**
     * Значеиие фильтра "Сортировка рейтинга"
     */
    public int sortOrderId;

    /**
     * Значеиие фильтра "Система-источник"
     */
    public long[] sourceSystemIds;

    /**
     * Значеиие фильтра "Тип сортировки"
     */
    public int sortTypeId;

    /**
     * Id операции.
     */
    public int operationId;

    /**
     * Id вкладки, используется для отчетов и выгрузок
     */
    public Integer reportTypeId;

    @Override
    public LocalDate getStartDate() {
        return startDate;
    }

    @Override
    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public Integer getTimeUnitId() {
        return timeUnitId;
    }
}
