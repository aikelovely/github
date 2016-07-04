<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Количество конечных продуктов"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .chart-section header {
                background-color: #f3f3f4;
                border-style: solid;
                border-width: 1px 1px 0 1px;
                border-color: #dbdbde;
                font-size: 17px;
                padding: 3px 7px 7px;
                text-align: left;
                line-height: 32px;
                position: relative;
                min-height: 40px;
            }

            .is-manual-row:not(.k-state-selected) {
                background-color: #D9EDF7 !important;
            }

            .no-bottom-margin {
                margin-bottom: 0;
            }

            .highcharts-container {
                border-radius: 0;
            }

            .k-grid-header th {
                font-weight: bold !important;
                font-size: 14px;
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
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "nomFilter/timeUnits"
                            },
                            defaultValue: "4",
                            width: 125
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
                                url: "nomFilter/startDates",
                                params: [{name: "startYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            defaultValue: moment().subtract(2, 'month').month(),
                            width: 220
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
                                url: "nomFilter/endDates",
                                params: [{name: "endYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            defaultValue: moment().month(),
                            width: 220
                        }
                    },
                    charts: {
                        mainTable: {
                            jsFunc: createMainTable,
                            dataSource: "NomMainTable"
                        }
                    },
                    slaves: [
                        {
                            name: "dynamic",
                            charts: {
                                dynamic: {
                                    jsFunc: createDynamic,
                                    dataSource: "NomDynamic"
                                },
                                detailsTable: {
                                    jsFunc: createDetailsTable,
                                    dataSource: "NomDetailsTable"
                                }
                            },
                            dontShowAfterMaster: true
                        },
                        {
                            name: "manual",
                            filters: {
                                innerEndProducts: {
                                    type: "Select",
                                    multiple: true,
                                    enableSearch: true,
                                    enableClear: true,
                                    dataSource: {
                                        data: []
                                    },
                                    width: 379
                                },
                                viewMode: {
                                    type: "Select",
                                    multiple: false,
                                    dataSource: {
                                        data: [{id: 1, name: 'Подразделение / КП'}, {id: 2, name: 'КП / Подразделение'}]
                                    },
                                    width: 200
                                }
                            },
                            dontShowAfterMaster: true
                        }
                    ]
                }],
                cookies: true
            };

            var additionalFilterData = {divisionGroupIds: []},
                    detailGridData = [];

            function forAllRows(list, action) {
                var treeList = list || $($(event.srcElement).parents("[data-role='treelist']")[0]).data("kendoTreeList");
                var rows = $("tr.k-treelist-group", treeList.tbody);
                $.each(rows, function (ids, row) {
                    action(row, treeList);
                });
            }

            function expandAllRows(treeList) {
                forAllRows(treeList, function (row, list) {
                    list.expand(row);
                });
            }

            function collapseAllRows(treeList) {
                forAllRows(treeList, function (row, list) {
                    list.collapse(row);
                });
            }

            app.init(config, function (vm) {
                vm.dynamicVisibility = ko.observable(false);

                vm.groups.default.processing.subscribe(function (value) {
                    if (value) {
                        additionalFilterData = {divisionGroupIds: []};
                    }
                });

                vm.dataCache = ko.observableArray([]);

                var innerEndProductFilter = vm.getFilter("manual.innerEndProducts");
                vm.dataCache.subscribe(function (cachedData) {
                    var uniqData = _.chain(cachedData)
                            .uniqBy("id")
                            .map(function (x) {
                                return {
                                    id: x.id,
                                    name: x.innerEndProductName
                                }
                            })
                            .orderBy("name")
                            .value();

                    if (innerEndProductFilter) {
                        innerEndProductFilter.options(uniqData);
                    }
                });

                ko.computed(function () {
                    var viewMode = vm.groups.manual.filters.viewMode.value();
                    var innerEndProductIds = vm.groups.manual.filters.innerEndProducts.value();
                    var dataCache = vm.dataCache().slice(0);
                    var data = [];
                    var $treeList = $("#treeList");
                    var treeList = $treeList.data("kendoTreeList");
                    if (!$treeList || !treeList) return;

                    if (innerEndProductIds && innerEndProductIds.length) {
                        dataCache = _.filter(dataCache, function (x) {
                            return innerEndProductIds.indexOf(x.id + "") !== -1;
                        });
                    }

                    var $title = $treeList.find("th[data-field='innerEndProductName'] > a");
                    if (viewMode == "1") {
                        $title.text("Подразделение / КП");
                        data = dataCache;

                        _.each(_.groupBy(dataCache, 'divisionGroupId'),
                                function (group, divisionGroupId) {
                                    var firstItem = group[0];
                                    data.push({
                                        id: firstItem.parentId,
                                        divisionGroupId: firstItem.parentId,
                                        innerEndProductName: firstItem.divisionGroupName,
                                        factCount: null,
                                        parentId: null,
                                        level: 0
                                    });
                                });

                        treeList.dataSource.data(data);
                        if (innerEndProductIds && innerEndProductIds.length) {
                            setTimeout(function () {
                                expandAllRows(treeList);
                            }, 50);
                        }
                    } else {
                        $title.text("КП / Подразделение");
                        _.each(_.groupBy(dataCache, 'id'),
                                function (group, innerEndProductId) {
                                    var firstItem = group[0];
                                    data.push({
                                        id: firstItem.id,
                                        divisionGroupId: null,
                                        innerEndProductName: firstItem.innerEndProductName,
                                        factCount: _.sumBy(group, "factCount"),
                                        parentId: null,
                                        level: 0
                                    });

                                    _.each(group, function (g) {
                                        if (firstItem.id === g.divisionGroupId) return;

                                        data.push({
                                            id: g.divisionGroupId,
                                            divisionGroupId: g.divisionGroupId,
                                            innerEndProductName: g.divisionGroupName,
                                            factCount: g.factCount,
                                            parentId: firstItem.id,
                                            level: 1
                                        });
                                    });
                                });
                    }

                    treeList.dataSource.data(data);
                });
            });

            function onDataBound() {
                var dataView = this.dataSource.view(), element = this.element;
                _.forEach(dataView, function (row) {
                    if (row.isManual) {
                        element.find("tr[data-uid=" + row.uid + "]").addClass("is-manual-row").attr("title", "Ручные операции");
                    }
                });
            }

            function createMainTable($container, filterData, jsonData) {
                app.viewModel.dynamicVisibility(false);

                // чистка старого виджета, иначе при создании нового остается какой-то мусор и не работает иерархия
                var oldTreeList = $container.data("kendoTreeList");
                if (oldTreeList) {
                    oldTreeList.destroy();
                }

                var dataSource = new kendo.data.TreeListDataSource({
                    data: [],
                    schema: {
                        model: {
                            id: "id",
                            expanded: false
                        }
                    }
                });

                var $treeList = $container.kendoTreeList({
                    dataSource: dataSource,
                    height: 700,
                    dataBound: onDataBound,
                    sortable: true,
                    scrollable: true,
                    filterable: false,
                    pageable: false,
                    selectable: 'row',
                    change: function () {
                        var row = $(event.srcElement).closest("tr"),
                                dataItem = treeList.dataItem(row);

                        if (dataItem.parentId && app.viewModel.groups.manual.filters.viewMode.value() == "1") {
                            additionalFilterData = {
                                divisionGroupIds: [dataItem.parentId],
                                innerProductIds: [dataItem.id]
                            };

                            app.viewModel.dynamicVisibility(true);
                            app.viewModel.groups.dynamic.drillDown(0, additionalFilterData);
                        } else if (app.viewModel.groups.manual.filters.viewMode.value() == "2") {
                            if (dataItem.parentId) {
                                additionalFilterData = {
                                    divisionGroupIds: [dataItem.id],
                                    innerProductIds: [dataItem.parentId]
                                };
                            }
                            else {
                                additionalFilterData = {
                                    innerProductIds: [dataItem.id]
                                };
                            }

                            app.viewModel.dynamicVisibility(true);
                            app.viewModel.groups.dynamic.drillDown(0, additionalFilterData);
                        } else {
                            app.viewModel.dynamicVisibility(false);
                        }
                    },
                    columns: [
                        {
                            field: "innerEndProductName",
                            title: "Подразделение / КП",
                            expandable: true,
                            attributes: {
                                style: "font-weight: bold; font-size: 14px;"
                            },
                            template: "<div class='treelist-wrapnormal-cell' style='width:195px'>#=innerEndProductName#</div>"
                        },
                        {
                            field: "factCount",
                            title: "Кол-во",
                            width: 105,
                            format: "{0:n0}"
                        }
                    ]
                }), treeList = $treeList.data("kendoTreeList");

                jsonData = jsonData || [];

                var filteredData = jsonData.filter(function (i) {
                    return i.id && i.divisionGroupId;
                });

                app.viewModel.dataCache(filteredData);
            }

            function createDynamic($container, filterData, jsonData) {
                var chart = jsonData[0], series = chart.series, height = 260;

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

                var startDateOpt = app.viewModel.getFilter("default.startDateId").getSelectedOptions()[0];
                var endDateOpt = app.viewModel.getFilter("default.endDateId").getSelectedOptions()[0];

                var xAxisFilterData = {
                    startDate: new Date(startDateOpt.startDate),
                    endDate: new Date(endDateOpt.startDate),
                    timeUnitId: filterData.timeUnitId
                };

                var xAxis;
                if (filterData.timeUnitId == 3) {
                    xAxis = {
                        type: "category",
                        categories: app.chartUtils.getWeekCategoriesByPeriodNum(series[0].data)
                    };
                } else {
                    xAxis = app.chartUtils.createDateTimeXAxis(xAxisFilterData, false);
                }

                var roundToThousand = series[0].data.every(function (e) {
                            return !e.y || e.y > 1000;
                        }),
                        roundToMillions = series[0].data.every(function (e) {
                            return !e.y || e.y > 1000000;
                        });

                var averageValue = (_.sumBy(series[0].data, "y") / series[0].data.length).toFixed(2),
                        averageValueAsString = formatNumber(averageValue);

                $container.highcharts({
                    chart: {
                        type: "column",
                        height: height,
                        marginTop: 20,
                        marginRight: 40,
                        animation: false
                    },
                    title: {text: ""},
                    plotOptions: {
                        series: {
                            animation: {
                                duration: 300
                            }
                        },
                        column: {
                            dataLabels: {
                                enabled: true,
                                formatter: function () {
                                    if (roundToMillions) {
                                        return (this.point.y / 1000000).toFixed(2);
                                    }
                                    if (roundToThousand) {
                                        return (this.point.y / 1000).toFixed(2);
                                    }
                                    return formatNumber(this.point.y);
                                },
                                style: {
                                    color: '#FFFFFF',
                                    "fontSize": "13px",
                                    "fontWeight": "bold",
                                    "textShadow": "#000 0px 0px 6px, #000 0px 0px 3px"
                                }
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            formatter: function () {
                                if (roundToMillions) {
                                    return this.value / 1000000;
                                }
                                if (roundToThousand) {
                                    return this.value / 1000;
                                }
                                return this.value;
                            },
                            style: {
                                fontWeight: "bold",
                                fontSize: "13px"
                            }
                        },
                        plotLines: [{
                            color: 'grey',
                            label: {
                                align: 'left', text: '<b>Сред. зн.</b>: {0}'.format(
                                        function () {
                                            if (roundToMillions) {
                                                return (averageValue / 1000000).toFixed(2);
                                            }
                                            if (roundToThousand) {
                                                return (averageValue / 1000).toFixed(2);
                                            }
                                            return averageValueAsString;
                                        }())
                            },
                            value: averageValue,
                            width: 2,
                            zIndex: 10
                        }]
                    },
                    series: series,
                    legend: {
                        enabled: false
                    },
                    credits: {
                        enabled: roundToMillions || roundToThousand,
                        text: "Округлено до {0}".format(roundToMillions ? "миллионов" : "тысяч"),
                        href: "",
                        style: {
                            fontSize: '13px'
                        }
                    }
                });

                $container.parent().addClass("no-bottom-margin");
            }

            function createDetailsTable($container, filterData, jsonData) {
                jsonData = jsonData || [];

                var calcDate;
                if (filterData.timeUnitId == 3) {
                    var periodOpt = app.viewModel.getFilter("default.endDateId").getSelectedOptions()[0];

                    calcDate = periodOpt.startDate;
                } else {
                    calcDate = moment(filterData.endYear).month(parseInt(filterData.endDateId))
                            .startOf("month")
                            .valueOf();
                }



                if (!jsonData || jsonData.length === 0) {
                    $container.highcharts({
                        title: {text: null},
                        chart: {
                            height: 100
                        }
                    });
                    return;
                }

                var gridData = [];
                _.each(_.groupBy(jsonData, "id"), function(group, id){
                    var first = _.clone(group[0]);
                    first.factCount = _.sumBy(group, "factCount");
                    gridData.push(first);
                });

                $container.kendoGrid({
                    dataSource: gridData,
                    sortable: true,
                    height: 400,
                    columns: [
                        {
                            field: "innerEndProductName",
                            title: "Аналитический разрез"
                        },
                        {
                            field: "factCount",
                            title: "Кол-во",
                            width: 105,
                            format: "{0:n0}"
                        }
                    ],
                    detailTemplate: '<div class="chart-container"></div>',
                    detailInit: detailInit
                });

                function detailInit(e) {
                    var detailRow = e.detailRow,
                            id = e.data.id,
                            $container = detailRow.find(".chart-container");


                    var filteredData = _.sortBy(_.filter(jsonData, function (x) {
                        return x.id == id;
                    }), "calcDate");

                    var points = _.map(filteredData, function (item) {
                        return {
                            x: item.calcDate, y: item.factCount, periodNum: item.periodNum
                        }
                    });

                    var jsonDataForDynamic = [{
                        series: [{
                            name: "Количество",
                            data: points
                        }]
                    }];


                    createDynamic($container, filterData, jsonDataForDynamic);
                }
            }

            function formatNumber(num) {
                var str = num.toString().split('.');
                if (str[0].length >= 5) {
                    str[0] = str[0].replace(/(\d)(?=(\d{3})+$)/g, '$1 ');
                }
                if (str[1] && str[1].length >= 5) {
                    str[1] = str[1].replace(/(\d{3})/g, '$1 ');
                }
                return str.join('.');
            }
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
                <div class="filter-element">
                    <refresh-button></refresh-button>
                </div>
                <div class="filter-element" style="margin-top: 18px;">
                    <report-button params="reportUrl: 'NomReport', group: 'default'"></report-button>
                </div>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <div class="filter-row" style="margin: -10px 0 10px">
                <div class="filter-element">
                    <filter params="name: 'viewMode', group: 'manual'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'innerEndProducts', group: 'manual'"></filter>
                </div>
            </div>
            <div class="row chart">
                <div class="col-xs-4">
                    <chart params="name: 'mainTable', group: 'default'">
                        <div id="treeList" class="chart-container"></div>
                    </chart>
                </div>
                <div class="col-xs-8">
                    <div data-bind="visible: dynamicVisibility">
                        <chart params="name: 'dynamic', group: 'dynamic'"></chart>
                        <div>
                            <chart params="name: 'detailsTable', group: 'dynamic'"></chart>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>

