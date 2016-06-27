package ru.alfabank.dmpr.widget.ctq;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ctq.*;
import ru.alfabank.dmpr.repository.ctq.CTQFilterRepository;
import ru.alfabank.dmpr.repository.ctq.CTQRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * Динамики по показателям CTQ<br/>
 * Значения параметров следующие: <br/>
 * <i>timeUnitId</i> - тип периода.<br/>
 * <i>startDate</i> начало выбранного периода<br/>
 * <i>endDate</i> - конец выбранного периода.<br/>
 * <i>kpiIds</i> - идентификаторы выбранных показателей
 */
@Service
public class CTQDashboardDynamic extends BaseChart<CTQDashboardDynamicOptions> {
    @Autowired
    private CTQRepository repository;

    @Autowired
    private CTQWidgetHelper widgetHelper;

    @Autowired
    protected CTQFilterRepository filterRepository;

    public CTQDashboardDynamic() {
        super(CTQDashboardDynamicOptions.class);
    }

    @Override
    public ChartResult[] getData(final CTQDashboardDynamicOptions options) {
        CTQDashboardIndexItem[] data = repository.getDynamicData(new CTQDashboardDynamicQueryOptions(options, filterRepository.getWeeks()));

        return LinqWrapper.from(data).group(new Selector<CTQDashboardIndexItem, String>() {
            @Override
            public String select(CTQDashboardIndexItem ctqDashboardIndexItem) {
                return ctqDashboardIndexItem.kpiid;
            }
        }).select(new Selector<Group<String,CTQDashboardIndexItem>, ChartResult>() {
            @Override
            public ChartResult select(Group<String, CTQDashboardIndexItem> ctqDashboardIndexItems) {
                CTQDashboardKPIEntity kpiEntity = filterRepository.getKPIbyId(ctqDashboardIndexItems.getKey());

                Map<String, Object> bag = new HashMap<>();
                bag.put("code", kpiEntity.code);
                bag.put("name", kpiEntity.name);
                bag.put("id", kpiEntity.id);

                Double normative = options.paramType == CTQDashboardParamType.Fact ? ctqDashboardIndexItems.getItems().first().kpiNorm : 1d;

                bag.put("normative", normative*100);

                Point[] points = ctqDashboardIndexItems.getItems().sort(new Selector<CTQDashboardIndexItem, LocalDate>() {
                    @Override
                    public LocalDate select(CTQDashboardIndexItem ctqDashboardIndexItem) {
                        return ctqDashboardIndexItem.timeUnitDD;
                    }
                }).select(new Selector<CTQDashboardIndexItem, Point>() {
                    @Override
                    public Point select(CTQDashboardIndexItem ctqDashboardIndexItem) {
                        CTQRichPoint p = widgetHelper.createCTQRichPoint(options.timeUnitId, ctqDashboardIndexItem, options.paramType);
                        p.y = p.y * 100;
                        p.customHTMLTooltip = "ОК: " + ctqDashboardIndexItem.inKpiCount + "<br>Всего: " + ctqDashboardIndexItem.totalCount;
                        return p;
                    }
                }).toArray(Point.class);

                Series series = new Series(kpiEntity.code, points);

                return new ChartResult(new Series[]{series}, bag);
            }
        }).toArray(ChartResult.class);
    }
}

