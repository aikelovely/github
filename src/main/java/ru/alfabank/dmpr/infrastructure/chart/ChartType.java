package ru.alfabank.dmpr.infrastructure.chart;

/**
 * Тип графика Highcharts. По-умолчанию Line. Подробности можно найти тут http://www.highcharts.com/docs/chart-and-series-types/chart-types
 */
public enum ChartType {
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/line-chart">Line Chart</a>
     */
    line,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/spline-chart">Spline Chart</a>
     */
    spline,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/area-chart">Area Chart</a>
     */
    area,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/areaspline-chart">Areaspline Chart</a>
     */
    areaspline,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/column-chart">Column Chart</a>
     */
    column,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/bar-chart">Bar Chart</a>
     */
    bar,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/pie-chart">Pie Chart</a>
     */
    pie,
    /**
     * <a href="http://www.highcharts.com/docs/chart-and-series-types/scatter-chart">Scatter Chart</a>
     */
    scatter,
    /**
     * <a href="http://www.highcharts.com/demo/gauge-speedometer">Gauge Chart</a>
     */
    gauge,
    /**
     * <a href="http://www.highcharts.com/demo/box-plot">Box Plot</a>
     */
    boxplot,
    /**
     * <a href="http://www.highcharts.com/demo/waterfall">Waterfall</a>
     */
    waterfall,
    /**
     * <a href="http://www.highcharts.com/demo/funnel">Funnel Chart</a>
     */
    funnel,
    /**
     * <a href="http://www.highcharts.com/demo/bubble">Bubble Chart</a>
     */
    bubble
}
