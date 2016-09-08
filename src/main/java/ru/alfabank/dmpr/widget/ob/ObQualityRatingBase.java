package ru.alfabank.dmpr.widget.ob;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ob.ObQualityDataItem;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository;
import ru.alfabank.dmpr.repository.ob.ObQualityRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * test
 * Базовый класс для отображения рейтинга.
 */
public abstract class ObQualityRatingBase extends BaseChart<ObQualityOptions> {
    class TableRow {
        public String unitCode;
        public String unitName;
        public Double kpiRatioAvg;
        public Double kpiNorm;
        public TableRow(String _unitCode, String _unitName, Double _kpiRatioAvg, Double _kpiNorm){
            unitCode = _unitCode;
            unitName = _unitName;
            kpiRatioAvg = _kpiRatioAvg;
            kpiNorm = _kpiNorm;
        }
    }

    @Autowired
    private ObQualityRepository repository;

    @Autowired
    protected ObQualityFilterRepository filterRepository;

    public ObQualityRatingBase() {
        super(ObQualityOptions.class);
    }

    protected abstract ObQualityQueryOptions getQueryOptions(ObQualityOptions options);

    protected abstract String getMainItemName();

    @Override
    public ChartResult[] getData(final ObQualityOptions options) {
        ObQualityQueryOptions queryOptions = getQueryOptions(options);
        ObQualityDataItem[] items = repository.getRatingData(queryOptions);

        final Selector<ObQualityDataItem, Double> selectValue = new Selector<ObQualityDataItem, Double>() {
            @Override
            public Double select(ObQualityDataItem item) {
                return options.kpiId != null ? item.kpiRatioAvg : item.getQualityLevel();
            }
        };

        final Selector<ObQualityDataItem, Double> selectNormative = new Selector<ObQualityDataItem, Double>() {
            @Override
            public Double select(ObQualityDataItem item) {
                return options.kpiId != null ? item.getNormative() : item.getQualityLevelNormative();
            }
        };

        List<TableRow> data = LinqWrapper.from(items).select(new Selector<ObQualityDataItem, TableRow>() {
            @Override
            public TableRow select(ObQualityDataItem item) {
                return new TableRow(item.unitCode, item.unitName, selectValue.select(item), selectNormative.select(item));
            }
        }).toList();


       Boolean j=true;
 //z       String  jcode;
       for(int i = 0; i < data.size(); ++i) {
           if (data.get(i).unitName.contains("Операционный Блок") )
           {
                j=false;
           //    unitCode=data.get(i).unitCode;
            }



       }

        if (j) {
            queryOptions.systemUnitCode = null;
            ObQualityDataItem[] baseItems = repository.getRatingData(queryOptions);
            if (baseItems.length > 0) {
                //     if (baseItems.length > 0) {

                data.add(0, new TableRow("", getMainItemName(), selectValue.select(baseItems[0]), selectNormative.select(baseItems[0])));
            }
        }
        Map<String, Object> bag = new HashMap<>();
        bag.put("data", data);

        return new ChartResult[]{new ChartResult(null, bag)};
    }
}
