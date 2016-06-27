package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.stereotype.Service;

@Service
public class MassOpenAccountDynamicByDay extends MassOpenAccountDynamicBase {
    public MassOpenAccountDynamicByDay() {
        super("0-24;");
    }
}
