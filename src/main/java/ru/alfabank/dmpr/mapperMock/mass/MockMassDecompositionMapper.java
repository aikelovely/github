package ru.alfabank.dmpr.mapperMock.mass;

import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.dev.MockXlsGenericMapperBean;
import ru.alfabank.dmpr.mapper.mass.MassDecompositionMapper;
import ru.alfabank.dmpr.model.mass.decomposition.*;

@Component
public class MockMassDecompositionMapper implements MassDecompositionMapper{
    private MassDecompositionMapper delegate;

    public MockMassDecompositionMapper() {
        delegate = MockXlsGenericMapperBean.createProxy(MassDecompositionMapper.class);
    }

    @Override
    public KpiDataItem[] getKpiData(MassDecompositionOptions options) {
        String excelFileName = "";

        switch (options.getWidgetName()){
            case "DynamicAvgDuration":
                excelFileName = "Dynamic_AvgDuration";
                break;
            case "DynamicPercent":
                excelFileName = "Dynamic_Percent";
                break;
            case "ButtonsAvgDuration" : {
                excelFileName = "Buttons";
                break;
            }
            case "ButtonsPercent" : {
                excelFileName = "Buttons";
                break;
            }
            case "DynamicByStageAvgDuration" : {
                excelFileName = "Dynamic_ByStage";
                break;
            }
            case "DynamicByStagePercent" : {
                excelFileName = "Dynamic_ByStage";
                break;
            }
            case "TableByStageAvgDuration" : {
                excelFileName = "Table_ByStage";
                break;
            }
            case "TableByStagePercent" : {
                excelFileName = "Table_ByStage";
                break;
            }
        }

        options.widgetUrl = "MassDecomposition" + excelFileName;
        return delegate.getKpiData(options);
    }

    @Override
    public StageInfo[] getStages() {
        return delegate.getStages();
    }

    @Override
    public TresholdItem[] getTresholds() {
        return delegate.getTresholds();
    }

    @Override
    public SlowCompanyItem[] getSlowCompaniesData(MassDecompositionOptions options) {
        options.widgetUrl = "MassDecomposition" + "Table_SlowCompanies";
        return delegate.getSlowCompaniesData(options);
    }
}
