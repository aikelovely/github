package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;
import ru.alfabank.dmpr.model.mass.decomposition.SlowCompanyItem;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

@Service
public class MassDecompositionTableSlowCompanies extends BaseWidget<MassDecompositionOptions, SlowCompanyItem[]> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionTableSlowCompanies() {
        super(MassDecompositionOptions.class);
    }

    @Override
    public SlowCompanyItem[] getData(final MassDecompositionOptions options) {
        return repository.getSlowCompaniesData(options);
    }
}
