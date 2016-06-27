package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;

@Service
public class MassDecompositionTableByStagePercent extends MassDecompositionTableByStageBase {
    @Override
    protected void fixOptions(MassDecompositionOptions options) {
        options.durationLimitHours = 24d;
    }
}
