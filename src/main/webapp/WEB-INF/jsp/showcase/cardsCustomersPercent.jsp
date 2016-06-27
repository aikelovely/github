<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Доля клиентов, получивших карту в указанном отделении"/>

<t:layout title="${title}">

    <jsp:attribute name="css">
        <style scoped>
            .details-report-wrapper {
                margin-top: 20px;
                background: white;
                text-align: center;
                font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                font-size: 18px;
                color: #333333;
                padding: 5px;
            }

            .details-report-wrapper .btn {
                font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
            }

            .k-grid-content {
                min-height: 30px;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
            var defaultWidth = 170;

            var config = {
                groups: [{
                    filters: {
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            notAfter: "endDate"
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            notBefore: "startDate"
                        },
                        macroRegionIds: {
                            type: "Select",
                            multiple: true,
                            title: "Регион",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/macroRegions"
                            },
                            width: defaultWidth
                        },
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/citiesByMacroRegionIds",
                                params: ["macroRegionIds"]
                            },
                            width: defaultWidth,
                            enableSearch: true,
                            enableClear: true
                        },
                        branchTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Тип отделения",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/branchTypes"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        branchIds: {
                            type: "Select",
                            multiple: true,
                            title: "Отделение",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/branches",
                                params: ["macroRegionIds", "cityIds", "branchTypeIds"]
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        zpSignIds: {
                            type: "Select",
                            multiple: true,
                            title: "Признак ЗП",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/zpSigns"
                            },
                            width: defaultWidth
                        },
                        pyTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Тип ПУ",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/pyTypesByZPSignIds",
                                params: ["zpSignIds"]
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        cardCategoryIds: {
                            type: "Select",
                            multiple: true,
                            title: "Категория карты",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/cardCategories"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        debitOrCreditIds: {
                            type: "Select",
                            multiple: true,
                            title: "Дебет/Кредит",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/debitOrCredits"
                            },
                            width: defaultWidth
                        },
                        clientSegmentIds: {
                            type: "Select",
                            multiple: true,
                            title: "Сегмент клиента",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/clientSegments"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        reissueId: {
                            type: "Select",
                            multiple: false,
                            optionsCaption: "Все",
                            title: "Признак перевыпуска",
                            dataSource: {
                                data: [
                                    {id: 0, name: "Не перевыпущенные"},
                                    {id: 1, name: "Перевыпущенные"}
                                ]
                            },
                            width: defaultWidth
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/timeUnits"
                            },
                            width: defaultWidth - 8
                        },
                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица сети",
                            dataSource: {
                                url: "cardsCustomersPercentFilter/systemUnits"
                            },
                            width: defaultWidth - 8
                        },
                        paramType: {
                            type: "Select",
                            multiple: false,
                            title: "Отображение параметра",
                            dataSource: {
                                data: [
                                    {id: "Percent", name: "Процент в KPI"},
                                    {id: "Count", name: "Количество"}
                                ]
                            },
                            width: defaultWidth - 8
                        }
                    },
                    tabStrips: {
                        notPickedUp: {
                            tabs: ["Москва", "Регионы"],
                            dynamicConfig: {
                                jsFunc: configureTabs,
                                params: ["macroRegionIds"]
                            },
                            reportUrl: "CardsCustomersPercentNotPickedUpReport"
                        },
                        notReceived: {
                            tabs: ["Москва", "Регионы"],
                            dynamicConfig: {
                                jsFunc: configureTabs,
                                params: ["macroRegionIds"]
                            },
                            reportUrl: "CardsCustomersPercentNotReceivedReport"
                        }
                    },
                    charts: {
                        notPickedUpPieMoscow: {
                            jsFunc: createNotPickedUpPie,
                            dataSource: "CardsCustomersPercentNotPickedUpPie",
                            customParams: {macroRegionId: 1}
                        },
                        notPickedUpDynamicMoscow: {
                            jsFunc: createNotPickedUpDynamic,
                            dataSource: "CardsCustomersPercentNotPickedUpDynamic",
                            customParams: {macroRegionId: 1}
                        },
                        notPickedUpPieRegions: {
                            jsFunc: createNotPickedUpPie,
                            dataSource: "CardsCustomersPercentNotPickedUpPie",
                            customParams: {macroRegionId: 2}
                        },
                        notPickedUpDynamicRegions: {
                            jsFunc: createNotPickedUpDynamic,
                            dataSource: "CardsCustomersPercentNotPickedUpDynamic",
                            customParams: {macroRegionId: 2}
                        },
                        notReceivedPieMoscow: {
                            jsFunc: createNotReceivedPie,
                            dataSource: "CardsCustomersPercentNotReceivedPie",
                            customParams: {macroRegionId: 1}
                        },
                        notReceivedDynamicMoscow: {
                            jsFunc: createNotReceivedDynamic,
                            dataSource: "CardsCustomersPercentNotReceivedDynamic",
                            customParams: {macroRegionId: 1}
                        },
                        notReceivedPieRegions: {
                            jsFunc: createNotReceivedPie,
                            dataSource: "CardsCustomersPercentNotReceivedPie",
                            customParams: {macroRegionId: 2}
                        },
                        notReceivedDynamicRegions: {
                            jsFunc: createNotReceivedDynamic,
                            dataSource: "CardsCustomersPercentNotReceivedDynamic",
                            customParams: {macroRegionId: 2}
                        },
                        detailsTable: {
                            jsFunc: createDetailsTable,
                            dataSource: "CardsCustomersPercentDetailsTable",
                            reportUrl: "CardsCustomersPercentDetailsTableReport"
                        }
                    }
                }],
                cookies: true
            };

            function configureTabs(tabs, params) {
                var macroRegionIds = params.macroRegionIds || [];

                tabs[0].visible(_.emptyOrContains(macroRegionIds, "1"));
                tabs[1].visible(_.emptyOrContains(macroRegionIds, "2"));
            }

            function getPieConfiguration(title, series) {
                return {
                    chart: {
                        type: 'pie',
                        height: 350,
                        width: 357
                    },
                    legend: {
                        layout: 'horizontal',
                        verticalAlign: 'bottom'
                    },
                    plotOptions: {
                        pie: {
                            dataLabels: {
                                distance: -30,
                                format: "{percentage:.2f}%",
                                style: {fontWeight: 'bold', color: 'white', textShadow: '0px 1px 2px black'}
                            },
                            showInLegend: true,
                            size: 220
                        }
                    },
                    title: {text: title},
                    tooltip: {
                        headerFormat: "<b>{point.key}</b><br/>",
                        pointFormat: "{point.percentage:.2f}% ({point.y})",
                        useHTML: true
                    },
                    series: series
                };
            }

            function createNotPickedUpPie($container, filterData, jsonData, customParams) {
                var data = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });
                var series = data ? data.series : [];
                $container.highcharts(getPieConfiguration('% незабора карт из отделений', series));
            }

            function getDynamicConfiguration(title, normativeOptions, filterData, series) {

                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, true),
                        isPercentParam = filterData.paramType == "Percent",
                        valueSuffix = isPercentParam ? "%" : " ",
                        valueFormat = isPercentParam ? "{point.y:.2f}%" : "{point.y}",
                        yAxisMaxValue = isPercentParam && series[0] && series[0].data.some(function (el) {
                            return el.y > 80;
                        }) ? 100 : null;

                var plotLines = [];

                if (!!normativeOptions.value) {
                    plotLines.push({
                        value: normativeOptions.value,
                        color: '#FDA6A2',
                        width: 2,
                        zIndex: 10,
                        label: {
                            text: '<b>{0}</b>: {1}'.format(normativeOptions.title, normativeOptions.value + valueSuffix)
                        }
                    });
                }

                return {
                    chart: {
                        height: 350,
                        width: 780,
                        zoomType: 'x',
                        type: "column"
                    },
                    legend: {
                        enabled: false
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                rotation: xAxis.ticks.length < 10 ? 0 : 270,
                                x: xAxis.ticks.length < 10 ? 0 : 5,
                                format: valueFormat
                            },
                            tooltip: {
                                pointFormat: '<span style=\"color:{point.color}\">{series.name}</span> <b>' + valueFormat + '</b><br>'
                            }
                        }
                    },
                    tooltip: {
                        enabled: true,
                        useHTML: true,
                        crosshairs: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        labels: {
                            format: '{value}' + valueSuffix
                        },
                        max: yAxisMaxValue,
                        min: 0,
                        title: {text: ''},
                        allowDecimals: isPercentParam,
                        plotLines: plotLines
                    },
                    series: series
                };
            }

            function createNotPickedUpDynamic($container, filterData, jsonData, customParams) {
                var title = "Динамика по {0} карт, не забранных из отделений"
                        .format(filterData.paramType === "Count" ? "количеству" : "проценту");

                var data = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });

                var series = data ? data.series : [];
                var normativeOptions = {
                    title: "KPI",
                    value: data ? data.bag.normative : null
                };

                $container.highcharts(getDynamicConfiguration(title, normativeOptions, filterData, series));
            }

            function createNotReceivedPie($container, filterData, jsonData, customParams) {
                var data = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });
                var series = data ? data.series : [];
                $container.highcharts(getPieConfiguration('% клиентов, которые получили карту', series));
            }

            function createNotReceivedDynamic($container, filterData, jsonData, customParams) {
                var title = "Динамика по {0} клиентов, получивших карту в указанном"
                        .format(filterData.paramType === "Count" ? "количеству" : "проценту");

                var data = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });

                var series = data ? data.series : [];
                var normativeOptions = {
                    title: "Метрика",
                    value: data ? data.bag.normative : null
                };

                $container.highcharts(getDynamicConfiguration(title, normativeOptions, filterData, series));
            }

            function getTableConfiguration(systemUnit, jsonData, filterData, detailInit) {
                function formatCellValue(data) {
                    if (data === null) return "";
                    var value = data["unTakenCardPercent"];
                    if (value === null) return "";
                    var greenCss = "label-success", redCss = "label-danger",
                            css = (data["unTakenCardPercent"] >= data["unTakenCardKPI"] ? redCss : greenCss);
                    return "<span class='label label-cell " + css + "'>" + (value * 100).toFixed(2) + "%</span>";
                }

                var columns = [{
                    field: "unitName",
                    title: systemUnit.name,
                    width: 200
                },
                    {
                        field: "unTakenCardPercent",
                        title: "% незабора карт из отделений",
                        template: function (dataItem) {
                            return formatCellValue(dataItem);
                        }
                    },
                    {
                        field: "unTakenCardKPI",
                        title: "KPI",
                        format: "{0:p0}",
                        filterable: false,
                        width: 60
                    },
                    {
                        field: "unTakenCardDiviation",
                        title: "отклонение от KPI",
                        format: "{0:p2}"
                    },
                    {
                        field: "RCDOCardPercent",
                        title: "% клиентов, получивших карту в отделении",
                        format: "{0:p2}"
                    },
                    {
                        field: "RCDOCardKPI",
                        title: "KPI",
                        format: "{0:p0}",
                        width: 60,
                        filterable: false
                    },
                    {
                        field: "destructedCardCount",
                        title: "Количество карт со статусом уничтожено"
                    }];

                var transport = {
                    read: function (o) {
                        var request = app.ajaxUtils.postData("CardsCustomersPercentDetailsTable", JSON.stringify(filterData));
                        request.done(function (ajaxResult) {
                            if (ajaxResult.success) {
                                o.success(ajaxResult.data);
                                if (app.viewModel.showSQL()) {
                                    console.log("\n" + ajaxResult.queryList[0].sql);
                                }
                            } else {
                                app.showAlert(ajaxResult.message);
                            }
                        });
                    }
                };

                var dataSource = !!jsonData ? {data: jsonData, pageSize: 10} : {
                    transport: transport,
                    serverPaging: false,
                    serverSorting: false,
                    pageSize: 10
                };

                return {
                    dataSource: dataSource,
                    sortable: true,
                    pageable: {
                        pageSizes: true,
                        buttonCount: 5
                    },
                    filterable: true,
                    scrollable: true,
                    columns: columns,
                    detailInit: detailInit
                };
            }

            function createDetailsTable($container, filterData, jsonData, customParams) {
                function getSystemUnitByIdOrDefault(id) {
                    return _.find(app.viewModel.getFilter("systemUnitId").options(),
                                    function (el) {
                                        return el.id === id;
                                    }) || {id: null, name: "Единица сети"};
                }

                var systemUnit = getSystemUnitByIdOrDefault(parseInt(filterData.systemUnitId));

                $container.kendoGrid(getTableConfiguration(systemUnit, jsonData, filterData,
                        !!getSystemUnitByIdOrDefault(parseInt(filterData.systemUnitId) + 1).id ? function (e) {
                            return detailInit(e, filterData);
                        } : null));

                function detailInit(e, filterData) {
                    var systemUnit = getSystemUnitByIdOrDefault(parseInt(filterData.systemUnitId) + 1),
                            $container = $("<div/>").appendTo(e.detailCell),
                            detailsId = e.data.unitCode;

                    var extender = {
                        systemUnitId: systemUnit.id,
                        macroRegionIds: filterData.systemUnitId == 0 ? [detailsId] : filterData.macroRegionIds,
                        regionIds: filterData.systemUnitId == 1 ? [detailsId] : filterData.regionIds,
                        cityIds: filterData.systemUnitId == 2 ? [detailsId] : filterData.cityIds
                    };

                    var filterData = _.assign(_.clone(filterData), extender);

                    $container.kendoGrid(getTableConfiguration(systemUnit, null, filterData,
                            !!getSystemUnitByIdOrDefault(parseInt(filterData.systemUnitId) + 1).id ? function (e) {
                                return detailInit(e, filterData);
                            } : null));
                }
            }

            app.init(config);

        </script>
  </jsp:attribute>

    <jsp:body>

        <div class="showcase-title">${title}</div>

        <div class="section-filter-container clearfix">
            <div class="col-xs-2">
                <div class="filter-section">
                    <div class="filter-section-header">Период</div>
                    <div class="filter-element">
                        <filter params="name: 'startDate'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'endDate'"></filter>
                    </div>
                </div>
                <div style="position: absolute; width: 400px; top: 164px; left: 5px;">
                    <div class="filter-element" class="pull-left">
                        <reset-filter-button></reset-filter-button>
                    </div>
                    <div class="filter-element">
                        <refresh-button params="text: 'Выполнить'" class="pull-left"></refresh-button>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <div class="filter-section">
                    <div class="filter-section-header">Объект отчетности</div>
                    <div class="filter-element">
                        <filter params="name: 'macroRegionIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'cityIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'branchTypeIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'branchIds'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-4">
                <div class="filter-section">
                    <div class="filter-section-header">Условия</div>
                    <div class="filter-element">
                        <filter params="name: 'zpSignIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'pyTypeIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'cardCategoryIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'debitOrCreditIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'clientSegmentIds'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'reissueId'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-2">
                <div class="filter-section">
                    <div class="filter-section-header">Формат</div>
                    <div class="filter-element">
                        <filter params="name: 'timeUnitId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'systemUnitId'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'paramType'"></filter>
                    </div>
                </div>
            </div>
        </div>

        <filter-log></filter-log>

        <div data-bind="visible: visibleCharts" class="charts-container">

            <div class="chart">
                <tab-strip params="name: 'notPickedUp'">
                    <tab>
                        <div class="row">
                            <chart class="col-md-4" params="name: 'notPickedUpPieMoscow'"></chart>
                            <chart class="col-md-8" params="name: 'notPickedUpDynamicMoscow'"></chart>
                        </div>
                    </tab>
                    <tab>
                        <div class="row">
                            <chart class="col-md-4" params="name: 'notPickedUpPieRegions'"></chart>
                            <chart class="col-md-8" params="name: 'notPickedUpDynamicRegions'"></chart>
                        </div>
                    </tab>
                </tab-strip>
            </div>

            <div class="chart chart-inner details-report-wrapper">
                Детализированный отчет по не забранным картам
                <report-button params="reportUrl: 'CardsCustomersPercentNotPickedUpDetailsReport',
                style: {'margin-top': '1px'}"></report-button>
                <csv-report-button params="reportUrl: 'CardsCustomersPercentNotPickedUpDetailsCsvReport',
                style: {'margin-top': '1px'}"></csv-report-button>
            </div>

            <div class="chart chart-inner details-report-wrapper">
                Детализированный отчет по потерянным картам
                <report-button params="reportUrl: 'CardsCustomersPercentLostCardsPercentReport',
                style: {'margin-top': '1px'}"></report-button>
                <csv-report-button params="reportUrl: 'CardsCustomersPercentLostCardsPercentCsvReport',
                style: {'margin-top': '1px'}"></csv-report-button>
            </div>

            <div class="chart">
                <tab-strip params="name: 'notReceived'">
                    <tab>
                        <div class="row">
                            <chart class="col-md-4" params="name: 'notReceivedPieMoscow'"></chart>
                            <chart class="col-md-8" params="name: 'notReceivedDynamicMoscow'"></chart>
                        </div>
                    </tab>
                    <tab>
                        <div class="row">
                            <chart class="col-md-4" params="name: 'notReceivedPieRegions'"></chart>
                            <chart class="col-md-8" params="name: 'notReceivedDynamicRegions'"></chart>
                        </div>
                    </tab>
                </tab-strip>
            </div>

            <div class="chart">
                <chart params="name: 'detailsTable'"></chart>
            </div>
        </div>
    </jsp:body>
</t:layout>
