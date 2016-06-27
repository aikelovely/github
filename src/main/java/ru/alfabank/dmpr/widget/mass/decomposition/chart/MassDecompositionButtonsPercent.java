package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.model.mass.decomposition.*;

@Service
public class MassDecompositionButtonsPercent extends MassDecompositionButtonsBase {
    @Override
    protected StageInfoColor getStageColor(KpiDataItem item, StageInfo stage, TresholdItem threshold) {
        double value = MathHelper.safeDivide(item.inLimitBpCount, item.bpCount);
        return value >= threshold.warning ? StageInfoColor.Green : value <= threshold.error ? StageInfoColor.Red : StageInfoColor.Yellow;
    }

    @Override
    protected TresholdCode getTresholdCode(){
        return TresholdCode.ByDay;
    }

    @Override
    protected void fixOptions(MassDecompositionOptions options) {
        options.durationLimitHours = 24d;
    }
}
