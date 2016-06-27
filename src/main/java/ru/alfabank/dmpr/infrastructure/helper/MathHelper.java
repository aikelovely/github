package ru.alfabank.dmpr.infrastructure.helper;

public class MathHelper {
    private MathHelper() {
    }

    public static double safeDivide(double a, double b) {
        if (b == 0) return 0;
        return a / b;
    }

    public static Double safeDivide(Double a, Double b) {
        if (b == null || b == 0) return 0d;
        return a / b;
    }
}
