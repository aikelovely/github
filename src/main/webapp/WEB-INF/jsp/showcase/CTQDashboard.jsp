<%@ page import="ru.alfabank.dmpr.filter.ctq.CTQFilter" %>
<%@ page import="ru.alfabank.dmpr.model.ctq.CTQLayoutItem" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="ru.alfabank.dmpr.infrastructure.linq.LinqWrapper" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="ru.alfabank.dmpr.filter.ctq.CTQFilterHelper" %>
<%@ page import="ru.alfabank.dmpr.model.Week" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Витрина показателей CTQ"/>

<%
    CTQFilter ctqFilter = RequestContextUtils.getWebApplicationContext(request).getBean(CTQFilter.class);
    CTQLayoutItem[] layout = ctqFilter.getLayout();
    Week[] weeks = ctqFilter.getWeeks();

    LinqWrapper<CTQLayoutItem> layoutWrapper = LinqWrapper.from(layout);

    request.setAttribute("layout", new ObjectMapper().writeValueAsString(layout));
    request.setAttribute("weeks", new ObjectMapper().writeValueAsString(weeks));

    CTQLayoutItem[] sections = CTQFilterHelper.getSections(layoutWrapper);
%>

<c:set var="dashboardSections">
    <% for (int i = 0; i < sections.length; i++) { %>
    <div class="dashboard-section">
        <header><%=sections[i].blockGroupName%>
        </header>
        <% LinqWrapper<CTQLayoutItem> sectionBlocks = CTQFilterHelper.getBlocksBySection(layoutWrapper, sections[i].blockGroupId); %>
        <div class="row">
            <div class="col-md-6" style="width: 610px !important;">
                <%
                    CTQLayoutItem[] firstColumnBlocks = CTQFilterHelper.getBlocksByColumn(sectionBlocks, 1);
                    for (int j = 0; j < firstColumnBlocks.length; j++) { %>
                <chart params="name: '<%= firstColumnBlocks[j].getBlockCode()%>', group: 'default'"></chart>
                <% } %>
            </div>
            <div class="col-md-6 shift-col" style="width: 610px !important;">
                <%
                    CTQLayoutItem[] secondColumnBlocks = CTQFilterHelper.getBlocksByColumn(sectionBlocks, 2);
                    for  (int j = 0; j < secondColumnBlocks.length; j++) { %>
                <chart params="name: '<%= secondColumnBlocks[j].getBlockCode()%>', group: 'default'"></chart>
                <% } %>
            </div>
        </div>
    </div>
    <% } %>
</c:set>

<t:layout title="${title}">

    <jsp:attribute name="css">
        <style scoped>

            /* Dashboard Common */

            @media (max-width: 1250px) {
                .shift-col {
                    margin-left: 15px;
                    margin-top: 20px;
                }
            }

            .dashboard-section {
                background-color: #fff;
                margin: 20px 0;
                border: 1px solid #e5e5e5;
                padding-bottom: 5px;
            }

            .dashboard-section header {
                background-color: #D8D8D8;
                font-size: 17px;
                padding: 9px;
                margin-bottom: 2px;
                border-radius: 4px;
                text-align: left;
            }

            .dashboard-section .row div:first-child {
                padding-right: 0;
            }

            .dashboard-section .row div:last-child {
                padding-left: 0;
            }

            .dashboard-section .row .col-md-6 chart:last-child .chart {
                margin-bottom: 0;
            }

            .dashboard-chart {
                margin-left: 5px;
                border: 1px solid rgba(0, 0, 0, .15);
                border-radius: 4px;
                margin-right: 5px;
                height: 170px;
            }

            .dashboard-help-btn {
                position: absolute;
                left: 4px;
                top: 6px;
                z-index: 2;
                padding: 0px 2px 2px 2px;
                margin-left: 7px;
            }

            .dashboard-help-btn-right {
                left: 550px;
            }

            .dashboard-chart .table {
                width: 61%;
                float: left;
                margin-bottom: 0;
            }

            .popover {
                max-width: 600px;
            }

            .popover.right {
                margin-left: 15px;
            }

            .popover-content {
                padding-left: 10px !important;
            }

            .dashboard-error-message {
                max-height: 106px;
            }

            /* Dashboard Index Classes */

            .dashboard-chart .index-name {
                font-size: 16px;
                font-weight: bold;
                margin: 0 0 0 36px;
                color: #707070;
                text-align: left;
                padding-bottom: 0px;
            }

            .dashboard-chart .index-value {
                padding-right: 5px;
                line-height: 15px;
                font-size: 32px;
                margin-top: 7px;
            }

            .dashboard-chart .index-goal {
                color: #3770A7;
            }

            .dashboard-chart .index-goal span {
                font-size: 22px;
            }

            .dashboard-chart .index-icon-suffix-wrapper {
                margin-top: -10px;
            }

            .dashboard-chart .index-suffix {
                font-size: 17px;
                margin-top: -12px;
            }

            /* Dashboard Highcharts Classes */

            .dashboard-chart .highcharts-container {
                border-width: 0;
                overflow: visible !important;
            }

            g[class=highcharts-tooltip] {
                visibility: hidden;
            }

            .dashboard-chart .highcharts-container div[class=highcharts-tooltip] {
                background-color: rgba(249, 249, 249, 0.85098);
                z-index: 1000;
                padding: 0px 15px 15px 5px;
                border-style: solid;
                border-color: rgb(204, 204, 204);
                border-width: 1px;
                border-radius: 3px;
            }

            .dashboard-chart  .highcharts-container div[class=highcharts-tooltip] > span:first-child {
                position: relative !important;
            }
            /* Dashboard Icons&Labels Classes */

            .icon-help {
                background-size: 20px 20px;
                width: 20px;
                height: 20px;
                margin-right: 0;
            }

            .icon-dynamic {
                background-size: 20px 20px;
                width: 20px;
                height: 20px;
                margin-right: 0;
            }

            .green-label {
                color: #001000;
            }

            .red-label {
                color: #060B28;
            }

            .icon-triangle-green {
                width: 15px;
                height: 14px;
                background: url("../images/triangle-green.png");
            }

            .icon-triangle-green-down {
                width: 15px;
                height: 14px;
                background: url("../images/triangle-green-down.png");
            }

            .icon-triangle-red {
                width: 15px;
                height: 13px;
                background: url("../images/triangle-red.png");
            }

            .icon-triangle-red-up {
                width: 15px;
                height: 13px;
                background: url("../images/triangle-red-up.png");
            }

            .icon-triangle-yellow {
                width: 17px;
                height: 8px;
                background: url("../images/triangle-yellow.png");
            }

            /* Dashboard Simple Template */

            .dashboard-chart .simple-template {
                width: 63%;
            }

            .dashboard-chart .simple-template td {
                padding-top: 25px;
            }

            .dashboard-chart .simple-template td:first-child {
                padding-left: 7px;
                width: 47%;
            }

            .dashboard-chart .simple-template-highchart {
                width: 37%;
                height: 145px;
            }

            /* Dashboard NoData Template */
            .no-data-chart .index-name {
                padding-top: 8px;
            }

            .no-data-chart .no-data-message {
                position: absolute;
                top: 50%;
                left: 45%;
            }

            /* Dashboard Grouped Template */

            .dashboard-chart .grouped-template td, .dashboard-chart .grouped-template th {
                padding-top: 1px;
            }

            .dashboard-chart .grouped-template .index-icon-suffix-wrapper {
                margin-top: 1px;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>

            var chartsConfig = _.chain(${layout}).groupBy('blockCode').reduce(function (obj, e) {
                var first = e[0], result = {
                    jsFunc: createDashboardChart,
                    dataSource: "CTQDashboardIndex"
                };

                result.additionalParams = {
                    kpiIds: _.map(e, function (el) {
                        return el.id;
                    })
                };

                result.customParams = {
                    blockGroupId: first.blockGroupId,
                    blockId: first.blockId,
                    blockName: first.blockName,
                    blockDescription: first.blockDescription,
                    blockShowNameFlag: first.blockShowNameFlag === "Y",
                    blockShowNormFlag: first.blockShowNormFlag === "Y",
                    blockShowFactFlag: first.blockShowFactFlag === "Y",
                    blockShowSlFlag: first.blockShowSlFlag === "Y",
                    blockHideCaptionFlag: first.blockHideCaptionFlag === "Y",
                    blockInverseFlag: first.blockZeroTargetFlag === "Y",
                    blockDrillDownUrl: first.blockDrillDownUrl,
                    metrics: _.chain(e).map(function (el) {
                        return {
                            id: el.id,
                            alias: el.alias,
                            description: el.description,
                            barColor: el.barColor,
                            normPrefix: el.normPrefix == null ? "" : el.normPrefix,
                            orderNum: el.orderNum
                        }
                    }).sortBy("orderNum").value(),
                    series: []
                };

                result.customParams.blockType = result.customParams.metrics.length > 1 ? "grouped" : "single";

                obj[first.blockCode] = result;

                return obj;
            }, {}).value();

            var config = {
                groups: [{
                    name: "default",
                    filters: {
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            dataSource: {
                                url: "CTQFilter/timeUnits"
                            },
                            width: 150,
                            forceShowCharts: true
                        }
                    },
                    charts: chartsConfig
                }],
                cookies: false,
                forceShowCharts: true
            };

            app.init(config);

            var helpers = {
                templatesWrappers: {
                    single: "<div class=\"dashboard-chart\" data-bind=\"template: { name: 'single-chart-template'}\"></div>",
                    grouped: "<div class=\"dashboard-chart\" data-bind=\"template: { name: 'grouped-chart-template'}\"></div>",
                    nodata: "<div class=\"dashboard-chart no-data-chart\" data-bind=\"template: { name: 'nodata-chart-template'}\"></div>"
                },
                styleHelpers: {
                    colors: {
                        black: "#001000",
                        green: "green",
                        red: "red"
                    },
                    labelClasses: {
                        green: "green-label",
                        red: "red-label"
                    },
                    iconClasses: {
                        yellow: "icon-triangle-yellow",
                        green: "icon-triangle-green",
                        green_down: "icon-triangle-green-down",
                        red: "icon-triangle-red",
                        red_up: "icon-triangle-red-up"
                    }
                },
                highchartsConfigurations: {
                    single: function (xAxis, series) {
                        return {
                            chart: {
                                type: 'column',
                                marginTop: 25
                            },
                            title: {
                                text: null
                            },
                            xAxis: xAxis,
                            yAxis: {
                                lineWidth: 0,
                                minorGridLineWidth: 0,
                                gridLineColor: 'transparent',
                                title: null,
                                labels: {
                                    enabled: false
                                },
                                minorTickLength: 0,
                                tickLength: 0
                            },
                            legend: {
                                enabled: false,
                                itemStyle: {
                                    "fontSize": "9px"
                                }
                            },
                            tooltip: {
                                useHTML: true,
                                pointFormat: "{point.toolTip}",
                                headerFormat: "",
                                valueDecimals: 2,
                                borderColor: "#CCCCCC",
                                positioner: function(labelWidth, labelHeight, point){
                                    var tooltipX, tooltipY;
                                    if (point.plotX + labelWidth > this.chart.plotWidth) {
                                        tooltipX = point.plotX + this.chart.plotLeft - labelWidth - 20;
                                    } else {
                                        tooltipX = point.plotX + this.chart.plotLeft + 20;
                                    }
                                    tooltipY = point.plotY + this.chart.plotTop - 20;
                                    return {
                                        x: tooltipX,
                                        y: tooltipY
                                    }
                                }
                            },
                            plotOptions: {
                                column: {
                                    pointPadding: 0.1,
                                    borderWidth: 0,
                                    dataLabels: {
                                        enabled: true,
                                        crop: false,
                                        format: '{point.formatter}%',
                                        overflow: "none",
                                        y: 3,
                                        style: {
                                            color: helpers.styleHelpers.colors.black
                                        }
                                    }
                                }
                            },
                            credits: {
                                enabled: false
                            },
                            exporting: {
                                enabled: false
                            },
                            series: series
                        }
                    },
                    grouped: function (xAxis, series) {
                        xAxis.labels.style.fontSize = '9px';
                        return {
                            chart: {
                                type: 'column'
                            },
                            title: {
                                text: null
                            },
                            xAxis: xAxis,
                            yAxis: {
                                lineWidth: 0,
                                minorGridLineWidth: 0,
                                gridLineColor: 'transparent',
                                title: null,
                                labels: {
                                    enabled: false
                                },
                                minorTickLength: 0,
                                tickLength: 0
                            },
                            legend: {
                                align: 'center',
                                verticalAlign: 'bottom',
                                layout: 'horizontal',
                                itemStyle: {
                                    "fontSize": "9px"
                                },
                                y: 5,
                                itemMarginTop: 0,
                                margin: 0,
                                itemDistance: 5
                            },
                            plotOptions: {
                                column: {
                                    pointPadding: 0.1,
                                    borderWidth: 0,
                                    dataLabels: {
                                        enabled: true,
                                        format: '{point.formatter}' + (series.length <= 2 ? "%" : ""),
                                        crop: false,
                                        overflow: "none",
                                        y: 3,
                                        style: {
                                            "fontSize": "8px",
                                            color: helpers.styleHelpers.colors.black
                                        }
                                    }
                                }
                            },
                            tooltip: {
                                useHTML: true,
                                pointFormat: '{point.toolTip}',
                                headerFormat: "",
                                borderColor: "#CCCCCC",
                                positioner: function(labelWidth, labelHeight, point){
                                    var tooltipX, tooltipY;
                                    if (point.plotX + labelWidth > this.chart.plotWidth) {
                                        tooltipX = point.plotX + this.chart.plotLeft - labelWidth - 20;
                                    } else {
                                        tooltipX = point.plotX + this.chart.plotLeft + 20;
                                    }
                                    tooltipY = point.plotY + this.chart.plotTop - 20;
                                    return {
                                        x: tooltipX,
                                        y: tooltipY
                                    }
                                }
                            },
                            credits: {
                                enabled: false
                            },
                            exporting: {
                                enabled: false
                            },
                            series: series
                        };
                    }
                },
                weekArray: ${weeks}
            };

            function getHighChartConfiguration(viewModel, filterData) {
                var series = viewModel.series,
                        xAxisFilterData = {
                            startDate: moment(_.minBy(series[0].data, function (e) {
                                return e.x;
                            }).x).startOf('day').toDate(),
                            endDate: moment(_.maxBy(series[0].data, function (e) {
                                return e.x;
                            }).x).startOf('day').toDate(),
                            timeUnitId: filterData.timeUnitId
                        };
                var xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);
                xAxis.labels = {
                    formatter: function () {
                        var result = "";
                        switch (parseInt(filterData.timeUnitId)) {
                            case 3:
                                result = moment(this.value).format('DD.MM') + ' - ' + moment(this.value).add(6, 'days').format('DD.MM');
                                break;
                            case 4:
                                result = moment(this.value).format('MMMM, YY');
                                result = result.charAt(0).toUpperCase() + result.slice(1);
                                break;
                            default:
                                result = moment(this.value).format('DD.MM.YY');
                                break;
                        }
                        return result;
                    },
                    style: {
                        color: helpers.styleHelpers.colors.black
                    },
                    tickPixelInterval: 5
                };
                return helpers.highchartsConfigurations[viewModel.blockType](xAxis, series);
            }

            function completeMetric(viewModel, metricConfig, metricData, metricSeries) {
                function toStringRangeCondition(value, decimals) {
                    if (typeof value !== "number") return null;
                    var percentValue = value * 100;
                    return percentValue.toFixed((percentValue > 1 || percentValue == 0) ? (decimals || 0) : 2);
                }

                function toStringValueCondition(value, decimals) {
                    if (typeof value !== "number") return null;
                    var percentValue = value * 100;
                    return percentValue.toFixed(parseFloat(percentValue.toFixed(0)) == parseFloat(percentValue.toFixed(2)) ? (decimals || 0) : 2);
                }

                function getCountTooltip(ok, total) {
                    return 'ОК: {0}<br>Всего: {1}'.format(ok, total);
                }

                function getValueLabelCss() {
                    return (viewModel.blockInverseFlag ? (metricData.value <= metricData.normativeValue) :
                            (metricData.value >= metricData.normativeValue)) ? helpers.styleHelpers.labelClasses.green : helpers.styleHelpers.labelClasses.red;
                }

                function getValueIconCss() {
                    if (!metricData.prevValue || metricData.value === metricData.prevValue) return helpers.styleHelpers.iconClasses.yellow;
                    if (viewModel.blockInverseFlag) {
                        return metricData.value <= metricData.prevValue ? helpers.styleHelpers.iconClasses.green_down : helpers.styleHelpers.iconClasses.red_up;
                    } else {
                        return metricData.value >= metricData.prevValue ? helpers.styleHelpers.iconClasses.green : helpers.styleHelpers.iconClasses.red;
                    }
                }

                function getSLLabelCss() {
                    return (metricData.qualityLevel >= metricData.qualityLevelNormative) ? helpers.styleHelpers.labelClasses.green : helpers.styleHelpers.labelClasses.red;
                }

                function getSLIconCss() {
                    if (!metricData.prevQualityLevel || metricData.qualityLevel === metricData.prevQualityLevel) return helpers.styleHelpers.iconClasses.yellow;
                    return (metricData.qualityLevel >= metricData.prevQualityLevel) ? helpers.styleHelpers.iconClasses.green : helpers.styleHelpers.iconClasses.red;
                }

                function getColumnColor(value, colors) {
                    return (viewModel.blockInverseFlag ? (value <= metricData.normativeValue) :
                            (value >= metricData.normativeValue)) ? colors.green : colors.red;
                }

                if (!metricSeries || !metricData) {
                    metricConfig.noData = true;
                    return;
                }

                var isCountFilled = metricData.numerator > 0 && metricData.denominator > 0;

                metricConfig.title = metricConfig.alias != null ? metricConfig.alias : metricData.shortName;

                metricConfig.factValue = toStringRangeCondition(metricData.value);
                metricConfig.slValue = toStringRangeCondition(metricData.qualityLevel);

                metricConfig.valueNormative = toStringValueCondition(metricData.normativeValue);
                metricConfig.slNormative = toStringRangeCondition(metricData.qualityLevelNormative);

                metricConfig.normSuffix = "%";
                metricConfig.valueSuffix = "%";
                metricConfig.slSuffix = "%";

                metricConfig.valueTooltip = isCountFilled ? getCountTooltip(metricData.numerator, metricData.denominator) : "";

                metricConfig.valueLabelCss = getValueLabelCss();
                metricConfig.valueIconCss = getValueIconCss();

                metricConfig.slLabelCss = getSLLabelCss();
                metricConfig.slIconCss = getSLIconCss();

                var series = _.cloneDeep(metricSeries);

                series.name = metricConfig.title;

                var singleChartColors = {
                    green: helpers.styleHelpers.colors.green,
                    red: helpers.styleHelpers.colors.red
                };

                if (viewModel.blockType !== "single") {
                    if (metricConfig.barColor) {
                        series.color = metricConfig.barColor;
                    }
                } else {
                    if (metricConfig.barColor) {
                        var colorsArray = metricConfig.barColor.split(',');
                        singleChartColors.green = colorsArray[0] || singleChartColors.green;
                        singleChartColors.red = colorsArray[1] || singleChartColors.red;
                    }
                }

                _.forEach(series.data, function (point) {
                    point.formatter = toStringRangeCondition(point.y);
                    if (viewModel.blockType === "single") {
                        point.color = getColumnColor(point.y, singleChartColors);
                    }
                    point.toolTip = '<span style="font-size:10px">{0}</span><br>{1}<b>{2}{3}</b>'.format(point.periodName, metricConfig.title.length ? metricConfig.title + " :" : "", toStringRangeCondition(point.y, 2), metricConfig.valueSuffix);
                });

                if (isCountFilled) {
                    var point = series.data[0];
                    point.toolTip += '<br>' + getCountTooltip(metricData.numerator, metricData.denominator);

                    if (series.data.length > 1) {
                        point = series.data[series.data.length - 1];
                        point.toolTip += '<br>' + getCountTooltip(metricData.prevNumerator, metricData.prevDenominator);
                    }
                }

                viewModel.series.push(series);
            }

            function createDashboardChart($container, filterData, jsonData, customParams) {
                var bag = jsonData[0].bag, viewModel = _.cloneDeep(customParams);

                if (!bag || !bag.metrics || !bag.metrics.length) {
                    $container.append(helpers.templatesWrappers.nodata);
                    ko.applyBindingsToDescendants(viewModel, $container.get(0));
                    return;
                }

                var data = bag.metrics, series = jsonData[0].series;

                viewModel.filterData = filterData;

                _.forEach(viewModel.metrics, function (metricConfig) {
                    var metricSeries = _.find(series, function (s) {
                                return metricConfig.id === parseFloat(s.bag.kpiId);
                            }),
                            metricData = _.find(data, function (d) {
                                return metricConfig.id === parseFloat(d.id);
                            });
                    completeMetric(viewModel, metricConfig, metricData, metricSeries);
                });

                $container.append(_.every(viewModel.metrics, ["noData", true]) ? helpers.templatesWrappers.nodata : helpers.templatesWrappers[viewModel.blockType]);

                ko.applyBindingsToDescendants(viewModel, $container.get(0));

                if (viewModel.series.length) {
                    $container.find(".hc").highcharts(getHighChartConfiguration(viewModel, filterData));
                }
            }

            function redirectToDynamic(data) {
                function weekObjectDateToMoment(weekDate){
                    return moment(new Date(weekDate.year, weekDate.monthOfYear - 1, weekDate.dayOfMonth));
                }

                var minTick, maxTick, minId, maxId, deductSize = 12, isWeekPeriod = data.filterData.timeUnitId == 3, drillDownUrl;
                _.each(data.series, function (s) {
                    minTick = Math.min(minTick || Number.POSITIVE_INFINITY, _.minBy(s.data, function (p) { return p.x; }).x);
                    maxTick = Math.max(minTick || Number.NEGATIVE_INFINITY, _.maxBy(s.data, function (p) { return p.x; }).x);
                    minId = Math.min(minId || Number.POSITIVE_INFINITY, _.minBy(s.data, function (p) { return p.periodId; }).periodId);
                    maxId = Math.max(maxId || Number.NEGATIVE_INFINITY, _.maxBy(s.data, function (p) { return p.periodId; }).periodId);
                });

                var minMoment = moment(minTick), maxMoment = moment(maxTick),
                    firstWeekOfYear = _.chain(helpers.weekArray).filter(function(w){return w.year == minMoment.year();})
                        .sortBy(function(w){return w.weekNum;}).value()[0],
                    firstWeekOfYearStartDate = weekObjectDateToMoment(firstWeekOfYear.startDate),
                    deductedDateFrom = minMoment.clone().subtract(deductSize, isWeekPeriod ? 'week' : 'month').startOf('day');

                if (data.blockDrillDownUrl) {
                    var dateTo = maxMoment.clone().add(1, isWeekPeriod ? 'week' : 'month').subtract(1, 'days').startOf('day'),
                        dateFrom;

                    if (!isWeekPeriod) {
                        dateFrom = deductedDateFrom.isBefore(minMoment.startOf('year')) ? minMoment.startOf('year') : deductedDateFrom;
                    } else {
                       dateFrom = deductedDateFrom.isBefore(firstWeekOfYearStartDate) ? firstWeekOfYearStartDate : deductedDateFrom;
                    }

                    drillDownUrl = data.blockDrillDownUrl
                            .replace("{DateFrom}", encodeURIComponent(dateFrom.format('YYYY-MM-DD')))
                            .replace("{DateTo}", encodeURIComponent(dateTo.format('YYYY-MM-DD')))
                            .replace("{IntervalType}", encodeURIComponent(data.filterData.timeUnitId));

                    if (drillDownUrl[0] === "/") {
                        app.openNewShowcase(drillDownUrl.slice(1), {});
                    } else {
                        window.open(drillDownUrl);
                    }
                } else {
                    var year = minMoment.year(), newStarDateId;

                    if (!isWeekPeriod) {
                        newStarDateId = (minId - deductSize < 0) ? 0 : (minId - deductSize);
                    } else {
                        var deductedWeek = _.find(helpers.weekArray, function(w) {
                            return (weekObjectDateToMoment(w.startDate).isBefore(deductedDateFrom) ||
                                   weekObjectDateToMoment(w.startDate).isSame(deductedDateFrom)) &&
                                   weekObjectDateToMoment(w.endDate).isAfter(deductedDateFrom);
                        });

                        newStarDateId = deductedWeek && weekObjectDateToMoment(deductedWeek.startDate).isAfter(firstWeekOfYearStartDate)
                                ? deductedWeek.id : firstWeekOfYear.id;
                    }

                    drillDownUrl = "CTQDashboardDynamic?blockGroupId=" + encodeURIComponent(data.blockGroupId) +
                            "&blockId=" + encodeURIComponent(data.blockId) +
                            "&timeUnitId=" + encodeURIComponent(data.filterData.timeUnitId) +
                            "&startYear=" + encodeURIComponent(year) +
                            "&startDateId=" + encodeURIComponent(newStarDateId) +
                            "&endDateId=" + encodeURIComponent(maxId) +
                            "&forceShowCharts=true";

                    app.openNewShowcase(drillDownUrl, {}, false);
                }
            }

            function redirectToReport(data) {
                app.openNewShowcase("CTQDashboardReport", {}, false);
            }

            Highcharts.setOptions({
                colors: ["#BF6B24", "#3770A7", "#69C656", "#B05955", "#e4d354", "#361356", "#8d4653", "#91e8e1", "#4B966E", "#D46A6A"]
            });

            // IE9 support
            if (!window.location.origin) {
                window.location.origin = window.location.protocol + "//"
                        + window.location.hostname
                        + (window.location.port ? ":" + window.location.port : "");
            }
        </script>

        <script type="text/html" id="single-chart-template">
            <table class="table simple-template">
                <button class="dashboard-help-btn btn btn-default"
                        data-bind="popover: {options: {title: 'Пояснение к показателям', html: true, content: blockDescription}}">
                    <i class="icon-help" title="Пояснение к показателям"></i>
                </button>
                <button class="dashboard-help-btn dashboard-help-btn-right btn btn-default"
                        data-bind="click: redirectToDynamic">
                    <i class="icon-dynamic" title="Перейти к динамике по показателю"></i>
                </button>
                <caption class="index-name" data-bind="text: blockName"></caption>
                <thead data-bind="visible: !blockHideCaptionFlag">
                <tr>
                    <th data-bind="visible: blockShowNormFlag">Цель</th>
                    <th data-bind="visible: blockShowFactFlag">Факт</th>
                    <th data-bind="visible: blockShowSlFlag">Ур. качества</th>
                </tr>
                </thead>
                <tr data-bind="with: metrics[0]">
                    <td data-bind="visible: $parent.blockShowNormFlag" class="index-value index-goal">
                        <div data-bind="tooltip: {title: description, html: true}">
                            <span data-bind="text: normPrefix"></span>
                            <!--ko text: valueNormative--><!--/ko-->
                            <span data-bind="text: normSuffix"></span>
                        </div>
                    </td>
                    <td data-bind="visible: $parent.blockShowFactFlag" class="index-value">
                        <div data-bind="css: valueLabelCss, tooltip: {title: valueTooltip, html: true}"
                             class="pull-left">
                            <!--ko text: factValue--><!--/ko-->
                        </div>
                        <div class="pull-left index-icon-suffix-wrapper" data-bind="css: valueLabelCss">
                            <i data-bind="css: valueIconCss"></i>

                            <div class="index-suffix" data-bind="text: valueSuffix"></div>
                        </div>
                    </td>
                    <td data-bind="visible: $parent.blockShowSlFlag" class="index-value">
                        <div data-bind="css: slLabelCss" class="pull-left">
                            <!--ko text: slValue--><!--/ko-->
                        </div>
                        <div class="pull-left index-icon-suffix-wrapper" data-bind="css: slLabelCss">
                            <i data-bind="css: slIconCss"></i>

                            <div class="index-suffix" data-bind="text: slSuffix"></div>
                        </div>
                    </td>
                </tr>
            </table>
            <div class="pull-left simple-template-highchart hc"></div>
            <div class="clearfix"></div>
        </script>

        <script type="text/html" id="grouped-chart-template">
            <table class="table grouped-template">
                <button class="dashboard-help-btn btn btn-default"
                        data-bind="popover: {options: {title: 'Пояснение к показателям', html: true, content: blockDescription}}">
                    <i class="icon-help" title="Пояснение к показателям"></i>
                </button>
                <button class="dashboard-help-btn dashboard-help-btn-right btn btn-default"
                        data-bind="click: redirectToDynamic">
                    <i class="icon-dynamic" title="Перейти к динамике по показателю"></i>
                </button>
                <caption class="index-name" data-bind="text: blockName"></caption>
                <thead data-bind="visible: !blockHideCaptionFlag">
                <tr>
                    <th data-bind="visible: blockShowNameFlag"></th>
                    <th data-bind="visible: blockShowNormFlag">Цель</th>
                    <th data-bind="visible: blockShowFactFlag">Факт</th>
                    <th data-bind="visible: blockShowSlFlag">Ур. кач.</th>
                </tr>
                </thead>
                <tbody>
                <!-- ko foreach: metrics -->
                <tr>
                    <td data-bind="visible: $parent.blockShowNameFlag, text: title"></td>
                    <td data-bind="visible: $parent.blockShowNormFlag" class="index-goal">
                        <div data-bind="tooltip: {title: description, html: true}, text: normPrefix + valueNormative + normSuffix"></div>
                    </td>
                    <td data-bind="visible: $parent.blockShowFactFlag">
                        <div data-bind="css: valueLabelCss, tooltip: {title: valueTooltip, html: true}, text: factValue + valueSuffix"
                             class="pull-left">
                        </div>
                        <div class="pull-left index-icon-suffix-wrapper" data-bind="css: valueLabelCss">
                            <i data-bind="css: valueIconCss"></i>
                        </div>
                    </td>
                    <td data-bind="visible: $parent.blockShowSlFlag">
                        <div data-bind="css: slLabelCss, text: slValue + slSuffix" class="pull-left">
                        </div>
                        <div class="pull-left index-icon-suffix-wrapper" data-bind="css: slLabelCss">
                            <i data-bind="css: slIconCss"></i>
                        </div>
                    </td>
                </tr>
                <!--/ko-->
                </tbody>
            </table>
            <div class="pull-left simple-template-highchart hc"></div>
        </script>

        <script type="text/html" id="nodata-chart-template">
            <button class="dashboard-help-btn btn btn-default"
                    data-bind="popover: {options: {title: 'Пояснение к показателям', html: true, content: blockDescription}}">
                <i class="icon-help" title="Пояснение к показателям"></i>
            </button>
            <div class="index-name" data-bind="text: blockName"></div>
            <div class="no-data-message">
                Нет данных
            </div>
            <div class="clearfix"></div>
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>
        <button data-bind="click: redirectToReport" class="btn btn-default btn-mini pull-right">
            <i class="icon-excel"></i>
            <span data-bind="text: 'К выгрузке показателей'"></span>
        </button>
        <filter params="name: 'timeUnitId'"></filter>
        <filter-log></filter-log>
        <div data-bind="visible: visibleCharts" class="charts-container">
                ${dashboardSections}
        </div>
    </jsp:body>
</t:layout>
