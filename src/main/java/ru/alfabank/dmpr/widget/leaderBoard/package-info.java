/**
 * Пакет содержит графики для 3х витрин КПЭ ОБ.<br/>
 * Для получения данных для графиков используется следующий запрос:<br/>
 *
 * <pre>
 * select
 *   VALUE_DAY as calcDate,                     -- Отчетная дата
 *   KPI_UK as kpiId,                           -- Показатель (UK)
 *   KPI_CCODE as kpiCode,                      -- Показатель (CCODE)
 *   KPI_NAME as kpiName,                       -- Показатель (Наименование)
 *   DivisionGroup_UK as divisionGroupId,       -- Группы подразделений (UK)
 *   DivisionGroup_CCODE as divisionGroupCode,  -- Группы подразделений (CCODE)
 *   DivisionGroup_NAME as divisionGroupName,   -- Группа подразделений (Наименование)
 *   PREV_FACTVALUE as prevValue,               -- Предыдущее факт. значение показателя (-12 мес. от текущей даты)
 *   FACTVALUE as currentValue,                 -- Фактическое значение показателя
 *   PLANVALUE_HIHBOUND_CNT as planValue,       -- Плановое значение показателя
 *   Fact_Description as description
 *   from table(PKG_LEADERBOARD_API.get_kpi_data (
 *       p_DateFrom        => #{startDate},
 *       p_DateTill        => #{endDate},
 *       p_KPIIDs           => #{kpiIds},
 *       p_DateIntervalType => #{dateIntervalType},
 *       p_DivisionGroupIDs => #{divisionGroupId}
 *   ))
 *   </pre>
 *   Если параметры <i>endDate</i> и <i>divisionGroupId</i> не заданы явно, то расчитываются они следующим образом:<br>
 *   <i>endDate</i> - конец выбранного года<br>
 *   <i>divisionGroupId</i> - "ОБ" <br>
 *
 *   <br/>
 *   Расчет параметров для каждого графика находится в описании их классов:
 *
 */
package ru.alfabank.dmpr.widget.leaderBoard;