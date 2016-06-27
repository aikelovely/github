package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.model.mass.decomposition.*;

@Service
public class MassDecompositionButtonsAvgDuration extends MassDecompositionButtonsBase {
    @Override
    protected StageInfoColor getStageColor(KpiDataItem item, StageInfo stage, TresholdItem threshold) {
        return MathHelper.safeDivide(item.totalDuration, item.bpCount) < stage.normative ? StageInfoColor.Green : StageInfoColor.Red;
    }

    @Override
    protected TresholdCode getTresholdCode(){
        return TresholdCode.AvgDuration;
    }

    @Override
    protected void fixOptions(MassDecompositionOptions options) {}
}
