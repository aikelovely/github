package ru.alfabank.dmpr.widget.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptionsGenerationType;

/**
 * Динамика по регионам.
 */
@Service
public class ObQualityDynamicRegions extends ObQualityDynamicBase {
    @Override
    protected ObQualityQueryOptions getQueryOptions(ObQualityOptions options){
        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        addOptions.doudrFlag = "Y";
        return new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromPeriod, filterRepository.getWeeks());
    }
}
