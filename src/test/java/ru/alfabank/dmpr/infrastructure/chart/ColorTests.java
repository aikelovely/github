package ru.alfabank.dmpr.infrastructure.chart;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorTests {

    @Test
    public void testValueOf() throws Exception {
        assertSame(Color.valueOf(255, 255, 255), Color.valueOf("#FFFFFF"));
        assertSame(Color.valueOf(253, 166, 162), Color.RedColor);
        assertEquals("#FFFFFF", Color.valueOf("#FFFFFF").toString());
    }
}