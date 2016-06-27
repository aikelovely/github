package ru.alfabank.dmpr.controller;

import org.joda.time.LocalDateTime;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.model.BaseOptions;

public class TestOptions extends BaseOptions {
    public LocalDateTime start;
    public Color color;
    public Color colorNull;

    public TestOptions() {
    }

    public TestOptions(LocalDateTime start) {
        this.start = start;
    }

    public TestOptions(LocalDateTime start, Color color) {
        this.start = start;
        this.color = color;
    }
}
