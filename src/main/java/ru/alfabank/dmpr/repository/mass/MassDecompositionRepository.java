package ru.alfabank.dmpr.repository.mass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.mapper.mass.MassDecompositionMapper;
import ru.alfabank.dmpr.model.mass.decomposition.*;

@Repository
public class MassDecompositionRepository {
    @Autowired
    MassDecompositionMapper mapper;

    public KpiDataItem[] getKpiData(MassDecompositionOptions options) {
        return mapper.getKpiData(options);
    }

    public StageInfo[] getStages() {
        return mapper.getStages();
    }

    public TresholdItem[] getTresholds() { return mapper.getTresholds(); }

    public SlowCompanyItem[] getSlowCompaniesData(MassDecompositionOptions options) {
        return mapper.getSlowCompaniesData(options);
    }

    public TresholdItem getTresholdByCode(final TresholdCode code) {
        TresholdItem item = LinqWrapper.from(getTresholds()).firstOrNull(new Predicate<TresholdItem>() {
            @Override
            public boolean check(TresholdItem item) {
                return item.code.equalsIgnoreCase(code.dbCode());
            }
        });

        if (item == null) {
            TresholdItem mockItem = new TresholdItem();
            mockItem.code = "";
            mockItem.warning = mockItem.error = 1;
            return mockItem;
        }

        return item;
    }
}
