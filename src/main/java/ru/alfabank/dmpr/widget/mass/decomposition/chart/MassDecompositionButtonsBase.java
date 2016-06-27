package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.decomposition.*;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

public abstract class MassDecompositionButtonsBase extends BaseChart<MassDecompositionOptions> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionButtonsBase() {
        super(MassDecompositionOptions.class);
    }

    protected abstract StageInfoColor getStageColor(KpiDataItem item, StageInfo stage, TresholdItem threshold);

    protected abstract TresholdCode getTresholdCode();

    protected abstract void fixOptions(MassDecompositionOptions options);

    @Override
    public ChartResult[] getData(MassDecompositionOptions options) {
        options.timeUnitId = null;
        options.systemUnitId = 0;
        options.stageDetalization = 1;
        fixOptions(options);

        LinqWrapper<KpiDataItem> data = LinqWrapper.from(repository.getKpiData(options)).filter(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.stageId != 0;
            }
        });

        final LinqWrapper<StageInfo> stages =  LinqWrapper.from(repository.getStages());

        final TresholdItem threshold = repository.getTresholdByCode(getTresholdCode());

        StageInfo[] result = data.select(new Selector<KpiDataItem, StageInfo>() {
            @Override
            public StageInfo select(final KpiDataItem kpiDataItem) {

                StageInfo stage = stages.firstOrNull(new Predicate<StageInfo>() {
                    @Override
                    public boolean check(StageInfo item) {
                        return item.id == kpiDataItem.stageId;
                    }
                });

                if (stage != null) {
                    stage.color = getStageColor(kpiDataItem, stage, threshold);
                }

                return stage;
            }
        }).filter(new Predicate<StageInfo>() {
            @Override
            public boolean check(StageInfo item) {
                return item != null;
            }
        }).toArray(StageInfo.class);

        Map<String, Object> bag = new HashMap<>();
        bag.put("stages", result);

        return new ChartResult[]{new ChartResult(null, bag)};
    }
}
