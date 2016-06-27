package ru.alfabank.dmpr.mapper.zp;

import org.apache.ibatis.annotations.Param;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.zp.*;

/**
 * Маппер, отвечающий за загрузку данных для фильтров витрин "Зарплатные проекты".
 */
public interface ZPFilterMapper {
    /**
     * Возвращает данные для фильтра "Куст"
     * @return
     */
    BaseEntity[] getBushes();

    /**
     * Возвращает данные для фильтра "Операционный офис"
     * @return
     */
    BaseEntity[] getOperationOffices();

    /**
     * Возвращает данные для фильтра "Город"
     * @return
     */
    City[] getCities();

    /**
     * Возвращает данные для фильтра "Город" (тип открытия - дооткрытие)
     * @return
     */
    City[] getRoCities();

    /**
     * Возвращает данные для фильтра "Менеджер"
     * @return
     */
    ManagerDto[] getManagers(@Param("bushIds") long[] bushIds,
                             @Param("operationOfficeIds") long[] operationOfficeIds,
                             @Param("cityIds") long[] cityIds,
                             @Param("openingTypeId") int openingTypeId);

    /**
     * Возвращает данные для фильтра "Компания"
     * @return
     */
    CompanyDto[] getCompanies(@Param("bushIds") long[] bushIds,
                              @Param("operationOfficeIds") long[] operationOfficeIds,
                              @Param("cityIds") long[] cityIds,
                              @Param("openingTypeId") int openingTypeId);

    /**
     * Возвращает данные для фильтра "Компания" (тип открытия - дооткрытие)
     * @param bushIds bushIds Значения фильтра "Куст"
     * @param operationOfficeIds Значения фильтра "Операционный офис"
     * @param cityIds Значение фильтра "Город"
     * @param managerIds Значение фильтра "Менеджер"
     * @return
     */
    CompanyDto[] getRoCompanies(@Param("bushIds") long[] bushIds,
                                @Param("operationOfficeIds") long[] operationOfficeIds,
                                @Param("cityIds") long[] cityIds,
                                @Param("managerIds") String[] managerIds);

    /**
     * Возвращает данные для фильтра "Этап"
     * @return
     */
    ProcessStage[] getProcessStages();

    /**
     * Возвращает данные для фильтра "Подэтап"
     * @return
     */
    SubProcessStage[] getSubProcessStages();

    /**
     * Возвращает данные для фильтра "Детализация"
     * @return
     */
    BaseEntity[] getSystemUnits();

    /**
     * Возвращает данные для фильтра "Единица времени"
     * @return
     */
    BaseEntity[] getTimeUnits();

    /**
     * Возвращает данные для фильтра "Тип схемы"
     * @return
     */
    BaseEntity[] getSchemaTypes();

    /**
     * Возвращает данные для фильтра "Тип открытия"
     * @return
     */
    BaseEntity[] getOpeningTypes();
}
