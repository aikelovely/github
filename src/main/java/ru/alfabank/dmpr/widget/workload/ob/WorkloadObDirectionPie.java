package ru.alfabank.dmpr.widget.workload.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.workload.SliceCode;

@Service
public class WorkloadObDirectionPie extends WorkloadObPieBase {
    @Override
    protected SliceCode getSliceCode() {
        return SliceCode.DIRECTION;
    }

    @Override
    protected int getWeekCount() {
        return 1;
    }
}
