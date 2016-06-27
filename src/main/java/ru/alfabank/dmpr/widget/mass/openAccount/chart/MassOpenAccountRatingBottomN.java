package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.stereotype.Service;

@Service
public class MassOpenAccountRatingBottomN extends MassOpenAccountRatingBase {
    public MassOpenAccountRatingBottomN() {
        super(-3);
    }
}
