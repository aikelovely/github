package ru.alfabank.dmpr.widget.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptionsGenerationType;

/**
 * Рейтинг по дирекциям.
 */
@Service
public class ObQualityRatingDirections extends ObQualityRatingBase {
    @Override
    protected ObQualityQueryOptions getQueryOptions(ObQualityOptions options){
        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        addOptions.systemUnitCode = "BPDG";
        return new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromEndDatePeriod, filterRepository.getWeeks());
    }

    @Override
    protected String getMainItemName(){
        return "Операционный Блок";
    }
}
