package ru.alfabank.dmpr.widget.leaderBoard;

import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.chart.ChartType;
import ru.alfabank.dmpr.infrastructure.chart.Point;
import ru.alfabank.dmpr.infrastructure.chart.Series;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.leaderBoard.IntervalType;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseChart;

import java.util.*;

/**
 * График "Эффективность" (KPIOB~19)<br/>
 */
@Service
public class LeaderBoardKpi19Chart extends LeaderBoardQuarterDynamicBase {
    public LeaderBoardKpi19Chart() {
        super("KPIOB~19");
    }
}
