<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Оформление договорных документов факторинга. Декомпозиция"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .charts-container {
                margin-bottom: 20px;
            }

            .large-modal {
                width: 1300px;
            }

            .tab-pane chart:last-child .chart {
                margin-bottom: 0;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var config = {
                groups: [{
                    filters: {
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип Периода",
                            dataSource: {
                                url: "QualityDssFilter2/timeUnits"
                            },
                            width: 125
                        },
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            enableClear: true,
                            withGroups: true,
                            dataSource: {
                                url: "QualityDssFilter2/Cities"
                            }
                        },
                        divisionIds: {
                            type: "Select",
                            multiple: false,
                            title: "Подразделение",
                            dataSource: {
                                url: "QualityDssFilter2/Division",
                                params: [
                                    {name: "startDate", group: "default", required: false},
                                    {name: "endDate", group: "default", required: false},
                                    {name: "startYear", group: "default", required: true},
                                    {name: "endYear", group: "default", required: true},
                                    {name: "startDateId", group: "default", required: true},
                                    {name: "endDateId", group: "default", required: true},
                                    {name: "timeUnitId", group: "default", required: true}
                                ]
                            }
                        },
                        employeeIds: {
                            type: "Select",
                            multiple: false,
                            title: "Сотрудник",
                            dataSource: {
                                url: "QualityDssFilter2/Employee",
                                params: [
                                    {name: "startDate", group: "default", required: false},
                                    {name: "endDate", group: "default", required: false},
                                    {name: "startYear", group: "default", required: true},
                                    {name: "endYear", group: "default", required: true},
                                    {name: "startDateId", group: "default", required: true},
                                    {name: "endDateId", group: "default", required: true},
                                    {name: "timeUnitId", group: "default", required: true}
                                ]
                            }
                        },
                        operationIds: {
                            type: "Select",
                            multiple: false,
                            title: "Операция",
                            dataSource: {
                                url: "QualityDssFilter2/Operation",
                                params: [
                                    {name: "startDate", group: "default", required: false},
                                    {name: "endDate", group: "default", required: false},
                                    {name: "startYear", group: "default", required: true},
                                    {name: "endYear", group: "default", required: true},
                                    {name: "startDateId", group: "default", required: true},
                                    {name: "endDateId", group: "default", required: true},
                                    {name: "timeUnitId", group: "default", required: true}
                                ]
                            }
                        },
                        typeProductIds: {
                            type: "Select",
                            multiple: false,
                            title: "Тип продукта",
                            dataSource: {
                                url: "QualityDssFilter2/TypeProduct",
                                params: [
                                    {name: "startDate", group: "default", required: false},
                                    {name: "endDate", group: "default", required: false},
                                    {name: "startYear", group: "default", required: true},
                                    {name: "endYear", group: "default", required: true},
                                    {name: "startDateId", group: "default", required: true},
                                    {name: "endDateId", group: "default", required: true},
                                    {name: "timeUnitId", group: "default", required: true}
                                ]
                            }
                        },
                        salesChannelIds: {
                            type: "Select",
                            multiple: true,
                            title: "Канал продаж",
                            dataSource: {
                                url: "QualityDssFilter2/SalesChannel"
                            }
                        },
                        dopOfficeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Наименование офиса",
                            enableClear: true,
                            enableSearch: true,
                            dataSource: {
                                url: "MassFilter/DopOffices",
                                params: ["cityIds"]
                            },
                            disableIfNull: "cityIds"
                        },
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            notAfter: "endDate",
                            defaultValue: moment().add(-7, 'days').startOf('day').toDate()
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            notBefore: "startDate",
                            defaultValue: moment().add(-1, 'days').startOf('day').toDate()
                        },

                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица сети",
                            dataSource: {
                                data: [
                                    {id: 4, name: "Офис"},
                                    {id: 7, name: "Менеджер"}
                                ]
                            },
                            width: 150
                        },

                        startYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: new Date(moment().add(2, 'month').year(), 0, 1),
                            notAfter: "endYear",
                            width: 90
                        },
                        startDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, с",
                            dataSource: {
                                url: "QualityDssFilter2/startDates",
                                params: [{name: "startYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            defaultValue: moment().subtract(2, 'month').month(),
                            width: 260,
                            postInit: createStartDateIdSubscriptions
                        },
                        endYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            notBefore: "startYear",
                            width: 90
                        },
                        endDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, по",
                            dataSource: {
                                url: "QualityDssFilter2/endDates",
                                params: [{name: "endYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            defaultValue: moment().month(),
                            width: 260,
                            postInit: createEndDateIdSubscriptions
                        },
                        check1: {
                            type: "CheckBox",
                            title: "Учитывать время смежных подразделений"
                        }
                    },
                    charts: {
                        avgDurationDynamic: {
                            jsFunc: createDynamicAvgDuration,
                            dataSource: "MassDecompositionDynamicAvgDuration"
                        },
                        percentDynamic: {
                            jsFunc: createDynamicPercent,
                            dataSource: "MassDecompositionDynamicPercent"
                        },
                        avgDurationDecompositionButtons: {
                            jsFunc: createButtons,
                            dataSource: "MassDecompositionButtonsAvgDuration",
                            customParams: { slaveGroup: "avgDurationDetails" }
                        },
                        percentDecompositionButtons: {
                            jsFunc: createButtons,
                            dataSource: "MassDecompositionButtonsPercent",
                            customParams: { slaveGroup: "percentDetails" }
                        }
                    },
                    tabStrips: {
                        main: {
                            tabs: ["Оформление договорных документов кредита/гарантии. Декомпозиция", "Оформление договорных документов факторинга. Декомпозиция"]
                        }
                    },
                    slaves: [{
                        name: "avgDurationDetails",
                        charts: {
                            dynamicByStage: {
                                jsFunc: createAvgDurationDynamicByStage,
                                dataSource: "MassDecompositionDynamicByStageAvgDuration"
                            },
                            tableByStage: {
                                jsFunc: createTableByStageAvgDuration,
                                dataSource: "MassDecompositionTableByStageAvgDuration",
                                customParams: { source: "MassDecompositionTableByStageAvgDuration" }
                            }
                        },
                        dontShowAfterMaster: true
                    },
                        {
                            name: "percentDetails",
                            charts: {
                                dynamicByStage: {
                                    jsFunc: createPercentDynamicByStage,
                                    dataSource: "MassDecompositionDynamicByStagePercent",
                                },
                                tableByStage: {
                                    jsFunc: createTableByStagePercent,
                                    dataSource: "MassDecompositionTableByStagePercent",
                                    customParams: { source: "MassDecompositionTableByStagePercent" }
                                }
                            },
                            dontShowAfterMaster: true
                        },
                        {
                            name: "slowCompanies",
                            charts: {
                                dynamicSlowCompanies: {
                                    jsFunc: createTableSlowCompanies,
                                    dataSource: "MassDecompositionTableSlowCompanies"
                                }
                            },
                            dontShowAfterMaster: true
                        }]
                }],
                cookies: true
            };

            var helpers = {
                styleHelpers: {
                    badgesColors :{
                        Red: "button-badge-error",
                        Yellow: "button-badge-normal",
                        Green: "button-badge-success"
                    }
                }
            };

            function createStartDateIdSubscriptions(config, filter, viewModel) {
                filter.value.subscribe(function (currentValue) {
                    var startDateIdFilter = viewModel.getFilter("startDateId");

                    var endDateIdFilter = viewModel.getFilter("endDateId");

                    if (startDateIdFilter.getSelectedOptions().length && endDateIdFilter.getSelectedOptions().length) {
                        var startDateSelected = moment(startDateIdFilter.getSelectedOptions()[0].startDate);
                        var endDateSelected = moment(endDateIdFilter.getSelectedOptions()[0].startDate);

                        if (startDateSelected > endDateSelected) {
                            var firstPositive = _.find(endDateIdFilter.options(), function (op) {
                                return moment(op.startDate) >= startDateSelected;
                            });

                            if (firstPositive) {
                                endDateIdFilter.value(firstPositive.id);
                            }
                        }
                    }
                });
            }

            function createEndDateIdSubscriptions(config, filter, viewModel) {
                filter.value.subscribe(function (currentValue) {
                    var startDateIdFilter = viewModel.getFilter("startDateId");
                    var endDateIdFilter = viewModel.getFilter("endDateId");

                    if (startDateIdFilter.getSelectedOptions().length && endDateIdFilter.getSelectedOptions().length) {
                        var startDateSelected = moment(startDateIdFilter.getSelectedOptions()[0].startDate);
                        var endDateSelected = moment(endDateIdFilter.getSelectedOptions()[0].startDate);

                        if (startDateSelected > endDateSelected) {
                            var firstPositive = _.findLast(startDateIdFilter.options(), function (op) {
                                return moment(op.startDate) <= endDateSelected;
                            });

                            if (firstPositive) {
                                startDateIdFilter.value(firstPositive.id);
                            }
                        }
                    }
                });
            }

            function createDynamicAvgDuration($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var width = 1184;

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: ''},
                        chart: {
                            height: 100
//                            ,width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column"
//                        ,width: width
                    },
                    title: {text: ''},
                    plotOptions: {
                        column: {
                            stacking: "normal",
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                formatter: function () {
                                    return this.y / this.series.chart.axes[1].dataMax > 0.08
                                            ? app.chartUtils.daysToDDHH(this.y)
                                            : '';
                                },
                                zIndex: 4
                            },
                            tooltip: {
                                pointFormatter: function () {
                                    return "" + this.series.name + "<br/>Длительность: <b>" +
                                            app.chartUtils.daysToDDHH(this.y) +
                                            "</b><br/>Открыто счетов: " + this.tag + "<b>"
                                }
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        endOnTick: false,
                        title: {text: ''},
                        labels: {
                            enabled: true,
                            formatter: function() {
                                return Math.round(this.value*24) + ' ч';
                            }
                        },
                        stackLabels: {
                            enabled: xAxis.ticks.length < 40,
                            formatter: function () {
                                return app.chartUtils.daysToDDHH(this.total);
                            }
                        }
                    },
                    series: series,
                    legend: {enabled: true}
                });
            }

            function createDynamicPercent($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var width = 1184;

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: ''},
                        chart: {
                            height: 100
//                            ,width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column"
//                        ,width: width
                    },
                    title: {text: ''},
                    plotOptions: {
                        column: {
                            turboThreshold: 0,
                            dataLabels: {
                                enabled: xAxis.ticks.length < 40,
                                inside: true,
                                formatter: function () {
                                    return this.y > 0 ? this.y.toFixed(0) : '';
                                },
                                zIndex: 4
                            },
                            tooltip: {
                                pointFormat: "Доля: <b>{point.y:.1f}%</b>"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        endOnTick: false,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {enabled: false}
                });
            }

            function createButtons($container, filterData, jsonData, customParams) {
                if (!jsonData[0].bag.stages || !jsonData[0].bag.stages.length) {
                    return;
                }

                _.each(jsonData[0].bag.stages, function(s) {
                    s.badgeCss = helpers.styleHelpers.badgesColors[s.color];
                    s.slaveGroup = customParams.slaveGroup
                });

                var viewModel = { stages: jsonData[0].bag.stages };

                $container.append("<div data-bind=\"template: { name: 'buttons-template'}\"></div>");
                ko.applyBindingsToDescendants(viewModel, $container.get(0));

                var firstBtn = $container.find("button")[0];

                $(firstBtn).trigger("click", [ firstBtn ]);
            }

            function refreshDetailGroup(data, jqueryEvent, target){
                var $btn = $(target || event.srcElement);
                $btn.siblings().removeClass("active");
                $btn.addClass("active");
                app.viewModel.groups[data.slaveGroup].drillDown(0, { stageId: data.id });
            }

            function createAvgDurationDynamicByStage($container, filterData, jsonData, customParams){
                var chart = jsonData[0], width = 1184;

                var series = chart.series, bag = chart.bag;

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: bag.title},
                        chart: {
                            height: 100
//                            ,width: width
                        }
                    });
                    return;
                }

                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                _.each(series, function(s) {
                    _.each(s.data, function(p){
                        if (p.y >= bag.stage.normative) {
                            p.tag += "<br/>Норматив превышен на: <b>{0}</b>".format(app.chartUtils.daysToDDHH(p.y - bag.stage.normative));
                        }
                        p.customHtmlToolTip = app.chartUtils.daysToDDHH(p.y);
                    });
                });

                $container.highcharts({
                    chart: {
                        type: "column"
//                        ,width: width
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            formatter: function(){
                                return (this.value*24).toFixed(1) + ' ч';
                            }
                        },
                        plotLines: [{
                            color: 'red',
                            label: {
                                align: 'left',
                                text: '<b>KPI</b>: {0}'.format(app.chartUtils.daysToDDHH(bag.stage.normative))
                            },
                            value: bag.stage.normative,
                            width: 2,
                            zIndex: 10
                        }]
                    },
                    title: {text: bag.title},
                    plotOptions: {
                        column: {
                            stacking: "normal",
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                inside: true,
                                style: {
                                    fontWeight: 'bold',
                                    color: 'white',
                                    textShadow: '0px 1px 2px black'
                                },
                                formatter: function () {
                                    return this.y > 0 ? app.chartUtils.daysToDDHH(this.y) : '';
                                }
                            },
                            pointWidth: xAxis.pointWidth,
                            tooltip: {
                                pointFormat: "<span>{series.name}</span><br/>Длительность: <b>{point.customHtmlToolTip}</b><br/>{point.tag}"
                            },
                            events: {
                                click: function(event) {
                                    return seriesPointClick(event, bag.stage, "avgDuration");
                                }
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    series: series,
                    legend: {enabled: false}
                });
            }

            function createPercentDynamicByStage($container, filterData, jsonData, customParams){
                var chart = jsonData[0], width = 1184;

                var series = chart.series, bag = chart.bag;

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: bag.title},
                        chart: {
                            height: 100
//                            ,width: width
                        }
                    });
                    return;
                }

                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "column"
//                        ,width: width
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            format: "{value}%"
                        }
                    },
                    title: {text: bag.title},
                    plotOptions: {
                        column: {
                            stacking: "normal",
                            dataLabels: {
                                enabled: xAxis.ticks.length < 25,
                                inside: true,
                                style: {
                                    fontWeight: 'bold',
                                    color: 'white',
                                    textShadow: '0px 1px 2px black'
                                },
                                formatter: function () {
                                    return this.y > 0 ? this.y.toFixed(0) : '';
                                }
                            },
                            pointWidth: xAxis.pointWidth,
                            tooltip: {
                                pointFormat: "Доля: <b>{point.y:.1f}%</b>"
                            },
                            events: {
                                click: function(){
                                    return seriesPointClick(event, bag.stage, "percent");
                                }
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    series: series,
                    legend: {enabled: false}
                });
            }

            function createTable($container, filterData, jsonData, fields, detailInit){
                var greenCss = "label-success", redCss = "label-danger", yellowCss = "label-warning",
                        template = "<span class='label label-cell {0}'> {1} </span>";

                function formatCellValue(data) {
                    if (!data || data[fields.valueField] === undefined || data[fields.valueField] === null) return "";
                    var valueToDisplay, css;
                    var value = data[fields.valueField], treshold = data[fields.tresholdField];
                    if (fields.valueField === "avgDurationValue") {
                        valueToDisplay = app.chartUtils.daysToDDHH(value);
                        css = value*24 <= treshold.error ? greenCss : redCss;
                    } else {
                        valueToDisplay = "" + Math.round(value*100, 2) + "%";
                        css = value >= treshold.warning ? greenCss : value <= treshold.error ? redCss : yellowCss;
                    }
                    return template.format(css, valueToDisplay);
                }

                var systemUnitOption = _.find(app.viewModel.getFilter("systemUnitId").options(),
                                function(o) { return o.id == filterData.systemUnitId; }),
                        systemUnitName = systemUnitOption ? systemUnitOption.name : "Единица сети",
                        columns = app.chartUtils.createDateTimeColumns(filterData, fields.valueField, formatCellValue),
                        isBranchLevel = filterData.systemUnitId == "4",
                        isFirstLevel = !filterData.secondLevel;

                if (isFirstLevel) {
                    _.each(columns, function(c) {
                        c.filterable = false;
                    });

                    // Footer
                    var footerRow = jsonData.splice(-1, 1)[0];
                    _.forEach(columns, function (c) {
                        var value = footerRow[c.field.split('.')[0]]
                        if (value === null || value === undefined) return;
                        c.footerTemplate = formatCellValue(value);
                    });
                }

                // Задаем колонку "Среднее"
                columns.push({
                    field: "average",
                    title: "Среднее",
                    template: function (dataItem) {
                        return formatCellValue(dataItem.average);
                    },
                    filterable: false,
                    width: columns[0].width
                });

                // Задаем первую колонку
                columns.unshift({
                    field: "unitName",
                    title: systemUnitName,
                    width: 250,
                    footerTemplate: isFirstLevel ? "Итого" : null
                });

                var transport = {
                    read: function (o) {
                        var request = app.ajaxUtils.postData(fields.source, JSON.stringify(filterData));
                        request.done(function (ajaxResult) {
                            if (ajaxResult.success) {
                                o.success(isFirstLevel ? ajaxResult.data : ajaxResult.data.slice(0, -1));
                                if (app.viewModel.showSQL() && ajaxResult.queryList && ajaxResult.queryList.length) {
                                    console.log("\n" + ajaxResult.queryList[0].sql);
                                }
                            } else {
                                app.showAlert(ajaxResult.message);
                            }
                        });
                    }
                };

                var dataSource = !!jsonData ? { data: jsonData } : {
                    transport: transport,
                    serverPaging: false,
                    serverSorting: false
                };

                var gridConfiguration = {
                    dataSource: dataSource,
                    sortable: true,
                    filterable: isFirstLevel,
                    columns: columns
                };

                if (isFirstLevel) {
                    gridConfiguration.height = 450;
                    if (isBranchLevel) {
                        gridConfiguration.detailInit = detailInit;
                    }
                }

                $container.kendoGrid(gridConfiguration);

                if (isFirstLevel) {
                    $container.find(".k-grid-content").css({ height: '380px' });
                }
            }

            function detailInit(e, filterData, fields) {
                var $container = $("<div/>").appendTo(e.detailCell);

                var extender = {
                    systemUnitId: 7,
                    dopOfficeIds: [e.data.unitId],
                    secondLevel: true
                };

                var newFilterData = _.assign(_.cloneDeep(filterData), extender);

                createTable($container, newFilterData, null, fields, function() {});
            }

            function createTableByStageAvgDuration($container, filterData, jsonData, customParams){
                var fields = {
                    valueField: "avgDurationValue",
                    tresholdField: "avgDurationTreshold",
                    source: customParams.source
                };
                createTable($container, filterData, jsonData, fields, function(e) { return detailInit(e, filterData, fields); });
            }

            function createTableByStagePercent($container, filterData, jsonData, customParams){
                var fields = {
                    valueField: "percentValue",
                    tresholdField: "percentTreshold",
                    source: customParams.source
                };
                createTable($container, filterData, jsonData, fields, function(e) { return detailInit(e, filterData, fields); });
            }

            function seriesPointClick(event, stage, valueType) {
                var timestamp = event.point.x, filterData = app.viewModel.groups.default.filterData();

                var period = app.chartUtils.getPeriodFromTimestamp(timestamp, filterData.timeUnitId, filterData.startDate, filterData.endDate);

                var extender = {
                    startDate: period.startDate,
                    endDate: period.endDate,
                    stageId: stage.id,
                    durationLimitHours: stage.normative,
                    valueType: valueType
                };

                var $modal = $("#massDecompositionSlowCompaniesModal"),
                        $modalTitle = $modal.find(".modal-title");

                $modalTitle.text("Отчет по превышениям за период с {0} по {1} (этап \"{2}\")".format(
                        moment(period.startDate).format("DD.MM.YYYY"),
                        moment(period.endDate).format("DD.MM.YYYY"),
                        stage.name
                ));

                $modal.unbind("shown").on("shown.bs.modal", function(e){
                    app.viewModel.groups["slowCompanies"].drillDown(0, extender);
                }).modal('show');
            }

            function createTableSlowCompanies($container, filterData, jsonData, customParams) {
                function dateFormat(value){
                    return moment(value).format("DD.MM.YYYY");
                }
                function timeFormat(value){
                    return moment(value).format("hh:mm:ss");
                }

                var columns = [
                    {
                        field: "clientName",
                        title: "Наименование клиента",
                        width: 170
                    },
                    {
                        field: "pin",
                        title: "ПИН",
                        width: 110
                    },
                    {
                        field: "accountNumber",
                        title: "Номер счета",
                        width: 150
                    },
                    {
                        field: "startDate",
                        template: function(dataItem) { return dateFormat(dataItem.startDate); },
                        title: "Дата начала на этапе",
                        width: 90
                    },
                    {
                        field: "startDate",
                        template: function(dataItem) { return timeFormat(dataItem.startDate); },
                        title: "Время начала на этапе",
                        width: 75
                    },
                    {
                        field: "finishDate",
                        template: function(dataItem) { return dateFormat(dataItem.finishDate); },
                        title: "Дата окончания на этапе",
                        width: 90
                    },
                    {
                        field: "finishDate",
                        template: function(dataItem) { return timeFormat(dataItem.finishDate); },
                        title: "Время окончания на этапе",
                        width: 90
                    },
                    {
                        field: "duration",
                        template: function(dataItem) { return app.chartUtils.hoursToHHMM(dataItem.duration); },
                        title: "Длительность",
                        width: 90
                    },
                    {
                        field: "cityName",
                        title: "Город"
                    },
                    {
                        field: "saleChannelName",
                        title: "Канал продаж",
                        width: 90
                    },
                    {
                        field: "branchName",
                        title: "Исполнитель (фронт-офис/бэк-офис)"
                    },
                    {
                        field: "managerName",
                        title: "Исполнитель"
                    }
                ];

                $container.kendoGrid({
                    dataSource: { data: jsonData },
                    height: 430,
                    scrollable: true,
                    sortable: true,
                    columns: columns
                });
            }

            app.init(config);
        </script>

        <script type="text/html" id="buttons-template">
            <div class="btn-group-sm" role="group" style="white-space: normal;">
                <!-- ko foreach: stages -->
                <button class="btn btn-default" data-bind="click: refreshDetailGroup">
                    <span data-bind="css: badgeCss"></span>
                    <!-- ko text: name --><!--/ko-->
                </button>
                <!--/ko-->
            </div>
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
                <!-- ko if: groups.default.filters.timeUnitId.value() != 2 -->
                <div class="filter-element">
                    <filter params="name: 'startYear'"></filter>
                </div>

                <div class="filter-element">
                    <filter params="name: 'startDateId'"></filter>
                </div>
                <!-- /ko -->
                <!-- ko if: groups.default.filters.timeUnitId.value() == 2 -->
                <div class="filter-element">
                    <filter params="name: 'startDate'"></filter>
                </div>
                <!-- /ko -->
                <!-- ko if: groups.default.filters.timeUnitId.value() != 2 -->
                <div class="filter-element">
                    <filter params="name: 'endYear'"></filter>
                </div>

                <div class="filter-element">
                    <filter params="name: 'endDateId'"></filter>
                </div>
                <!-- /ko -->
                <!-- ko if: groups.default.filters.timeUnitId.value() == 2 -->
                <div class="filter-element">
                    <filter params="name: 'endDate'"></filter>
                </div>
                <!-- /ko -->
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'divisionIds' , group: 'default'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'employeeIds' , group: 'default'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'operationIds' , group: 'default'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'typeProductIds' , group: 'default'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'check1'"></filter>
                </div>
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <refresh-button params="style: {marginTop: '0'}"></refresh-button>
                </div>
            </div>
            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <tab-strip params="name: 'main'">
                <tab>
                    <chart params="name: 'avgDurationDynamic'"></chart>
                    <chart params="name: 'avgDurationDecompositionButtons'"></chart>
                    <chart params="name: 'dynamicByStage', group: 'avgDurationDetails'"></chart>
                    <chart params="name: 'tableByStage', group: 'avgDurationDetails'"></chart>
                </tab>
                <tab>
                    <chart params="name: 'percentDynamic'"></chart>
                    <chart params="name: 'percentDecompositionButtons'"></chart>
                    <chart params="name: 'dynamicByStage', group: 'percentDetails'"></chart>
                    <chart params="name: 'tableByStage', group: 'percentDetails'"></chart>
                </tab>
            </tab-strip>
            <div id="massDecompositionSlowCompaniesModal" class="modal fade bs-example-modal-lg" role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-lg large-modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 class="modal-title"></h4>
                        </div>
                        <div class="modal-body">
                            <chart params="name: 'dynamicSlowCompanies', group: 'slowCompanies'"></chart>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>

