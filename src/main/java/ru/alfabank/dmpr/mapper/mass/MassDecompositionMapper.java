package ru.alfabank.dmpr.mapper.mass;

import ru.alfabank.dmpr.model.mass.decomposition.*;

public interface MassDecompositionMapper {
    KpiDataItem[] getKpiData(MassDecompositionOptions options);
    StageInfo[] getStages();
    TresholdItem[] getTresholds();
    SlowCompanyItem[] getSlowCompaniesData(MassDecompositionOptions options);
}
