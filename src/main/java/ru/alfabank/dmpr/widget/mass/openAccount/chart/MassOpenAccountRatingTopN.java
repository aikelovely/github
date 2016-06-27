package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.stereotype.Service;

@Service
public class MassOpenAccountRatingTopN extends MassOpenAccountRatingBase {
    public MassOpenAccountRatingTopN() {
        super(3);
    }
}
