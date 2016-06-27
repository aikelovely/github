package ru.alfabank.dmpr.widget.pilAndCC.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.linq.Group;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.pilAndCC.KpiStageInfo;
import ru.alfabank.dmpr.model.pilAndCC.PILAndCCOptions;
import ru.alfabank.dmpr.repository.pilAndCC.PILAndCCRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * Набор кнопок-переключателей выбранного этапа.
 */
@Service
public class PILAndCCStageInfo extends BaseChart<PILAndCCOptions> {
    @Autowired
    private PILAndCCRepository repository;

    public PILAndCCStageInfo() {
        super(PILAndCCOptions.class);
    }

    @Override
    public ChartResult[] getData(final PILAndCCOptions options) {
        KpiStageInfo[] data = repository.getStageInfo(options);

        final String title = String.format("Период с " + options.getStartDate().toString("dd.MM.yyyy") + " по "
        + options.getEndDate().toString("dd.MM.yyyy"));

        return LinqWrapper.from(data)
                .group(new Selector<KpiStageInfo, Integer>() {
                    @Override
                    public Integer select(KpiStageInfo kpiStageInfo) {
                        return kpiStageInfo.reportTypeId;
                    }
                })
                .select(new Selector<Group<Integer, KpiStageInfo>, ChartResult>() {
                    @Override
                    public ChartResult select(Group<Integer, KpiStageInfo> items) {

                        Map<String, Object> bag = new HashMap<>();

                        bag.put("title", title);
                        bag.put("reportTypeId", items.getKey());
                        bag.put("stages", items.getItems().sort(new Selector<KpiStageInfo, Comparable>() {
                            @Override
                            public Comparable select(KpiStageInfo kpiStageInfo) {
                                return kpiStageInfo.sortOrder;
                            }
                        }));

                        return new ChartResult(null, bag);
                    }
                })
                .toArray(ChartResult.class);
    }
}
