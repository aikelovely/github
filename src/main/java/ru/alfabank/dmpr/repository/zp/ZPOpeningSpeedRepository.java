package ru.alfabank.dmpr.repository.zp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.alfabank.dmpr.mapper.zp.ZPOpeningSpeedMapper;
import ru.alfabank.dmpr.model.zp.*;

@Repository
public class ZPOpeningSpeedRepository {
    @Autowired
    ZPOpeningSpeedMapper mapper;

    public final static double warningThreshold = 0.8; // 80%

    public ZPKPIDataItem[] getDynamic(ZPOpeningSpeedOptions options) {
        if(options.openingTypeId == 1) return mapper.getDynamic(options);
        return mapper.getRoDynamic(options);
    }

    public ZPKPIDataItem[] getRating(ZPOpeningSpeedOptions options) {
        if(options.openingTypeId == 1) return mapper.getRating(options);
        return mapper.getRoRating(options);
    }

    public ZPKPIDataItem[] getDetails(ZPOpeningSpeedOptions options) {
        if(options.openingTypeId == 1) return mapper.getDetails(options);
        return mapper.getRoDetails(options);
    }

    public ZPProjectDynamic[] getProjectDynamic(ZPOpeningSpeedOptions options) {
        return mapper.getProjectDynamic(options);
    }

    public ZPProjectDynamic[] getProjectDetails(ZPOpeningSpeedOptions options) {
        return mapper.getProjectDetails(options);
    }

    public ZPQualityInfo[] getCrmFillQualityDetails(ZPOpeningSpeedOptions options) {
        return mapper.getCrmFillQualityDetails(options);
    }

    public ZPQualityDynamic[] getCrmFillQualityDynamic(ZPOpeningSpeedOptions options) {
        return mapper.getCrmFillQualityDynamic(options);
    }

    public double getInKPIThreshold(ZPOpeningSpeedOptions options) {
        return mapper.getInKPIThreshold(options);
    }

    public double getRetardedThreshold(ZPOpeningSpeedOptions options) {
        return mapper.getRetardedThreshold(options);
    }

    public ZPReportDetailsItem[] getReportDetails(ZPOpeningSpeedOptions options) {
        return mapper.getReportDetails(options);
    }

    public ZPReportDetailsItem[] getRoReportDetails(ZPOpeningSpeedOptions options) {
        return mapper.getRoReportDetails(options);
    }
}
