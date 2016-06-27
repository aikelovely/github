package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.zp.Company;
import ru.alfabank.dmpr.model.zp.Manager;
import ru.alfabank.dmpr.model.zp.ZPKPIDataItem;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.widget.zp.openingSpeed.EmissionTableResult;

import java.util.List;
import java.util.Map;

/**
 * Таблица с "Аномальная длительность процесса". Отображается в модальном окне.
 */
@Service
public class ZPOpeningSpeedEmission extends BaseWidget<ZPOpeningSpeedOptions, EmissionTableResult[]> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedEmission() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public EmissionTableResult[] getData(ZPOpeningSpeedOptions options) {
        boolean showManager = options.systemUnitId == 1 || options.systemUnitId == 11 || options.systemUnitId == 12;
        options.systemUnitId = 3;

        List<EmissionTableResult> data = LinqWrapper.from(repository.getRating(options))
                .filter(new Predicate<ZPKPIDataItem>() {
                    @Override
                    public boolean check(ZPKPIDataItem item) {
                        return item.retardedCount > 0;
                    }
                })
                .sortDesc(new Selector<ZPKPIDataItem, Comparable>() {
                    @Override
                    public Comparable select(ZPKPIDataItem dataItem) {
                        return dataItem.avgDuration;
                    }
                })
                .select(new Selector<ZPKPIDataItem, EmissionTableResult>() {
                    @Override
                    public EmissionTableResult select(ZPKPIDataItem item) {
                        return new EmissionTableResult(item);
                    }
                })
                .toList();

        final Map<String, Manager> managers = LinqWrapper.from(
                filterRepository.getAllManagers(new long[0], new long[0], new long[0], options.openingTypeId))
                .toMap(new Selector<Manager, String>() {
                    @Override
                    public String select(Manager manager) {
                        return manager.id;
                    }
                });

        final Map<String, Company> companies = LinqWrapper.from(filterRepository.getAllCompanies(new long[0], new long[0], new long[0],
                options.openingTypeId))
                .toMap(new Selector<Company, String>() {
                    @Override
                    public String select(Company company) {
                        return company.id;
                    }
                });

        if(showManager) {
            for(EmissionTableResult item : data){
                Company company = companies.get(item.companyCode);
                if(company == null) continue;
                Manager manager = managers.get(company.managerId);
                if(manager == null) continue;

                item.managerName = manager.name;
            }
        }

        return data.toArray(new EmissionTableResult[data.size()]);
    }
}
