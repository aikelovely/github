<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Корпоративный бизнес"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .scrollable-highchart-fixed-title {
                max-height: 915px;
            }

            .scrollable-grid .k-grid .k-grid-content {
                max-height: 900px;
            }

        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var tabsConfig = {
                tabs: [
                    {title: "КБ 10-100", customParams: {processId: 1}},
                    {title: "КБ 100+", customParams: {processId: 2}}],
                sameMarkup: true,
                dynamicConfig: {
                    jsFunc: function (tabs, params) {
                        var processIds = params.processIds;

                        tabs[0].visible(processIds.length == 0 || processIds.indexOf("1") !== -1);
                        tabs[1].visible(processIds.length == 0 || processIds.indexOf("2") !== -1);
                    },
                    params: ["processIds"]
                }
            };

            var config = {
                groups: [{
                    filters: {
                        blTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "ЦО/РП",
                            dataSource: {
                                url: "crFilter/BLTypes"
                            },
                            width: 250
                        },
                        blIds: {
                            type: "Select",
                            multiple: true,
                            title: "Куст",
                            dataSource: {
                                url: "crFilter/BLs",
                                params: ["blTypeIds"]
                            },
                            disableIfNull: "blTypeIds",
                            width: 230
                        },
                        dopOfficeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Доп. офис",
                            dataSource: {
                                url: "crFilter/DopOffices",
                                params: ["blIds"]
                            },
                            disableIfNull: "blIds"
                        },
                        processIds: {
                            type: "Select",
                            multiple: true,
                            title: "Процесс",
                            dataSource: {
                                url: "crFilter/Processes"
                            },
                            width: 140
                        },
                        bpTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип показателя",
                            dataSource: {
                                url: "crFilter/BPTypes"
                            },
                            width: 140
                        },
                        productTypeIds: {
                            type: "Select",
                            multiple: true,
                            enableClear: true,
                            title: "Тип продукта",
                            dataSource: {
                                url: "crFilter/productTypes",
                                params: ["bpTypeId"]
                            },
                            width: 250
                        },
                        committeeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Уполномоченный орган",
                            dataSource: {
                                url: "crFilter/Committees",
                                params: ["processIds"]
                            }
                        },
                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Уровень агрегации",
                            dataSource: {
                                url: "crFilter/SystemUnits",
                                computed: {
                                    jsFunc: function (context) {
                                        var allowedUnits = [],
                                                loadedData = context.loadedData,
                                                blTypeIds = context.params.blTypeIds || [],
                                                blIds = context.params.blIds || [],
                                                dopOfficeIds = context.params.dopOfficeIds || [];

                                        if (loadedData.length === 0) return allowedUnits;

                                        if (blTypeIds.length === 0) {
                                            allowedUnits.push(loadedData[0]); // Банк
                                        }

                                        if (blIds.length === 0) {
                                            allowedUnits.push(loadedData[1]); // ЦО/РП
                                        }

                                        if (dopOfficeIds.length === 0) {
                                            allowedUnits.push(loadedData[2]); // Куст
                                        }

                                        allowedUnits.push(loadedData[3]); // Доп. офис

                                        return allowedUnits;
                                    },
                                    params: ["blTypeIds", "blIds", "dopOfficeIds"]
                                }
                            },
                            width: 150
                        },
                        orderBy: {
                            type: "Select",
                            multiple: false,
                            title: "Сортировка",
                            dataSource: {
                                data: [{id: 1, name: "По возрастанию"}, {id: -1, name: "По убыванию"}]
                            },
                            width: 150
                        },
                        floatingYear: {
                            type: "CheckBox",
                            title: "Учитывать плавающий год"
                        },
                        clientLevelId: {
                            type: "Select",
                            multiple: false,
                            title: "Уровень клиента",
                            optionsCaption: "Все",
                            dataSource: {
                                url: "crFilter/clientLevels"
                            },
                            width: 200
                        },
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            notAfter: "endDate",
                            disableIf: "floatingYear",
                            defaultValue: moment().startOf("year").toDate()
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            notBefore: "startDate"
                        },
                        paramType: {
                            type: "Select",
                            multiple: false,
                            title: "Отображение параметра",
                            dataSource: {
                                data: [
                                    {id: "AvgDuration", name: "Среднее время"},
                                    {id: "Percent", name: "Процент в KPI"}
                                ],
                                computed: {
                                    jsFunc: function calculateParamTypes(context) {
                                        var allowedUnits = [],
                                                loadedData = context.loadedData,
                                                bpTypeId = context.params.bpTypeId;

                                        if (loadedData.length === 0) return allowedUnits;

                                        allowedUnits.push(loadedData[0]); // Среднее время

                                        if (bpTypeId !== "1" && bpTypeId !== "2")
                                            allowedUnits.push(loadedData[1]); // Процент

                                        return allowedUnits;
                                    },
                                    params: ["bpTypeId"]
                                }
                            },
                            width: 175
                        },
                        isSmoothMetric: {
                            type: "CheckBox",
                            title: "Использовать плавающий год при расчёте динамики"
                        }
                    },
                    tabStrips: {
                        dynamicTabs: _.assign({}, tabsConfig, {reportUrl: "TTYAndTTMDynamicReport"}),
                        dynamicDetailsTabs:  _.assign({}, tabsConfig, {reportUrl: "TTYAndTTMDynamicDetailsReport"})
                    },
                    charts: {
                        rating: {
                            jsFunc: createRating,
                            dataSource: "TTYAndTTMRating",
                            reportUrl: "TTYAndTTMRatingReport"
                        },
                        ratingTable: {
                            jsFunc: createRatingTable,
                            dataSource: "TTYAndTTMRatingDetailsTable",
                            reportUrl: "TTYAndTTMRatingDetailsReport"
                        },
                        dynamic: {
                            jsFunc: createDynamic,
                            dataSource: "TTYAndTTMDynamic"
                        },
                        dynamicDetailsTable: {
                            jsFunc: createDynamicDetailsTable,
                            dataSource: "TTYAndTTMDynamicDetailsTable"
                        }
                    }
                }]
            };

            function createRating($container, filterData, jsonData) {
                var chart = jsonData[0],
                        categories = chart.bag.categories,
                        rowCount = categories.length;

                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var $title = $container.siblings(".scrollable-chart-title");
                var titleText = "{0} за период с {1} по {2}".format(
                        filterData.paramType === "AvgDuration" ? "Среднее значение" : "Процент в KPI",
                        moment(filterData.startDate).format("DD.MM.YY"),
                        moment(filterData.endDate).format("DD.MM.YY")
                );
                $title.text(titleText);

                if (rowCount === 0) {
                    $container.highcharts({
                        title: {text: ""},
                        chart: {
                            height: 100
                        }
                    });
                    return;
                }

                var plotLines = [];

                var chartConfig = {
                    chart: {
                        type: 'bar',
                        height: rowCount * 75 + 200,
                        margin: [45, 50, 30, 160]
                    },
                    plotOptions: {
                        bar: {
                            dataLabels: {
                                enabled: true,
                                inside: true,
                                align: "left"
                            },
                            tooltip: {
                                headerFormat: "{point.key}<br/>"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    title: {text: ""},
                    xAxis: {
                        categories: categories,
                        title: {text: null}
                    },
                    yAxis: {
                        title: {text: null},
                        plotLines: plotLines
                    },
                    legend: {enabled: false},
                    series: chart.series
                };

                if (filterData.paramType === "AvgDuration") {

                    var kpis = chart.bag.kpis;
                    _.each(kpis, function (kpi, index) {
                        plotLines.push({
                            color: index == 0 ? "#b20000" : "#337ab7",
                            label: {
                                rotation: 0,
                                text: '<b>{0}</b>: {1}'.format(kpi.name, app.chartUtils.daysToDDHH(kpi.value)),
                                y: index == 0 ? 10 : -5
                            },
                            value: kpi.value,
                            width: 2,
                            zIndex: 2
                        });
                    });

                    _.merge(chartConfig, {
                        plotOptions: {
                            bar: {
                                dataLabels: {
                                    formatter: function () {
                                        return this.series.name + ": " + app.chartUtils.daysToDDHH(this.y)
                                    }
                                },
                                tooltip: {
                                    pointFormatter: function () {
                                        return this.series.name + "<br/>" + app.chartUtils.daysToDDHH(this.y)
                                    }
                                }
                            }
                        }
                    });
                } else {
                    var quotaPercent = chart.bag.quotaPercent,
                            quotaInDays = app.chartUtils.daysToDDHH(chart.bag.quotaInDays);

                    plotLines.push({
                        color: "#b20000",
                        label: {
                            rotation: 0,
                            text: '{0}% за {1}'.format(quotaPercent, quotaInDays)
                        },
                        value: quotaPercent,
                        width: 2,
                        zIndex: 2
                    });

                    chart.series.push({
                        name: 'Goal',
                        type: 'scatter',
                        marker: {
                            enabled: false
                        },
                        data: [quotaPercent]
                    });

                    _.merge(chartConfig, {
                        plotOptions: {
                            bar: {
                                dataLabels: {
                                    format: "{series.name}: {y:.2f}%"
                                },
                                tooltip: {
                                    pointFormat: "{series.name}: {y:.2f}%"
                                }
                            }
                        }
                    });
                }


                $container.highcharts(chartConfig);
            }

            function createRatingTable($container, filterData, jsonData) {
                var processIds = filterData.processIds,
                        withDrillDown = processIds.length === 0 || processIds.length === 2;

                var allData = jsonData, filteredData = [];
                if (withDrillDown) {
                    filteredData = _.filter(allData, function (row) {
                        return row.processId === null;
                    });
                } else {
                    filteredData = _.filter(allData, function (row) {
                        return row.processId == processIds[0];
                    });
                }

                var gridConfig = {
                    dataSource: {
                        data: filteredData
                    },
                    sortable: true,
                    columns: [
                        {field: "name", title: "Значение"},
                        {field: "dealCount", title: "Количество выдач", format: "{0:N0}"},
                        {field: "ttxDuration", title: "Время обработки заявок, дн"},
                        {field: "averageValue", title: "Отклонение"}
                    ]
                };

                if (!withDrillDown) {
                    gridConfig.columns.push({field: "processName", title: "Процесс"})
                } else {
                    gridConfig.detailInit = function detailInit(e) {
                        var data = e.data;
                        var filteredData = _.filter(allData, function (row) {
                            return row.processId !== null && row.id == data.id;
                        });

                        $("<div/>").appendTo(e.detailCell).kendoGrid({
                            dataSource: {data: filteredData},
                            scrollable: false,
                            sortable: true,
                            columns: [
                                {field: "processName", title: "Процесс"},
                                {field: "dealCount", title: "Количество выдач", format: "{0:N0}"},
                                {field: "ttxDuration", title: "Время обработки заявок, дн"},
                                {field: "averageValue", title: "Отклонение"}
                            ]
                        });
                    }
                }

                $container.kendoGrid(gridConfig);
            }

            function createDynamic($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (c) {
                    return c.bag.processId == customParams.processId;
                });

                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var periodText = app.chartUtils.getPeriodText(filterData.startDate, filterData.endDate),
                        title = "Динамика {0} за период {1}".format(
                                filterData.paramType == "AvgDuration" ? "среднего значения" : "процента в KPI",
                                periodText);

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {height: 100}
                    });
                    return;
                }

                var series = chart.series,
                        quotaInDays = chart.bag.quotaInDays || 0,
                        quotaPercent = chart.bag.quotaPercent || 0,
                        average = chart.bag.average || 0;

                filterData.timeUnitId = 4;
                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);
                var plotLines;

                var chartConfig = {
                    chart: {
                        type: "column",
                        marginRight: 50
                    },
                    title: {text: title},
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ""}
                    },
                    tooltip: {
                        pointFormat: "{series.name}: <b>{point.y:,.2f}%</b>"
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                inside: true
                            }
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: series
                };

                if (filterData.paramType == "AvgDuration") {
                    plotLines = [{
                        color: "#b20000",
                        label: {
                            x: 40,
                            align: "right",
                            text: "KPI: {0}".format(app.chartUtils.daysToDDHH(quotaInDays))
                        },
                        value: quotaInDays,
                        width: 2,
                        zIndex: 6
                    }, {
                        color: "#444",
                        label: {
                            zIndex: 12,
                            align: "left",
                            text: "Среднее: {0}".format(app.chartUtils.daysToDDHH(average))
                        },
                        value: average,
                        width: 2,
                        zIndex: 6
                    }];

                    _.merge(chartConfig, {
                        yAxis: {plotLines: plotLines},
                        plotOptions: {
                            column: {
                                tooltip: {
                                    pointFormatter: function () {
                                        return this.series.name + ": <b>" + app.chartUtils.daysToDDHH(this.y) + "</b>"
                                    }
                                },
                                dataLabels: {
                                    formatter: function () {
                                        return app.chartUtils.daysToDDHH(this.y)
                                    }
                                }
                            }
                        }
                    });
                } else {
                    plotLines = [{
                        color: "#b20000",
                        label: {
                            x: 40,
                            align: "right",
                            text: "KPI: {0}%".format(quotaPercent.toFixed(2))
                        },
                        value: quotaPercent,
                        width: 2,
                        zIndex: 6
                    }, {
                        color: "#444",
                        label: {
                            zIndex: 12,
                            align: "left",
                            text: "Среднее: {0}".format(average.toFixed(2))
                        },
                        value: average,
                        width: 2,
                        zIndex: 6
                    }];

                    _.merge(chartConfig, {
                        yAxis: {
                            plotLines: plotLines,
                            labels: {
                                format: '{value}%'
                            }
                        },
                        plotOptions: {
                            column: {
                                tooltip: {
                                    pointFormat: "<b>{point.y:,.2f}%</b>"
                                },
                                dataLabels: {
                                    format: "{y:,.2f}%"
                                }
                            }
                        }
                    });
                }

                $container.highcharts(chartConfig);
            }

            function createDynamicDetailsTable($container, filterData, jsonData, customParams) {
                var greenCss = "label-success",
                        redCss = "label-danger";

                var paramType = filterData.paramType;

                var cellTemplate = "<span class='label label-cell {0}'>{1}</span>";

                jsonData = _.filter(jsonData, function(row){
                    return row.processId == customParams.processId;
                });

                function formatCellValue(data) {
                    if (data == null || data.value === null) return "";
                    var css, valueToDisplay;
                    if (paramType == "AvgDuration") {
                        css = data.value <= data.quotaInDays ? greenCss : redCss;
                        valueToDisplay = data.value.toFixed(2) + " дн.";
                    }
                    else {
                        css = data.value > data.quotaPercent ? greenCss : redCss;
                        valueToDisplay = data.value.toFixed(2) + "%";
                    }
                    return cellTemplate.format(css, valueToDisplay);
                }

                filterData.timeUnitId = 4;
                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var systemUnitOptions = app.viewModel.getFilter("systemUnitId").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети",
                        columns = app.chartUtils.createDateTimeColumns(
                                filterData,
                                "value",
                                formatCellValue);


                // Задаем первую колонку
                columns.unshift({
                    field: "name",
                    title: systemUnitName,
                    width: 200,
                    locked: !_.isEmpty(jsonData)
                });

                $container.kendoGrid({
                    dataSource: {
                        data: jsonData
                    },
                    height: _.isEmpty(jsonData) ? 100 : 460,
                    sortable: true,
                    columns: columns
                });
            }

            app.init(config);
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'blTypeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'blIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'dopOfficeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'processIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'bpTypeId'"></filter>
                </div>
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'committeeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'systemUnitId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'paramType'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'clientLevelId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'productTypeIds'"></filter>
                </div>
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'floatingYear'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'startDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'endDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'orderBy'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'isSmoothMetric'"></filter>
                </div>
            </div>
            <div>
                <refresh-button params="style: {marginTop: '5px'}"></refresh-button>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <div class="row">
                <div class="col-xs-6">
                    <chart params="name: 'rating'">
                        <report-button params="group: group, reportUrl: reportUrl"></report-button>
                        <div class="scrollable-chart">
                            <p class="scrollable-chart-title"></p>

                            <div class="chart-container" style="max-height: 915px;"></div>
                        </div>
                    </chart>
                </div>
                <div class="col-xs-6 scrollable-grid">
                    <chart params="name: 'ratingTable'"></chart>
                </div>
            </div>
            <div class="chart">
                <tab-strip params="name: 'dynamicTabs'">
                    <tab>
                        <chart params="name: 'dynamic'"></chart>
                    </tab>
                </tab-strip>
            </div>
            <div>
                <tab-strip params="name: 'dynamicDetailsTabs'">
                    <tab>
                        <chart params="name: 'dynamicDetailsTable'"></chart>
                    </tab>
                </tab-strip>
            </div>
        </div>
    </jsp:body>
</t:layout>