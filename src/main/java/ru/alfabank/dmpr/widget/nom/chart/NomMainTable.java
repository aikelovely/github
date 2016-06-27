package ru.alfabank.dmpr.widget.nom.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.ChildEntityWithCode;
import ru.alfabank.dmpr.model.nom.NomDetailsTableRow;
import ru.alfabank.dmpr.model.nom.NomKpiDataItem;
import ru.alfabank.dmpr.model.nom.NomOptions;
import ru.alfabank.dmpr.model.nom.NomQueryOptions;
import ru.alfabank.dmpr.repository.nom.NomFilterRepository;
import ru.alfabank.dmpr.repository.nom.NomRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.List;

@Service
public class NomMainTable extends BaseWidget<NomOptions, List<NomDetailsTableRow>> {
    @Autowired
    private NomFilterRepository filterRepository;

    @Autowired
    private NomRepository repository;

    public NomMainTable() {
        super(NomOptions.class);
    }

    @Override
    public List<NomDetailsTableRow> getData(NomOptions options) {
        options.levels = new int[]{1};
        options.onlyLastPeriod = 1;
        NomKpiDataItem[] data = repository.getKpiData(new NomQueryOptions(options, filterRepository.getWeeks()));

        final LinqWrapper<ChildEntity> divisionGroups = LinqWrapper.from(filterRepository.getDivisionGroups());
        final LinqWrapper<ChildEntityWithCode> innerEndProducts = LinqWrapper.from(repository.getInnerEndProducts());

        return LinqWrapper
                .from(data)
                .select(new Selector<NomKpiDataItem, NomDetailsTableRow>() {
                    @Override
                    public NomDetailsTableRow select(final NomKpiDataItem nomKpiDataItem) {
                        BaseEntity divisionGroup = divisionGroups.firstOrNull(new Predicate<ChildEntity>() {
                            @Override
                            public boolean check(final ChildEntity item) {
                                return item.id == nomKpiDataItem.divisionGroupId;
                            }
                        });

                        ChildEntityWithCode innerEndProduct = innerEndProducts.firstOrNull(new Predicate<ChildEntityWithCode>() {
                            @Override
                            public boolean check(final ChildEntityWithCode item) {
                                return item.id == nomKpiDataItem.innerEndProductId;
                            }
                        });

                        if (divisionGroup == null) {
                            divisionGroup = new BaseEntity();
                        }
                        if (innerEndProduct == null) {
                            innerEndProduct = new ChildEntityWithCode();
                        }

                        return new NomDetailsTableRow(
                                innerEndProduct.id,
                                nomKpiDataItem.calcDate,
                                nomKpiDataItem.periodNum,
                                innerEndProduct.parentId != 0 ? innerEndProduct.parentId : divisionGroup.id,
                                divisionGroup.id,
                                divisionGroup.name,
                                innerEndProduct.name,
                                nomKpiDataItem.factCount,
                                nomKpiDataItem.isManual,
                                innerEndProduct.parentId == 0 ? 1 : 2);
                    }
                })
                .toList();
    }
}
