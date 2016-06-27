package ru.alfabank.dmpr.widget.ctq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.ctq.CTQDashboardIndexItem;
import ru.alfabank.dmpr.model.ctq.CTQDashboardParamType;
import ru.alfabank.dmpr.model.ctq.CTQRichPoint;
import ru.alfabank.dmpr.repository.ctq.CTQFilterRepository;

/**
 * Вспомогательный класс, который выполняет преобразование объектов из одного типа в другой и другие полезные действия.
 */
@Component
public class CTQWidgetHelper {
    @Autowired
    protected CTQFilterRepository filterRepository;

    public CTQRichPoint createCTQRichPoint(int timeUnitId, CTQDashboardIndexItem item, CTQDashboardParamType paramType) {
        CTQRichPoint p = new CTQRichPoint(item.timeUnitDD, paramType == CTQDashboardParamType.Fact ? item.kpiRatioAvg : item.kpi2RatioAvg);
        PeriodSelectOption pSOptions;
        if (timeUnitId == Period.week.getValue()) {
            pSOptions = PeriodSelectHelper.getWeekByYearAndNum(item.timeUnitYear, item.timeUnitPrdNum, filterRepository.getWeeks());
        } else {
            pSOptions = PeriodSelectHelper.getMonthById(item.timeUnitYear, item.timeUnitPrdNum - 1);
        }
        p.periodName = pSOptions.name;
        p.periodNum = pSOptions.periodNum;
        p.periodId = pSOptions.id;
        return p;
    }
}
