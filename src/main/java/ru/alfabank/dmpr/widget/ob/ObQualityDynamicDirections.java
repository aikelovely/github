package ru.alfabank.dmpr.widget.ob;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.model.ob.ObQualityAdditionalOptions;
import ru.alfabank.dmpr.model.ob.ObQualityOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptions;
import ru.alfabank.dmpr.model.ob.ObQualityQueryOptionsGenerationType;

/**
 * Динамика по дирекциям.
 */
@Service
public class ObQualityDynamicDirections extends ObQualityDynamicBase {
    @Override
    protected ObQualityQueryOptions getQueryOptions(ObQualityOptions options){
        ObQualityAdditionalOptions addOptions = new ObQualityAdditionalOptions();
        return new ObQualityQueryOptions(options, addOptions, filterRepository,
                ObQualityQueryOptionsGenerationType.FromPeriod, filterRepository.getWeeks());
    }
}
