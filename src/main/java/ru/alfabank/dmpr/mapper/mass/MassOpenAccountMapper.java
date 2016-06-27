package ru.alfabank.dmpr.mapper.mass;

import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;

public interface MassOpenAccountMapper {
    KpiDataItem[] getDynamic(MassOpenAccountOptions options);
    KpiDataItem[] getDonut(MassOpenAccountOptions options);
    KpiDataItem[] getDetailsTable(MassOpenAccountOptions options);
    KpiDataItem[] getRating(MassOpenAccountOptions options);
}
