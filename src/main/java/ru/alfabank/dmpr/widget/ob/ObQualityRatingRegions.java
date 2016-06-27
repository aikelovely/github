package ru.alfabank.dmpr.widget.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptionsGenerationType;

/**
 * Рейтинг по регионам.
 */
@Service
public class ObQualityRatingRegions extends ObQualityRatingBase {
    @Override
    protected ObQualityQueryOptions getQueryOptions(ObQualityOptions options){
        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        addOptions.systemUnitCode = "BUSH";
        addOptions.doudrFlag = "Y";
        return new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromEndDatePeriod, filterRepository.getWeeks());
    }

    @Override
    protected String getMainItemName(){
        return "ДУОДР";
    }
}
