package ru.alfabank.dmpr.mapperMock.zp;

import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.dev.MockXlsGenericMapperBean;
import ru.alfabank.dmpr.mapper.zp.ZPOpeningSpeedMapper;
import ru.alfabank.dmpr.model.zp.*;

@Component
public class MockZPOpeningSpeedMapper implements ZPOpeningSpeedMapper {
    private ZPOpeningSpeedMapper delegate;

    public MockZPOpeningSpeedMapper(){
        delegate = MockXlsGenericMapperBean.createProxy(ZPOpeningSpeedMapper.class);
    }

    @Override
    public ZPKPIDataItem[] getDynamic(ZPOpeningSpeedOptions options) {
        return delegate.getDynamic(options);
    }

    @Override
    public ZPKPIDataItem[] getRating(ZPOpeningSpeedOptions options) {
        return delegate.getRating(options);
    }

    @Override
    public ZPKPIDataItem[] getDetails(ZPOpeningSpeedOptions options) {
        return delegate.getDetails(options);
    }

    @Override
    public ZPKPIDataItem[] getRoDynamic(ZPOpeningSpeedOptions options) {
        return new ZPKPIDataItem[0];
    }

    @Override
    public ZPKPIDataItem[] getRoRating(ZPOpeningSpeedOptions options) {
        return new ZPKPIDataItem[0];
    }

    @Override
    public ZPKPIDataItem[] getRoDetails(ZPOpeningSpeedOptions options) {
        return new ZPKPIDataItem[0];
    }

    @Override
    public ZPProjectDynamic[] getProjectDynamic(ZPOpeningSpeedOptions options) {
        return delegate.getProjectDynamic(options);
    }

    @Override
    public ZPProjectDynamic[] getProjectDetails(ZPOpeningSpeedOptions options) {
        return delegate.getProjectDetails(options);
    }

    @Override
    public ZPQualityInfo[] getCrmFillQualityDetails(ZPOpeningSpeedOptions options) {
        return delegate.getCrmFillQualityDetails(options);
    }

    @Override
    public ZPQualityDynamic[] getCrmFillQualityDynamic(ZPOpeningSpeedOptions options) {
        return delegate.getCrmFillQualityDynamic(options);
    }

    @Override
    public double getInKPIThreshold(ZPOpeningSpeedOptions options) {
        return 0.9d;
    }

    @Override
    public double getRetardedThreshold(ZPOpeningSpeedOptions options) {
        return 30d;
    }

    @Override
    public ZPReportDetailsItem[] getReportDetails(ZPOpeningSpeedOptions options) {
        return delegate.getReportDetails(options);
    }

    @Override
    public ZPReportDetailsItem[] getRoReportDetails(ZPOpeningSpeedOptions options) {
        return delegate.getRoReportDetails(options);
    }
}
