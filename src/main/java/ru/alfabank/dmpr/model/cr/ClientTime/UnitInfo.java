package ru.alfabank.dmpr.model.cr.ClientTime;

import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ParamType;

/**
 * Используется при дриллдаун. Позволяет отобразить кнопку переключатель этапа/бизнесс процесса/функционалой группы.
 */
public class UnitInfo {
    public String id;
    public String name;

    /**
     * Флаг, указывающий на то что сдренее значение укладывается в KPI
     */
    public boolean inKpi;

    /**
     * Id процесса
     */
    public int processId;

    public UnitInfo(KpiDataItem item, ParamType paramType){
        id = item.unitCode;
        name = item.unitName;
        processId = item.processId;

        if(paramType == ParamType.AvgDuration){
            inKpi = MathHelper.safeDivide(item.totalDuration, item.bpCount) <= item.quotaInDays;
        } else {
            inKpi = MathHelper.safeDivide(item.inKpiBpCount, item.bpCount) * 100 >= item.quotaPercent;
        }
    }
}
