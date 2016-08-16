<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Срок доставки пластиковых карт в отделения" />

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .filter-block-custom {
                padding-left: 3px;
                padding-right: 3px;
                margin-top: 5px;
            }
            .filter-block-height {
                height: 240px;
            }

            .details-report-wrapper {
                margin-top: 20px;
                background: white;
                text-align: center;
                font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                font-size: 18px;
                color: #333333;
                padding: 5px;
            }

            .details-report-wrapper div.filter {
                display: inline-block;
                font-size: 16px;
            }

            .details-report-wrapper .btn {
                font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
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
                                url: "cardsDeliveryPeriodFilter/macroRegions"
                            },
                            width: defaultWidth
                        },
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/citiesByMacroRegionIds",
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
                                url: "cardsDeliveryPeriodFilter/branchTypes"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        salePlaceIds: {
                            type: "Select",
                            multiple: true,
                            title: "Отделение",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/branches",
                                params: ["macroRegionIds", "cityIds", "branchTypeIds"]
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        automaticExtensionMode: {
                            type: "CheckBox",
                            title: "С автоматическим продлением"
                        },
                        zpSignIds: {
                            type: "Select",
                            multiple: true,
                            title: "Признак ЗП",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/zpSigns"
                            },
                            width: defaultWidth
                        },
                        pyTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Тип ПУ",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/pyTypesByZPSignIds",
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
                                url: "cardsDeliveryPeriodFilter/cardCategories"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        debitOrCreditIds: {
                            type: "Select",
                            multiple: true,
                            title: "Дебет/Кредит",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/debitOrCredits"
                            },
                            width: defaultWidth
                        },
                        clientSegmentIds: {
                            type: "Select",
                            multiple: true,
                            title: "Сегмент клиента",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/clientSegments"
                            },
                            width: defaultWidth,
                            enableClear: true
                        },
                        mvkSignId: {
                            type: "Select",
                            multiple: false,
                            title: "Признак МВК",
                            optionsCaption: "Все",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/mvkSigns"
                            },
                            width: defaultWidth
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/timeUnits"
                            },
                            width: defaultWidth - 8
                        },
                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица сети",
                            dataSource: {
                                url: "cardsDeliveryPeriodFilter/systemUnits"
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
                                    {id: "AvgDuration", name: "Среднее время"}
                                ]
                            },
                            width: defaultWidth - 8
                        }
                    },
                    tabStrips: {
                        dayDistribution: {
                            tabs: ["Москва", "Регионы"],
                            dynamicConfig: {
                                jsFunc: configureTabs,
                                params: ["macroRegionIds"]
                            },
                            reportUrl: "CardsDeliveryPeriodDistributionDynamicReport"
                        },
                        dynamic: {
                            tabs: ["Москва", "Регионы"],
                            dynamicConfig: {
                                jsFunc: configureTabs,
                                params: ["macroRegionIds"]
                            }
                        },
                        pie: {
                            tabs: ["Москва", "Регионы"],
                            dynamicConfig: {
                                jsFunc: configureTabs,
                                params: ["macroRegionIds"]
                            }
                        }
                    },
                    charts: {
                        distributionDynamicMoscow: {
                            jsFunc: createDistributionDynamic,
                            dataSource: "CardsDeliveryPeriodDistributionDynamic",
                            customParams: {macroRegionId: 1}
                        },
                        distributionDynamicRegions: {
                            jsFunc: createDistributionDynamic,
                            dataSource: "CardsDeliveryPeriodDistributionDynamic",
                            customParams: {macroRegionId: 2}
                        },
                        periodDynamicMoscow: {
                            jsFunc: createPeriodDynamic,
                            dataSource: "CardsDeliveryPeriodDynamic",
                            customParams: {macroRegionId: 1}
                        },
                        periodDynamicRegion: {
                            jsFunc: createPeriodDynamic,
                            dataSource: "CardsDeliveryPeriodDynamic",
                            customParams: {macroRegionId: 2}
                        },
                        pieMoscow: {
                            jsFunc: createPie,
                            dataSource: "CardsDeliveryPeriodPie",
                            customParams: {macroRegionId: 1}
                        },
                        pieRegions: {
                            jsFunc: createPie,
                            dataSource: "CardsDeliveryPeriodPie",
                            customParams: {macroRegionId: 2}
                        },
                        table: {
                            jsFunc: createTable,
                            dataSource: "CardsDeliveryPeriodTable"
                        }
                    },
                    slaves: [{
                        name: "details",
                        filters: {
                            daysLowerBound: {
                                type: "NumericTextBox",
                                defaultValue: 2
                            }
                        },
                        charts: {}
                    }]
                }],
                cookies: true
            };

            function configureTabs(tabs, params) {
                var macroRegionIds = params.macroRegionIds || [];

                tabs[0].visible(_.emptyOrContains(macroRegionIds, "1"));
                tabs[1].visible(_.emptyOrContains(macroRegionIds, "2"));
            }

            function createDistributionDynamic($container, filterData, jsonData, customParams) {
                var title = filterData.automaticExtensionMode
                                ? "Срок доставки карт в отделение относительно месяца окончания срока действия карты"
                                : "Распределение {0} карт от количества рабочих дней, потраченных на доставку".format(
                                filterData.paramType === "AvgDuration" ? "количества" : "процента"),
                        height = 400,
                        width = 1182;

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });

                if (chart === undefined) {
                    $container.highcharts({
                        chart: {
                            height: height
//                            ,width: width
                            ,zoomType: 'x'
                        },
                        title: {text: title}
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, true),
                        isPercentParam = filterData.paramType == "Percent",
                        valueSuffix = isPercentParam ? "%" : " ",
                        valueFormat = isPercentParam
                                ? "{point.y:.2f}% ({point.cardCount})"
                                : "{point.y} ({point.valuePercent:.2f}%)";

                $container.highcharts({
                    chart: {
                        height: height,
//                        width: width,
                        zoomType: 'x',
                        type: "column"
                    },
                    legend: {
                        enabled: true,
                        verticalAlign: "bottom"
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            stacking: 'normal',
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                formatter: function () {
                                    return this.y / this.series.chart.axes[1].dataMax > 0.08 ? this.y.toFixed(0) + valueSuffix : "";
                                },
                                inside: true,
                                rotation: xAxis.ticks.length < 10 ? 0 : 270,
                                x: 0
                            },
                            tooltip: {
                                pointFormat: '<span style=\"color:{series.color}\">\u25CF</span> {series.name}: <b>'
                                + valueFormat + '</b><br>'
                            }
                        }
                    },
                    tooltip: {
                        crosshairs: true,
                        shared: true,
                        useHTML: true,
                        valueSuffix: ''
                    },
                    xAxis: xAxis,
                    yAxis: {
                        labels: {
                            format: '{value}' + valueSuffix
                        },
                        max: isPercentParam ? 100 : null,
                        min: 0,
                        title: {text: ''},
                        stackLabels: {enabled: true}
                    },
                    series: series
                });
            }

            function createPeriodDynamic($container, filterData, jsonData, customParams) {
                var title = "Гистограмма: динамика показателя",
                        height = 350,
                        width = 776;

                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });

                if (chart === undefined) {
                    $container.highcharts({
                        chart: {
                            height: height,
//                            width: width,
                            zoomType: 'x'
                        },
                        title: {text: title}
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, true),
                        durationNormative = chart.bag.durationNormative,
                        percentNormative = chart.bag.percentNormative,
                        isPercentParam = filterData.paramType == "Percent",
                        normative = isPercentParam
                                ? percentNormative
                                : durationNormative,
                        averageValue = chart.bag.averageValue.toFixed(0),
                        valueSuffix = isPercentParam ? "%" : " дн.",
                        valueFormat = '{point.y:.' + (isPercentParam ? 2 : 0) + 'f}' + valueSuffix;

                var plotLines = [{
                    color: 'lightgray',
                    label: {align: 'right', text: '<b>Среднее</b>: {0}{1}'.format(averageValue, valueSuffix)},
                    value: averageValue,
                    width: 2,
                    zIndex: 10
                }];

                if (isPercentParam) {
                    plotLines.push({
                        color: '#FDA6A2',
                        label: {text: '<b>KPI</b>: {0} дня в {1}%'.format(durationNormative, percentNormative)},
                        value: normative,
                        width: 2,
                        zIndex: 10
                    });
                }

                $container.highcharts({
                    chart: {
                        height: height,
//                        width: width,
                        zoomType: 'x'
                    },
                    legend: {enabled: false},
                    title: {text: title},
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                format: valueFormat,
                                inside: true,
                                rotation: xAxis.ticks.length < 10 ? 0 : 270,
                                x: 0
                            },
                            tooltip: {
                                pointFormat: '<span style="color:{point.color}">{series.name}</span>: <b>' +
                                valueFormat +
                                '</b> (Общее количество: {point.cardCount}, В KPI: {point.inKPICardCount})<br>'
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        allowDecimals: isPercentParam,
                        labels: {
                            format: '{value}' + valueSuffix
                        },
                        max: isPercentParam ? 100 : null,
                        min: 0,
                        plotLines: plotLines,
                        title: {text: ''}
                    },
                    series: series
                });
            }

            function createPie($container, filterData, jsonData, customParams) {
                var chart = _.find(jsonData, function (chart) {
                    return chart.bag.macroRegionId == customParams.macroRegionId;
                });

                var series = chart ? chart.series : [];

                $container.highcharts({
                    chart: {
                        type: 'pie',
                        height: 350
//                        width: 366
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
                    title: {text: 'Круговая диаграмма уложившихся в KPI'},
                    tooltip: {
                        headerFormat: "<b>{point.key}</b><br/>",
                        pointFormat: "{point.percentage:.2f}% ({point.y})",
                        useHTML: true
                    },
                    series: series
                });
            }

            function createTable($container, filterData, jsonData, customParams) {
                var greenCss = "label-success",
                        redCss = "label-danger",
                        template = "<span class='label label-cell {0}'" +
                                "data-toggle='tooltip' data-original-title='{1}'>" +
                                "{2}</span>";

                function formatCellValue(data) {
                    if (!data || data.mainValue === undefined || data.mainValue === null) return "";

                    var css, tooltip, text;
                    if (filterData.paramType == "Percent") {
                        css = data.mainValue < data.percentNormative ? redCss : greenCss;
                        tooltip = Math.round(data.totalDuration / data.cardCount) + "дн.";
                        text = "{0}% ({1})".format(data.mainValue.toFixed(2), data.cardCount);
                    } else {
                        var duration = Math.round(data.mainValue),
                                percent = Math.round(data.inKPICardCount / data.cardCount * 100, 2);
                        css = duration > data.durationNormative ? redCss : greenCss;
                        tooltip = percent + "%";
                        text = "{0} дн. ({1})".format(duration, data.cardCount);
                    }

                    return template.format(css, tooltip, text);
                }

                var systemUnitOptions = app.viewModel.getFilter("systemUnitId").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети",
                        columns = app.chartUtils.createDateTimeColumns(
                                filterData,
                                "mainValue",
                                formatCellValue);

                // Footer
                var footerRow = jsonData.splice(-1, 1)[0];
                _.forEach(columns, function (c) {
                    var value = _.get(footerRow, c.field),
                            footerText;

                    if (value === null || value === undefined) return;

                    if (filterData.paramType == "Percent") {
                        footerText = value.toFixed(2) + "%";
                    } else {
                        footerText = Math.round(value) + " дн.";
                    }

                    c.footerTemplate = footerText;
                });

                // Если больше одной колонки с датами
                if (columns.length > 1) {
                    // Задаем колонку "Среднее"
                    columns.push({
                        field: "average",
                        title: "Среднее",
                        template: function (dataItem) {
                            return formatCellValue(dataItem.average);
                        },
                        width: columns[0].width
                    });
                }

                // Задаем первую колонку
                columns.unshift({
                    field: "unitName",
                    title: systemUnitName,
                    width: filterData.systemUnitId == 3 ? 250 : 150,
                    locked: true,
                    footerTemplate: "Итого"
                });

                $container.kendoGrid({
                    dataSource: {
                        data: jsonData
                    },
                    height: 550,
                    sortable: true,
                    columns: columns,
                    dataBound: function () {
                        // показываем всплывающие полсказки
                        $container.find("[data-toggle='tooltip']").tooltip({
                            placement: "right",
                            container: 'body'
                        });
                    }
                });
            }

            app.init(config);
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="showcase-title">${title}</div>

        <filter params="name: 'automaticExtensionMode'"></filter>

        <div class="row">
            <div class="col-xs-2 filter-block-custom" style="min-width: 192px;" >
                <div class="filter-section filter-block-height">
                    <div class="filter-section-header">Период</div>
                    <div class="filter-element">
                        <filter params="name: 'startDate'"></filter>
                    </div>
                    <div class="filter-element">
                        <filter params="name: 'endDate'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-4 filter-block-custom" style="min-width: 400px;" >
                <div class="filter-section filter-block-height">
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
                        <filter params="name: 'salePlaceIds'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-4 filter-block-custom" style="min-width: 400px;">
                <div class="filter-section filter-block-height">
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
                        <filter params="name: 'mvkSignId'"></filter>
                    </div>
                </div>
            </div>
            <div class="col-xs-2 filter-block-custom" style="min-width: 192px;">
                <div class="filter-section filter-block-height">
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

        <div class="row">
            <div>
                <div class="filter-element filter-block-custom">
                    <reset-filter-button></reset-filter-button>
                </div>
                <div class="filter-element filter-block-custom">
                    <refresh-button params="text: 'Выполнить'"></refresh-button>
                </div>
            </div>
        </div>

        <filter-log></filter-log>

        <div data-bind="visible: visibleCharts" class="charts-container">

            <tab-strip params="name: 'dayDistribution'">
                <tab>
                    <chart params="name: 'distributionDynamicMoscow'"></chart>
                </tab>
                <tab>
                    <chart params="name: 'distributionDynamicRegions'"></chart>
                </tab>
            </tab-strip>

            <div class="chart chart-inner details-report-wrapper">
                Детализированный отчет по картам со сроком доставки более
                <!-- ko ifnot: groups.default.filters.automaticExtensionMode.value -->
                <filter params="name: 'daysLowerBound', group: 'details'" style="display: inline-block;"></filter>
                дней
                <!-- /ko -->
                <!-- ko if: groups.default.filters.automaticExtensionMode.value -->
                трех недель
                <!-- /ko -->
                <report-button params="reportUrl: 'CardsDeliveryPeriodDetailsReport', group: 'details',
                style: {'margin-top': '1px'}"></report-button>
                <csv-report-button params="reportUrl: 'CardsDeliveryPeriodDetailsCsvReport', group: 'details',
                style: {'margin-top': '1px'}"></csv-report-button>
            </div>

            <!-- ko ifnot: groups.default.filters.automaticExtensionMode.value() -->
            <div class="row chart">
                <div class="col-xs-8">
                    <tab-strip params="name: 'dynamic'">
                        <tab>
                            <chart params="name: 'periodDynamicMoscow'"></chart>
                        </tab>
                        <tab>
                            <chart params="name: 'periodDynamicRegion'"></chart>
                        </tab>
                    </tab-strip>
                </div>
                <div class="col-xs-4">
                    <tab-strip params="name: 'pie'">
                        <tab>
                            <chart params="name: 'pieMoscow'"></chart>
                        </tab>
                        <tab>
                            <chart params="name: 'pieRegions'"></chart>
                        </tab>
                    </tab-strip>
                </div>
            </div>

            <div class="chart">
                <chart params="name: 'table'"></chart>
            </div>
            <!-- /ko -->
        </div>
    </jsp:body>
</t:layout>