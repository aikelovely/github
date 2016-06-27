package ru.alfabank.dmpr.widget.cr.ClientTime.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.*;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.ClientTime.ClientTimeOptions;
import ru.alfabank.dmpr.model.cr.ClientTime.KpiDataItem;
import ru.alfabank.dmpr.model.cr.ClientTime.UnitInfo;
import ru.alfabank.dmpr.repository.cr.ClientTimeRepository;
import ru.alfabank.dmpr.widget.BaseChart;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис, который возвращает данные для компонента breadcrumbs. Используется для drilldown.
 */
@Service
public class ClientTimeBreadcrumbs extends BaseWidget<ClientTimeOptions, UnitInfo[]> {
    @Autowired
    ClientTimeRepository repository;

    public ClientTimeBreadcrumbs() {
        super(ClientTimeOptions.class);
    }


    @Override
    public UnitInfo[] getData(final ClientTimeOptions options) {
        options.timeUnitId = null;
        switch (options.drillDownLevel){
            case 1:
                options.systemUnitId = 1;
                break;
            case 2:
                options.systemUnitId = 2;
                break;
            case 3:
                options.systemUnitId = 5;
                break;
        }

        LinqWrapper<UnitInfo> data = LinqWrapper.from(repository.getKpiData(options))
                .sortDesc(new Selector<KpiDataItem, Comparable>() {
                    @Override
                    public Comparable select(KpiDataItem item) {
                        if (options.paramType == ParamType.AvgDuration) {
                            return MathHelper.safeDivide(item.totalDuration, item.bpCount);
                        }
                        return -1 * MathHelper.safeDivide(item.inKpiBpCount, item.bpCount);
                    }
                })
                .select(new Selector<KpiDataItem, UnitInfo>() {
                    @Override
                    public UnitInfo select(KpiDataItem item) {
                        return new UnitInfo(item, options.paramType);
                    }
                });

        return data.toArray(UnitInfo.class);
    }
}
