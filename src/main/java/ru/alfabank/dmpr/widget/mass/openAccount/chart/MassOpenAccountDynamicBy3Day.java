package ru.alfabank.dmpr.widget.mass.openAccount.chart;

import org.springframework.stereotype.Service;

@Service
public class MassOpenAccountDynamicBy3Day extends MassOpenAccountDynamicBase {
    public MassOpenAccountDynamicBy3Day() {
        super("0-72;");
    }
}
