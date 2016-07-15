<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="ru.alfabank.dmpr.repository.workload.WorkloadFilterRepository" %>
<%@ page import="ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper" %>
<%@ page import="ru.alfabank.dmpr.model.PeriodSelectOption" %>
<%@ page import="org.joda.time.LocalDate" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Нагрузка"/>

<%
    WorkloadFilterRepository workloadFilter = RequestContextUtils.getWebApplicationContext(request).getBean(WorkloadFilterRepository.class);
    PeriodSelectOption defaultFilterOption = PeriodSelectHelper.getWeekByDate(LocalDate.now().minusWeeks(8), workloadFilter.getWeeks());

    if (defaultFilterOption == null) {
        defaultFilterOption = new PeriodSelectOption(1, 1, "", new LocalDate(), new LocalDate());
    }

    request.setAttribute("defaultYear", new ObjectMapper().writeValueAsString(defaultFilterOption.endDate.getYear()));
    request.setAttribute("defaultWeekId", new ObjectMapper().writeValueAsString((defaultFilterOption.id)));
%>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .chart {
                margin-bottom: 0;
            }

            .row {
                margin: 0;
            }

            .col-xs-4, .col-xs-8, .col-xs-12 {
                padding: 0 10px;
            }

            [class*="col-"] .col-xs-6 {
                padding-left: 0;
                padding-right: 10px;
            }

            [class*="col-"] .col-xs-6:last-child {
                padding-right: 0;
                padding-left: 10px;
            }

            .chart-tabs > .tab-content > .tab-pane {
                padding: 10px 4px;
            }

            .chart-tabs > .tab-content {
                margin-bottom: 20px;
            }

            .tab-1, .tab-2, .tab-3, .tab-4, .tab-5 {
                color: #fff !important;
            }

            .tab-1 {
                background-color: rgba(187, 132, 132, 0.75) !important;
            }

            .active .tab-1 {
                background-color: #D00B0B !important;
            }

            .tab-2 {
                background-color: rgba(117, 168, 194, 0.75) !important;
            }

            .active .tab-2 {
                background-color: #249AD6 !important;
            }

            .ob-custom-table-wrapper {
                margin-left: 0;
                width: 100%;
            }

            .ob-custom-table-item {
                padding: 3px 0;
                border-bottom: 1px solid rgba(0, 0, 0, 0.15);
                text-align: center;
            }

            .ob-custom-table-wrapper .ob-custom-table-item:last-child {
                border-bottom: 0;
            }

            .small-title-font {
                font-size: 15px;
                font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
                font-weight: bold;
            }

            .big-title-font {
                font-size: 26px;
                font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
            }

            .label-tooltip-font {
                font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                font-size: 12px;
                color: #555555;
                fill: #555555;
                font-weight: normal;
            }

            .ob-custom-table-item .value {
                font-size: 28px;
                display: inline-block;
            }

            .icon-green-arrow-down, .icon-red-arrow-up {
                width: 33px;
                height: 33px;
                background-size: 33px 33px;
                -webkit-transform: rotate(180deg);
                -moz-transform: rotate(180deg);
                -o-transform: rotate(180deg);
                -ms-transform: rotate(180deg);
                transform: rotate(180deg);
            }

            .icon-green-arrow-down {
                background-image: url("../images/green_arrow.png");
            }

            .icon-red-arrow-up {
                background-image: url("../images/red_arrow.png");
            }

            .icon-small-arrow {
                width: 25px;
                height: 25px;
                background-size: 25px;
            }

            .icon-people {
                width: 33px;
                height: 33px;
                background: url("../images/icon-people.png");
                background-size: 33px 33px;
            }

            .icon-truck {
                width: 33px;
                height: 33px;
                background: url("../images/icon-truck.png");
                background-size: 33px 33px;
            }

            .styled-chart-container {
                border: 1px solid rgba(0, 0, 0, 0.15);
                padding: 5px 5px 5px 5px;
                background-color: #F5F7FA;
                text-align: center;
                margin-bottom: 20px;
            }

            .styled-chart-container .chart {
                margin-bottom: 0;
            }

            .styled-chart-container .chart-container {
                height: 235px;
            }

            .styled-chart-title {
                font-weight: bold;
                margin-bottom: 5px;
            }

            .styled-chart-container th {
                font-weight: bold;
                text-align: center;
                border-bottom-width: 1px !important;
                line-height: 15px !important;
            }

            .styled-chart-container tbody {
                background-color: #FFF;
                font-size: 26px;
                font-family: Helvetica Neue, Helvetica, Arial, sans-serif;
            }

            .mb-0 {
                margin-bottom: 0;
            }

            .tab-content {
                padding: 10px 4px;
            }

            .k-treelist .k-grid-content {
                height: 570px !important;
            }

            .treelist-wrapnormal-cell {
                display: inline-block;
                white-space: normal;
                vertical-align: top;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var config = {
                groups: [{
                    filters: {
                        year: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: new Date(${defaultYear}, 0, 1),
                            width: 90
                        },
                        week: {
                            type: "Select",
                            multiple: false,
                            title: "Неделя",
                            dataSource: {
                                url: "workloadFilter/weeks",
                                params: [{name: "year", group: "default", required: true}]
                            },
                            defaultValue: ${defaultWeekId},
                            width: 250
                        },
                        rpTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип РП",
                            optionsCaption: "Все",
                            dataSource: {
                                url: "workloadFilter/rpTypes"
                            },
                            width: 200
                        },
                        duodrReg: {
                            type: "Select",
                            multiple: false,
                            title: "ОО/ДО",
                            enableSearch: true,
                            optionsCaption: "Все",
                            dataSource: {
                                url: "workloadFilter/DuodrReg"
                            },
                            width: 250
                        }

                    },
                    tabStrips: {
                        showcases: {
                            tabs: [
                                "ОБ",
                                "Дирекции"
                            ]
                        }
                    },
                    charts: {
                        divisionList: {
                            jsFunc: createDivisionList,
                            dataSource: "WorkloadDivisionStatList"
                        },
                        posTypeDistribution: {
                            jsFunc: createPie,
                            dataSource: "WorkloadPosTypeDistribution",
                            customParams: {title: "Распределение ш.ч. по типам позиций"}
                        },
                        territorialDistribution: {
                            jsFunc: createPie,
                            dataSource: "WorkloadTerritorialDistribution",
                            customParams: {title: "Распределение ш.ч. по территориальному размещению"}
                        },

                        /*Ob charts*/
                        obPosTypePie: {
                            jsFunc: createPie,
                            dataSource: "WorkloadObPosTypePie"
                        },
                        obTerritorialPie: {
                            jsFunc: createPie,
                            dataSource: "WorkloadObTerritorialPie"
                        },
                        obDirectionPie: {
                            jsFunc: createPie,
                            dataSource: "WorkloadObDirectionPie"
                        },
                        obGauge: {
                            jsFunc: createObGauge,
                            dataSource: "WorkloadObGauge"
                        },
                        obCustomTable: {
                            jsFunc: createObCustomTable,
                            dataSource: "WorkloadObCustomTable"
                        },
                        obPosTypeDynamic: {
                            jsFunc: createObPosTypeDynamic,
                            dataSource: "WorkloadObPosTypeDynamic"
                        },
                        obWLDynamic: {
                            jsFunc: createObWLDynamic,
                            dataSource: "WorkloadObWLDynamic"
                        },
                        obTopNTable: {
                            jsFunc: createObTopNTable,
                            dataSource: "WorkloadObTopNTable"
                        }
                    },
                    slaves: [{
                        name: "rightSide",
                        charts: {
                            posTypeDistribution: {
                                jsFunc: createPie,
                                dataSource: "WorkloadPosTypeDistribution",
                                customParams: {title: "Распределение ш.ч. по типам позиций"}
                            },
                            territorialDistribution: {
                                jsFunc: createPie,
                                dataSource: "WorkloadTerritorialDistribution",
                                customParams: {title: "Распределение ш.ч. по территориальному размещению"}
                            },
                            workloadDynamic: {
                                jsFunc: createWorkloadDynamic,
                                dataSource: "WorkloadDynamic"
                            },
                            workloadDynamic2Level: {
                                jsFunc: createWorkloadDynamic,
                                dataSource: "WorkloadDynamic"
                            },
                            tableWithDynamic2Level: {
                                jsFunc: createTableWithDynamic,
                                dataSource: "WorkloadTableWithDynamic",
                                customParams: {height: 500}
                            },
                            duodrTable: {
                                jsFunc: createDuodrTable,
                                dataSource: "WorkloadDuodrTable"
                            }
                        },
                        dontShowAfterMaster: true
                    }]
                }],
                cookies: true
            };

            var helpers = {
                tooltips: {
                    staffCountDelta: function(dataItem) {
                        return dataItem.staffCountDeltaCnt > 0 ? "Увеличение численности" : "Уменьшение численности";
                    },
                    hfCriterion: function(dataItem) {
                        var template = "{0}<br>Шт. единиц за 4 недели: {1}<br>Шт. единиц за 8 недель: {2}";
                        return template.format(dataItem.hfCriterionCnt > 0 ? "Требуется увеличение фактической численности" :
                                "Требуется уменьшение фактической численности", dataItem.hfCriterionW4Cnt, dataItem.hfCriterionW8Cnt);
                    }
                }
            };

            function createDivisionList($container, filterData, jsonData, customParams, group) {
                jsonData = jsonData || [];

                _.each(jsonData, function (item) {
                    if (item.parentId == 0) {
                        item.parentId = null;
                    }
                });

                var rightSideGroup = app.viewModel.getGroup("rightSide");

                var dataSource = new kendo.data.TreeListDataSource({
                    data: jsonData,
                    schema: {
                        model: {
                            id: "id",
                            expanded: false
                        }
                    }
                });

                var oldTreeList = $container.data("kendoTreeList");
                if (oldTreeList) {
                    oldTreeList.destroy();
                }

                $container.kendoTreeList({
                    dataSource: dataSource,
                    height: 600,
                    selectable: true,
                    filterable: {
                        extra: false,
                        messages: {
                            filter: "задать"
                        },
                        operators: {
                            string: {
                                contains: "Содержит"
                            }
                        }
                    },
                    columns: [
                        {
                            field: "unitName",
                            title: "DrillDown",
                            template: "<div class='treelist-wrapnormal-cell' style='width:165px'>#=unitName#</div>"
                        },
                        {
                            field: "staffCountFact",
                            title: "Ф.ч.",
                            width: 65,
                            format: "{0}",
                            filterable: false,
                            template: "#=staffCountFact#" +
                                        "#if(slaFactValueDoubleCnt > 0){#" +
                                            "<span data-toggle='tooltip' data-placement='left' title='Дублирование: #=slaFactValueDoubleCnt#' style='color: red; font-weight: bold;'> !</span>" +
                                        "#}#",
                            attributes: {
                                style: "text-align: right"
                            }
                        },
                        {
                            field: "workloadRate",
                            title: "Нагрузка",
                            width: 70,
                            filterable: false,
                            template: function(item){
                                var greenCss = "label-success",
                                        yellowCss = "label-warning",
                                        redCss = "label-danger";

                                var css;
                                var value = (item.workloadRate * 100).toFixed(0);
                                if(value < 85){
                                    css = yellowCss;
                                } else if (value < 115){
                                    css = greenCss;
                                } else if (value < 130) {
                                    css = yellowCss;
                                } else {
                                    css = redCss;
                                }

                                return "<span class='label label-cell {0}'>{1}%</span>".format(css, value);
                            }
                        }
                    ],
                    change: function (e) {
                        var selectedRow = e.sender.select();
                        var dataItem = e.sender.dataItem(selectedRow);
                        if (dataItem) {
                            var drillDownData = {
                                divisionGroupId: dataItem.divisionGroupId,
                                regionId: dataItem.regionId
                            };

                            group.selectedDivision(dataItem);

                            rightSideGroup.drillDown(dataItem.lvl, drillDownData);
                        }
                    },
                    dataBound: function () {
                        this.select(this.tbody.find("tr").first());
                        this.tbody.find("[data-toggle=tooltip]").tooltip();
                    }
                });
            }

            function createPie($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: null}
                    });
                    return;
                }

                $container.highcharts({
                    title: {text: null},
                    chart: {
                        type: 'pie'
                    },
                    tooltip: {
                        headerFormat: "",
                        pointFormat: '<b>{point.name}</b>: {point.percentage:.1f}%, {point.y} ш.е.'
                    },
                    plotOptions: {
                        pie: {
                            size: 130,
                            dataLabels: {
                                enabled: true,
                                distance: 5,
                                useHTML: true,
                                format: '{point.percentage:.1f}%'
                            },
                            showInLegend: true
                        }
                    },
                    series: chart.series,
                    legend: {
                        y: 15
                    }
                });
            }

            function createWorkloadDynamic($container, filterData, jsonData) {
                var chart = jsonData[0], series = chart.series, ticks = [];

                if (series === undefined || series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {
                            height: 200
                        }
                    });
                    return;
                }

                _.each(series, function(s){ ticks = _.concat(ticks, s.data)});

                var xAxisFilterData = {
                    startDate: moment(_.minBy(ticks, 'x').x).startOf('day').toDate(),
                    endDate: moment(_.maxBy(ticks, 'x').x).startOf('day').toDate(),
                    timeUnitId: 3
                };
                var xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);

                xAxis.labels = {
                    formatter: function () {
                        var value = this.value, item = _.find(ticks, function(t){ return t.x == value;});
                        return "Нед. " + (item ? item.periodNum : "не определена");
                    }
                };

                series[2].yAxis = 1;

                $container.highcharts({
                    title: {text: null},
                    chart: {
                        animation: 0
                    },
                    xAxis: xAxis,
                    yAxis: [{
                        labels: {
                            format: '{value}'
                        },
                        title: {
                            text: 'Численность'
                        },
                        maxPadding: 0
                    }, {
                        title: {
                            text: 'Процент'
                        },
                        labels: {
                            format: '{value:.1f}%'
                        },
                        opposite: true
                    }],
                    tooltip: {
                        shared: true,
                        useHTML: true,
                        headerFormat: "",
                        formatter: function(){
                            var result = '<span style="font-size:10px">{0}</span><br>'.format(this.points[0].point.periodName);
                            _.each(this.points, function(p){
                                result += '<span style="color:{0}">\u25CF</span> {1}: <b>{2} {3}</b><br/>'
                                        .format(p.color, p.series.name, p.series.index == 2 ? p.y.toFixed(2) : p.y,
                                        p.series.index == 2 ?  "%" : ' ш.е.');
                            });
                            return result;
                        }
                    },
                    plotOptions: {
                        series: {
                            animation: {
                                duration: 200
                            }
                        },
                        area: {}
                    },
                    legend: {
                        reversed: true,
                        y: 15
                    },
                    series: chart.series
                });
            }

            function createTableWithDynamic($container, filterData, jsonData, customParams) {
                jsonData = jsonData || [];

                var calcDate = app.viewModel.getFilter("default.week").getSelectedOptions()[0].startDate;

                var gridData = _.filter(jsonData, function (x) {
                    return calcDate === x.calcDate;
                });

                if (!gridData || gridData.length === 0) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {
                            height: 100
                        }
                    });
                    return;
                }

                $container.kendoGrid({
                    dataSource: gridData,
                    height: customParams.height,
                    columns: [
                        {
                            field: "unitName",
                            title: "Конечный продукт"
                        },
                        {
                            field: "iepRate",
                            title: "Доля в общей р.ч.",
                            width: 200,
                            format: "{0:p1}"
                        }
                    ],
                    detailTemplate: '<div class="styled-chart-container">' +
                    '<div class="styled-chart-title small-title-font">Динамика количества и расчетной численности</div>' +
                    '<div class="chart-container"></div>' +
                    '</div>',
                    detailInit: detailInit
                });

                function detailInit(e) {
                    var detailRow = e.detailRow,
                            unitId = e.data.unitId,
                            $container = detailRow.find(".chart-container");


                    var filteredData = _.filter(jsonData, function (x) {
                        return x.unitId == unitId;
                    });

                    createEsuDynamic($container, filterData, filteredData);
                }
            }

            function createEsuDynamic($container, filterData, data) {

                if (data === undefined || data.length === 0) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {}
                    });
                    return;
                }

                var xAxisFilterData = {
                    startDate: moment(_.minBy(data, 'calcDate').calcDate).startOf('day').toDate(),
                    endDate: moment(_.maxBy(data, 'calcDate').calcDate).startOf('day').toDate(),
                    timeUnitId: 3
                };
                var xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);

                xAxis.labels = {
                    formatter: function () {
                        var value = moment(this.value).startOf('day').valueOf(), item = _.find(data, function(t){ return t.calcDate == value;});
                        return "Нед. " + (item ? item.timeUnitPrdNum : "не определена");
                    }
                };

                var countSeries = {
                    color: "#434348",
                    name: "Количество конечных продуктов",
                    yAxis: 1,
                    data: _.map(data, function (item) {
                        return {
                            x: item.calcDate,
                            y: item.iepCount,
                            periodName: item.timeUnitName
                        };
                    })
                };

                var countCalcSeries = {
                    color: "#7cb5ec",
                    yAxis: 0,
                    name: "Расчетная численность",
                    data: _.map(data, function (item) {
                        return {
                            x: item.calcDate,
                            y: item.staffCountCalc
                        };
                    })
                };

                $container.highcharts({
                    title: {text: null},
                    chart: {
                        type: 'area',
                        marginTop: 15
                    },
                    xAxis: xAxis,
                    yAxis: [{
                        labels: {
                            format: '{value}'
                        },
                        title: {
                            text: 'Расчетная численность'
                        },
                        maxPadding: 0
                    }, {
                        title: {
                            text: 'Количество'
                        },
                        labels: {
                            format: '{value}'
                        },
                        opposite: true
                    }],
                    tooltip: {
                        shared: true,
                        useHTML: true,
                        formatter: function(){
                            var result = '<span style="font-size:10px">{0}</span><br>'.format(this.points[0].point.periodName);
                            _.each(this.points, function(p){
                                result += '{0}: <b>{1}</b><br/>'.format(p.series.name, p.y);
                            });
                            return result;
                        }
                    },
                    plotOptions: {
                        area: {}
                    },
                    legend: {
                        reversed: true,
                        y: 15
                    },
                    series: [countSeries, countCalcSeries]
                });
            }

            function createDuodrTable($container, filterData, jsonData) {
                jsonData = jsonData || [];

                $container.kendoGrid({
                    dataSource: jsonData,
                    height: 600,
                    scrollable: true,
                    columns: [
                        {
                            field: "unitName",
                            title: "Отделы"
                        },
                        {
                            field: "staffCountFact",
                            title: "Ф.ч.",
                            width: 80,
                            format: "{0:n0}"
                        },
                        {
                            field: "nonSLAStaffCnt",
                            title: "в т.ч. не SLA",
                            width: 80,
                            format: "{0:n0}"
                        },
                        {
                            field: "workloadRate",
                            title: "Нагрузка",
                            width: 80,
                            format: "{0:p1}"
                        },
                        {
                            field: "vacancyCnt",
                            title: "Вакансии",
                            width: 80,
                            format: "{0:n0}"
                        },
                        {
                            field: "decretVacancyCnt",
                            title: "Декретные вакансии",
                            width: 90,
                            format: "{0:n0}"
                        },
                        {
                            field: "hfCriterionCnt",
                            title: "Критерий на ввод/вывод",
                            width: 180,
                            template: "# var iconCss = staffCountDeltaCnt > 0 ? 'icon-red-arrow-up' : 'icon-green-arrow-down' #" +
                            "<i class='#= iconCss # icon-small-arrow'></i> #= hfCriterionCnt #"
                        }
                    ],
                    detailTemplate: '<div class="chart">' +
                    '<div class="roller"></div>' +
                    '<div class="chart-container"></div>' +
                    '</div>',
                    detailInit: detailInit
                });

                function detailInit(e) {
                    var detailRow = e.detailRow,
                            $container = detailRow.find(".chart-container"),
                            $roller = detailRow.find(".roller");

                    var requestData = _.assign({}, filterData, {
                        divisionGroupId: e.data.divisionGroupId,
                        regionId: e.data.regionId
                    });

                    app.ajaxUtils.postData("WorkloadTableWithDynamic", JSON.stringify(requestData))
                            .done(function (ajaxResult) {
                                $roller.hide();

                                if (!ajaxResult.success) {
                                    $container.append("<div class=\"panel panel-danger\">" + ajaxResult.error + "</div>");
                                    return;
                                }

                                console.log(ajaxResult.queryList);

                                createTableWithDynamic($container, filterData, ajaxResult.data, {});
                            });
                }
            }

            function createObGauge($container, filterData, data, customParams) {
                if (data === undefined || data.value === undefined || data.value === null) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {}
                    });
                    return;
                }

                var value = (Math.round(data.value * 10000) / 100),
                        axisMax = Math.max(Math.ceil(value / 100) * 100, 200);

                $container.highcharts({
                    chart: {
                        type: 'gauge',
                        plotBackgroundColor: null,
                        plotBackgroundImage: null,
                        plotBorderWidth: 0,
                        plotShadow: false
                    },
                    title: {
                        text: null
                    },
                    pane: {
                        startAngle: -150,
                        endAngle: 150,
                        background: [{
                            backgroundColor: {
                                linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                                stops: [
                                    [0, '#FFF'],
                                    [1, '#333']
                                ]
                            },
                            borderWidth: 0,
                            outerRadius: '109%'
                        }, {
                            backgroundColor: {
                                linearGradient: {x1: 0, y1: 0, x2: 0, y2: 1},
                                stops: [
                                    [0, '#333'],
                                    [1, '#FFF']
                                ]
                            },
                            borderWidth: 1,
                            outerRadius: '107%'
                        }, {
                            // default background
                        }, {
                            backgroundColor: '#DDD',
                            borderWidth: 0,
                            outerRadius: '105%',
                            innerRadius: '103%'
                        }]
                    },
                    // the value axis
                    yAxis: {
                        min: 0,
                        max: axisMax,

                        minorTickInterval: 'auto',
                        minorTickWidth: 1,
                        minorTickLength: 10,
                        minorTickPosition: 'inside',
                        minorTickColor: '#666',

                        tickPixelInterval: 30,
                        tickWidth: 2,
                        tickPosition: 'inside',
                        tickLength: 10,
                        tickColor: '#666',
                        labels: {
                            step: 2,
                            rotation: 'auto'
                        },
                        title: {
                            text: '%'
                        },
                        plotBands: [{
                            from: 0,
                            to: 85,
                            color:  '#DDDF0D' // yellow
                        }, {
                            from: 85,
                            to: 115,
                            color: '#55BF3B' // green
                        }, {
                            from: 115,
                            to: 130,
                            color:  '#DDDF0D' // yellow
                        }, {
                            from: 130,
                            to: axisMax,
                            color: '#DF5353' // red
                        }]
                    },
                    plotOptions: {
                        gauge: {
                            dataLabels: {
                                enabled: true,
                                format: "{y}%",
                                style: {
                                    fontSize: '15px'
                                }
                            }
                        }
                    },
                    tooltip: {
                        enabled: false
                    },
                    series: [{
                        name: "Процент нагрузки",
                        data: [value],
                        tooltip: {
                            valueSuffix: ' %'
                        }
                    }]

                });
            }

            function createObCustomTable($container, filterData, data) {
                if (!data) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {}
                    });
                    return;
                }

                $container.append("<div data-bind=\"template: { name: 'obCustomTable-template'}\"></div>");
                ko.applyBindingsToDescendants(data, $container.get(0));
            }

            function createObPosTypeDynamic($container, filterData, data, customParams) {
                if (data === undefined
                        || data.length === 0
                        || data[0].series === undefined
                        || data[0].series.length === 0
                        || data[0].series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: customParams.title},
                        chart: {}
                    });
                    return;
                }

                var series = data[0].series, ticks = [];
                _.each(series, function(s){ ticks = _.concat(ticks, s.data)});

                var xAxisFilterData = {
                    startDate: moment(_.minBy(ticks, 'x').x).startOf('day').toDate(),
                    endDate: moment(_.maxBy(ticks, 'x').x).startOf('day').toDate(),
                    timeUnitId: 3
                };
                var xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);

                xAxis.labels = {
                    formatter: function () {
                        var value = this.value, item = _.find(ticks, function(t){ return t.x == value;});
                        return "Нед. " + (item ? item.periodNum : "не определена");
                    }
                };

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: customParams.title},
                    plotOptions: {
                        column: {
                            stacking: "normal",
                            dataLabels: {
                                enabled: xAxis.ticks.length < 10,
                                format: "{point.y}",
                                zIndex: 4
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        shared: true,
                        headerFormat: "",
                        formatter: function(){
                            var result = '<span style="font-size:10px">{0}</span><br>'.format(this.points[0].point.periodName);
                            _.each(this.points, function(p){
                                result += '<span style="color:{0}">\u25CF</span> {1}: <b>{2} ш.е.'.format(p.color, p.series.name, p.y);
                                result += p.percentage ? ", {0}%</b><br/>".format(p.percentage.toFixed(2)) : "</b><br/>"
                            });
                            return result;
                        }
                    },
                    xAxis: xAxis,
                    yAxis: {
                        endOnTick: false,
                        title: {text: ''},
                        labels: {
                            enabled: true,
                            format: "{value}"
                        },
                        stackLabels: {
                            enabled: xAxis.ticks.length < 10
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        y: 15
                    }
                });
            }

            function createObWLDynamic($container, filterData, data, customParams) {
                if (data === undefined || data.length === 0 || data[0].series === undefined || data[0].series.length === 0
                        || data[0].series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {}
                    });
                    return;
                }

                var series = data[0].series, ticks = [];
                _.each(series, function(s){ ticks = _.concat(ticks, s.data)});

                var xAxisFilterData = {
                            startDate: moment(_.minBy(ticks, 'x').x).startOf('day').toDate(),
                            endDate: moment(_.maxBy(ticks, 'x').x).startOf('day').toDate(),
                            timeUnitId: 3
                        };
                var xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);

                xAxis.labels = {
                    formatter: function () {
                        var value = this.value, item = _.find(ticks, function(t){ return t.x == value;});
                        return "Нед. " + (item ? item.periodNum : "не определена");
                    }
                };

                series[2].yAxis = 1;

                $container.highcharts({
                    title: {text: null},
                    chart: {},
                    xAxis: xAxis,
                    yAxis: [{
                        labels: {
                            format: '{value}'
                        },
                        title: {
                            text: 'Численность'
                        },
                        maxPadding: 0
                    }, {
                        title: {
                            text: 'Процент'
                        },
                        labels: {
                            format: '{value:.1f}%'
                        },
                        opposite: true
                    }],
                    tooltip: {
                        shared: true,
                        useHTML: true,
                        headerFormat: "",
                        formatter: function(){
                            var result = '<span style="font-size:10px">{0}</span><br>'.format(this.points[0].point.periodName);
                            _.each(this.points, function(p){
                                result += '<span style="color:{0}">\u25CF</span> {1}: <b>{2} {3}</b><br/>'
                                        .format(p.color, p.series.name, p.series.index == 2 ? p.y.toFixed(2) : p.y,
                                        p.series.index == 2 ?  "%" : ' ш.е.');
                            });
                            return result;
                        }
                    },
                    legend: {
                        reversed: true,
                        y: 15
                    },
                    series: series
                });
            }

            function createObTopNTable($container, filterData, data, customParams){
                data = data || {};

                var bestOnes = data.best || [], worstOnes = data.worst || [];

                if (bestOnes.length === 0 && worstOnes.length === 0) {
                    $container.highcharts({
                        title: {text: "ТОП-5 КП"},
                        chart: {
                            height: 100
                        }
                    });
                    return;
                }

                var viewModel = {
                    bestOnes: bestOnes,
                    worstOnes: worstOnes,
                    regions: _.chain(bestOnes).concat(worstOnes).groupBy("regionId").map(function(gr){
                        return {
                            regionId: gr[0].regionId,
                            regionName:gr[0].regionName
                        }
                    }).value()
                };

                viewModel.regions.unshift({
                    regionId: 0,
                    regionName: "Всего"
                });

                $container.append("<div data-bind=\"template: { name: 'obTopNTable-template'}\"></div>");
                ko.applyBindingsToDescendants(viewModel, $container.get(0));

                $container.find("#topNTableTabs a:first").tab('show');
            }

            app.init(config, function (viewModel) {
                // Раскрашиваем табы
                var subscription = viewModel.visibleCharts.subscribe(function (value) {
                    if (value) {
                        var tabLinks = $(".charts-container > tab-strip > .chart-tabs > ul > li > a");

                        $(tabLinks).each(function (index, el) {
                            index++;
                            $(el).addClass("tab-" + index).addClass("small-title-font");
                        });

                        subscription.dispose();
                    }
                });

                viewModel.groups.default.selectedDivision = ko.observable();
            });
        </script>

        <script type="text/html" id="obCustomTable-template">
            <div class="ob-custom-table-wrapper row highcharts-container">
                <div class="col-xs-12 ob-custom-table-item">
                    <div class="small-title-font">
                        Штатная численность
                    </div>
                    <div class="value">
                        <i class="icon-people"></i>
                        <span data-bind="text: staffCountFact" class="big-title-font"></span>
                    </div>
                </div>
                <div class="col-xs-12 ob-custom-table-item">
                    <div class="small-title-font">
                        Изменение штатной численности
                    </div>
                    <div class="value">
                        <i data-bind="if: staffCountDeltaCnt !== 0, css: staffCountDeltaCnt > 0 ? 'icon-red-arrow-up' : 'icon-green-arrow-down',
                        tooltip: {title: helpers.tooltips.staffCountDelta($data), html: true}"></i>
                        <span data-bind="text: staffCountDeltaCnt" class="big-title-font"></span>
                    </div>
                </div>
                <div class="col-xs-12 ob-custom-table-item" data-bind="tooltip: {title: helpers.tooltips.hfCriterion($data), html: true}">
                    <div class="small-title-font">
                        Критерий на ввод/вывод
                    </div>
                    <div class="value">
                        <i data-bind="if: hfCriterionCnt !== 0, css: hfCriterionCnt > 0 ? 'icon-red-arrow-up' : 'icon-green-arrow-down'"></i>
                        <span data-bind="text: hfCriterionCnt" class="big-title-font"></span>
                    </div>
                </div>
                <div class="col-xs-12 ob-custom-table-item">
                    <div class="small-title-font">
                        Дублирование
                    </div>
                    <div class="value">
                        <i class="icon-truck"></i>
                        <span data-bind="text: slaFactValueDoubleCnt" class="big-title-font"></span>
                    </div>
                </div>
            </div>
        </script>

        <script type="text/html" id="obTopNTable-template">
            <div class="chart-tabs">
                <ul class="nav nav-tabs" id="topNTableTabs" data-bind="foreach: regions">
                    <li>
                        <a data-bind="text: regionName, attr: { 'data-target': '#Tab_' + regionId}" data-toggle="tab"></a>
                    </li>
                </ul>
                <div class="tab-content" data-bind="foreach: { data: regions, as: 'regionItem' }">
                    <div class="tab-pane fade" data-bind="attr: { id: 'Tab_' + regionItem.regionId}">
                        <div class="row">
                            <div class="col-xs-6">
                                <table class="table" style="margin-bottom: 0px; margin-top: -20px;">
                                    <colgroup>
                                        <col style="width: 60%">
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th class="text-center">ТОП-5 КП</th>
                                            <th class="text-center">Прирост расчетной численности к предыдущей неделе, ш.е.</th>
                                        </tr>
                                    </thead>
                                    <tbody data-bind="foreach: { data: $root.bestOnes, as: 'dataItem' }">
                                        <tr data-bind="if: (dataItem.regionId == regionItem.regionId) || (regionItem.regionId == 0 && $index() < 5)">
                                            <td data-bind="text: dataItem.unitName"></td>
                                            <td class="text-center" data-bind="text: '+' + (dataItem.staffCountDeltaCnt / 1).toFixed(2) + ' ш.е.'"></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="col-xs-6">
                                <table class="table" style="margin-bottom: 0px; margin-top: -20px;">
                                    <colgroup>
                                        <col style="width: 60%">
                                    </colgroup>
                                    <thead>
                                    <tr>
                                        <th class="text-center">ТОП-5 КП</th>
                                        <th class="text-center">Уменьшение расчетной численности к предыдущей неделе, ш.е.</th>
                                    </tr>
                                    </thead>
                                    <tbody data-bind="foreach: { data: $root.worstOnes, as: 'dataItem' }">
                                    <tr data-bind="if: (dataItem.regionId == regionItem.regionId) || (regionItem.regionId == 0 && $index() < 5)">
                                        <td data-bind="text: dataItem.unitName"></td>
                                        <td class="text-center" data-bind="text: (dataItem.staffCountDeltaCnt / 1).toFixed(2) + ' ш.е.'"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'year'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'week'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'rpTypeId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'duodrReg'"></filter>
                </div>
                <div class="filter-element">
                    <refresh-button></refresh-button>
                </div>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <tab-strip params="name: 'showcases'">
                <tab>
                    <div class="row">
                        <div class="col-xs-4">
                            <chart params="name: 'obCustomTable'"></chart>
                        </div>
                        <div class="col-xs-4">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Процент нагрузки
                                </div>
                                <chart params="name: 'obGauge'"></chart>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Распределение ш.ч. по дирекциям
                                </div>
                                <chart params="name: 'obDirectionPie'"></chart>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Распределение ш.ч. по тер. размещению
                                </div>
                                <chart params="name: 'obTerritorialPie'"></chart>
                            </div>
                        </div>
                        <div class="col-xs-8">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Динамика нагрузки и фактической численности
                                </div>
                                <chart params="name: 'obWLDynamic'"></chart>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-4">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Распределение ш.ч. по типам позиции
                                </div>
                                <chart params="name: 'obPosTypePie'"></chart>
                            </div>
                        </div>
                        <div class="col-xs-8">
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Динамика ш.ч. ОБ по типам позиций
                                </div>
                                <chart params="name: 'obPosTypeDynamic'"></chart>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-xs-12">
                            <chart params="name: 'obTopNTable'"></chart>
                        </div>
                    </div>
                </tab>
                <tab>
                    <div class="row">
                        <div class="col-xs-4">
                            <chart params="name: 'divisionList'"></chart>
                        </div>
                        <div class="col-xs-8">
                            <!-- ko if: groups.rightSide.drillDownLevel() < 3 -->
                            <div data-bind="with: groups.default.selectedDivision">
                                <table class="styled-chart-container table">
                                    <thead>
                                    <tr>
                                        <th>Изменение ш.ч.</th>
                                        <th>Критерий на ввод/вывод</th>
                                        <th>Фактическая численность</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            <i data-bind="css: staffCountDeltaCnt > 0 ? 'icon-red-arrow-up' : 'icon-green-arrow-down',
                                            tooltip: {title: helpers.tooltips.staffCountDelta($data), html: true}"></i>
                                            <span data-bind="text: staffCountDeltaCnt"></span>
                                        </td>
                                        <td>
                                            <div data-bind="tooltip: {title: helpers.tooltips.hfCriterion($data), html: true}">
                                                <i data-bind="css: hfCriterionCnt > 0 ? 'icon-red-arrow-up' : 'icon-green-arrow-down'"></i>
                                                <span data-bind="text: hfCriterionCnt"></span>
                                            </div>
                                        </td>
                                        <td>
                                            <span data-bind="text: staffCountFact"></span>
                                            <span data-bind="if: slaFactValueDoubleCnt > 0, tooltip: {title: 'Дублирование: ' + slaFactValueDoubleCnt, html: true}" style="color: red; font-weight: bold;">!</span>
                                        </td>
                                    </tr>
                                    </tbody>
                                    <thead>
                                    <tr>
                                        <th>Декретные вакансии</th>
                                        <th>Текущие вакансии</th>
                                        <th>Численность не SLA</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td data-bind="text: decretVacancyCnt"></td>
                                        <td data-bind="text: vacancyCnt"></td>
                                        <td data-bind="text: nonSLAStaffCnt"></td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!-- /ko -->
                            <!-- ko if: groups.rightSide.drillDownLevel() == 1 -->
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="styled-chart-container">
                                        <div class="styled-chart-title small-title-font">
                                            Распределение ш.ч. по типам позиций
                                        </div>
                                        <chart params="name: 'posTypeDistribution', group: 'rightSide'"></chart>
                                    </div>
                                </div>
                                <div class="col-xs-6">
                                    <div class="styled-chart-container">
                                        <div class="styled-chart-title small-title-font">
                                            Распределение ш.ч. по тер. размещению
                                        </div>
                                        <chart params="name: 'territorialDistribution', group: 'rightSide'"></chart>
                                    </div>
                                </div>
                            </div>
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Динамика нагрузки и фактической численности
                                </div>
                                <chart params="name: 'workloadDynamic', group: 'rightSide'"></chart>
                            </div>
                            <!-- /ko -->

                            <!-- ko if: groups.rightSide.drillDownLevel() == 2 -->
                            <div class="styled-chart-container">
                                <div class="styled-chart-title small-title-font">
                                    Динамика нагрузки и фактической численности
                                </div>
                                <chart params="name: 'workloadDynamic2Level', group: 'rightSide'"></chart>
                            </div>

                            <div>
                                <chart params="name: 'tableWithDynamic2Level', group: 'rightSide'"></chart>
                            </div>
                            <!-- /ko -->

                            <!-- ko if: groups.rightSide.drillDownLevel() == 3 -->
                            <chart params="name: 'duodrTable', group: 'rightSide'"></chart>
                            <!-- /ko -->
                        </div>
                    </div>
                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>

