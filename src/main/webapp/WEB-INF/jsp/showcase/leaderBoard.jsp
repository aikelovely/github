<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="КПЭ ОБ"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            @media (max-width: 900px) {
                .custom-chart-container {
                    top:-60px;
                }
            }
            .mb-0 > .chart {
                margin-bottom: 0;
            }

            .chart-tabs > .tab-content > .tab-pane {
                padding: 10px 4px;
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
                background-color: rgba(141, 179, 150, 0.75) !important;
            }

            .active .tab-2 {
                background-color: #179433 !important;
            }

            .tab-3 {
                background-color: rgba(117, 168, 194, 0.75) !important;
            }

            .active .tab-3 {
                background-color: #249AD6 !important;
            }

            .tab-4 {
                background-color: rgba(186, 172, 189, 0.75) !important;
            }

            .active .tab-4 {
                background-color: #9E32B7 !important;
            }

            .tab-5 {
                background-color: rgba(187, 191, 135, 0.75) !important;
            }

            .active .tab-5 {
                background-color: #DEC53E !important;
            }

            hr {
                border-color: #B0B0B0;
            }

            .col-xs-1, .col-sm-1, .col-md-1, .col-lg-1, .col-xs-2, .col-sm-2, .col-md-2, .col-lg-2,
            .col-xs-3, .col-sm-3, .col-md-3, .col-lg-3, .col-xs-4, .col-sm-4, .col-md-4, .col-lg-4,
            .col-xs-5, .col-sm-5, .col-md-5, .col-lg-5, .col-xs-6, .col-sm-6, .col-md-6, .col-lg-6,
            .col-xs-7, .col-sm-7, .col-md-7, .col-lg-7, .col-xs-8, .col-sm-8, .col-md-8, .col-lg-8,
            .col-xs-9, .col-sm-9, .col-md-9, .col-lg-9, .col-xs-10, .col-sm-10, .col-md-10, .col-lg-10,
            .col-xs-11, .col-sm-11, .col-md-11, .col-lg-11, .col-xs-12, .col-sm-12, .col-md-12, .col-lg-12 {
                padding-right: 5px;
                padding-left: 5px;
            }

            .row {
                margin-right: 0;
                margin-left: 0;
            }

            .chart {
                margin-bottom: 12px;
            }

            .chart-part-1 {
                float: left;
                width: 792px;
            }

            .chart-part-2 {
                float: left;
                width: 370px;
                height: 400px;
                padding: 50px 20px;
                font-size: 17px;
                background-color: #FFF;
                border: 1px solid rgba(0, 0, 0, 0.15);
                border-left-width: 0;
            }

            .charts-container {
                position: relative;
            }

            .custom-chart-container {
                position: relative;
            }

            .custom-chart-container .chart {
                position: absolute;
                bottom: -75px;
                right: 5px;
                font-size: 18px;
                font-weight: bold;
                text-align: right;
            }

            .custom-chart-container .panel-danger {
                width: 250px;
                max-height: 65px;
                overflow: hidden;
            }
        </style>
    </jsp:attribute>

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
                    },
                    pie: {
                        dataLabels: {
                            style: {
                                "fontSize": "14px",
                                "fontWeight": "bold"
                            }
                        }
                    }
                }
            });

            var color = {
                prevValue: "#91e8e1",
                currentValue: "#337ab7",
                planValue: "#f7a35c",
                normativeValue: "#b20000"
            };

            var config = {
                forceShowCharts: true,
                groups: [{
                    name: "default",
                    filters: {
                        startDate: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: moment().startOf("year").toDate()
                        },
                        divisionGroupId: {
                            type: "Select",
                            multiple: false,
                            title: "Группа подразделений",
                            dataSource: {
                                url: "leaderBoardFilter/DivisionGroups",
                                params: ["default.startDate"]
                            },
                            defaultValue: 800022430857, // Операционный Блок
                            width: 220
                        }
                    },
                    tabStrips: {
                        showcases: {
                            tabs: [
                                "Сотрудники",
                                "Клиенты",
                                "Процессы и технологии",
                                "Эффективность",
                                "Риски"
                            ]
                        }
                    },
                    charts: {
                        placeholder1: {
                            jsFunc: createChartPlaceholder,
                            dataSource: "LeaderBoardPlaceholderChart",
                            customParams: {code: "DSHSCOBEMPLOYEE"}
                        },
                        placeholder2: {
                            jsFunc: createChartPlaceholder,
                            dataSource: "LeaderBoardPlaceholderChart",
                            customParams: {code: "DSHSCCLIENT"}
                        },
                        placeholder5: {
                            jsFunc: createChartPlaceholder,
                            dataSource: "LeaderBoardPlaceholderChart",
                            customParams: {code: "DSHSCRISK"}
                        },
                        placeholder3: {
                            jsFunc: createChartPlaceholder,
                            dataSource: "LeaderBoardPlaceholderChart",
                            customParams: {code: "DSHSCPROC"}
                        },
                        placeholder4: {
                            jsFunc: createChartPlaceholder,
                            dataSource: "LeaderBoardPlaceholderChart",
                            customParams: {code: "DSHSCEFF"}
                        },
                        kpi5Chart: {
                            jsFunc: createKpi5Chart,
                            dataSource: "LeaderBoardKpi5Chart"
                        },
                        kpi8_1Pie: {
                            jsFunc: createKpi8Pie,
                            dataSource: "LeaderBoardKpi8PieChart",
                            customParams: {kpiCode: "KPIOB~8~1"}
                        },
                        kpi8_2Pie: {
                            jsFunc: createKpi8Pie,
                            dataSource: "LeaderBoardKpi8PieChart",
                            customParams: {kpiCode: "KPIOB~8~2"}
                        },
                        kpi15ByMonthChart: {
                            jsFunc: createKpi15_16ByMonthChart,
                            dataSource: "LeaderBoardKpi15ByMonthChart"
                        },
                        kpi15ByYearChart: {
                            jsFunc: createKpi15ByYearChart,
                            dataSource: "LeaderBoardKpi15ByYearChart"
                        },
                        kpi16Chart: {
                            jsFunc: createKpi15_16ByMonthChart,
                            dataSource: "LeaderBoardKpi16Chart"
                        },
                        kpi18Chart: {
                            jsFunc: createKpi18Chart,
                            dataSource: "LeaderBoardKpi18Chart"
                        },
                        kpi19Chart: {
                            jsFunc: createQuarterDynamicChartDeprecated,
                            dataSource: "LeaderBoardKpi19Chart",
                            customParams: {addDivisionToTitle: true, valueSuffix: "%"}
                        },
                        kpi21Chart: {
                            jsFunc: createQuarterDynamicChartDeprecated,
                            dataSource: "LeaderBoardKpi21Chart",
                            customParams: {valueSuffix: ""}
                        },
                        kpi22Chart: {
                            jsFunc: createQuarterDynamicChartDeprecated,
                            dataSource: "LeaderBoardKpi22Chart",
                            customParams: {valueSuffix: "%", smallLabels: true}
                        },
                        kpi23_24Chart: {
                            jsFunc: createKpi23_24Chart,
                            dataSource: "LeaderBoardKpi23_24Chart"
                        }
                    },
                    slaves: [{
                        name: "withQuarter",
                        filters: {
                            quarter: {
                                type: "Select",
                                multiple: false,
                                title: "Квартал",
                                dataSource: {
                                    data: [
                                        {id: 1, name: "1 квартал"},
                                        {id: 2, name: "2 квартал"},
                                        {id: 3, name: "3 квартал"},
                                        {id: 4, name: "4 квартал"}
                                    ]
                                },
                                forceShowCharts: true,
                                defaultValue: moment().quarter(),
                                width: 120
                            }
                        },
                        charts: {
                            kpi9PrevYearChart: {
                                jsFunc: createKpi9Chart,
                                dataSource: "LeaderBoardKpi9Chart",
                                customParams: {isPrevYear: true}
                            },
                            kpi9CurrentYearChart: {
                                jsFunc: createKpi9Chart,
                                dataSource: "LeaderBoardKpi9Chart",
                                customParams: {isPrevYear: false}
                            },
                            kpi10PrevYearChart: {
                                jsFunc: createKpi10Chart,
                                dataSource: "LeaderBoardKpi10Chart",
                                customParams: {
                                    isPrevYear: true,
                                    messagesContainer: "#kpi10PrevYearChartMessages"
                                }
                            },
                            kpi10CurrentYearChart: {
                                jsFunc: createKpi10Chart,
                                dataSource: "LeaderBoardKpi10Chart",
                                customParams: {
                                    isPrevYear: false,
                                    messagesContainer: "#kpi10CurrentYearChartMessages"
                                }
                            }
                        }
                    }, {
                        name: "kpi14",
                        filters: {
                            kpi14Value: {
                                type: "Select",
                                multiple: false,
                                title: "Затраты",
                                dataSource: {
                                    data: [
                                        {id: "1", name: "Более $500K"},
                                        {id: "2", name: "Все"}
                                    ]
                                },
                                defaultValue: "1",
                                width: 145,
                                forceShowCharts: true
                            }
                        },
                        charts: {
                            kpi14Chart: {
                                jsFunc: createQuarterDynamicChartDeprecated,
                                dataSource: "LeaderBoardKpi14Chart",
                                customParams: {valueSuffix: "%", smallLabels: true}
                            }
                        }
                    }]
                }, {
                    name: "kpi8",
                    filters: {
                        startDate: {
                            type: "DatePicker",
                            title: "Месяц",
                            datepickerOptions: {
                                minViewMode: 1
                            },
                            defaultValue: moment().date(0).toDate(),
                            lastDayOfMonth: true,
                            forceShowCharts: true
                        }
                    },
                    charts: {
                        kpi8_1Dynamic: {
                            jsFunc: createKpi8Dynamic,
                            dataSource: "LeaderBoardKpi8DynamicChart",
                            customParams: {kpiCode: "KPIOB~8~1"}
                        },
                        kpi8_2Dynamic: {
                            jsFunc: createKpi8Dynamic,
                            dataSource: "LeaderBoardKpi8DynamicChart",
                            customParams: {kpiCode: "KPIOB~8~2"}
                        }
                    }
                }]
            };

            function isSeriesFullOfZeros(series) {
                return !_.some(series, function (s) {
                    return _.some(s.data, function (p) {
                        return p && p.y;
                    })
                });
            }

            function createChartPlaceholder($container, filterData, jsonData, customParams, group) {
                if (!jsonData || jsonData.length === 0) return;

                jsonData = _.filter(jsonData, function (c) {
                    return c.placeholderCode === customParams.code;
                });

                if (!jsonData || jsonData.length === 0) return;

                var groups = _.groupBy(jsonData, function (c) {
                    return c.groupId;
                });

                var chartNames = [];

                _.each(groups, function (chartsMetaData, groupId) {
                    chartsMetaData = _.sortBy(chartsMetaData, function (c) {
                        return c.rowIndex;
                    });

                    var rowIds = _.chain(chartsMetaData)
                            .map(function (c) {
                                return c.rowIndex;
                            })
                            .uniq()
                            .value();

                    var first = chartsMetaData[0];
                    var columnCount = first.columnCount || 1;
                    var colClass = "col-xs-" + 12 / columnCount;

                    _.each(rowIds, function (rowIndex) {
                        var $row = $("<div />", {
                            class: "row"
                        });

                        for (i = 1; i <= columnCount; i++) {
                            var $col = $("<div />", {
                                class: colClass
                            });

                            var seriesByChart = _.filter(chartsMetaData, function (s) {
                                return s.rowIndex === rowIndex && s.columnIndex === i;
                            });

                            if (seriesByChart && seriesByChart.length) {
                                var first = seriesByChart[0];

                                $col.attr("class", "col-xs-" + first.width * 12 / columnCount);

                                var groupId = seriesByChart[0].groupId;
                                var chartName = "blockChart_{0}".format(first.chartId);

                                $("<chart />", {
                                    params: "name: '{0}', group: '{1}'".format(chartName, group.name)
                                }).appendTo($col);

                                group.config.charts[chartName] = {
                                    jsFunc: createBlockChart,
                                    dataSource: "LeaderBoardBlockChart",
                                    customParams: seriesByChart,
                                    additionalParams: {
                                        kpiIdForBlockChart: seriesByChart[0].kpiId,
                                        dateIntervalType: seriesByChart[0].intervalType
                                    }
                                };

                                chartNames.push(chartName);
                            }

                            $col.appendTo($row);
                        }

                        $row.appendTo($container);
                    })
                });


                // Отрисовываем компоненты
                ko.applyBindingsToDescendants(app.viewModel, $container[0]);

                // Отрисовываем графики
                setTimeout(function () {
                    _.forEach(chartNames, function (name) {
                        group.showSingleChart(name);
                    });

                    // Удаляем только что созданные графики из группы, чтобы при следующем обновлении не выполнялось
                    // лишней работы.
                    setTimeout(function () {
                        group.charts = _.remove(group.charts, function (c) {
                            return chartNames.indexOf(c.name) === -1;
                        });
                    }, 100);
                }, 100);
            }

            var seriesCode = {
                currentValue: "FACT",
                planValue: "PLAN",
                prevValue: "PFACT"
            };

            var chartTypeCodes = {
                PFPFH: "bar",
                PFPFV: "column",
                PFAREA: "areaspline"
            };

            function getValueBySeriesCode(kpiDataItem, code, metaData) {
                var value;
                switch (code) {
                    case seriesCode.currentValue:
                        value = kpiDataItem.currentValue;
                        break;
                    case seriesCode.planValue:
                        value = kpiDataItem.planValue;

                        if (!value && chartTypeCodes[metaData.widgetType] === 'areaspline' && metaData.unitCode === 'PRC') {
                            value = 1;
                        }

                        break;
                    case seriesCode.prevValue:
                        value = kpiDataItem.prevValue;
                        break;
                }

                if (value && metaData.unitCode == "PRC") {
                    value *= 100;
                }

                return value;
            }

            // Создает серию "Факт"
            function createFactSeries(metaData) {
                var color = metaData.fontColor || "white";
                var style = {};
                if (color.toUpperCase() === "BLACK") {
                    style.color = "#000000";
                    style.textShadow = "0 0 6px #ffffff, 0 0 3px #ffffff";
                } else {
                    style.color = "#ffffff";
                    style.textShadow = "0 0 6px #000000, 0 0 3px #000000";
                }

                var chartType = getChartTypeByWidgetType(metaData.widgetType);

                var seriesConfig = {
                    type: chartType,
                    name: metaData.seriesName || "Факт",
                    color: metaData.seriesColor || color.currentValue,
                    zIndex: 1,
                    dataLabels: {
                        inside: true,
                        enabled: metaData.dataLabelPosition !== "N",
                        formatter: function () {
                            if (metaData.unitCode == "PRC") {
                                return this.y < 4 ? null : (this.y.toFixed(metaData.dataLabelPrecision) + "%");
                            }
                            return this.y < 0.05 ? null : (this.y.toFixed(metaData.dataLabelPrecision));
                        },
                        style: style,
                        rotation: metaData.dataLabelPosition === "H" ? 0 : 270
                    },
                    pointPlacement: 0,
                    groupPadding: 0,
                    pointPadding: getPointPadding(metaData.barWidth, 40)
                };

                if (chartType === 'areaspline') {
                    _.assign(seriesConfig,
                            {
                                zIndex: 2,
                                marker: {
                                    lineWidth: 3,
                                    lineColor: "white",
                                    radius: 6,
                                    symbol: 'circle'
                                },
                                index: 1
                            });
                }
                return seriesConfig;
            }

            // Создает серию "Факт за предыдущий год"
            function createPrevFactSeries(metaData) {
                return {
                    type: metaData.widgetType == "PFPFV" ? "column" : "bar",
                    name: metaData.seriesName || "Факт за предыдущий год",
                    color: metaData.seriesColor || color.prevValue,
                    zIndex: 1,
                    pointPlacement: 0,
                    pointPadding: getPointPadding(metaData.barWidth, 85),
                    groupPadding: 0
                };
            }

            // Создает серию "План"
            function createPlanSeries(metaData) {
                var dataPrecision = metaData.dataPrecision || 2;

                var color = metaData.fontColor || "black";
                var style = {};
                if (color.toUpperCase() === "WHITE") {
                    style.color = "#ffffff";
                    style.textShadow = "0 0 6px #000000, 0 0 3px #000000";

                } else {
                    style.color = "#000000";
                    style.textShadow = "0 0 6px #ffffff, 0 0 3px #ffffff";
                }

                var chartType = getChartTypeByWidgetType(metaData.widgetType);

                var seriesConfig = {
                    type: chartType === 'areaspline' ? 'areaspline' : "line",
                    name: metaData.seriesName || "План",
                    color: metaData.seriesColor || color.planValue,
                    zIndex: 2,
                    dataLabels: {
                        inside: true,
                        enabled: metaData.dataLabelPosition !== "N",
                        formatter: function () {
                            if (metaData.unitCode == "PRC") {
                                return this.y < 4 ? null : (this.y.toFixed(dataPrecision) + "%");
                            }
                            return this.y < 0.05 ? null : (this.y.toFixed(dataPrecision));
                        },
                        rotation: metaData.dataLabelPosition === "H" ? 0 : 270,
                        marker: {enabled: false},
                        style: style
                    }
                };

                if (chartType === 'areaspline') {
                    _.assign(seriesConfig,
                            {
                                zIndex: 1,
                                marker: {
                                    lineWidth: 3,
                                    lineColor: "white",
                                    radius: 6,
                                    symbol: 'circle'
                                },
                                index: 0
                            });
                }

                return seriesConfig;
            }

            function findMetaDataByCode(metaDataArray, seriesCode) {
                return _.find(metaDataArray, function (x) {
                    return x.seriesCode === seriesCode;
                })
            }

            function getPointPadding(barWidth, defaultValue) {
                var width = barWidth === null ? defaultValue : barWidth;
                return 0.5 - (width / 200);
            }

            function getChartTypeByWidgetType(widgetType) {
                return chartTypeCodes[widgetType] || "bar";
            }

            function createBlockChart($container, filterData, jsonData, seriesMetaData) {
                var chartMeta = seriesMetaData[0];
                var selectedOption = app.viewModel.getFilter("divisionGroupId").getSelectedOptions()[0];
                if (!selectedOption) {
                    selectedOption = app.viewModel.getFilter("divisionGroupId").options()[0];

                }
                if (chartMeta.chartName == 'UC по конечным продуктам операционного блока в разрезе Дирекций.') {

                    $container.parent().find(".chart-url").html("" +
                    "<a href=${pageContext.request.contextPath}/showcase/unitCost?divisionGroupId=" + selectedOption.id + " >" +
                    "На витрину UnitCost" +
//                        selectedOption.name +
                    "</a>");
                }

                if (chartMeta.chartName == 'Динамика изменения CTQ' ||
                        chartMeta.chartName == 'Динамика изменения CQI' ||
                        chartMeta.chartName == 'Уровень безошибочности') {

                    $container.parent().find(".chart-url").html("" +
                            "<a href=${pageContext.request.contextPath}/showcase/obQuality?divisionGroupId=" +
                            selectedOption.id + "&periodType=4" +
                            "  >" +
                            "На витрину -> Показатели качества ОБ" +
//                        selectedOption.name +
                            "</a>"
                    )
                    ;
                }


                var config = {
                    chart: {
                        height: chartMeta.rowHeight
                    },
                    title: {text: chartMeta.showName ? chartMeta.chartName : ""}
                };

                var isEmpty = jsonData === undefined
                        || jsonData.length === 0;

                // Если данных нет
                if (isEmpty) {
                    if (!chartMeta.hideIfEmpty) {
                        $container.parent().parent().addClass("mb-0");
                        $container.empty();
                        $container.parent().find(".chart-url").empty();
                    }
                    else {
                        $container.highcharts(config);
                    }
                    return;
                }
                else {
                    $container.parent().parent().removeClass("mb-0");
                }

                config.series = [];

                function addSeriesIfExists(code) {
                    var metaData = findMetaDataByCode(seriesMetaData, code);
                    if (metaData) {
                        var series;
                        switch (code) {
                            case seriesCode.currentValue:
                                series = createFactSeries(metaData);
                                break;
                            case seriesCode.planValue:
                                series = createPlanSeries(metaData);
                                break;
                            case seriesCode.prevValue:
                                series = createPrevFactSeries(metaData);
                                break;
                        }

                        series.data = _.map(jsonData.map(function (kpiDataItem) {
                            return getValueBySeriesCode(kpiDataItem, code, metaData);
                        }));

                        config.series.push(series);
                    }
                }

                var dataPrecision = chartMeta.dataLabelPrecision;
                var valueSuffix = chartMeta.unitCode == "PRC" ? "%" : "";
                var firstRow = jsonData[0];

                // Тип виджета
                config.chart.type = getChartTypeByWidgetType(chartMeta.widgetType);

                config.xAxis = {
                    categories: [],
                    crosshair: true
                };

                config.yAxis = {
                    title: {text: ""},
                    labels: {
                        format: '{value}' + valueSuffix
                    }
                };

                config.tooltip = {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.' + dataPrecision + 'f}' + valueSuffix + '</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                };

                config.plotOptions = {
                    column: {
                        grouping: false,
                        shadow: false,
                        borderWidth: 0
                    },
                    bar: {
                        grouping: false,
                        shadow: false,
                        borderWidth: 0
                    },
                    area: {}
                };
                if (chartMeta.chartName == "Соблюдение лимитов наличных денежных средств в ККО") {
                    config.yAxis.plotBands = [{from: -1000000, to: 100, color: 'rgba(0, 255, 0, 0.8)'}, {
                        from: 100,
                        to: 300000,
                        color: 'rgba(255, 0, 0, 0.8)'
                    }];
                }
                ;
                var currentYear = moment(filterData.startDate).year();
                switch (chartMeta.intervalType) {
                    case "Y":
                        // Динамика по годам за 2 года
                        var currentValue = getValueBySeriesCode(firstRow, seriesCode.currentValue, chartMeta),
                                prevValue = getValueBySeriesCode(firstRow, seriesCode.prevValue, chartMeta),
                                planValue = getValueBySeriesCode(firstRow, seriesCode.planValue, chartMeta);

                        config.xAxis.categories = [currentYear];
                        config.legend = {enabled: false};

                        if (config.yAxis.min == undefined) {
                            config.yAxis.min = Math.max(Math.min(currentValue, prevValue, planValue) - 5, 0);
                        }

//                        config.chart.marginTop = 60;
//                        config.chart.marginBottom = 30;

                        var series;
                        var metaData = findMetaDataByCode(seriesMetaData, seriesCode.prevValue);
                        if (metaData) {
                            series = createPrevFactSeries(metaData);
                            series.data = [prevValue];

                            var barWidth = metaData.barWidth === undefined ? 100 : metaData.barWidth;
                            series.pointPadding = getPointPadding(metaData.barWidth, 100);
                            config.series.push(series);
                        }

                        metaData = findMetaDataByCode(seriesMetaData, seriesCode.currentValue);
                        if (metaData) {
                            series = createFactSeries(metaData);
                            series.data = [currentValue];
                            series.pointPadding = getPointPadding(metaData.barWidth, 40);
                            config.series.push(series);
                        }

                        metaData = findMetaDataByCode(seriesMetaData, seriesCode.planValue);
                        if (metaData && planValue) {
                            config.yAxis.plotLines = [{
                                color: metaData.seriesColor || color.planValue,
                                value: planValue,
                                width: 2,
                                zIndex: 5,
                                label: {
                                    text: 'План: {0}{1}'.format(planValue.toFixed(dataPrecision), valueSuffix),
                                    zIndex: 8,
                                    x: 5,
                                    rotation: 0,
                                    align: 'center',
                                    y: -10

                                }
                            }];
                        }
                        break;
                    case "Q":
                        var quarterCount = 4; // TODO: Добавить логику с пятым кварталом

                        for (var i = 1; i <= quarterCount; i++) {
                            config.xAxis.categories.push("{0} '{1}".format(i, currentYear));
                        }
                        config.xAxis.min = 0;
                        config.xAxis.max = quarterCount - 1;

                        addSeriesIfExists(seriesCode.prevValue);
                        addSeriesIfExists(seriesCode.currentValue);
                        addSeriesIfExists(seriesCode.planValue);

                        break;
                    case "M":
                        config.xAxis.categories = [
                            "Январь", "Февраль", "Март", "Апрель",
                            "Май", "Июнь", "Июль", "Август",
                            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
                        ];

                        addSeriesIfExists(seriesCode.prevValue);
                        addSeriesIfExists(seriesCode.currentValue);
                        addSeriesIfExists(seriesCode.planValue);
                        break;
                }

                // Min & Max
                try {
                    var minMaxParts = chartMeta.dataMinMax.split("-");
                    if (config.chart.type !== 'areaspline') {
                        config.yAxis.min = minMaxParts[0] == "auto" ? undefined : minMaxParts[0];
                        config.yAxis.max = minMaxParts[1] == "auto" ? undefined : minMaxParts[1];
                    }
                    else {
                        config.yAxis.min = minMaxParts[0] == "auto"
                                ? Math.floor(_.min(_.map(config.series, function (s) {
                            return _.min(s.data);
                        })))
                                : minMaxParts[0];

                        config.yAxis.max = minMaxParts[1] == "auto"
                                ? Math.ceil(_.max(_.map(config.series, function (s) {
                            return _.max(s.data);
                        })))
                                : minMaxParts[1];

                        if (config.yAxis.max === config.yAxis.min) {
                            config.yAxis.min -= 1;
                        }

                        config.yAxis.endOnTick = false;
                    }


                } catch (e) {
                }
                if (chartMeta.widgetType === 'PFAREA' && chartMeta.intervalType === 'M'
//                        ||
//                        chartMeta.widgetType === 'PFPFH'
                ) {
//                    _.forEach(jsonData, function(data) {
//
//                    });
                    var filterJsonData = jsonData.filter(function (data) {
                        return data.currentValue != 0 && data.currentValue != null;
                    });
                    var minC = Math.min.apply(
                            Math,
                            filterJsonData.map(function (o) {
                                        return o.currentValue;
                                    }
                            ));
                    var minP = Math.min.apply(
                            Math,
                            filterJsonData.map(function (o) {
                                        if (!o.planValue) {
                                            return 100;
                                        }
                                        return o.planValue;
                                    }
                            ));
                    var maxC = Math.max.apply(
                            Math,
                            filterJsonData.map(function (o) {
                                        return o.currentValue;
                                    }
                            ));
                    var maxP = Math.max.apply(
                            Math,
                            filterJsonData.map(function (o) {
                                        return o.planValue;
                                    }
                            ));

                    var min = Math.min(minC, minP);
                    var max = Math.max(maxC, maxP);
                    var yAxisMin = Math.floor(min * 10) / 10;
                    if (chartMeta.widgetType === 'PFAREA' && chartMeta.intervalType === 'M') {
                        if (max - min < 0.2) {
                            yAxisMin = Math.floor(min * 320) / 320;
                        }
                        else {
                            yAxisMin = Math.floor(min * 60) / 60;
                        }
                    }

                    config.yAxis.min = yAxisMin * 100;
                }
                $container.highcharts(config);
            }

            // Динамика за 2 года по годам
            function createByYearDynamic($container, filterData, jsonData, seriesMetaData) {
                // Пакет возвращает только одну строку
                var firstRow = jsonData.data[0];

                var currentYear = moment(filterData.startDate).year(),
                        categories = [currentYear - 1, currentYear],
                        currentValue = getValueBySeriesCode(firstRow, seriesCode.currentValue),
                        prevValue = getValueBySeriesCode(firstRow, seriesCode.prevValue),
                        planValue = getValueBySeriesCode(firstRow, seriesCode.planValue),
                        dataPrecision = seriesMetaData[0].dataPrecision || 2,
                        delta = currentValue - prevValue,
                        title = seriesMetaData[0].chartTitle || jsonData.kpiName,
                        subTitleFormat = delta > 0
                                ? "<span style='color: #32B026'><i class='fa fa-arrow-up'> {0}%</sp an>"
                                : "<span style='color: #C0423C'><i class='fa fa-arrow-down'> {0}%</span>";

                // Создаем series
                var chartSeries = [], series;
                var yAxisMin = Math.max(Math.min(currentValue, prevValue, planValue) - 5, 0);

                var metaData = findMetaDataByCode(seriesMetaData, seriesCode.currentValue);
                if (metaData) {
                    series = createFactSeries(metaData);
                    series.data = [prevValue, currentValue];
                    chartSeries.push(series);
                }

                metaData = findMetaDataByCode(seriesMetaData, seriesCode.planValue);
                if (metaData) {
                    series = createPlanSeries(metaData);
                    series.data = [planValue, planValue];
                    chartSeries.push(series);
                }

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    subtitle: {
                        text: subTitleFormat.format(delta.toFixed(dataPrecision)),
                        useHTML: true,
                        style: {
                            "fontSize": "15px"
                        }
                    },
                    xAxis: {
                        categories: categories,
                        crosshair: true
                    },
                    yAxis: {
                        min: yAxisMin,
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.' + dataPrecision + 'f}%</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    legend: {enabled: false},
                    series: chartSeries
                });
            }

            function createByQuarterDynamic($container, filterData, jsonData, seriesMetaData) {
                var dataPrecision = seriesMetaData[0].dataPrecision || 2,
                        title = seriesMetaData[0].chartTitle || jsonData.kpiName;

                // Создаем series
                var chartSeries = [];

                function addSeriesIfExists(code) {
                    var metaData = findMetaDataByCode(seriesMetaData, code);
                    if (metaData) {
                        var series;
                        switch (code) {
                            case seriesCode.currentValue:
                                series = createFactSeries(metaData);
                                break;
                            case seriesCode.planValue:
                                series = createPlanSeries(metaData);
                                break;
                            case seriesCode.prevValue:
                                series = createPrevFactSeries(metaData);
                                break;
                        }

                        series.data = _.map(jsonData.data.map(function (kpiDataItem) {
                            return getValueBySeriesCode(kpiDataItem, code);
                        }));

                        chartSeries.push(series);
                    }
                }

                addSeriesIfExists(seriesCode.prevValue);
                addSeriesIfExists(seriesCode.currentValue);
                addSeriesIfExists(seriesCode.planValue);

                var currentYear = moment(filterData.startDate).year();
                var categories = [];
                var quarterCount = 4; // TODO: Добавить логику с пятым кварталом

                for (var i = 1; i <= quarterCount; i++) {
                    categories.push("{0} '{1}".format(i, currentYear));
                }

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        min: 0,
                        max: quarterCount - 1,
                        categories: categories,
                        crosshair: true
                    },
                    yAxis: {
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.' + dataPrecision + 'f}%</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    series: chartSeries
                });
            }

            function createByMonthDynamic($container, filterData, jsonData, seriesMetaData) {
                var dataPrecision = seriesMetaData[0].dataPrecision || 2,
                        title = seriesMetaData[0].chartTitle || jsonData.kpiName;

                // Создаем series
                var chartSeries = [];

                function addSeriesIfExists(code) {
                    var metaData = findMetaDataByCode(seriesMetaData, code);
                    if (metaData) {
                        var series;
                        switch (code) {
                            case seriesCode.currentValue:
                                series = createFactSeries(metaData);
                                break;
                            case seriesCode.planValue:
                                series = createPlanSeries(metaData);
                                break;
                            case seriesCode.prevValue:
                                series = createPrevFactSeries(metaData);
                                break;
                        }

                        series.data = _.map(jsonData.data.map(function (kpiDataItem) {
                            return getValueBySeriesCode(kpiDataItem, code);
                        }));

                        chartSeries.push(series);
                    }
                }

                addSeriesIfExists(seriesCode.prevValue);
                addSeriesIfExists(seriesCode.currentValue);
                addSeriesIfExists(seriesCode.planValue);

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: [
                            "Январь", "Февраль", "Март", "Апрель",
                            "Май", "Июнь", "Июль", "Август",
                            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
                        ],
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.' + dataPrecision + 'f}%</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    legend: {
                        verticalAlign: 'top',
                        y: 25
                    },
                    series: chartSeries
                });
            }

            function createKpi1Chart($container, filterData, jsonData) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName;

                if (series.length === 0 || series[0].data.length === 0 || isSeriesFullOfZeros(series)) {
                    $container.empty();
                    return;
                }

                filterData.endDate = moment(filterData.startDate).add(1, "y").add(-1, "d").toDate();
                filterData.timeUnitId = 4;

                series[0].color = color.prevValue;
                series[1].color = color.currentValue;
                series[2].color = color.planValue;

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: [
                            "Январь", "Февраль", "Март", "Апрель",
                            "Май", "Июнь", "Июль", "Август",
                            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
                        ],
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.2f}%</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    legend: {
                        verticalAlign: 'top',
                        y: 25
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                rotation: 270,
                                formatter: function () {
                                    return this.y < 4 ? null : (this.y.toFixed(2) + "%");
                                }
                            }
                        }
                    },
                    series: series
                });
            }

            function createKpi5Chart($container, filterData, jsonData) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName;

                if (series.length === 0 || series[0].data.length === 0 || isSeriesFullOfZeros(series)) {
                    $container.empty();
                    return;
                }

                var currentYear = moment(filterData.startDate).format("YY"),
                        previousYear = moment(filterData.startDate).add(-1, "Y").format("YY"),
                        categories = [
                            "Q4' " + previousYear,
                            "Q1' " + currentYear,
                            "Q2' " + currentYear,
                            "Q3' " + currentYear,
                            "Q4' " + currentYear
                        ];

                series[0].color = color.currentValue;
                series[1].color = color.planValue;

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: categories
                    },
                    yAxis: {
                        min: 0,
                        title: {text: ""},
                        labels: {
                            format: '{value}'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                formatter: function () {
                                    return this.y < 0.05 ? null : (this.y.toFixed(2));
                                }
                            }
                        },
                        spline: {
                            dataLabels: {
                                enabled: true,
                                style: {
                                    color: "#000",
                                    "fontSize": "13px",
                                    "fontWeight": "bold"
                                }
                            }
                        }
                    },
                    series: series
                });
            }

            function createKpi7Chart($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.kpiCode == customParams.kpiCode;
                });

                if (!chart || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                var title = "";

                if (customParams.showTitle) {
                    title = chart.bag.kpiName;
                }

                var series = chart.series;

                var planValue = chart.bag.planValue || 100;

                series[0].color = color.currentValue;
                series.push({
                    type: 'line',
                    name: 'Normative Line',
                    color: color.normativeValue,
                    data: [[-0.4, planValue], [series[0].data.length - 0.6, planValue]],
                    marker: {
                        enabled: false
                    },
                    states: {
                        hover: {
                            lineWidth: 0
                        }
                    },
                    enableMouseTracking: false
                });

                var isByMonth = filterData.dateIntervalType === "M";

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 300
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        min: 0,
                        max: chart.bag.categories.length - 1,
                        categories: isByMonth
                                ? app.chartUtils.getMonthCategories(chart.bag.categories)
                                : app.chartUtils.getWeekCategories(chart.bag.categories)
                    },
                    yAxis: {
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        pointFormat: "<b>{series.name}:</b>  {point.y:.2f}%"
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                rotation: 270,
                                formatter: function () {
                                    return this.y == null ? "" : this.y.toFixed(2) + "%";
                                }
                            }
                        }
                    },
                    legend: {enabled: false},
                    series: series
                });
            }

            function createKpi8Pie($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.kpiCode == customParams.kpiCode;
                });

                var unitName = customParams.kpiCode === "KPIOB~8~1" ? "Банка" : "ОБ";
                var title = "Эффект от процессного управления {0}.<br/> Цель {1}, млн. руб.".format(unitName,
                        moment(filterData.startDate).year());

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                $container.highcharts({
                    chart: {
                        type: "pie"
                    },
                    title: {text: title},
                    tooltip: {
                        headerFormat: "",
                        pointFormat: "{point.tag}: <b>{point.y:,.2f} млн. руб</b> ({point.percentage:.2f}%)"
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: "pointer",
                            dataLabels: {
                                enabled: true,
                                format: "<b>{point.name}</b>: {point.y:,.2f}"
                            }
                        }
                    },
                    series: chart.series
                });
            }

            function createKpi8Dynamic($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.kpiCode == customParams.kpiCode;
                });

                var title = "Выполнение плана за {0}".format(moment(filterData.startDate).format("MMMM YYYY"));

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                var series = chart.series;

                series[0].color = color.currentValue;
                series.push({
                    type: 'line',
                    name: 'Normative Line',
                    color: color.normativeValue,
                    data: [[-0.4, 100], [series[0].data.length - 0.6, 100]],
                    marker: {
                        enabled: false
                    },
                    states: {
                        hover: {
                            lineWidth: 0
                        }
                    },
                    enableMouseTracking: false
                });

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
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        pointFormat: "<b>{series.name}:</b>  {point.y:.2f}%"
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                formatter: function () {
                                    return this.y < 4 ? null : (this.y.toFixed(2) + "%");
                                }
                            }
                        }
                    },
                    legend: {enabled: false},
                    series: series
                });
            }

            function createKpi9Chart($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.isPrevYear === customParams.isPrevYear;
                });

                var title = chart.bag.kpiName;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                chart.series[0].color = "#B0B0B0";
                chart.series[1].color = "#F80000";

                $container.highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: chart.bag.categories
                    },
                    yAxis: {
                        title: {text: "тыс. $"}
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                format: "{point.y:.0f}"
                            }
                        }
                    },
                    series: chart.series
                });
            }

            function createKpi10Chart($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.isPrevYear === customParams.isPrevYear;
                });

                var title = chart.bag.kpiName;

                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                chart.series[0].borderColor = "#CCC";

                chart.series[1].color = "#EAF2FF";
                chart.series[1].borderColor = "#CCC";

                $container.highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: chart.bag.categories
                    },
                    yAxis: {
                        title: {text: "тыс. $"},
                        reversedStacks: false
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                format: "{point.y:.1f}"
                            },
                            stacking: 'normal'
                        }
                    },
                    series: chart.series
                });

                // Display kpi messages
                var kpi1Message = chart.bag.kpi1Message || "";
                var kpi2Message = chart.bag.kpi2Message || "";
                $(customParams.messagesContainer).html("" + kpi1Message + "<br/>" + kpi2Message);
            }

            function createKpi15_16ByMonthChart($container, filterData, jsonData) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName;

                if (series.length === 0 || series[0].data.length === 0 || isSeriesFullOfZeros(series)) {
                    $container.empty();
                    return;
                }

                filterData.endDate = moment(filterData.startDate).add(1, "y").add(-1, "d").toDate();
                filterData.timeUnitId = 4;

                series[0].color = color.currentValue;
                series[1].color = color.planValue;

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: [
                            "Январь", "Февраль", "Март", "Апрель",
                            "Май", "Июнь", "Июль", "Август",
                            "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"
                        ],
                        crosshair: true
                    },
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.2f}%</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    legend: {
                        verticalAlign: 'top',
                        y: 25
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                rotation: 270,
                                inside: true,
                                enabled: true,
                                formatter: function () {
                                    return this.y < 4 ? null : (this.y.toFixed(2) + "%");
                                }
                            }
                        }
                    },
                    series: series
                });
            }

            function createKpi15ByYearChart($container, filterData, jsonData) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName,
                        planValue = chart.bag.planValue;

                if (series.length === 0 || series[0].data.length === 0 || isSeriesFullOfZeros(series)) {
                    $container.empty();
                    return;
                }

                filterData.endDate = moment(filterData.startDate).add(1, "y").add(-1, "d").toDate();
                filterData.timeUnitId = 4;

                var currentYear = moment(filterData.startDate).format("YYYY");

                var plotLines = [{
                    color: color.planValue,
                    label: {
                        align: 'right',
                        text: '<b>План</b>: {0}%'.format(planValue.toFixed(2)),
                        x: -10
                    },
                    value: planValue.toFixed(2),
                    width: 2,
                    zIndex: 10
                }];

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        min: 0,
                        max: 0,
                        categories: [currentYear]
                    },
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ""},
                        labels: {
                            format: '{value}%'
                        },
                        plotLines: plotLines
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.2f}%</b></td></tr>',
                        footerFormat: '</table>',
                        useHTML: true
                    },
                    legend: {enabled: false},
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                formatter: function () {
                                    return this.y < 4 ? null : (this.y.toFixed(2) + "%");
                                }
                            }
                        }
                    },
                    series: series
                });
            }

            function createKpi18Chart($container, filterData, jsonData) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName;

                if (series.length === 0 || series[0].data.length === 0 || isSeriesFullOfZeros(series)) {
                    $container.empty();
                    return;
                }

                var defaultColors = Highcharts.getOptions().colors;
                try {
                    series[0].color = defaultColors[0];
                    series[1].color = defaultColors[2];
                    series[2].color = defaultColors[6];
                } catch (e) {
                }

                $container.highcharts({
                    chart: {
                        type: 'column'
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        categories: app.chartUtils.getWeekCategories(chart.bag.categories)
                    },
                    yAxis: {
                        title: {text: ""},
                        reversedStacks: false
                    },
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                            dataLabels: {
                                inside: true,
                                enabled: true
                            }
                        }
                    },
                    series: series
                });
            }

            function createQuarterDynamicChartDeprecated($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        series = chart.series,
                        title = chart.bag.kpiName;

                if (customParams.addDivisionToTitle) {
                    var divisionGroup = _.find(app.viewModel.getFilter("divisionGroupId").options(),
                            function (el) {
                                return el.id == filterData.divisionGroupId;
                            });

                    if (divisionGroup) {
                        title += " " + divisionGroup.name;
                    }
                }


                if (chart === undefined || chart.series.length === 0 || chart.series[0].data.length === 0 || isSeriesFullOfZeros(chart.series)) {
                    $container.empty();
                    return;
                }

                var valueSuffix = customParams.valueSuffix;

                series[0].color = color.currentValue;
                series[1].color = color.planValue;

                $container.highcharts({
                    chart: {
                        type: 'column',
                        height: 320
                    },
                    title: {
                        text: title
                    },
                    xAxis: {
                        min: 0,
                        max: chart.bag.categories.length - 1,
                        categories: app.chartUtils.getQuarterCategories(chart.bag.categories)
                    },
                    yAxis: {
                        min: 0,
                        title: {text: ""},
                        labels: {
                            format: "{value}" + valueSuffix
                        }
                    },
                    tooltip: {
                        headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                        pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                        '<td style="padding:0"><b>{point.y:.2f}' + valueSuffix + '</b></td></tr>',
                        footerFormat: '</table>',
                        shared: true,
                        useHTML: true
                    },
                    plotOptions: {
                        column: {
                            dataLabels: {
                                inside: true,
                                enabled: true,
                                formatter: function () {
                                    if (this.y == null) return null;
                                    if (customParams.smallLabels) {
                                        return this.y.toFixed(0) + valueSuffix;
                                    }
                                    return this.y.toFixed(2) + valueSuffix;
                                }
                            }
                        },
                        series: {
                            pointWidth: customParams.smallLabels ? 35 : undefined
                        }
                    },
                    series: series
                });
            }

            function createKpi23_24Chart($container, filterData, jsonData) {
                var kpeChart = _.find(jsonData, function (chart) {
                    return chart.bag.kpiCode == "KPIOB~23";
                });

                var metricChart = _.find(jsonData, function (chart) {
                    return chart.bag.kpiCode == "KPIOB~24";
                });

                if (!kpeChart && !metricChart) {
                    $container.empty();
                    return;
                }

                var text = "", calcDate = "нет данных";

                if (kpeChart) {
                    calcDate = kpeChart.bag.calcDate;
                    if (kpeChart.bag.value !== undefined) {
                        text = "КПЭ - {0}%".format(kpeChart.bag.value.toFixed(1));
                    }
                    else {
                        text = "КПЭ —"
                    }
                }
                text += "<br/>";
                if (metricChart) {
                    calcDate = metricChart.bag.calcDate;
                    if (metricChart.bag.value !== undefined) {
                        text += "метрики - {0}%".format(metricChart.bag.value.toFixed(1))
                    } else {
                        text += "метрики —"
                    }
                }

                text += "<br/>";
                if (calcDate) {
                    text += "данные за " + moment(calcDate).format("DD.MM.YYYY");
                } else {
                    text += "нет данных";
                }

                $container.html(text);
            }

            app.init(config, function (viewModel) {
                var startDate = viewModel.getFilter("default.startDate");
                var divisionGroupId = viewModel.getFilter("divisionGroupId");

                startDate.value.subscribe(function (newValue) {
                    var year = moment(newValue).year();
                    var kpi8MonthObs = viewModel.getFilter("kpi8.startDate").value;
                    var kpi8Date = moment(kpi8MonthObs()).set("year", year + 1).startOf("year").add(-1, "d").toDate();
                    kpi8MonthObs(kpi8Date);
                });

//                подписака на событие
                <%--divisionGroupId.value.subscribe(function (newValue) {--%>
                <%--var selectedOption = viewModel.getFilter("divisionGroupId").getSelectedOptions()[0];--%>
                <%--if (selectedOption) {--%>
                <%--$("#link-ob-quality").html("" +--%>
                <%--"<a href=${pageContext.request.contextPath}/showcase/unitCost?divisionGroupId="+selectedOption.id+ " >" +--%>
                <%--"На витрину UnitCost"+--%>
                <%--//                        selectedOption.name +--%>
                <%--"</a>");--%>
                <%--$(".link-ob-quality2").html("" +--%>
                <%--"<a href=${pageContext.request.contextPath}/showcase/unitCost?divisionGroupId="+selectedOption.id+ " >" +--%>
                <%--"На витрину UnitCost"+--%>
                <%--//                        selectedOption.name +--%>
                <%--"</a>");--%>

                <%--}--%>
                <%--});--%>

                // Раскрашиваем табы
                var subscription = viewModel.visibleCharts.subscribe(function (value) {
                    if (value) {
                        var tabLinks = $(".charts-container > tab-strip > .chart-tabs > ul > li > a");

                        $(tabLinks).each(function (index, el) {
                            index++;
                            $(el).addClass("tab-" + index);
                        });

                        subscription.dispose();
                    }
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <div class="filter-row">
            <div class="filter-element">
                <filter params="name: 'startDate', group: 'default'"></filter>
            </div>

            <div class="filter-element">
                <filter params="name: 'divisionGroupId', group: 'default'"></filter>
            </div>

            <div class="filter-element">
                <refresh-button></refresh-button>
            </div>

            <div class="filter-element" style="margin-top: 18px;">
                <report-button params="reportUrl: 'Nom', group: 'default'"></report-button>
            </div>
        </div>

        <filter-log></filter-log>

        <div class="custom-chart-container">
            <chart params="name: 'kpi23_24Chart'"></chart>
        </div>

        <div data-bind="visible: groups.default.visibleCharts" class="charts-container">
            <tab-strip params="name: 'showcases'">
                <tab>
                    <chart params="name: 'placeholder1'" class="mb-0"></chart>

                        <%--<p class="scrollable-chart-title">Измерение Unit Cost по «конечным продуктам» ОБ</p>--%>


                </tab>
                <tab>
                    <chart params="name: 'placeholder2'" class="mb-0"></chart>
                </tab>
                <tab>
                    <div class="row">
                        <div class="col-xs-5">
                            <div style="margin-top: 73px">
                                <chart params="name: 'kpi8_1Pie'"></chart>
                                <chart params="name: 'kpi8_2Pie'"></chart>
                            </div>
                        </div>
                            <%--<div class="col-xs-7">--%>
                            <%--&lt;%&ndash;<div class="group-section">&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<div style="margin-bottom: 15px;">&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<filter params="name: 'startDate', group: 'kpi8'"></filter>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>

                            <%--&lt;%&ndash;<chart params="name: 'kpi8_1Dynamic', group: 'kpi8'"></chart>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<chart params="name: 'kpi8_2Dynamic', group: 'kpi8'"></chart>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</div>&ndash;%&gt;--%>
                            <%--</div>--%>
                    </div>
                    <chart params="name: 'kpi21Chart'" class="mb-0"></chart>
                    <chart params="name: 'placeholder3'" class="mb-0"></chart>
                </tab>
                <tab>
                    <chart params="name: 'kpi19Chart'"></chart>
                    <div class="row">
                            <%--<div class="col-xs-6">--%>
                            <%--<div class="group-section ">--%>
                            <%--<div class="filter-row">--%>
                            <%--<div class="filter-element">--%>
                            <%--<filter params="name: 'kpi14Value', group: 'kpi14'"></filter>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="charts-container">--%>
                            <%--<chart params="name: 'kpi14Chart', group: 'kpi14'"></chart>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                        <div class="col-xs-6" style="margin-top: 92px">
                            <chart params="name: 'kpi22Chart'"></chart>
                        </div>
                    </div>

                        <%-- Попросили временно скрыть
                        <div class="group-section chart">
                            <div class="filter-row">
                                <div class="filter-element">
                                    <filter params="name: 'quarter', group: 'withQuarter'"></filter>
                                </div>
                            </div>

                            <div class="charts-container">
                                <div class="row chart">
                                    <div class="col-xs-6">
                                        <chart params="name: 'kpi9PrevYearChart', group: 'withQuarter'"></chart>
                                    </div>
                                    <div class="col-xs-6">
                                        <chart params="name: 'kpi9CurrentYearChart', group: 'withQuarter'"></chart>
                                    </div>
                                </div>

                                <chart params="name: 'kpi10CurrentYearChart', group: 'withQuarter'">
                                    <div class="chart-part-1">
                                        <div class="chart-container"></div>
                                    </div>
                                    <div class="chart-part-2">
                                        <div id="kpi10CurrentYearChartMessages">
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                </chart>
                                <chart params="name: 'kpi10PrevYearChart', group: 'withQuarter'">
                                    <div class="chart-part-1">
                                        <div class="chart-container"></div>
                                    </div>
                                    <div class="chart-part-2">
                                        <div id="kpi10PrevYearChartMessages">
                                        </div>
                                    </div>
                                    <div class="clearfix"></div>
                                </chart>
                            </div>
                        </div>
                        --%>

                    <div class="mb-0">
                        <chart params="name: 'kpi18Chart', group: 'default'"></chart>
                    </div>
                        <%--<p id="link-ob-quality"></p>--%>

                    <chart params="name: 'placeholder4'" class="mb-0"></chart>
                        <%--<p id="link-ob-quality2"></p>--%>
                </tab>
                <tab>
                    <div class="row mb-0">
                        <div class="col-xs-8">
                            <chart params="name: 'kpi16Chart'"></chart>
                        </div>
                        <div class="col-xs-4">
                            <chart params="name: 'kpi15ByYearChart'"></chart>
                        </div>
                        <chart params="name: 'placeholder5'" class="mb-0"></chart>
                    </div>

                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>