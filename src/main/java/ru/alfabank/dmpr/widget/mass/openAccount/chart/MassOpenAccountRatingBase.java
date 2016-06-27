package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountRatingRow;
import ru.alfabank.dmpr.repository.mass.MassOpenAccountRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.List;


public abstract class MassOpenAccountRatingBase extends BaseWidget<MassOpenAccountOptions, List<MassOpenAccountRatingRow>> {
    @Autowired
    MassOpenAccountRepository repository;

    private int topCount;

    public MassOpenAccountRatingBase(int topCount) {
        super(MassOpenAccountOptions.class);
        this.topCount = topCount;
    }

    @Override
    public List<MassOpenAccountRatingRow> getData(MassOpenAccountOptions options) {
        options.topCount = this.topCount;
        if(options.systemUnitIds != null && options.systemUnitIds.length > 0){
            options.rcbUnitId = options.systemUnitIds[0];
        } else {
            options.rcbUnitId = 2;
        }
        KpiDataItem[] data = repository.getRating(options);

        return LinqWrapper.from(data).select(new Selector<KpiDataItem, MassOpenAccountRatingRow>() {
            @Override
            public MassOpenAccountRatingRow select(KpiDataItem item) {
                return new MassOpenAccountRatingRow(item);
            }
        }).toList();
    }
}

