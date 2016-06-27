<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Декомпозиция процессов"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .hidden-chart .chart {
                margin-bottom: 0;
            }

            .details-report-wrapper {
                margin-top: 20px;
                text-align: center;
                font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                font-size: 18px;
                color: #333333;
                padding: 5px;
            }

            .details-report-wrapper .btn {
                font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var config = {
                groups: [{
                    filters: {
                        kpiId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип заявки",
                            dataSource: {
                                url: "clientTimeFilter/kpiMetrics"
                            },
                            width: 250
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
                                ]
                            },
                            width: 175
                        },
                        productTypeIds: {
                            type: "Select",
                            multiple: true,
                            enableClear: true,
                            title: "Тип продукта",
                            disableIfComputed: {
                                jsFunc: function (context) {
                                    var disabled = false,
                                            kpiId = context.params.kpiId;

                                    if (kpiId === "1") { // Лимит/изменение лимита
                                        disabled = true;
                                    }

                                    return disabled;
                                },
                                params: ["kpiId"]
                            },
                            dataSource: {
                                url: "clientTimeFilter/productTypes",
                                params: ["kpiId"]
                            },
                            width: 250
                        },
                        stageIds: {
                            type: "Select",
                            multiple: true,
                            title: "Этап",
                            enableClear: true,
                            dataSource: {
                                url: "clientTimeFilter/stages",
                                params: ["startDate", "endDate", "kpiId"]
                            },
                            width: 190
                        },
                        bpKindIds: {
                            type: "Select",
                            multiple: true,
                            title: "Бизнес-процесс",
                            enableClear: true,
                            dataSource: {
                                url: "clientTimeFilter/bpKinds",
                                params: ["startDate", "endDate", "kpiId", "stageIds"]
                            },
                            width: 190
                        },
                        blTypeIds: {
                            type: "Select",
                            title: "ЦО/РП",
                            multiple: true,
                            enableClear: true,
                            dataSource: {
                                url: "clientTimeFilter/BLTypes"
                            },
                            width: 130
                        },
                        blIds: {
                            type: "Select",
                            multiple: true,
                            title: "Куст",
                            enableClear: true,
                            disableIfNull: "blTypeIds",
                            dataSource: {
                                url: "clientTimeFilter/BLs",
                                params: ["blTypeIds"]
                            },
                            width: 190
                        },
                        dopOfficeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Доп. офис",
                            enableClear: true,
                            width: 220,
                            disableIfNull: "blIds",
                            dataSource: {
                                url: "clientTimeFilter/DopOffices",
                                params: ["blIds"]
                            }
                        },
                        creditDepartIds: {
                            type: "Select",
                            multiple: true,
                            title: "Кредитное подразделение",
                            disableIfNull: "dopOfficeIds",
                            dataSource: {
                                url: "clientTimeFilter/CreditDeparts",
                                params: ["dopOfficeIds"]
                            }
                        },
                        departIds: {
                            type: "Select",
                            multiple: true,
                            title: "Подразделение",
                            disableIfNull: "dopOfficeIds",
                            dataSource: {
                                url: "clientTimeFilter/Departs",
                                params: ["dopOfficeIds"]
                            }
                        },
                        committeeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Уполномоченный орган",
                            dataSource: {
                                url: "crFilter/Committees"
                            }
                        },
                        floatingYear: {
                            type: "CheckBox",
                            title: "Учитывать плавающий год"
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Период агрегации",
                            dataSource: {
                                url: "clientTimeFilter/timeUnits"
                            },
                            width: 120
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
                        }
                    },
                    tabStrips: {
                        main: {
                            tabs: [
                                {title: "КБ 10-100", customParams: {processId: 1, group: 'cb10And100'}, group: "cb10And100"},
                                {title: "КБ 100+", customParams: {processId: 2, group: 'cb100Plus'}, group: "cb100Plus"},
                                {title: "Total", customParams: {processId: 0, group: 'total'}, group: "total"}],
                            sameMarkup: true
                        }
                    },
                    charts: {
                        dynamicWithPie: {
                            jsFunc: createDynamic,
                            dataSource: "ClientTimeDynamicWithPie"
                        }
                    }, slaves: [{
                        name: "cb10And100",
                        charts: {
                            breadcrumbs: {
                                jsFunc: createBreadcrumbs,
                                dataSource: "ClientTimeBreadcrumbs",
                                additionalParams: {processId: 1}
                            },
                            dynamicRequestByGroup: {
                                jsFunc: createDynamicRequestByGroup,
                                dataSource: "ClientTimeDynamicRequestByGroup",
                                additionalParams: {processId: 1}
                            }
                        },
                        slaves: [{
                            name: "cb10And100ByUnit",
                            charts: {
                                dynamicWithPie: {
                                    jsFunc: createDynamic,
                                    dataSource: "ClientTimeDynamicWithPie",
                                    additionalParams: {processId: 1}
                                },
                                detailsTable: {
                                    jsFunc: createDetailsTable,
                                    dataSource: "ClientTimeDetailsTable",
                                    additionalParams: {processId: 1}
                                }
                            },
                            dontShowAfterMaster: true
                        }]
                    }, {
                        name: "cb100Plus",
                        charts: {
                            breadcrumbs: {
                                jsFunc: createBreadcrumbs,
                                dataSource: "ClientTimeBreadcrumbs",
                                additionalParams: {processId: 2}
                            },
                            dynamicRequestByGroup: {
                                jsFunc: createDynamicRequestByGroup,
                                dataSource: "ClientTimeDynamicRequestByGroup",
                                additionalParams: {processId: 2}
                            }
                        },
                        slaves: [{
                            name: "cb100PlusByUnit",
                            charts: {
                                dynamicWithPie: {
                                    jsFunc: createDynamic,
                                    dataSource: "ClientTimeDynamicWithPie",
                                    additionalParams: {processId: 2}
                                },
                                detailsTable: {
                                    jsFunc: createDetailsTable,
                                    dataSource: "ClientTimeDetailsTable",
                                    additionalParams: {processId: 2}
                                }
                            },
                            dontShowAfterMaster: true
                        }]
                    }, {
                        name: "total",
                        charts: {
                            breadcrumbs: {
                                jsFunc: createBreadcrumbs,
                                dataSource: "ClientTimeBreadcrumbs",
                                additionalParams: {processId: 0}
                            },
                            dynamicRequestByGroup: {
                                jsFunc: createDynamicRequestByGroup,
                                dataSource: "ClientTimeDynamicRequestByGroup",
                                additionalParams: {processId: 0}
                            }
                        },
                        slaves: [{
                            name: "totalByUnit",
                            charts: {
                                dynamicWithPie: {
                                    jsFunc: createDynamic,
                                    dataSource: "ClientTimeDynamicWithPie",
                                    additionalParams: {processId: 0}
                                },
                                detailsTable: {
                                    jsFunc: createDetailsTable,
                                    dataSource: "ClientTimeDetailsTable",
                                    additionalParams: {processId: 0}
                                }
                            },
                            dontShowAfterMaster: true
                        }]
                    }]
                }]
            };

            function legendFormatter() {
                return "<div>" + this.name + " <p style='font-weight: normal'>" + this.y + " заявок </p><div>";
            }

            function getTitleForDynamic(filterData, group) {
                var level = filterData.drillDownLevel,
                        periodText = "с {0} по {1}".format(
                                moment(filterData.startDate).format("DD.MM.YYYY"),
                                moment(filterData.endDate).format("DD.MM.YYYY"));

                if (level === 0) {
                    return filterData.paramType == "AvgDuration"
                            ? "Среднее время в разрезе заявки"
                            : "Доля заявок, удовлетворяющих KPI";
                }

                var masterGroup = app.viewModel.getGroup(group.name.replace("ByUnit", "")),
                        valueName = masterGroup.activeBreadcrumb().name();

                if (filterData.paramType != "AvgDuration") {
                    var unitName = "";
                    switch (level) {
                        case 1:
                            unitName = "этапов";
                            break;

                        case 2:
                            unitName = "бизнес-процессов";
                            break;

                        case 3:
                            unitName = "функциональных групп";
                            break;
                    }

                    return "Доля {0}, удовлетворяющих KPI, за период {1}".format(unitName, periodText);
                }

                switch (level) {
                    case 1:
                        unitName = "на этапе";
                        break;

                    case 2:
                        unitName = "по бизнес процессу";
                        break;

                    case 3:
                        unitName = "по функциональной группе";
                        break;
                }

                return "Среднее время {0} \"{1}\" за период {2}".format(unitName, valueName, periodText);
            }

            function createDynamic($container, filterData, jsonData, customParams, group) {
                var chart = _.find(jsonData, function (c) {
                    return c.bag.processId == customParams.processId;
                });

                var title = getTitleForDynamic(filterData, group);

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

                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);
                var plotLines;

                var chartConfig = {
                    chart: {
                        type: "column",
                        marginRight: 270
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
                            showInLegend: false,
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                inside: true
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 75],
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
                    legend: {
                        enabled: true,
                        x: -20,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: legendFormatter
                    },
                    series: series
                };

                if (filterData.drillDownLevel == 0) {
                    _.merge(chartConfig, {
                        plotOptions: {
                            column: {
                                events: {
                                    click: function (event) {
                                        seriesPointClick(event, customParams.group);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    _.merge(chartConfig, {
                        plotOptions: {
                            column: {
                                events: {
                                    click: function (event) {
                                        drillDownSeriesPointClick(event, customParams.group);
                                    }
                                }
                            }
                        }
                    });
                }

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
                            text: "Среднее: {0}%".format(average.toFixed(2))
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

            function createDynamicRequestByGroup($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var title = "Группировка заявок по длительности";

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {height: 100}
                    });
                    return;
                }

                var series = chart.series;

                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column",
                        marginRight: 270
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
                            stacking: "percent",
                            showInLegend: false,
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                inside: true,
                                formatter: function () {
                                    return this.percentage < 5 ? '' : this.percentage.toFixed(0)
                                }
                            },
                            tooltip: {
                                pointFormat: "<span><b>{series.name}</b></span><br/>" +
                                "Средняя длительность: <b>{point.y:.1f}</b> дн ({point.percentage:.2f}%)<br/>" +
                                "Количество заявок: <b>{point.bpCount}</b>"
                            }
                        },
                        pie: {
                            showInLegend: true,
                            size: 100,
                            center: [995, 75],
                            dataLabels: {
                                inside: true,
                                format: "{point.percentage:.0f}%",
                                distance: 5
                            },
                            tooltip: {
                                pointFormat: "<b>{point.y:.1f}</b> дн ({point.percentage:.1f}%)"
                            }
                        }
                    },
                    legend: {
                        enabled: true,
                        x: -20,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: function () {
                            return "<div>" + this.name + " <p style='font-weight: normal'>Среднее: "
                                    + this.y.toFixed(1) + " дн.</p><div>";
                        }
                    },
                    series: series
                });
            }

            function createDetailsTable($container, filterData, jsonData, customParams, group) {
                var greenCss = "label-success",
                        redCss = "label-danger";

                var paramType = filterData.paramType;

                var cellTemplate = "<span class='label label-cell {0}'>{1}</span>";

                function formatCellValue(data) {
                    if (data == null || data.value === null) return "";
                    var css, valueToDisplay;
                    if (paramType == "AvgDuration") {
                        css = data.value <= data.quotaInDays ? greenCss : redCss;
                        valueToDisplay = data.value.toFixed(2) + " дн.";
                    }
                    else {
                        css = data.value >= data.quotaPercent ? greenCss : redCss;
                        valueToDisplay = data.value.toFixed(2) + "%";
                    }
                    return cellTemplate.format(css, valueToDisplay);
                }

                if(filterData.floatingYear){
                    filterData.startDate = moment(filterData.endDate)
                            .add(-12, "month")
                            .add(1, "day")
                            .toDate();
                }

                var columns = app.chartUtils.createDateTimeColumns(
                                filterData,
                                "value",
                                formatCellValue),
                        unitName,
                        valueName,
                        dimensionName,
                        firstColumnTitle,
                        title;

                var masterGroup = app.viewModel.getGroup(group.name.replace("ByUnit", "")),
                        valueName = masterGroup.activeBreadcrumb().name();

                switch (filterData.drillDownLevel) {
                    case 1:
                        unitName = "этапа";
                        dimensionName = "бизнес-процессов";
                        firstColumnTitle = "Бизнес процесс";
                        break;
                    case 2:
                        unitName = "бизнес процесса";
                        dimensionName = "функциольных групп";
                        firstColumnTitle = "Функциональная группа";
                        break;
                    case 3:
                        unitName = "функциональной группы";
                        dimensionName = "подразделений";
                        firstColumnTitle = "Подразделение";
                }

                title = "Детализация {0} \"{1}\" в разрезе {2}".format(unitName, valueName, dimensionName);
                $container.siblings().filter(".table-header").html(title);

                // Задаем колонку "Среднее"
                if (columns.length > 1) {
                    columns.push({
                        field: "totals",
                        title: "Среднее",
                        template: function (dataItem) {
                            return formatCellValue(dataItem["totals"]);
                        },
                        width: columns[0].width
                    });
                }

                // Задаем первую колонку
                columns.unshift({
                    field: "unitName",
                    title: firstColumnTitle,
                    width: 200,
                    locked: true
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


            /* DrillDown stuff */

            var Breadcrumb = function (name) {
                var self = this;
                self.name = ko.observable(name);
            };

            var UnitButton = function (options) {
                var self = this;

                self.id = options.id;
                self.name = options.name;
                self.inKpi = options.inKpi;

                self.css = self.inKpi ? "button-badge-success" : "button-badge-error";
            };

            function createBreadcrumbs($container, filterData, jsonData, customParams, group) {
                var data = _.filter(jsonData, function (x) {
                    return x.processId === customParams.processId;
                });

                group.unitButtons([]);
                _.each(data, function (x) {
                    group.unitButtons.push(new UnitButton(x));
                });

                if (data.length > 0) {
                    var unitId = null,
                            level = group.drillDownLevel(),
                            drillDownData = app.viewModel.getGroup(group.slaves[0].name).drillDownData(level),
                            selectedBtn;

                    if (drillDownData !== undefined) {
                        switch (level) {
                            case 1:
                                unitId = drillDownData.stageIds && drillDownData.stageIds[0];
                                break;
                            case 2:
                                unitId = drillDownData.bpKindIds && drillDownData.bpKindIds[0];
                                break;
                            case 3:
                                unitId = drillDownData.funcGroupIds && drillDownData.funcGroupIds[0];
                                break;
                        }

                        selectedBtn = _.find(data, function (btn) {
                            return btn.id == unitId;
                        });
                    }

                    if (selectedBtn) {
                        group.unitButtonClick(selectedBtn);
                    } else {
                        group.unitButtonClick(data[0]);
                    }
                }
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

            function drillDownSeriesPointClick(event, groupName) {
                var parentGroupName = groupName.replace("ByUnit", ""),
                        timeStamp = event.point.x,
                        group = app.viewModel.groups[groupName],
                        parentGroup = app.viewModel.groups[parentGroupName],
                        mainGroupOptions = app.viewModel.groups.default.filterData();

                var timeUnitId = parentGroup.filterData().timeUnitId;

                var period = app.chartUtils.getPeriodFromTimestamp(
                        timeStamp,
                        timeUnitId,
                        mainGroupOptions.startDate,
                        mainGroupOptions.endDate);

                var drillDownLevel = group.drillDownLevel();
                if (drillDownLevel < 3) {
                    drillDownLevel++;
                }

                parentGroup.drillDown(drillDownLevel, extendDrillDownData(group,
                        drillDownLevel, {
                            floatingYear: false,
                            startDate: period.startDate,
                            endDate: period.endDate,
                            timeUnitId: timeUnitId
                        }));
            }

            function extendDrillDownData(group, drillDownLevel, drillDownData) {
                switch (drillDownLevel) {
                    case 2:
                        drillDownData.stageIds = group.drillDownData(1).stageIds;
                        break;
                    case 3:
                        drillDownData.stageIds = group.drillDownData(1).stageIds;
                        drillDownData.bpKindIds = group.drillDownData(2).bpKindIds;
                        break;
                }

                return drillDownData;
            }

            app.init(config, function (viewModel) {
                _.chain(viewModel.groups).filter(function (group) {
                    return group.name !== "default" && group.name.indexOf("ByUnit") === -1;
                }).each(function (group) {
                    var slaveGroupName = group.name + "ByUnit",
                            slaveGroup = viewModel.getGroup(slaveGroupName);

                    group.drillDownByPeriod = function () {
                        group.drillDown(1, {}, true);
                    };

                    group.breadcrumbs = ko.observableArray();
                    group.activeBreadcrumb = ko.observable();
                    group.breadcrumbClick = function (breadcrumb) {
                        if (breadcrumb === group.activeBreadcrumb()) return;

                        var allBreadcrumbs = group.breadcrumbs(),
                                index = ko.utils.arrayIndexOf(allBreadcrumbs, breadcrumb);

                        for (var i = allBreadcrumbs.length - 1; i > index; i--) {
                            group.breadcrumbs.remove(allBreadcrumbs[i]);
                        }

                        group.activeBreadcrumb(allBreadcrumbs[index]);
                        group.drillDown(index + 1);
                    };

                    group.unitButtons = ko.observableArray();
                    group.selectedUnitButtonId = ko.observable();
                    group.unitButtonClick = function (btn) {
                        group.selectedUnitButtonId(btn.id);

                        var drillDownData = {},
                                level = group.drillDownLevel();

                        var activeBreadcrumb = group.activeBreadcrumb();
                        if (!activeBreadcrumb || level > group.breadcrumbs().length) {
                            activeBreadcrumb = new Breadcrumb(btn.name);
                            group.activeBreadcrumb(activeBreadcrumb);
                            group.breadcrumbs.push(activeBreadcrumb);
                        } else {
                            activeBreadcrumb.name(btn.name);
                        }

                        switch (level) {
                            case 1:
                                drillDownData.stageIds = [btn.id];
                                break;
                            case 2:
                                drillDownData.bpKindIds = [btn.id];
                                break;
                            case 3:
                                drillDownData.funcGroupIds = [btn.id];
                                break;
                        }

                        slaveGroup.drillDown(level, drillDownData);
                    };

                    group.drillDownByUnit = function () {
                        var drillDownLevel = group.drillDownLevel();
                        if (drillDownLevel < 3) {
                            drillDownLevel++;
                        }

                        group.drillDown(drillDownLevel, extendDrillDownData(slaveGroup,
                                drillDownLevel,
                                _.assign({}, group.drillDownData())));
                    };

                    group.returnBtnClick = function () {
                        group.drillDown(0);
                        group.unitButtons([]);
                        group.breadcrumbs([]);
                    }
                }).commit();

                viewModel.showDetailsReportButton = ko.pureComputed(function(){
                    return !viewModel.groups.default.processing() &&
                                    moment(app.viewModel.groups.default.filters.endDate.value()).diff(
                                            app.viewModel.groups.default.filters.startDate.value(), 'days') <= 31;
                });
            });

        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'kpiId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'paramType'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'productTypeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'stageIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'bpKindIds'"></filter>
                </div>
            </div>
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
                    <filter params="name: 'creditDepartIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'departIds'"></filter>
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
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'committeeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'clientLevelId'"></filter>
                </div>
            </div>
            <div>
                <refresh-button params="style: {marginTop: '5px'}"></refresh-button>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container" style="display: none;">
            <tab-strip params="name: 'main'">
                <tab>
                    <div style="margin-bottom: 5px;">
                        <button data-bind="click: groups.{groupName}.drillDownByPeriod,
                            enable: groups.{groupName}.drillDownLevel() === 0"
                                class="pull-right btn btn-default btn-xs">
                            <i class="fa fa-chevron-right"></i>
                            Детализировать отчетный период
                        </button>
                        <div class="clearfix"></div>
                    </div>

                    <chart params="name: 'dynamicWithPie', group: 'default'"></chart>

                    <!-- ko if: groups.{groupName}.drillDownLevel() === 0 -->
                    <chart params="name: 'dynamicRequestByGroup'"></chart>
                    <!-- /ko -->

                    <!-- ko if: groups.{groupName}.drillDownLevel() >= 1 -->
                    <hr>
                    <div style="padding: 10px;">
                        <div class="crumbs pull-left">
                            <ul data-bind="foreach: groups.{groupName}.breadcrumbs">
                                <li><a data-bind="text: name, click: $parent.groups.{groupName}.breadcrumbClick,
                                    css: {'active': $data == $parent.groups.{groupName}.activeBreadcrumb()}"></a></li>
                            </ul>
                        </div>

                        <button data-bind="click: groups.{groupName}.returnBtnClick"
                                class="pull-right btn btn-default btn-xs">
                            <i class="fa fa-chevron-left"></i> Назад
                        </button>

                        <div class="clearfix"></div>

                        <div data-bind="foreach: groups.{groupName}.unitButtons" class="btn-group"
                             style="white-space: normal; width: 1000px">
                            <button data-bind="click: $parent.groups.{groupName}.unitButtonClick,
                                    css: {active: $parent.groups.{groupName}.selectedUnitButtonId() == id}"
                                    class="btn btn-xs btn-default">
                                <span data-bind="css: css"></span>
                                <span data-bind="text: name"></span>
                            </button>
                        </div>

                        <div style="margin: 10px 0 5px;">
                            <button data-bind="click: groups.{groupName}.drillDownByUnit,
                                    enable: groups.{groupName}.drillDownLevel() < 3"
                                    class="pull-right btn btn-default btn-xs">
                                <i class="fa fa-chevron-right"></i> Детализировать отчетный период
                            </button>
                            <div class="clearfix"></div>
                        </div>

                        <chart params="name: 'breadcrumbs'" class="hidden-chart"></chart>
                        <chart params="name: 'dynamicWithPie', group: '{groupName}ByUnit'"></chart>
                        <chart params="name: 'detailsTable', group: '{groupName}ByUnit'">
                            <p class="table-header table-header-without-report"></p>

                            <div class="chart-container"></div>
                        </chart>

                        <div class="chart chart-inner details-report-wrapper" data-bind="if: showDetailsReportButton">
                            Детализированный отчет
                            <report-button params="reportUrl: 'ClientTimeDetails', group: '{groupName}ByUnit'"></report-button>
                        </div>
                    </div>
                    <!-- /ko -->
                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>