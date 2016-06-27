package ru.alfabank.dmpr.infrastructure.chart;

import org.apache.commons.lang3.ArrayUtils;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;

public class TrendLine {
    public static Series create(final Point[] points, final Double maxY) {
        LinqWrapper<Double> xAxisValues = LinqWrapper.from(points.clone())
                .select(new Selector<Point, Double>() {
                    @Override
                    public Double select(Point point) {
                        return point.x;
                    }
                });

        LinqWrapper<Double> yAxisValues = LinqWrapper.from(points)
                .select(new Selector<Point, Double>() {
                    @Override
                    public Double select(Point point) {
                        return point.x;
                    }
                });

        int count = yAxisValues.count();
        double yAxisValuesSum = 0;
        double xAxisValuesSum = 0;
        double xxSum = 0;
        double xySum = 0;

        for (Point p : points) {
            double x = p.x != null ? p.x : 0d;
            double y = p.y != null ? p.y : 0d;

            yAxisValuesSum += y;
            xAxisValuesSum += x;
            xySum += x * y;
            xxSum += x * x;
        }

        final double slope = MathHelper.safeDivide(
                ((count * xySum) - (xAxisValuesSum * yAxisValuesSum)),
                ((count * xxSum) - (xAxisValuesSum * xAxisValuesSum)));

        final double intercept = (yAxisValuesSum - (slope * xAxisValuesSum)) / count;
        double start = (slope * xAxisValues.first()) + intercept;

        double lastXAxisValue = points[points.length - 1].x;
        double end = (slope * lastXAxisValue) + intercept;

        Point[] result = xAxisValues.select(new Selector<Double, Point>() {
            @Override
            public Point select(Double x) {
                double y = Math.max(intercept + slope * x, 0);

                return new Point(x, y);
            }
        }).toArray(Point.class);

        if(maxY != null){
            boolean anyLargerThenMax = LinqWrapper.from(points)
                    .any(new Predicate<Point>() {
                        @Override
                        public boolean check(final Point item) {
                            return item.y != null && item.y > maxY;
                        }
                    });

            if(anyLargerThenMax){
                // Calculate intersection
                /* f(y) = Intercept + Slope*x
                 * g(y) = _maxY
                 * Intercept + Slope*x = _maxY                 *
                 * x = (_maxY - Intercept) / Slope
                 * */


                Point interceptionPoint = new Point((maxY - intercept) / slope, maxY);
                for (Point p : result) {
                    if (p.y != null && p.y > maxY)
                    {
                        p.y = maxY;
                    }
                }
                ArrayUtils.add(result, interceptionPoint);

                result = LinqWrapper.from(result)
                        .sort(new Selector<Point, Double>() {
                            @Override
                            public Double select(Point point) {
                                return point.x;
                            }
                        }).toArray(Point.class);
            }
        }

        Series s = new Series("Линия тренда", result, ChartType.line);
        s.color = Color.valueOf("#1E90FF");

        return s;
    }
}
