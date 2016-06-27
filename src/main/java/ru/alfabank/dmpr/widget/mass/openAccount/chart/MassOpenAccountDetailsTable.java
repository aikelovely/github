package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountDetailsTableRow;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;
import ru.alfabank.dmpr.repository.mass.MassOpenAccountRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.List;

@Service
public class MassOpenAccountDetailsTable extends BaseWidget<MassOpenAccountOptions, List<MassOpenAccountDetailsTableRow>> {
    @Autowired
    MassOpenAccountRepository repository;

    public MassOpenAccountDetailsTable() {
        super(MassOpenAccountOptions.class);
    }

    @Override
    public List<MassOpenAccountDetailsTableRow> getData(MassOpenAccountOptions options) {
        if(options.systemUnitIds != null && options.systemUnitIds.length > 0){
            options.rcbUnitId = options.systemUnitIds[0];
        } else {
            options.rcbUnitId = 2;
        }

        KpiDataItem[] data = repository.getDetailsTable(options);

        final KpiDataItem summation = new KpiDataItem();

        List<MassOpenAccountDetailsTableRow> rows = LinqWrapper.from(data)
                .select(new Selector<KpiDataItem, MassOpenAccountDetailsTableRow>() {
                    @Override
                    public MassOpenAccountDetailsTableRow select(KpiDataItem item) {

                        summation.bpCount += item.bpCount;

                        summation.bpCountGrp1 += item.bpCountGrp1;
                        summation.bpCountGrp2 += item.bpCountGrp2;
                        summation.bpCountGrp3 += item.bpCountGrp3;
                        summation.bpCountGrp4 += item.bpCountGrp4;
                        summation.bpCountGrp5 += item.bpCountGrp5;

                        summation.sumDurationInHours += item.sumDurationInHours;
                        if (summation.maxDurationInHours < item.maxDurationInHours) {
                            summation.maxDurationInHours = item.maxDurationInHours;
                        }

                        return new MassOpenAccountDetailsTableRow(item);
                    }
                })
                .sort(new Selector<MassOpenAccountDetailsTableRow, Comparable>() {
                    @Override
                    public Comparable select(MassOpenAccountDetailsTableRow row) {
                        return row.unitName;
                    }
                })
                .toList();

        rows.add(new MassOpenAccountDetailsTableRow(summation));

        return rows;
    }
}
