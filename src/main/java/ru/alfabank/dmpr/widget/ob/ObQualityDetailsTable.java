package ru.alfabank.dmpr.widget.ob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.model.ob.*;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;
import ru.alfabank.dmpr.repository.ob.ObQualityRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.Map;

/**
 * Таблица с детализацией за отчетный период
 */
@Service
public class ObQualityDetailsTable extends BaseChart<ObQualityOptions> {
    @Autowired
    private ObQualityRepository repository;

    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObQualityDetailsTable() {
        super(ObQualityOptions.class);
    }

    @Override
    public ChartResult[] getData(final ObQualityOptions options) {
        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        addOptions.detailsMode = options.kpiId == null ? 2 : 1;
        addOptions.doudrFlag = options.doudrFlag;
        ObQualityQueryOptions queryOptions = new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromYear, filterRepository.getWeeks());

        ObQualityDataItem[] items = repository.getDetailsData(queryOptions);

        Map<String, Object> bag = new HashMap<>();
        bag.put("data", items);

        return new ChartResult[]{new ChartResult(null, bag)};
    }
}
