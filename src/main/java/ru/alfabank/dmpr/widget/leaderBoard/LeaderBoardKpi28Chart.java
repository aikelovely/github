package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoardKpi28Chart extends LeaderBoardByMonthChart {
    public LeaderBoardKpi28Chart() {
        super("KPIOB~28");
    }
}
