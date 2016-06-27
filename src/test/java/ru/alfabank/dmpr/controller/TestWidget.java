package ru.alfabank.dmpr.controller;

import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.widget.BaseWidget;

@Service
public class TestWidget extends BaseWidget<TestOptions, TestOptions> {
    public TestWidget() {
        super(TestOptions.class);
    }

    @Override
    public TestOptions getData(TestOptions options) {
        return options;
    }
}
