package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoardKpi25Chart extends LeaderBoardByMonthChart {
    public LeaderBoardKpi25Chart() {
        super("KPIOB~25");
    }
}
