package ru.alfabank.dmpr.repository.mass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.mass.MassOpenAccountMapper;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;

@Repository
public class MassOpenAccountRepository {
    @Autowired
    MassOpenAccountMapper mapper;

    public KpiDataItem[] getDynamic(MassOpenAccountOptions options) {
        return mapper.getDynamic(options);
    }

    public KpiDataItem[] getDonut(MassOpenAccountOptions options) {
        return mapper.getDonut(options);
    }

    public KpiDataItem[] getDetailsTable(MassOpenAccountOptions options) {
        return mapper.getDetailsTable(options);
    }

    public KpiDataItem[] getRating(MassOpenAccountOptions options) {
        return mapper.getRating(options);
    }
}
