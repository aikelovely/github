<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="ru.alfabank.dmpr.repository.ob.ObQualityFilterRepository" %>
<%@ page import="ru.alfabank.dmpr.model.PeriodSelectOption" %>
<%@ page import="ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper" %>
<%@ page import="org.joda.time.LocalDate" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Показатели качества ОБ"/>

<%
    ObQualityFilterRepository obQualityFilterRepository = RequestContextUtils.getWebApplicationContext(request).getBean(ObQualityFilterRepository.class);
    PeriodSelectOption startDateDefaultOption = PeriodSelectHelper.getWeekByDate(LocalDate.now().minusWeeks(6), obQualityFilterRepository.getWeeks());
    PeriodSelectOption endDateDefaultOption = PeriodSelectHelper.getWeekByDate(LocalDate.now().minusWeeks(1), obQualityFilterRepository.getWeeks());

    if (startDateDefaultOption == null) {
        startDateDefaultOption = new PeriodSelectOption(1, 1, "", new LocalDate(), new LocalDate());
    }

    if (endDateDefaultOption == null) {
        endDateDefaultOption = new PeriodSelectOption(1, 1, "", new LocalDate(), new LocalDate());
    }

    request.setAttribute("startDateYear", new ObjectMapper().writeValueAsString(startDateDefaultOption.endDate.getYear()));
    request.setAttribute("startDateId", new ObjectMapper().writeValueAsString((startDateDefaultOption.id)));
    request.setAttribute("endDateYear", new ObjectMapper().writeValueAsString(endDateDefaultOption.endDate.getYear()));
    request.setAttribute("endDateId", new ObjectMapper().writeValueAsString((endDateDefaultOption.id)));
%>




<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .ob-section {
                background-color: #fff;
                border: 1px solid #e5e5e5;
                margin: 20px 0;
                padding-bottom: 5px;
                border-top-left-radius: 4px;
                border-top-right-radius: 4px;
                min-height: 574px;
            }

            .ob-section header {
                background-color: #d8d8d8;
                font-size: 17px;
                padding: 3px 7px 7px;
                margin-bottom: 1px;
                border-top-left-radius: 4px;
                border-top-right-radius: 4px;
                text-align: left;
                line-height: 32px;
                position: relative;
            }

            .ob-section filter {
                /*position: absolute;*/
                /*left: 288px;*/
                float: right;
            }


            @media (max-width: 1000px) {
                .ob-section filter .bootstrap-select {
                    width: 500px !important;
                }
            }

            @media (max-width: 850px) {
                .ob-section filter .bootstrap-select {
                    width: 400px !important;
                }
            }
/*тест*/
            .ob-section filter .filter {
                display: inline-block;
                margin-left: 10px;
                margin-right: 10px;
            }

            .ob-section filter .filter .dropdown-menu .inner .text {
                white-space: normal;
            }

            .ob-section .chart {
                margin-bottom: 0;
            }

            .modal-dialog {
                width: 1100px;
            }

            .label-cell {
                font-size: 13px;
                font-weight: bold;
                color: rgb(255, 255, 255);
                text-shadow: rgb(0, 0, 0) 0px 0px 6px, rgb(0, 0, 0) 0px 0px 3px;
            }

            .label-warning {
                background-color: #d9534f;
            }

            .label-success {
                background-color: #179433;
            }

            .label-unknown {
                background-color: #337AB7;
            }

            .ob-rating {
                padding-right: 0;
            }

            .ob-dynamic {
                padding-left: 0;
            }

            .modal .k-grid .k-grid-content {
                max-height: 480px;
            }

            .highcharts-container {
                -webkit-border-radius: 0;
                -moz-border-radius: 0;
                 border-radius: 0;
            }

            .k-grid-header th.k-header {
                vertical-align: top;
            }

            .dynamic-tooltip-table tr td:first-child{
                padding-right: 5px;
                text-align: right;
            }

            .dynamic-tooltip-table {
                font-size: 13px;
                font-weight: 600;
            }

            .k-grid-header th {
                font-weight: bold !important;
                font-size:   14px;
            }
        </style>
    </jsp:attribute>

<jsp:attribute name="js">
        <script>
           var map2 = window.app.getUrlVars();
           var periodType = window.app.getParamValue(map2,'periodType',3);
           var region = window.app.getParamValue(map2,'region',[1000650042,1000650015]);
           var directions = window.app.getParamValue(map2,'divisionGroupId',[]);
            var config = {
                groups: [{
                    name: "default",
                    filters: {
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип периода",
                            dataSource: {
                                url: "obQualityFilter/timeUnits"
                            },
                            defaultValue: periodType,
                            width: 150
                        },
                        regionIds: {
                            type: "Select",
                            multiple: true,
                            title: "Регион",
                            enableClear: true,
                            dataSource: {
                                url: "obQualityFilter/regions"
                            },
                            width: 230,
                            defaultValue: region,
                            onHide: function () {
                                app.viewModel.groups.default.showCharts();
                            }

                        },
                        directionIds: {
                            type: "Select",
                            multiple: true,
                            title: "Дирекция",
                            enableClear: true,
                            defaultValue: directions,
                            dataSource: {
                                url: "obQualityFilter/directions"
                            },
                            width: 210,
                            onHide: function () {
                                app.viewModel.groups.default.showCharts();
                            }
                        },
                        kpiKindId: {
                            type: "Select",
                            multiple: false,
                            title: "Группа показателей",
                            dataSource: {
                                url: "obQualityFilter/kpiKinds"
                            },
                            width: 160,
                            forceShowCharts: true
                        },
                        startYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: new Date(${startDateYear}, 0, 1),
                            notAfter: "endYear",
                            width: 90
                        },
                        startDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, с",
                            dataSource: {
                                url: "obQualityFilter/startDates",
                                params: [{name: "startYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            width: 265,
                            postInit: createStartDateIdSubscriptions
                            <%--defaultValue: ${startDateId}--%>
                        },
                        endYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: new Date(${endDateYear}, 0, 1),
                            notBefore: "startYear",
                            width: 90
                        },
                        endDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, по",
                            dataSource: {
                                url: "obQualityFilter/endDates",
                                params: [{name: "endYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            width: 265,
                            postInit: createEndDateIdSubscriptions
                            <%--defaultValue: ${endDateId}--%>
                        }
                    },
                    slaves: [{
                        name: "Directions",
                        charts: {
                            ratingTable: {
                                jsFunc: createRatingTable,
                                dataSource: "ObQualityRatingDirections",
                                customParams: {group: "Directions", unitCode: "directionIds"}
                            }
                        },
                        slaves: [{
                            name: "DirectionsDetails",
                            filters: {
                                kpiId: {
                                    type: "Select",
                                    multiple: false,
                                    title: "Показатель",
                                    enableSearch: true,
                                    optionsCaption: "Все",
                                    dataSource: {
                                        url: "obQualityFilter/KPIs",
                                        params: [
                                            {name: "kpiKindId", group: "default", required: true},
                                            {name: "directionIds", group: "default", required: false},
                                            {name: "regionIds", group: "default", required: false},
                                            {name: "startYear", group: "default", required: true},
                                            {name: "endYear", group: "default", required: true},
                                            {name: "startDateId", group: "default", required: true},
                                            {name: "endDateId", group: "default", required: true},
                                            {name: "timeUnitId", group: "default", required: true}
                                        ]
                                    },
                                    width: 650,
                                    onHide: function () {
                                        app.viewModel.groups.DirectionsDetails.showCharts();
                                    }
                                }
                            },
                            dontShowAfterMaster: true,
                            charts: {
                                ratingTable: {
                                    jsFunc: createRatingTable,
                                    dataSource: "ObQualityRatingRegions",
                                    customParams: {group: "DirectionsDetails", unitCode: "regionIds"}
                                },
                                dynamic: {
                                    jsFunc: createDynamic,
                                    dataSource: "ObQualityDynamicDirections",
                                    customParams: {group: "Directions"}
                                }
                            },
                            slaves: [{
                                name: "RegionsDetails",
                                dontShowAfterMaster: true,
                                charts: {
                                    dynamic: {
                                        jsFunc: createDynamic,
                                        dataSource: "ObQualityDynamicRegions",
                                        customParams: {group: "Regions"}
                                    }
                                }
                            }]
                        }]
                    }]
                },
                    {
                        name: "Details",
                        charts: {
                            detailsTable: {
                                jsFunc: createDetailsTable,
                                dataSource: "ObQualityDetailsTable"
                            }
                        }
                    }],
                cookies: false
            };

            app.init(config, function(viewModel){
                viewModel.firstRatingSlavesVisibility = ko.observable(true);
                viewModel.secondRatingSlavesVisibility = ko.observable(true);
            });

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

            function refreshGroup(groupName) {
                app.viewModel.groups[groupName].showCharts(false, false);
            }

            var tableHelper = {
                greenCss: "label-success",
                yellowCss: "label-warning",
                blueCss: "label-unknown",
                cellTemplate: "<span class='label label-cell {0}' style='font-size: 100%;'>{1}</span>",
                cellTemplateWithTooltip: "<span class='label label-cell {0}' data-toggle='tooltip' title='{1}' style='font-size: 100%;'>{2}</span>"
            };

            var tempStorage = {};
            function createRatingTable($container, filterData, jsonData, customParams) {
                function formatCellValue(dataItem) {
                    if (!dataItem || !($.type(dataItem.kpiRatioAvg) === "number")) return "";

                    var css = dataItem.kpiRatioAvg < dataItem.kpiNorm ? tableHelper.yellowCss : tableHelper.greenCss;
                    var text = (dataItem.kpiRatioAvg * 100).toFixed(2) + "%";

                    return tableHelper.cellTemplate.format(css, text);
                }

                function onChange() {
                    var item = this.dataItem(this.select());
                    var selectedFilterData = {};
                    selectedFilterData[customParams.unitCode] = [item.unitCode].filter(function(e){ return e;});

                    function refreshSlaves(){
                        tempStorage[customParams.unitCode] = selectedFilterData[customParams.unitCode];

                        app.viewModel.groups[customParams.group].slaves.forEach(function (gr) {
                            app.viewModel.groups[gr.name].drillDown(0, selectedFilterData);
                        });
                    }

                    // Если обновили параметр дирекций, нужно перегрузить фильтр показателей
                    if (customParams.group === "Directions" &&
                            !_.isEqual(selectedFilterData[customParams.unitCode].sort(), (
                            tempStorage[customParams.unitCode]
                            || filterData.directionIds
                            || []).sort())){
                            var filterGroup = app.viewModel.groups["DirectionsDetails"],
                                filterConfig = filterGroup.config.filters.kpiId,
                                filter = filterGroup.filters.kpiId;

                            filterGroup.getDataForFilter(filterConfig.title, filterConfig.dataSource.url,
                                    {
                                        startYear: moment(filterData.startYear).format('YYYY-MM-DD'),
                                        startDateId: filterData.startDateId,
                                        endYear: moment(filterData.endYear).format('YYYY-MM-DD'),
                                        endDateId: filterData.endDateId,
                                        kpiKindId: filterData.kpiKindId,
                                        directionIds: selectedFilterData[customParams.unitCode],
                                        timeUnitId: filterData.timeUnitId
                                    }, function(options){
                                        options.unshift({id: null, name: "Все"});
                                        filter.options(options);
                                    }).done(refreshSlaves);
                    } else {
                        refreshSlaves();
                    }
                }

                if (customParams.group === "Directions"){
                    if (!jsonData[0].bag.data.length) {
                        app.viewModel.firstRatingSlavesVisibility(false);
                    } else {
                        app.viewModel.firstRatingSlavesVisibility(true);
                    }
                } else {
                    if (!jsonData[0].bag.data.length) {
                        app.viewModel.secondRatingSlavesVisibility(false);
                    } else {
                        app.viewModel.secondRatingSlavesVisibility(true);
                    }
                }

                var gridElement = $container.kendoGrid({
                    dataSource: {
                        data: jsonData[0].bag.data
                    },
                    height: 260, //customParams.group === "Directions" ? 260 : 440,
                    sortable: false,
                    groupable: false,
                    scrollable: jsonData[0].bag.data.length > 5,
                    navigatable: false,
                    change: onChange,
                    selectable: true,
                    columns: [
                        {
                            field: "unitName",
                            title: customParams.group === "Directions" ? "Дирекция" : "Регион",
                            attributes: {
                                style: "font-weight: bold; font-size: 16px;"
                            }
                        }, {
                            field: "kpiRatioAvg",
                            title: "Ур. кач.",
                            template: function (dataItem) {
                                return formatCellValue(dataItem);
                            },
                            width: 85
                        }
                    ],
                    dataBound: function (e) {
                        var grid = $container.data("kendoGrid"),
                            tempValue = tempStorage[customParams.unitCode],
                            rowToSelect;

                        if (tempValue && tempValue.length) {
                            var index = _.findIndex(grid.dataItems(), function (item) {
                                return item.unitCode == tempValue[0];
                            });
                            if (index !== -1) {
                                rowToSelect = grid.tbody.find(">tr").eq(index);
                            }
                        }

                        if (rowToSelect === undefined) {
                            rowToSelect = grid.tbody.find("tr:first");
                        }
                        if (rowToSelect) {
                            setTimeout(function () {
                                grid.select(rowToSelect);
                            }, 50)
                        }
                    }
                });

                var kpiIdFilter = app.viewModel.getFilter("DirectionsDetails.kpiId");
                kpiIdFilter.value.subscribe(function(newValue){
                    var title = newValue == "Все" ? "Ур. кач." : "Факт";
                    $(gridElement).find("thead [data-field=kpiRatioAvg]").html(title)
                })
            }

            function createDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0], series = chart.series;
                var height = 262;//customParams.group === "Directions" ? 262 : 442;

                if (series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        chart: {
                            height: height,
                            marginTop: 20,
                            marginRight: 40
                        },
                        title: {text: ""}
                    });
                    return;
                }

                var xAxisFilterData = {
                    startDate: moment(_.minBy(series[0].data, function (e) {
                        return e.x;
                    }).x).startOf('day').toDate(),
                    endDate: moment(_.maxBy(series[0].data, function (e) {
                        return e.x;
                    }).x).startOf('day')
                            .add(filterData.timeUnitId == 3 ? {days: 1} : {}).toDate(),
                    timeUnitId: filterData.timeUnitId
                };

                var xAxis;
                if(filterData.timeUnitId == 3 && series[0].data){
                    xAxis = {
                        type: "category",
                        categories: app.chartUtils.getWeekCategoriesByPeriodNum(series[0].data),
                        categories: app.chartUtils.getWeekCategoriesByPeriodNum(series[1].data)
                    };
                } else {
                    xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);
                }

                xAxis.labels = {
                    style: {
                        fontWeight : "bold",
                        fontSize: "13px"
                    }
                };
                var plot =series[0].data[1];
                var normative = chart.bag.normative, plotLines = [],
                    minPoint = _.clone(_.minBy(series[0].data,function(p) { return p.y; })),
                    maxPoint = _.clone(_.maxBy(series[0].data,function(p) { return p.y; }));
                var minPoint2 =  _.chain(series).map(function(s) { return _.minBy(s.data, function(p) { return p.y; })})
                        .min(function(p) { return p.y; }).value().y;
                var maxPoint2 =  _.chain(series).map(function(s) { return _.maxBy(s.data, function(p) { return p.y; })})
                        .min(function(p) { return p.y; }).value().y;


                if (!plot) {
                    plotLines.push(
                            {
                                color: 'red',
                                label: {align: 'right', text: '<b>Цель</b>: {0}%'.format(normative.toFixed(2))},
                                value: normative,
                                width: 2,
                                zIndex: 5
                            }
                    );

                    minPoint.y = Math.min(minPoint.y, normative);
                    maxPoint.y = Math.max(maxPoint.y, normative);
                }

                function showDetailsTable(point) {
                    detailsModal.modal('toggle');
                    var detailsFilterData = _.assign(filterData, {
                        endYear: moment(point.dateTick).toDate(),
                        doudrFlag: customParams.group === "Regions" ? 'Y' : null,
                        title: point.periodName
                    });
                    app.viewModel.groups["Details"].drillDown(0, detailsFilterData);
                }

                $container.highcharts({
                    chart: {
                        type: "column",
                        height: height,
                        marginTop: 20,
                        marginRight: 40,
                        zoomType: 'x',
                        animation: false
                    },
                    title: {text: ""},
                    plotOptions: {
                        series: {
                            marker: {
                                enabled:false,

                            }
                        },
                        column: {
                            dataLabels: {
                                enabled: true,
                                formatter: function () { return this.point.y.toFixed(2); },
                                rotation: series[0].data.length >= 10 ? 270 : 0,
                                y: 35,
                                style: {
                                    color: '#FFFFFF',
                                    "fontSize": "13px",
                                    "fontWeight": "bold",
                                    "textShadow": "#000 0px 0px 6px, #000 0px 0px 3px"
                                }
                            },
                            cursor: 'pointer',
                            point: {
                                events: {
                                    click: function () {
                                        showDetailsTable(this);
                                    }
                                }
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        formatter: function() { return this.point.customHTMLTooltip; },
                        positioner: function(labelWidth, labelHeight, point){
                            var tooltipX, tooltipY;
                            if (point.plotX + labelWidth > this.chart.plotWidth) {
                                tooltipX = point.plotX + this.chart.plotLeft - labelWidth - 20;
                            } else {
                                tooltipX = point.plotX + this.chart.plotLeft + 20;
                            }
                            if (point.plotY + labelHeight > this.chart.plotHeight) {
                                tooltipY = this.chart.plotHeight - labelHeight + 20;
                            } else {
                                tooltipY = point.plotY + this.chart.plotTop - 20;
                            }
                            return {
                                x: tooltipX,
                                y: tooltipY
                            }
                        }
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: Math.max(Math.floor(minPoint2) - 2, 0),
                        max: Math.min(Math.floor(maxPoint2) + 2, 100),
                        title: {text: ''},
                        labels: {
                            format: '{value}%',
                            style: {
                                fontWeight : "bold",
                                fontSize: "13px"
                            }
                        },
                      plotLines: plotLines
                    },
                    series: series,
                    legend: {
                        enabled: false
                    }
                });
            }

            function createDetailsTable($container, filterData, jsonData, customParams) {
                var kpiId = app.viewModel.getFilterValue("DirectionsDetails.kpiId");

                function getCellClass(value, normative, item){
                    return value < normative ? tableHelper.yellowCss : tableHelper.greenCss;
                }

                function getNormativeClass(value, normative, item){
                    if (item.isRealNormativeNull) return tableHelper.blueCss;
                    return value < normative ? tableHelper.yellowCss : tableHelper.greenCss;
                }

                function formatCellValue(value, normative, item, cssGetter, tooltip) {
                    if (!($.type(value) === 'number')) return "";

                    var css = (cssGetter || getCellClass)(value, normative, item);
                    var text = (value * 100).toFixed(2) + "%";

                    return tooltip ? tableHelper.cellTemplateWithTooltip.format(css, tooltip, text) :tableHelper.cellTemplate.format(css, text);
                }

                function factTooltip(item){
                    if (kpiId !== "Все") {
                        return "Общее количество: " + item.totalCountAsString + "<br>Количество успешных: " + item.inKpiCountAsString;
                    }
                }

                function normativeTooltip(item){
                    if (item.isRealNormativeNull) return "Норматив задан не явно";
                }

                var columns = [{
                    field: "bpDivisionGroupName",
                    title: "Дирекция",
                    width: 85,
                    attributes: {
                        style: "font-weight: bold; font-size: 14px;"
                    }
                },
                    {
                        field: "bpOrgRegionName",
                        title: "Регион",
                        width: 140,
                        attributes: {
                            style: "font-weight: bold; font-size: 14px;"
                        }
                    },
                    {
                        field: "kpiName",
                        title: "Наименование KPI",
                        width: 225,
                        attributes: {
                            style: "font-weight: bold; font-size: 14px;"
                        }
                    },
                    {
                        field: "kpiRatioAvg",
                        title: "Факт",
                        template: function (dataItem) {
                            return formatCellValue(dataItem.kpiRatioAvg, dataItem.normative, dataItem, null, factTooltip(dataItem));
                        },
                        width: 87
                    },
                    {
                        field: "normative",
                        title: "Цель",
                        template: function (dataItem) {
                            return formatCellValue(dataItem.normative, dataItem.normative, dataItem, getNormativeClass, normativeTooltip(dataItem));
                        },
                        width: 87
                    }
                ];

                if (kpiId) {
                    columns = columns.concat([
                        {
                            field: "totalCountAsString",
                            title: "Общее количество",
                            width: 102,
                            attributes: {
                                style: "text-align: right"
                            }
                        },
                        {
                            field: "inKpiCountAsString",
                            title: "Количество успешных",
                            width: 102,
                            attributes: {
                                style: "text-align: right"
                            }
                        }
                    ]);
                } else {
                    columns = columns.concat([
                        {
                            field: "qualityLevel",
                            title: "Ур. кач.(отчетная неделя)",
                            template: function (dataItem) {
                                return formatCellValue(dataItem.qualityLevel, dataItem.qualityLevelNormative);
                            },
                            width: 87
                        },
                        {
                            field: "prevQualityLevel",
                            title: "Ур. кач.(пред. неделя)",
                            template: function (dataItem) {
                                return formatCellValue(dataItem.prevQualityLevel, dataItem.qualityLevelNormative);
                            },
                            width: 87
                        }
                    ]);
                }

                columns.push({
                    field: "description",
                    title: "Описание"
                });

                detailsModal.find('.modal-title').text(function(){
                    return 'Отчетный период : ' + filterData.title;
                });

                $container.kendoGrid({
                    dataSource: {
                        data: jsonData[0].bag.data,
                    },
                    sortable: true,
                    groupable: false,
                    selectable: false,
                    scrollable: true,
                    columns: columns
                });

                $container.find('[data-toggle="tooltip"]').tooltip({
                    html: true,
                    placement: 'left'
                });
            }

            var detailsModal = $("#detailsTableModal");

        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>
        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'startYear'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'startDateId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'endYear'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'endDateId'"></filter>
                </div>

            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'directionIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'regionIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'kpiKindId'"></filter>
                </div>
                <div class="filter-element">
                    <refresh-button></refresh-button>
                </div>
                <div class="filter-element" style="margin-top: 18px;">
                    <report-button params="reportUrl: 'ObReportDashboard', group: 'default'"></report-button>
                </div>
            </div>
            <filter-log></filter-log>
        </div>
        <div data-bind="visible: visibleCharts" class="charts-container">
            <div class="ob-section">
                <header>
                    <!-- ko text: groups.default.filters.kpiKindId.getSelectedOptions().length ?
                                  groups.default.filters.kpiKindId.getSelectedOptions()[0].name
                                   : "" -->
                    <!-- /ko -->
                    <filter params="name: 'kpiId', group: 'DirectionsDetails'">
                    </filter>
                </header>
                <div class="row chart">
                    <div class="col-xs-3 ob-rating">
                        <chart params="name: 'ratingTable', group: 'Directions'"></chart>
                        <div data-bind="visible: firstRatingSlavesVisibility">
                            <chart params="name: 'ratingTable', group: 'DirectionsDetails'"></chart>
                        </div>
                    </div>
                    <div class="col-xs-9 ob-dynamic">
                        <div data-bind="visible: firstRatingSlavesVisibility">
                            <chart params="name: 'dynamic', group: 'DirectionsDetails'"></chart>
                        </div>
                        <div data-bind="visible: firstRatingSlavesVisibility() && secondRatingSlavesVisibility()">
                            <chart params="name: 'dynamic', group: 'RegionsDetails'"></chart>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="detailsTableModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-body">
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                            <h4 class="modal-title"></h4>
                        </div>
                        <chart params="name: 'detailsTable', group: 'Details'"></chart>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>
