package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.workload.SliceCode;

@Service
public class WorkloadObTerritorialPie extends WorkloadObPieBase {
    @Override
    protected SliceCode getSliceCode() {
        return SliceCode.TERRITORY;
    }

    @Override
    protected int getWeekCount() {
        return 1;
    }
}
