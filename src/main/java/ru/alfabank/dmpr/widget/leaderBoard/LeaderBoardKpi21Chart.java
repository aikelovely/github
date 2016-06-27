package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.stereotype.Service;

@Service
public class LeaderBoardKpi21Chart extends LeaderBoardQuarterDynamicBase {
    public LeaderBoardKpi21Chart() {
        super("KPIOB~21");
    }

    @Override
    protected double getMultiplier() {
        return 1;
    }
}

