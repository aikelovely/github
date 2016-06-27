package ru.alfabank.dmpr.mapperMock.leaderboard;

import org.springframework.stereotype.Component;
import ru.alfabank.dmpr.infrastructure.helper.dev.MockXlsGenericMapperBean;
import ru.alfabank.dmpr.mapper.leaderBoard.LeaderBoardMapper;
import ru.alfabank.dmpr.model.BaseEntityWithCode;
import ru.alfabank.dmpr.model.leaderBoard.ChartMetaData;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;

import java.util.Objects;

@Component
public class MockLeaderBoardMapper implements LeaderBoardMapper {
    private LeaderBoardMapper delegate;

    public MockLeaderBoardMapper() {
        delegate = MockXlsGenericMapperBean.createProxy(LeaderBoardMapper.class);
    }

    @Override
    public BaseEntityWithCode[] getKPIs() {
        return delegate.getKPIs();
    }

    @Override
    public KpiDataItem[] getKpiData(LeaderBoardOptions options) {
        if (Objects.equals(options.getWidgetName(), "BlockChart")){
            options.widgetUrl = "LeaderBoard" + options.kpiIdForBlockChart;
        }

        return delegate.getKpiData(options);
    }

    @Override
    public KpiDataItem[] getKpiDataLastFact(LeaderBoardOptions options) {
        return delegate.getKpiDataLastFact(options);
    }

    @Override
    public ChartMetaData[] getLayout(LeaderBoardOptions options) {
        return delegate.getLayout(options);
    }
}
