<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Персональные кредиты и кредитные карты. Витрина декомпозиции"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .popover {
                max-width: 900px;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var filterWidth = 175;

            var chartsConfig = {
                requestByGroup: {
                    jsFunc: createRequestByGroupDynamic,
                    dataSource: "PILAndCCRequestByGroup"
                },
                ratingBest: {
                    jsFunc: createRating,
                    dataSource: "PILAndCCRating",
                    customParams: {isTop: true}
                },
                ratingWorst: {
                    jsFunc: createRating,
                    dataSource: "PILAndCCRating",
                    customParams: {isTop: false}
                },
                stageInfo: {
                    jsFunc: createStageInfoButtons,
                    dataSource: "PILAndCCStageInfo"
                },
                afterDrawDynamic: {
                    jsFunc: createAfterDrawDynamic,
                    dataSource: "PILAndCCAfterDrawDynamic"
                },
                rejectDynamic: {
                    jsFunc: createRejectDynamic,
                    dataSource: "PILAndCCRejectDynamic"
                }
            };

            var dynamicByStageConfig = {
                dynamicByStage: {
                    jsFunc: createDynamicByStage,
                    dataSource: "PILAndCCDynamicByStage"
                }
            };

            var config = {
                groups: [{
                    name: "default",
                    filters: {
                        kpiId: {
                            type: "Select",
                            multiple: false,
                            title: "KPI и метрики",
                            dataSource: {
                                url: "pilAndCCFilter/KPIs",
                                params: ["productTypeId"]
                            },
                            width: filterWidth
                        },
                        productTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Продукт",
                            dataSource: {
                                url: "pilAndCCFilter/productTypes"
                            },
                            width: filterWidth
                        },
                        manualCheckId: {
                            type: "Select",
                            multiple: false,
                            title: "Ручные проверки",
                            dataSource: {
                                url: "pilAndCCFilter/manualChecks"
                            },
                            disableIfComputed: {
                                params: ["kpiId"],
                                jsFunc: function (context) {
                                    return context.params.kpiId != 1000646009; // TTY окончательное
                                }
                            },
                            width: filterWidth
                        },
                        clientSegmentIds: {
                            type: "Select",
                            multiple: true,
                            title: "Клиент",
                            dataSource: {
                                url: "pilAndCCFilter/ClientSegments"
                            },
                            width: filterWidth
                        },
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            notAfter: "endDate",
                            defaultValue: moment().add(-1, "w").toDate()
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            notBefore: "startDate"
                        },
                        regionIds: {
                            type: "Select",
                            multiple: true,
                            title: "Регион",
                            enableClear: true,
                            enableSearch: true,
                            dataSource: {
                                url: "pilAndCCFilter/regions",
                                params: ["startDate", "endDate"]
                            },
                            width: filterWidth
                        },
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            enableClear: true,
                            enableSearch: true,
                            dataSource: {
                                url: "pilAndCCFilter/cities",
                                params: ["startDate", "endDate", "kpiId", "regionIds"]
                            },
                            width: filterWidth
                        },
                        doKkoIds: {
                            type: "Select",
                            multiple: true,
                            enableClear: true,
                            enableSearch: true,
                            title: "ДО/ККО",
                            dataSource: {
                                url: "pilAndCCFilter/branches",
                                params: ["startDate", "endDate", "kpiId", "regionIds", "cityIds"]
                            },
                            width: filterWidth
                        },
                        managerIds: {
                            type: "Select",
                            multiple: true,
                            title: "Менеджер",
                            enableClear: true,
                            enableSearch: true,
                            disableIfNull: "cityIds",
                            dataSource: {
                                url: "pilAndCCFilter/users",
                                params: ["startDate", "endDate", "kpiId", "regionIds", "cityIds", "doKkoIds"]
                            },
                            width: filterWidth
                        },
                        valueTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип значения",
                            dataSource: {
                                url: "pilAndCCFilter/FactValueTypes"
                            },
                            width: filterWidth
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Период агрегации",
                            dataSource: {
                                url: "pilAndCCFilter/TimeUnits"
                            },
                            width: filterWidth
                        },
                        requestTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип заявки",
                            optionsCaption: "Все",
                            dataSource: {
                                url: "pilAndCCFilter/RequestStatusTypes"
                            },
                            width: filterWidth
                        },
                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Уровень агрегации",
                            dataSource: {
                                url: "pilAndCCFilter/Dimensions"
                            },
                            width: filterWidth
                        },
                        sortOrderId: {
                            type: "Select",
                            multiple: false,
                            title: "Сортировка рейтинга",
                            dataSource: {
                                url: "pilAndCCFilter/SortDirections"
                            },
                            width: filterWidth
                        },
                        sourceSystemIds: {
                            type: "Select",
                            multiple: true,
                            title: "Система-источник",
                            dataSource: {
                                url: "pilAndCCFilter/Modules"
                            },
                            disableIfComputed: {
                                params: ["productTypeId"],
                                jsFunc: function (context) {
                                    return false;
                                    // return context.params.productTypeId != 1000646009; "CC" TODO: Указать правильный ID
                                }
                            },
                            width: filterWidth
                        },
                        sortTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип сортировки",
                            dataSource: {
                                url: "pilAndCCFilter/SortTypes"
                            },
                            width: filterWidth
                        }
                    },
                    charts: {
                        avgDynamic: {
                            jsFunc: createDynamicWithPie,
                            dataSource: "PILAndCCAvgDynamic"
                        },
                        timeRatioDynamic: {
                            jsFunc: createTimeRatioDynamic,
                            dataSource: "PILAndCCTimeRatioDynamic"
                        },
                        cardActivityDynamic: {
                            jsFunc: createCardActivityDynamic,
                            dataSource: "PILAndCCCardActivityDynamic",
                            reportUrl: "PILAndCCCardActivateReport"
                        }
                    },
                    tabStrips: {
                        main: {
                            sameMarkup: true,
                            tabs: [
                                {
                                    title: "Total",
                                    customParams: {reportTypeId: 0, group: "total"},
                                    additionalParams: {reportTypeId: 0},
                                    group: "total"
                                },
                                {
                                    title: "Internet",
                                    customParams: {reportTypeId: 1, group: "internet"},
                                    additionalParams: {reportTypeId: 1},
                                    group: "internet"
                                },
                                {
                                    title: "DSA",
                                    customParams: {reportTypeId: 2, group: "dsa"},
                                    additionalParams: {reportTypeId: 2},
                                    group: "dsa"
                                },
                                {
                                    title: "Branch",
                                    customParams: {reportTypeId: 3, group: "branch"},
                                    additionalParams: {reportTypeId: 3},
                                    group: "branch"
                                }
                            ]
                        }
                    },
                    slaves: [
                        {
                            name: "total",
                            charts: chartsConfig,
                            maxDrillDownLevel: 1,
                            slaves: [{
                                name: "byStagetotal",
                                charts: dynamicByStageConfig,
                                dontShowAfterMaster: true,
                                maxDrillDownLevel: 1
                            }]
                        },
                        {
                            name: "internet",
                            charts: chartsConfig,
                            maxDrillDownLevel: 1,
                            slaves: [{
                                name: "byStageinternet",
                                charts: dynamicByStageConfig,
                                dontShowAfterMaster: true,
                                maxDrillDownLevel: 1
                            }]
                        },
                        {
                            name: "dsa",
                            charts: chartsConfig,
                            maxDrillDownLevel: 1,
                            slaves: [{
                                name: "byStagedsa",
                                charts: dynamicByStageConfig,
                                dontShowAfterMaster: true,
                                maxDrillDownLevel: 1
                            }]
                        },
                        {
                            name: "branch",
                            charts: chartsConfig,
                            maxDrillDownLevel: 1,
                            slaves: [{
                                name: "byStagebranch",
                                charts: dynamicByStageConfig,
                                dontShowAfterMaster: true,
                                maxDrillDownLevel: 1
                            }]
                        }]
                }],
                cookies: true
            };

            function format2Digits(value) {
                return ("0" + value).slice(-2);
            }

            // TODO: Вынести в chartUtils если потребуется где-то еще
            function hoursTOHHMMSS(value) {
                var totalSecond = Math.round(value * 3600);

                var hours = Math.floor(value);
                totalSecond -= hours * 3600;

                var mins = Math.floor(totalSecond / 60);
                totalSecond -= mins * 60;

                var seconds = totalSecond;
                if (seconds === 60) {
                    seconds = 0;
                    mins++;
                }

                var hoursAsString = hours > 99 ? hours.toString() : format2Digits(hours);

                var result = "" + hoursAsString + ":" + format2Digits(mins) + ":" + format2Digits(seconds);
                return result;
            }

            function createDynamicWithPie($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var kpi = _.find(app.viewModel.getFilter("kpiId").options(),
                        function (el) {
                            return el.id == filterData.kpiId;
                        });

                var title = kpi.name,
                        width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false),
                        isPercent = filterData.valueTypeId == 2;

                if (!isPercent) {
                    var averageValue = chart.bag.averageValue.toFixed(2);

                    var plotLines = [{
                        color: 'lightgray',
                        label: {
                            align: 'right',
                            text: '<b>Среднее</b>: {0}'.format(hoursTOHHMMSS(averageValue)),
                            x: -5
                        },
                        value: averageValue,
                        width: 2,
                        zIndex: 10
                    }];

                    $container.highcharts({
                        chart: {
                            type: "column"
//                            width: width
                        },
                        legend: {enabled: false},
                        title: {text: title},
                        plotOptions: {
                            column: {
                                dataLabels: {
                                    rotation: xAxis.ticks.length < 10 ? 0 : 270,
                                    enabled: xAxis.ticks.length <= 31,
                                    y: -25,
                                    formatter: function () {
                                        return hoursTOHHMMSS(this.y);
                                    }
                                },
                                tooltip: {
                                    valueDecimals: 2,
                                    pointFormatter: function () {
                                        return this.series.name + ": <b>" + hoursTOHHMMSS(this.y) + "<b/>";
                                    }
                                },
                                events: {
                                    click: function (event) {
                                        {
                                            seriesPointClick(event, customParams.group);
                                        }
                                    }
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: xAxis,
                        yAxis: {
                            min: 0,
                            title: {text: ''},
                            labels: {
                                format: '{value} ч'
                            },
                            plotLines: plotLines
                        },
                        series: series
                    });
                }
                else {
                    var averageValue = 70; //chart.bag.averageValue.toFixed(2);
                    var planValue = 90;//chart.bag.planValue.toFixed(2);
                    var chartName = 'avgDynamic{0}'.format(customParams.group);
                    var containerId = 'avgDynamic{0}_container'.format(customParams.group);
                    // добавляю id к контейтеру для рендера чарта через new Highcharts
                    $container.attr("id", containerId);

                    var plotLines = [{
                        color: 'lightgray',
                        label: {
                            align: 'right',
                            text: '<b>Среднее</b>: {0}%'.format(averageValue),
                            x: 15
                        },
                        value: averageValue,
                        width: 2,
                        zIndex: 10
                    }, {
                        color: 'red',
                        label: {
                            align: 'right',
                            text: '<b>KPI</b>: {0}%'.format(planValue),
                            x: 15
                        },
                        value: planValue,
                        width: 2,
                        zIndex: 10
                    }];

                    this[chartName] = new Highcharts.Chart({
                        chart: {
                            renderTo: containerId,
                            type: "column",
//                            width: width,
                            marginRight: 210
                        },
                        title: {text: title},
                        plotOptions: {
                            column: {
                                stacking: "percent",
                                showInLegend: false,
                                dataLabels: {
                                    enabled: xAxis.ticks.length <= 31,
                                    formatter: function () {
                                        return this.percentage < 3 ? '' : this.percentage.toFixed(0);
                                    }
                                },
                                tooltip: {
                                    pointFormat: "{series.name}: <b>{point.y}</b> заявок ({point.percentage:.2f}%)"
                                },
                                events: {
                                    click: function (event) {
                                        {
                                            seriesPointClick(event, customParams.group);
                                        }
                                    }
                                }
                            },
                            pie: {
                                showInLegend: true,
                                size: 100,
                                center: [995, 45],
                                dataLabels: {
                                    inside: true,
                                    format: "{point.percentage:.0f}%",
                                    distance: 5
                                },
                                tooltip: {
                                    pointFormat: "<b>{point.y}</b> заявок ({point.percentage:.2f}%)"
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: xAxis,
                        yAxis: {
                            min: 0,
                            max: 100,
                            title: {text: ''},
                            labels: {
                                format: '{value}%'
                            },
                            plotLines: plotLines
                        },
                        series: series,
                        legend: {
                            enabled: true,
                            x: -20,
                            y: 200,
                            align: "right",
                            verticalAlign: "top",
                            layout: "vertical",
                            useHTML: true,
                            labelFormatter: function () {
                                {
                                    return createAvgDynamicLegendItem(this, chartName);
                                }
                            }
                        }
                    });
                }
            }

            function createRequestByGroupDynamic($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var title = "Распределение заявок по группам длительности",
                        width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                var chartName = 'requestByGroup{0}'.format(customParams.group);
                var containerId = 'requestByGroup{0}_container'.format(customParams.group);
                // добавляю id к контейтеру для рендера чарта через new Highcharts
                $container.attr("id", containerId);

                this[chartName] = new Highcharts.Chart({
                    chart: {
                        renderTo: containerId,
                        type: "column",
//                        width: width,
                        marginRight: 210
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: false,
                            stacking: "percent",
                            tooltip: {
                                pointFormat: "{series.name}: <b>{point.y}</b> заявок ({point.percentage:.2f}%)<br/>"
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 45],
                            dataLabels: {
                                inside: true,
                                format: "{point.percentage:.0f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: "<b>{point.y}</b> заявок ({point.percentage:.2f}%)"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        shared: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        x: -20,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: function () {
                            {
                                return createRequestByGroupLegendItem(this, chartName);
                            }
                        }
                    }
                });
            }

            function createRating($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId &&
                            chart.bag.isTop == customParams.isTop;
                });

                var title = "Топ-10 " + (customParams.isTop ? "лучших" : "худших"),
                        width = 577;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        isPercent = filterData.valueTypeId == 2,
                        categories = chart.bag.categories;

                if (!isPercent) {
                    $container.highcharts({
                        chart: {
                            type: "bar",
//                            width: width,
                            marginLeft: 210
                        },
                        title: {text: title},
                        plotOptions: {
                            bar: {
                                stacking: "normal",
                                tooltip: {
                                    pointFormat: "{series.name}: <b>{point.y:.2f} ч </b>"
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: {
                            categories: categories
                        },
                        yAxis: {
                            title: {text: ''},
                            labels: {
                                format: '{value} ч'
                            },
                            endOnTick: false
                        },
                        series: series,
                        legend: {
                            enabled: false
                        }
                    });
                } else {
                    $container.highcharts({
                        chart: {
                            type: "bar",
//                            width: width,
                            marginLeft: 210
                        },
                        title: {text: title},
                        plotOptions: {
                            bar: {
                                tooltip: {
                                    pointFormat: "<b>{point.y:.2f} %</b>"
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: {
                            categories: categories
                        },
                        yAxis: {
                            title: {text: ''},
                            labels: {
                                format: '{value}%'
                            },
                            min: 0,
                            endOnTick: false
                        },
                        series: series,
                        legend: {
                            enabled: false
                        }
                    });
                }
            }

            function createTimeRatioDynamic($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var title = "Динамика изменения доли каждого этапа в процессе", width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series, xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                var chartName = 'timeRatioDynamic{0}'.format(customParams.group);
                var containerId = 'timeRatioDynamic{0}_container'.format(customParams.group);
                // добавляю id к контейтеру для рендера чарта через new Highcharts
                $container.attr("id", containerId);

                this[chartName] = new Highcharts.Chart({
                    chart: {
                        renderTo: containerId,
                        type: "column",
//                        width: width,
                        marginRight: 210
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: false,
                            stacking: "normal",
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                formatter: function () {
                                    return this.y < 5 ? '' : this.y.toFixed(2) + '%';
                                }
                            },
                            tooltip: {
                                pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y:.2f}%</b><br/>'
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 45],
                            dataLabels: {
                                inside: true,
                                format: "{point.y:.2f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: '<b>{point.y:.2f}%</b>'
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        align: "center",
                        verticalAlign: "bottom",
                        layout: "horizontal",
                        useHTML: true,
                        labelFormatter: function () {
                            {
                                return createTimeRatioDynamicLegendItem(this, chartName);
                            }
                        }
                    }
                });
            }

            function createCardActivityDynamic($container, filterData, jsonData, customParams) {

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var title = "% активированных карт", width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series, xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                var normative = chart.bag.normative.toFixed(0);

                var plotLines = [{
                    color: 'red',
                    label: {
                        align: 'right',
                        text: '<b>KPI:</b>: {0}%'.format(normative),
                        x: -5
                    },
                    value: normative,
                    width: 2,
                    zIndex: 10
                }];

                $container.highcharts({
                    chart: {
                        type: "column",
//                        width: width,
                        marginRight: 210
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: false,
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                formatter: function () {
                                    return this.y < 5 ? '' : this.y.toFixed(2) + '%';
                                }
                            },
                            tooltip: {
                                pointFormat: '<span><b>{point.y:.2f}%</b></span><br/>{point.tag}'
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 45],
                            dataLabels: {
                                inside: true,
                                format: "{point.percentage:.2f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: '<b>{point.y}</b> шт ({point.percentage:.1f}%)'
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        },
                        plotLines: plotLines
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        x: 0,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: function () {
                            return "<div>{0}<p style='font-weight: normal'>{1}</p><div>".format(this.name, this.y);
                        }
                    }
                });
            }

            function createRejectDynamic($container, filterData, jsonData, customParams) {

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var title = "Доля клиентских отказов", width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series, xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column",
//                        width: width,
                        marginRight: 210
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: false,
                            stacked: 'normal',
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                formatter: function () {
                                    return this.y < 5 ? '' : this.y.toFixed(2) + '%';
                                }
                            },
                            tooltip: {
                                pointFormat: "<b>{point.y:.2f}%</b><br/>{point.tag}"
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 45],
                            dataLabels: {
                                inside: true,
                                format: "{point.percentage:.2f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: '<span><b>{point.y:.0f} шт</b></span><br/>{point.percentage:.1f}%'
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        x: 0,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: function () {
                            return "<div>{0}<p style='font-weight: normal'>{1}</p><div>".format(this.name, this.y);
                        }
                    }
                });
            }

            function createStageInfoButtons($container, filterData, jsonData, customParams) {
                var data = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                if (data === undefined) {
                    $container.highcharts({
                        title: {text: "Детализация"},
                        chart: {
                            height: 100
//                            width: 1184
                        }
                    });
                    return;
                }

                var titleTemplate = '<div><p class="table-header table-header-without-report">{0}</p></div>';
                var buttonsWrapperTemplate =
                        '<div id="{0}" class="pull-left btn-group" style="white-space: normal; width: 1000px; margin-left: 10px;"></div>';
                var buttonTemplate = '<button data-stage-id="{0}" class="btn btn-default btn-sm" onclick="stageButtonClick(this, \'{1}\')">' +
                        '<span class="{2}"></span>{3}</button>';
                var backButtonTemplate = '<button class="pull-right btn btn-default btn-sm" onclick="returnButtonClick(\'{0}\')">' +
                        'Назад </button> <div class="clearfix"></div>';

                $container.append(titleTemplate.format(data.bag.title));

                var $buttonsWrapper = $(buttonsWrapperTemplate.format('stageBtns_' + customParams.group));

                $container.append($buttonsWrapper);

                data.bag.stages.forEach(function (e) {
                    var badgeCss = e.inQuota == 1 ? "button-badge-success" :
                            e.inQuota == 2
                                    ? "button-badge-normal"
                                    : "button-badge-error";

                    $buttonsWrapper.append(buttonTemplate.format(
                            e.operationId, 'byStage' + customParams.group, badgeCss, e.operationName
                    ));
                });

                $container.append(backButtonTemplate.format(customParams.group));

                var $firstBtn = $buttonsWrapper.find('button')[0];
                if ($firstBtn) {
                    $firstBtn.click();
                }
            }

            function createDynamicByStage($container, filterData, jsonData, customParams) {

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: "Детализация"},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series, clonedFD = _.cloneDeep(filterData);

                clonedFD.startDate = moment(_.chain(series).map(function(s) { return _.minBy(s.data, function(p) { return p.x; })})
                        .min(function(p) { return p.x; }).value().x).toDate();
                clonedFD.endDate = moment(_.chain(series).map(function(s) { return _.maxBy(s.data, function(p) { return p.x; })})
                        .max(function(p) { return p.x; }).value().x).toDate();

                var xAxis = app.chartUtils.createDateTimeXAxis(clonedFD, false),
                        isPercent = filterData.valueTypeId == 2,
                        title = chart.bag.title;

                _.forEach(series, function (s) {
                    if (s.type === "column") {
                        s.yAxis = 0;
                    } else {
                        s.yAxis = 1;
                    }
                });

                if (!isPercent) {
                    $container.highcharts({
                        chart: {
                            type: "column",
//                            width: width
                        },
                        legend: {enabled: true},
                        title: {text: title},
                        plotOptions: {
                            column: {
                                stacking: "normal",
                                dataLabels: {
                                    enabled: false
                                }
                            },
                            line: {
                                dataLabels: {
                                    enabled: false
                                },
                                tooltip: {
                                    pointFormatter: function () {
                                        return '<span style=\"color:{0}\">\u25CF</span> {1}: <b>{2}</b><br/>'.format(
                                                this.color,
                                                this.series.name,
                                                this.y.toFixed(2));
                                    }
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true,
                            shared: true,
                            crosshairs: true,
                            pointFormatter: function () {
                                return '<span style=\"color:{0}\">\u25CF</span> {1}: <b>{2}</b><br/>'.format(this.series.color,
                                        this.series.name, hoursTOHHMMSS(this.y));
                            }
                        },
                        xAxis: xAxis,
                        yAxis: [{
                            min: 0,
                            title: {text: ''},
                            labels: {
                                format: '{value} ч'
                            },
                            stackLabels: {
                                enabled: true,
                                formatter: function () {
                                    return hoursTOHHMMSS(this.total);
                                }
                            }
                        }, {
                            title: {text: ""},
                            opposite: true
                        }],
                        series: series
                    });
                }
                else {
                    var planValue, plotLines = [];

                    if (chart.bag.normative) {
                        planValue = chart.bag.normative.toFixed(2);

                        plotLines.push({
                            color: chart.bag.normativeColor,
                            label: {
                                align: 'right',
                                text: '<b>Норматив 1</b>: {0}%'.format(planValue),
                                x: -20,
                                zIndex: 11
                            },
                            value: planValue,
                            width: 2,
                            zIndex: 10
                        })
                    }

                    if (chart.bag.normative2) {
                        planValue = chart.bag.normative2.toFixed(2);

                        plotLines.push({
                            color: chart.bag.normativeColor2,
                            label: {
                                align: 'right',
                                text: '<b>Норматив 2</b>: {0}%'.format(planValue),
                                x: -20,
                                y: -5,
                                zIndex: 11
                            },
                            value: planValue,
                            width: 2,
                            zIndex: 10
                        });
                    }

                    $container.highcharts({
                        chart: {
                            type: "column",
//                            width: width,
                            height: 450
                        },
                        title: {text: title},
                        plotOptions: {
                            column: {
                                dataLabels: {
                                    enabled: false
                                }
                            },
                            line: {
                                dataLabels: {
                                    enabled: false
                                },
                                tooltip: {
                                    pointFormatter: function () {
                                        return '<span style=\"color:{0}\">\u25CF</span> {1}: <b>{2}</b><br/>'.format(
                                                this.color,
                                                this.series.name,
                                                this.y.toFixed(2));
                                    }
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true,
                            shared: false,
                            crosshairs: true,
                            pointFormatter: function () {
                                return '<span style=\"color:{0}\">\u25CF</span> {1}: <b>{2}</b> %<br/>'.format(
                                        this.color,
                                        this.series.name,
                                        this.y.toFixed(2));
                            }
                        },
                        xAxis: xAxis,
                        yAxis: [{
                            min: 0,
                            max: 100,
                            title: {text: ''},
                            labels: {
                                format: '{value}%'
                            },
                            plotLines: plotLines
                        }, {
                            title: {text: ""},
                            opposite: true
                        }],
                        series: series,
                        legend: {
                            enabled: false
                        }
                    });
                }
            }

            function createAfterDrawDynamic($container, filterData, jsonData, customParams) {

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.reportTypeId == customParams.reportTypeId;
                });

                var kpi = _.find(app.viewModel.getFilter("kpiId").options(),
                        function (el) {
                            return el.id == filterData.kpiId;
                        });

                var title = kpi.id == 1000646002 ? "Процент дооформлений УИУК"
                        : "Процент дооформлений (после проверки на конвейере)", width = 1184;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series, xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column",
//                        width: width,
                        marginRight: 210
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                formatter: function () {
                                    return this.y < 3 ? '' : this.y.toFixed(0) + "%";
                                }
                            },
                            tooltip: {
                                pointFormat: "Количество этапов: {point.tag}"
                            }
                        },
                        pie: {
                            showInLegend: false,
                            size: 100,
                            center: [995, 45],
                            dataLabels: {
                                inside: true,
                                format: "{point.percentage:.0f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: "<b>{point.y}</b> шт ({point.percentage:.1f}%)"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: false
                    }
                });
            }

            function showPeriodDetalization(groupName) {
                var group = app.viewModel.groups[groupName];
                group.drillDown(1, {});
            }

            function returnButtonClick(groupName) {
                var group = app.viewModel.groups[groupName];
                group.drillDown(0);
            }

            function stageButtonClick(btn, groupName) {
                var $btn = $(btn),
                        stageId = parseInt($btn.attr("data-stage-id")),
                        group = app.viewModel.groups[groupName];

                $btn.siblings().removeClass("active");
                $btn.addClass("active");

                group.drillDown(0, {
                    operationId: stageId
                });
            }

            function seriesPointClick(event, groupName) {
                var timeStamp = event.point.x,
                        group = app.viewModel.groups[groupName],
                        mainGroupOptions = app.viewModel.groups.default.filterData();

                var timeUnitId = mainGroupOptions.timeUnitId;

                var period = app.chartUtils.getPeriodFromTimestamp(
                        timeStamp,
                        timeUnitId,
                        mainGroupOptions.startDate,
                        mainGroupOptions.endDate);

                if (timeUnitId > 2) {
                    timeUnitId--;
                }

                group.drillDown(1, {
                    floatingYear: false,
                    startDate: period.startDate,
                    endDate: period.endDate,
                    timeUnitId: timeUnitId
                });
            }

            function createAvgDynamicLegendItem(legendItem, chartName) {
                var legendTemplate = "<div onclick=\"dynamicWithPieToogleSeriesVisibility('{0}', {1})\">{0}<p style='font-weight: normal'>{2} заявок </p><div>";

                return legendTemplate.format(legendItem.name, chartName, legendItem.y);
            }

            function createRequestByGroupLegendItem(legendItem, chartName) {
                var legendTemplate = "<div onclick=\"dynamicWithPieToogleSeriesVisibility('{0}', {1})\">{0}<div>";

                return legendTemplate.format(legendItem.name, chartName);
            }

            function createTimeRatioDynamicLegendItem(legendItem, chartName) {
                var legendTemplate = "<div onclick=\"dynamicWithPieToogleSeriesVisibility('{0}', {1})\">{0}<div>";

                return legendTemplate.format(legendItem.name, chartName);
            }

            function dynamicWithPieToogleSeriesVisibility(seriesName, chartToToogle) {
                var redraw = chartToToogle.redraw;
                chartToToogle.redraw = function () {
                };

                for (var i = 0; i < chartToToogle.series.length; i++) {
                    var chartSeries = chartToToogle.series[i];
                    if (chartSeries.name === seriesName) {
                        chartSeries.setVisible(!chartSeries.visible);
                    }
                }

                chartToToogle.redraw = redraw;
                chartToToogle.redraw();
            }

            app.init(config, function (viewModel) {
                viewModel.allQuotasInfo = ko.observableArray([]);
                viewModel.kpiQuotasInfo = ko.pureComputed(function () {
                    return _.filter(viewModel.allQuotasInfo(), function (x) {
                        return x.type == "KPI";
                    });
                });
                viewModel.stageQuotasInfo = ko.pureComputed(function () {
                    return _.filter(viewModel.allQuotasInfo(), function (x) {
                        return x.type == "Stage";
                    });
                });

                app.ajaxUtils.getDataForFilter("PILAndCCFilter/QuotaDesc", {}, viewModel.allQuotasInfo);
            });
        </script>

        <script type="text/html" id="kpiQuotasTemplate">
            <table class="table">
                <tr>
                    <th>KPI/Метрика</th>
                    <th>Наименование показателя/этапа</th>
                    <th>Норматив, время</th>
                    <th>Норматив, %</th>
                </tr>
                <!-- ko foreach: kpiQuotasInfo -->
                <tr>
                    <td data-bind="text: typeDescription"></td>
                    <td data-bind="text: description"></td>
                    <td data-bind="text: averageTimeQuotaDescription"></td>
                    <td data-bind="text: percentQuotaDescription"></td>
                </tr>
                <!-- /ko -->
            </table>
        </script>

        <script type="text/html" id="stageQuotasTemplate">
            <table class="table">
                <tr>
                    <th>KPI/Метрика</th>
                    <th>Наименование показателя/этапа</th>
                    <th>Норматив, время</th>
                    <th>Норматив, %</th>
                    <th>Комментарии</th>
                </tr>
                <!-- ko foreach: stageQuotasInfo -->
                <tr>
                    <td data-bind="text: typeDescription"></td>
                    <td data-bind="text: description"></td>
                    <td data-bind="text: averageTimeQuotaDescription"></td>
                    <td data-bind="text: percentQuotaDescription"></td>
                    <td data-bind="text: comments"></td>
                </tr>
                <!-- /ko -->
            </table>
        </script>
    </jsp:attribute>

    <jsp:body>
        <a class="btn btn-default btn-xs support-button" href="<c:url value="/files/PIL_CC.pdf" />">Нужна помощь?</a>

        <div class="showcase-title">${title}</div>

        <div class="section-filter-container row">
            <div class="col-xs-4">
                <div class="filter-section">
                    <div class="filter-element">
                        <filter params="name: 'kpiId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'productTypeId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'manualCheckId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'clientSegmentIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'startDate'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'endDate'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <div class="filter-section">
                    <div class="filter-element">
                        <filter params="name: 'regionIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'doKkoIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'cityIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'managerIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'valueTypeId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'timeUnitId'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <div class="filter-section">
                    <div class="filter-element">
                        <filter params="name: 'requestTypeId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'systemUnitId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'sortOrderId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'sourceSystemIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'sortTypeId'"></filter>
                    </div>
                </div>
            </div>

            <filter-log></filter-log>
        </div>
        <div class="filter-element">
            <refresh-button params="style: {'margin-top': '5px'}"></refresh-button>
        </div>
        <div class="clearfix"></div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <tab-strip params="name: 'main'">
                <tab>
                    <div style="margin-bottom: 5px;">
                        <button class="pull-left btn btn-default btn-xs"
                                data-bind="popover: {template: 'kpiQuotasTemplate', options: {title: 'Пояснение к показателям'}}">
                            <i class="icon-help" title="Пояснение к показателям"></i>
                        </button>
                        <button data-bind="enable: groups.{groupName}.drillDownLevel() === 0"
                                onclick="showPeriodDetalization('{groupName}')" class="pull-right btn btn-default">
                            Детализировать отчетный период
                        </button>
                        <csv-report-button params="reportUrl: 'PilAndCCKpiDetailDataCsvReport', text: 'Отчет по заявкам (CSV)'"
                                           class="pull-right" style="margin-right: 10px; display: block">
                        </csv-report-button>
                        <report-button params="reportUrl: 'PilAndCCKpiDataReport', text: 'Отчет по KPI'"
                                       class="pull-right" style="margin-right: 10px; display: block">
                        </report-button>
                        <div class="clearfix"></div>
                    </div>

                    <div class="chart">
                        <chart params="name: 'avgDynamic', group: 'default'"></chart>
                    </div>

                    <!-- ko if: groups.default.filters.valueTypeId.value() == 1 && groups.{groupName}.drillDownLevel() === 0 -->
                    <div class="chart">
                        <chart params="name: 'timeRatioDynamic', group: 'default'"></chart>
                    </div>
                    <!-- /ko -->

                    <!-- ko if: groups.default.filters.kpiId.value() == "1000646000" -->
                    <div class="chart">
                        <chart params="name: 'cardActivityDynamic', group: 'default'">
                            <report-button params="reportUrl: 'PilAndCCCardActivateReport'"
                                           class="pull-right" style="margin-bottom: 5px; display: block">
                            </report-button>
                            <div class="clearfix"></div>
                            <div class="chart-container"></div>
                        </chart>
                    </div>
                    <!-- /ko -->

                    <!-- ko if: groups.{groupName}.drillDownLevel() === 0 -->
                    <div class="chart">
                        <chart params="name: 'requestByGroup'"></chart>
                    </div>
                    <div class="chart">
                        <chart params="name: 'rejectDynamic'"></chart>
                    </div>

                    <!-- ko if: groups.default.filters.systemUnitId.value() != 0 -->
                    <div class="row">
                        <div class="col-xs-6">
                            <chart params="name: 'ratingBest'"></chart>
                        </div>
                        <div class="col-xs-6">
                            <chart params="name: 'ratingWorst'"></chart>
                        </div>
                    </div>
                    <!-- /ko -->
                    <!-- /ko -->

                    <!-- ko if: groups.{groupName}.drillDownLevel() === 1 -->
                    <div class="chart">
                        <chart params="name: 'stageInfo'"></chart>
                    </div>
                    <div class="chart">
                        <div style="margin-bottom: 5px;">
                            <button class="pull-left btn btn-default btn-xs"
                                    data-bind="popover: {template: 'stageQuotasTemplate', options: {title: 'Пояснение к показателям'}}">
                                <i class="icon-help" title="Пояснение к показателям"></i>
                            </button>
                            <csv-report-button params="reportUrl: 'PilAndCCOperDetailDataСsvReport', text: 'Выгрузка по этапам (CSV)'"
                                               class="pull-right" style="display: block">
                            </csv-report-button>
                            <report-button params="reportUrl: 'PilAndCCOperDetailDataReport', text: 'Отчет по этапам'"
                                           class="pull-right" style="margin-right: 10px; display: block">
                            </report-button>
                            <div class="clearfix"></div>
                        </div>
                        <chart params="name: 'dynamicByStage', group: 'byStage{groupName}'"></chart>
                    </div>

                    <!-- ko if: (groups.default.filters.kpiId.value() == "1000646009" || groups.default.filters.kpiId.value() == "1000646002")-->
                    <div class="chart">
                        <chart params="name: 'afterDrawDynamic'">
                            <report-button params="reportUrl: 'PilAndCCAfterCheckReport'"
                                           class="pull-right" style="margin-bottom: 5px; display: block">
                            </report-button>
                            <div class="clearfix"></div>
                            <div class="chart-container"></div>
                        </chart>
                    </div>
                    <!-- /ko -->
                    <!-- /ko -->

                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>