package ru.alfabank.dmpr.mapper.cr;

import org.apache.ibatis.annotations.Param;
import org.joda.time.LocalDate;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.cr.ClientTime.CreditDeptHierarchyDto;
import ru.alfabank.dmpr.model.cr.ClientTime.DeptHierarchyDto;
import ru.alfabank.dmpr.model.cr.ClientTime.StageHierarchyDto;

/**
 * Маппер для загрузки фильтров витрины "Декомпозиция процессов"
 */
public interface ClientTimeFilterMapper {
    /**
     * Возвращает данные для фильтра "Тип заявки".
     * @return
     */
    BaseEntity[] getKpiMetrics();

    /**
     * Возвращает данные для фильтра "Тип продукта".
     * @return
     */
    BaseEntity[] getProductTypes(@Param("kpiMetricId") long kpiMetricId);

    /**
     * Возвращает данные для фильтра "Период агрегации".
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возращает иерархию подразделений: ЦО/РП -> Куст -> Доп. офис -> Подразделение.
     * @return
     */
    DeptHierarchyDto[] getDeptHierarchy();

    /**
     * Возращает иерархию кредитных подразделений: Доп. офис -> Кредитное подразделение.
     * @return
     */
    CreditDeptHierarchyDto[] getCreditDeptHierarchy();

    /**
     * Возращает иерархию этапов: Этап -> Бизнес процесс -> Дочерний бизнес процесс
     * @param startDate Значение фильтра "Период, с"
     * @param endDate Значение фильтра "Период, по"
     * @param kpiId Значение фильтра "Тип заявки"
     * @param stageIds Значения фильтра "Этап"
     * @return
     */
    StageHierarchyDto[] getStageHierarchy(LocalDate startDate, LocalDate endDate, long kpiId, long[] stageIds);

    BaseEntity[] getFuncGroups();
}
