<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="UnitCost"/>

<t:layout title="${title}">

    <jsp:attribute name="js">
        <script>
            Highcharts.setOptions({
                colors: [
                    "#337ab7",
                    "#91e8e1",
                    "#90ed7d",
                    "#f7a35c",
                    "#8085e9",
                    "#f15c80",
                    "#e4d354",
                    "#5be8e0",
                    "#8d4653",
                    "#434348",
                    "#4B966E",
                    "#D46A6A"
                ],
                plotOptions: {
                    column: {
                        dataLabels: {
                            style: {
                                color: '#FFFFFF',
                                "fontSize": "13px",
                                "fontWeight": "bold",
                                "textShadow": "#000 0px 0px 6px, #000 0px 0px 3px"
                            }
                        }
                    },
                    line: {
                        dataLabels: {
                            style: {
                                color: '#FFFFFF',
                                "fontSize": "13px",
                                "fontWeight": "bold",
                                "textShadow": "#000 0px 0px 6px, #000 0px 0px 3px"
                            }
                        }
                    }
                }
            });

            var secondTabCharts = {
                unitCost: {
                    jsFunc: createUnitCostDynamic,
                    dataSource: "UnitCostDynamic"
                },
                totalCostAndProducts: {
                    jsFunc: createTotalCostAndProductsDynamic,
                    dataSource: "UnitCostDynamic"
                }
            };

            var config = {
                groups: [{
                    filters: {
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            datepickerOptions: {
                                minViewMode: 1
                            },
                            lastDayOfMonth: true,
                            notAfter: "endDate",
                            defaultValue: moment().add(-1, "month").date(0).toDate()
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            datepickerOptions: {
                                minViewMode: 1
                            },
                            lastDayOfMonth: true,
                            notBefore: "startDate"
                        },
                        bgOrgRegionId: {
                            type: "Select",
                            multiple: false,
                            enableClear: true,
                            title: "ЦО/РОБ",
                            dataSource: {
                                url: "unitCostFilter/UCBpOrgRegions"
                            },
                            width: 250
                        },
                        currencyId: {
                            type: "Select",
                            multiple: false,
                            title: "Валюта",
                            dataSource: {
                                url: "unitCostFilter/UCCurrencies"
                            },
                            width: 100
                        }
                    },
                    charts: {},
                    tabStrips: {
                        showcases: {
                            tabs: [
                                {title: "Анализ расходов ОБ"},
                                {title: "Динамика изменения стоимости Unit Cost"}
                            ]
                        }
                    },
                    slaves: [{
                        name: "tab1",
                        filters: {
                            calcTypeId: {
                                type: "Select",
                                multiple: false,
                                title: "Тип расчета",
                                dataSource: {
                                    url: "unitCostFilter/UCCalcTypes"
                                },
                                defaultValue: "4",
                                width: 125
                            },
                            directionId: {
                                type: "Select",
                                title: "Дирекция",
                                multiple: false,
                                optionsCaption: "Все",
                                allowClear: true,
                                dataSource: {
                                    url: "unitCostFilter/UCDirections",
                                    params: ["endDate"]
                                },
                                disableIfComputed: {
                                    jsFunc: function (context) {
                                        if(app.viewModel) {
                                            var filter = app.viewModel.getFilter("bgOrgRegionId");
                                            if (filter) {
                                                var orgRegion = _.find(filter.options(), function (option) {
                                                    return option.code == "ЦО";
                                                });
                                                if (orgRegion) {
                                                    return context.params.bgOrgRegionId != orgRegion.id;
                                                }
                                            }
                                        }
                                        return true;
                                    },
                                    params: ["bgOrgRegionId"]
                                },
                                width: 250
                            }
                        },
                        charts: {
                            distribution: {
                                jsFunc: createDistributionDynamic,
                                dataSource: "UnitCostTabDistribution",
                                customParams: {titleFormat: "Распределение расходов {0}, тыс. {1}"}
                            },
                            costDistribution: {
                                jsFunc: createDistributionDynamic,
                                dataSource: "UnitCostTabCostDistribution",
                                customParams: {titleFormat: "Распределение расходов UnitCost {0}, тыс. {1}"}
                            },
                            indexChangeDynamic: {
                                jsFunc: createIndexChangeDynamic,
                                dataSource: "UnitCostTabIndexChangeDynamic"
                            },
                            fotDistribution: {
                                jsFunc: createFotDistribution,
                                dataSource: "UnitCostTabFotDistribution"
                            }
                        }
                    }, {
                        name: "tab2_left",
                        filters: {
                            startDate: {
                                type: "DatePicker",
                                title: "Период, с",
                                datepickerOptions: {
                                    minViewMode: 1
                                },
                                lastDayOfMonth: true,
                                notAfter: "tab2_left.endDate",
                                defaultValue: moment().add(-1, "month").date(0).toDate()
                            },
                            endDate: {
                                type: "DatePicker",
                                title: "Период, по",
                                datepickerOptions: {
                                    minViewMode: 1
                                },
                                lastDayOfMonth: true,
                                notBefore: "tab2_left.startDate"
                            },
                            directionId: {
                                type: "Select",
                                multiple: false,
                                title: "Дирекция",
                                dataSource: {
                                    url: "unitCostFilter/UCDirections",
                                    params: [{name: "endDate", group: "tab2_left", required: true}]
                                },
                                width: 250
                            },
                            innerEndProductId: {
                                type: "Select",
                                multiple: false,
                                title: "Конечный продукт",
                                dataSource: {
                                    url: "unitCostFilter/UCInnerEndProducts",
                                    params: [{name: "endDate", group: "tab2_left",  required: true},
                                        {name: "directionId", group: "tab2_left", required: true}]
                                },
                                width: 250,
                                validationRules: [{type: "Required"}]
                            }
                        },
                        charts: secondTabCharts
                    }, {
                        name: "tab2_right",
                        filters: {
                            startDate: {
                                type: "DatePicker",
                                title: "Период, с",
                                datepickerOptions: {
                                    minViewMode: 1
                                },
                                lastDayOfMonth: true,
                                notAfter: "tab2_right.endDate",
                                defaultValue: moment().add(-1, "month").date(0).toDate()
                            },
                            endDate: {
                                type: "DatePicker",
                                title: "Период, по",
                                datepickerOptions: {
                                    minViewMode: 1
                                },
                                lastDayOfMonth: true,
                                notBefore: "tab2_right.startDate"
                            },
                            directionId: {
                                type: "Select",
                                multiple: false,
                                title: "Дирекция",
                                dataSource: {
                                    url: "unitCostFilter/UCDirections",
                                    params: [{name: "endDate", group: "tab2_right", required: true}]
                                },
                                width: 250
                            },
                            innerEndProductId: {
                                type: "Select",
                                multiple: false,
                                title: "Конечный продукт",
                                dataSource: {
                                    url: "unitCostFilter/UCInnerEndProducts",
                                    params: [
                                        {name: "endDate", group: "tab2_right", required: true},
                                        {name: "directionId", group: "tab2_right", required: true}]
                                },
                                width: 250,
                                validationRules: [{type: "Required"}]
                            }
                        },
                        charts: secondTabCharts
                    }]
                }],
                cookies: true
            };

            function createDistributionDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        series = chart.series;

                filterData.timeUnitId = 4;
                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false),
                        periodText = app.chartUtils.getPeriodText(filterData.startDate, filterData.endDate);

                var currencyText = filterData.currencyId == "1" ? "руб." : "$";
                var title = customParams.titleFormat.format(periodText, currencyText);

                if (series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
                        }
                    });

                    return;
                }

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            stacking: "normal",
                            showInLegend: true,
                            dataLabels: {
                                enabled: true,
                                format: "{y:,.0f}"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.percentage:.2f}%</b>' +
                        '<br/>Рубли: {point.totalRurSum:,.0f} P<br/>Доллары: {point.totalUsdSum:,.0f} $'
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        title: {text: ''}
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        align: "right",
                        verticalAlign: "middle",
                        layout: "vertical",
                        useHTML: true
                    }
                })
            }

            function createIndexChangeDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        series = chart.series;

                var periodText = app.chartUtils.getPeriodText(filterData.startDate, filterData.endDate),
                        title = "Динамика изменения стоимости UnitCost {0}".format(periodText);

                if (series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {height: 100}
                    });
                    return;
                }

                filterData.timeUnitId = 4;
                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                series = series.reverse();

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: title},
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        pointFormat: "<b>{point.y:,.2f}%</b>"
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                format: "{point.y:,.2f}%",
                                inside: true
                            }
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: series
                });
            }

            function createFotDistribution($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        series = chart.series;


                var currencyText = filterData.currencyId == "1" ? "руб." : "$";
                var title = "Распределение ФОТ по категории расхода, тыс. {0}".format(currencyText);

                var categories = [];
                _.forEach(series[0].data, function (point) {
                    categories.push(point.name);
                });

                $container.highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        min: 0,
                        max: categories.length - 1,
                        categories: categories
                    },
                    yAxis: {
                        min: 0,
                        title: {text: ""}
                    },
                    tooltip: {
                        pointFormat: "Рубли: {point.totalRurSum:,.0f} P" +
                        "<br/>Доллары: {point.totalUsdSum:,.0f} $"
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                format: "{y:,.0f}"
                            }
                        }
                    },
                    legend: {enabled: false},
                    series: series
                });
            }

            function createUnitCostDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        series = chart.series;

                filterData.timeUnitId = 4;
                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                var currencyText = filterData.currencyId == "1" ? "руб." : "$";
                var title = "Динамика UnitCost, {0}".format(currencyText);

                if (series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
                        }
                    });

                    return;
                }

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: true,
                            dataLabels: {
                                enabled: true,
                                inside: true,
                                format: "{y:,.0f}"
                            }
                        },
                        line: {
                            dataLabels: {
                                enabled: true,
                                format: "{y:,.0f}"
                            },
                            zIndex: 5
                        }
                    },
                    tooltip: {
                        useHTML: true,
                        shared: true,
                        pointFormat: "<span style='color: {series.color}'>● </span><span>{series.name}</span>:" +
                        " <b>{point.y:.2f} {0}</b><br/>".format(currencyText)
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        title: {text: ''},
                        labels: {
                            format: '{value}'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true
                    }
                })
            }

            function createTotalCostAndProductsDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[1],
                        series = chart.series;

                filterData.timeUnitId = 4;
                var xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                var currencyText = filterData.currencyId == "1" ? "руб." : "$";
                var title = "Динамика Количество КП/TotalCost";

                if (series.length === 0 || series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
                        }
                    });

                    return;
                }

                _.each(series, function (s) {
                    if (s.type === "line") {
                        s.yAxis = 1
                    } else {
                        s.yAxis = 0
                    }
                });

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            showInLegend: true,
                            dataLabels: {
                                enabled: true,
                                inside: true,
                                format: "{y:,.0f}"
                            },
                            tooltip: {
                                pointFormat: "<span style='color: {series.color}'>● </span><span>{series.name}</span>:" +
                                " <b>{point.y:,.2f}</b><br/>"
                            }
                        },
                        line: {
                            dataLabels: {
                                enabled: true,
                                format: "{y:,.0f}"
                            },
                            tooltip: {
                                pointFormat: "<span style='color: {series.color}'>● </span><span>{series.name}</span>:" +
                                " <b>{point.y:,.0f}</b><br/>"
                            },
                            zIndex: 5
                        }
                    },
                    tooltip: {
                        shared: true,
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: [{
                        title: {text: ''},
                        labels: {
                            format: '{value}'
                        },
                        min: 0
                    }, {
                        title: {text: ''},
                        labels: {
                            format: '{value}'
                        },
                        opposite: true,
                        min: 0
                    }],
                    series: series,
                    legend: {
                        enabled: true
                    }
                })
            }

            app.init(config, function(viewModel){
                var defaultGroup = viewModel.getGroup("default"),
                        tab2LeftGroup = viewModel.getGroup("tab2_left"),
                        tab2RightGroup = viewModel.getGroup("tab2_right");

                defaultGroup.processing.subscribe(function(value){
                    if(!value) return;
                    tab2LeftGroup.filters.startDate.value(defaultGroup.filters.startDate.value());
                    tab2RightGroup.filters.startDate.value(defaultGroup.filters.startDate.value());

                    tab2LeftGroup.filters.endDate.value(defaultGroup.filters.endDate.value());
                    tab2RightGroup.filters.endDate.value(defaultGroup.filters.endDate.value());
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-container">
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'startDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'endDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'bgOrgRegionId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'currencyId'"></filter>
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
                    <div class="filter-container">
                        <div class="filter-row">
                            <div class="filter-element">
                                <filter params="name: 'calcTypeId', group: 'tab1'"></filter>
                            </div>
                            <div class="filter-element">
                                <filter params="name: 'directionId', group: 'tab1'"></filter>
                            </div>
                            <div class="filter-element">
                                <refresh-button params="group: 'tab1'"></refresh-button>
                            </div>
                        </div>

                        <filter-log params="group: 'tab1'"></filter-log>
                    </div>
                    <div class="charts-container">
                        <chart params="name: 'distribution', group: 'tab1'"></chart>
                        <chart params="name: 'costDistribution', group: 'tab1'"></chart>
                        <chart params="name: 'indexChangeDynamic', group: 'tab1'"></chart>
                        <chart params="name: 'fotDistribution', group: 'tab1'"></chart>
                    </div>
                </tab>
                <tab>
                    <div class="row">
                        <div class="col-xs-6">
                            <div class="filter-container">
                                <div class="filter-row">
                                    <div class="filter-element">
                                        <filter params="name: 'startDate', group: 'tab2_left'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <filter params="name: 'endDate', group: 'tab2_left'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <filter params="name: 'directionId', group: 'tab2_left'"></filter>
                                    </div>
                                </div>
                                <div class="filter-row">
                                    <div class="filter-element">
                                        <filter params="name: 'innerEndProductId', group: 'tab2_left'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <refresh-button params="group: 'tab2_left'"></refresh-button>
                                    </div>
                                </div>

                                <filter-log params="group: 'tab2_left'"></filter-log>
                            </div>
                            <div data-bind="visible: groups.tab2_left.visibleCharts" class="charts-container">
                                <chart params="name: 'unitCost', group: 'tab2_left'"></chart>
                                <chart params="name: 'totalCostAndProducts', group: 'tab2_left'"></chart>
                            </div>
                        </div>
                        <div class="col-xs-6">
                            <div class="filter-container">
                                <div class="filter-row">
                                    <div class="filter-element">
                                        <filter params="name: 'startDate', group: 'tab2_right'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <filter params="name: 'endDate', group: 'tab2_right'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <filter params="name: 'directionId', group: 'tab2_right'"></filter>
                                    </div>
                                </div>
                                <div class="filter-row">
                                    <div class="filter-element">
                                        <filter params="name: 'innerEndProductId', group: 'tab2_right'"></filter>
                                    </div>
                                    <div class="filter-element">
                                        <refresh-button params="group: 'tab2_right'"></refresh-button>
                                    </div>
                                </div>

                                <filter-log params="group: 'tab2_right'"></filter-log>
                            </div>
                            <div data-bind="visible: groups.tab2_right.visibleCharts" class="charts-container">
                                <chart params="name: 'unitCost', group: 'tab2_right'"></chart>
                                <chart params="name: 'totalCostAndProducts', group: 'tab2_right'"></chart>
                            </div>
                        </div>
                    </div>
                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>