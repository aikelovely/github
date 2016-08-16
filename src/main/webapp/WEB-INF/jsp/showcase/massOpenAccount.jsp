<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Открытие клиентских счетов. KPI и метрики"/>

<t:layout title="${title}">

    <jsp:attribute name="css">
        <style scoped>
            .two-checkboxes .checkbox {
                margin: 6px 0 0 !important;
            }

        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <style>

        </style>

        <script>
            var config = {
                groups: [{
                    filters: {
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            enableClear: true,
                            withGroups: true,
                            dataSource: {
                                url: "MassFilter/Cities"
                            }
                        },
                        salesChannelIds: {
                            type: "Select",
                            multiple: true,
                            title: "Канал продаж",
                            dataSource: {
                                url: "MassFilter/SalesChannel"
                            }
                        },
                        bpTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Процесс",
                            dataSource: {
                                url: "MassFilter/BpTypes"
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
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "massFilter/timeUnits",
                                computed: {
                                    jsFunc: function (context) {
                                        var data = context.loadedData,
                                                startDate = moment(context.params.startDate),
                                                endDate = moment(context.params.endDate);
                                        var durationInDays = endDate.diff(startDate, "days");
                                        return _.filter(data, function (x) {
                                            return x.maxDays >= durationInDays;
                                        });
                                    },
                                    params: ["startDate", "endDate"]
                                }
                            },
                            width: 125
                        },
                        systemUnitIds: {
                            type: "Select",
                            multiple: true,
                            optionsCaption: false,
                            defaultValues: ["1", "2"],
                            title: "Единица сети",
                            dataSource: {
                                url: "MassFilter/SystemUnits"
                            },
                            width: 150
                        },
                        includeOnfInRk: {
                            type: "CheckBox",
                            title: "Процесс с этапом «Правка фронт-офиса»"
                        },
                        includeDecisionInRk: {
                            type: "CheckBox",
                            title: "Процесс с этапом «Принятие решения по открытию счета»"
                        }
                    },
                    charts: {
                        dynamicByDay: {
                            jsFunc: createDynamic,
                            dataSource: "MassOpenAccountDynamicByDay"
                        },
                        dynamicBy3Day: {
                            jsFunc: createDynamic,
                            dataSource: "MassOpenAccountDynamicBy3Day"
                        },
                        donut: {
                            jsFunc: createDonut,
                            dataSource: "MassOpenAccountDonut"
                        },
                        ratingTopN: {
                            jsFunc: createRating,
                            dataSource: "MassOpenAccountRatingTopN"
                        },
                        ratingBottomN: {
                            jsFunc: createRating,
                            dataSource: "MassOpenAccountRatingBottomN"
                        },
                        detailsTable: {
                            jsFunc: createDetailsTable,
                            dataSource: "MassOpenAccountDetailsTable",
                            reportUrl: "MassMetricsOpenAccountReport"
                        }
                    },
                    tabStrips: {
                        dynamic: {
                            header: "Динамика распределения доли открытых счетов",
                            tabs: ["За 24 часа", "За 3 дня"]
                        }
                    },
                    slves: []
                }],
                cookies: true
            };

            function createDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var width = 768;

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: ''},
                        chart: {
                            height: 100
//                            width: width
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                $container.highcharts({
                    chart: {
                        type: "areaspline",
//                        width: width,
                        height: 300
                    },
                    title: {text: ''},
                    plotOptions: {
                        series: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                formatter: function () {
                                    if (this.y == null) return '';
                                    return this.y > 0 ? this.y.toFixed(1) : '';
                                },
                                zIndex: 4
                            },
                            tooltip: {
                                pointFormat: "<span>{series.name}</span>: <b>{point.y:.1f}%"
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

            function createDonut($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var title = "Распределение открытых счетов по длительности открытия";

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
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
                        type: 'pie',
                        height: 240
                    },
                    title: {text: title},
                    tooltip: {
                        pointFormat: "<b>{point.percentage:.2f}%</b> ({point.y} счет.)</b>"
                    },
                    plotOptions: {
                        pie: {
                            size: 150,
                            innerSize: 90,
                            center: [500, 60],
                            dataLabels: {
                                enabled: true,
                                distance: 5,
                                format: "<b>{percentage:.0f}%</b> ({y} счет.)"
                            },
                            showInLegend: true
                        }
                    },
                    series: chart.series,
                    legend: {
                        enabled: true,
                        x: 750,
                        y: 70,
                        align: "left",
                        verticalAlign: "top",
                        layout: "vertical",
                        floating: true,
                        useHTML: true
                    }
                });
            }

            function createRating($container, filterData, jsonData, customParams) {
                var rows = jsonData;

                var systemUnitOptions = app.viewModel.getFilter("systemUnitIds").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети";
                var th = $container.parent().find("tr > th")[0];
                $(th).text(systemUnitName);

                _.each(rows, function (row) {
                    var $tr = $("<tr />");

                    var $td = $("<td />", {
                        text: row.unitName
                    });
                    $td.appendTo($tr);

                    $td = $("<td />", {
                        text: row.percent.toFixed(1) + "%"
                    });
                    $td.appendTo($tr);

                    var arrowCss = row.diff > 0
                            ? "icon-green-arrow"
                            : row.diff < 0
                            ? "icon-red-arrow"
                            : "icon-yellow-arrow";
                    $td = $("<td />", {
                        html: "<i class=\"" + arrowCss + "\"></i>" + row.diff.toFixed(1) + "%"
                    });
                    $td.appendTo($tr);

                    $tr.appendTo($container);
                });
            }

            function getDetailsColumns(systemUnitName){
                var percentFormat = "{0:n1}%";

                return [
                    {
                        field: "unitName",
                        title: systemUnitName,
                        width: 200
                    },
                    {
                        field: "bpCount",
                        title: "Кол-во счетов"
                    },
                    {
                        field: "inCrmPercent",
                        title: "Доля счетов в CRM",
                        format: "-"
                    },
                    {
                        field: "bpGrp1Percent",
                        title: "Доля открытых за 24 часа",
                        format: percentFormat
                    },
                    {
                        field: "bpGrp2Percent",
                        title: "Более 24 часов",
                        format: percentFormat
                    },
                    {
                        field: "bpGrp3Percent",
                        title: "От 24 часов до 3 дней",
                        format: percentFormat
                    },
                    {
                        field: "bpGrp4Percent",
                        title: "От 3 до 5 дней",
                        format: percentFormat
                    },
                    {
                        field: "bpGrp5Percent",
                        title: "Более 5 дней",
                        format: percentFormat
                    },
                    {
                        field: "avgDurationInDays",
                        title: "Среднее время",
                        template: formatAvgDuration
                    },
                    {
                        field: "maxDurationInDays",
                        title: "Макс. срок открытия",
                        template: "#= app.chartUtils.daysToDDHH(data.maxDurationInDays) #"
                    }];
            }

            function formatAvgDuration(dataItem) {
                var value = dataItem.avgDurationInDays;

                if (value > 1) {
                    return "<span style='color: red;'>" +
                            app.chartUtils.daysToDDHH(value) +
                            "</span>";
                } else {
                    return "<span style='color: green;'>" +
                            app.chartUtils.daysToDDHH(value) +
                            "</span>";
                }
            }

            function createDetailsTable($container, filterData, jsonData, customParams) {
                var systemUnitOptions = app.viewModel.getFilter("systemUnitIds").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети",
                        rows = jsonData;

                var columns = getDetailsColumns(systemUnitName);

                if (rows && rows.length > 1) {
                    // Footer
                    columns[0].footerTemplate = "Итоги";

                    var footerRow = rows.splice(-1, 1)[0];
                    _.forEach(columns, function (c) {

                        var value = _.get(footerRow, c.field);
                        if (value === null || value === undefined) return;

                        if (c.field === "bpCount") {
                            c.footerTemplate = "" + value;
                        }
                        else if (c.field.indexOf("bpGrp") !== -1) {
                            value = parseFloat(value);
                            c.footerTemplate = value.toFixed(1) + "%";
                        }
                        else if (c.field === "inCrmPercent") {
                            c.footerTemplate = "-";
                        }
                        else if (c.field === "avgDurationInDays") {
                            c.footerTemplate = formatAvgDuration(footerRow);
                        } else if (c.field === "maxDurationInDays") {
                            c.footerTemplate = app.chartUtils.daysToDDHH(value);
                        }
                    });
                }

                var detailInit = null;

                if(systemUnitOptions.length > 1){
                    detailInit = function(e){
                        var $container = $("<div/>").appendTo(e.detailCell),
                                systemUnit = systemUnitOptions[1],
                                parentId = e.data.unitId;

                        var extender = {
                            systemUnitIds: [systemUnit.id],
                            cityIds: filterData.systemUnitIds[0] == 1 ? [parentId] : filterData.cityIds,
                            salesChannelIds: filterData.systemUnitIds[0] == 2 ? [parentId] : filterData.salesChannelIds,
                            bpTypeIds: filterData.systemUnitIds[0] == 3 ? [parentId] : filterData.bpTypeIds,
                            dopOfficeIds: filterData.systemUnitIds[0] == 4 ? [parentId] : filterData.dopOfficeIds
                        };

                        var filterData2Level = _.assign({}, filterData, extender);

                        $container.kendoGrid({
                            dataSource: {
                                transport: {
                                    read: function (o) {
                                        var request = app.ajaxUtils.postData("MassOpenAccountDetailsTable",
                                                JSON.stringify(filterData2Level));
                                        request.done(function (ajaxResult) {
                                            if (ajaxResult.success) {
                                                if(ajaxResult.data && ajaxResult.data.length > 1){
                                                    ajaxResult.data.splice(-1, 1);
                                                }
                                                o.success(ajaxResult.data);

                                                if (app.viewModel.showSQL()) {
                                                    console.log("\n" + ajaxResult.queryList[0].sql);
                                                }
                                            } else {
                                                app.showAlert(ajaxResult.message);
                                            }
                                        });
                                    }
                                }
                            },
                            columns: getDetailsColumns(systemUnit.name)
                        });
                    }
                }

                $container.kendoGrid({
                    dataSource: {data: rows},
                    sortable: true,
                    columns: columns,
                    detailInit: detailInit
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
                    <filter params="name: 'cityIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'salesChannelIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'bpTypeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'dopOfficeIds'"></filter>
                </div>
            </div>
            <div class="filter-row">
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
                    <filter params="name: 'systemUnitIds'"></filter>
                </div>
                <div class="filter-element two-checkboxes">
                    <filter params="name: 'includeOnfInRk'"></filter>
                    <filter params="name: 'includeDecisionInRk'"></filter>
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
            <div class="chart">
                <tab-strip params="name: 'dynamic'">
                    <tab>
                        <chart params="name: 'dynamicByDay'"></chart>
                    </tab>
                    <tab>
                        <chart params="name: 'dynamicBy3Day'"></chart>
                    </tab>
                </tab-strip>
            </div>
            <div class="chart">
                <chart params="name: 'donut'"></chart>
            </div>
            <div class="chart">
                <div class="row">
                    <div class="col-xs-6">
                        <chart params="name: 'ratingTopN'">
                            <table class="table-sparkline">
                                <colgroup>
                                    <col style="width: 180px;"/>
                                    <col style="width: 200px;"/>
                                    <col style="width: 200px;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>Город</th>
                                    <th>Доля счетов открытых за 24 часа</th>
                                    <th>Динамика относительно предыдущего периода</th>
                                </tr>
                                </thead>
                                <tbody class="chart-container">
                                </tbody>
                            </table>
                        </chart>
                    </div>
                    <div class="col-xs-6">
                        <chart params="name: 'ratingBottomN'">
                            <table class="table-sparkline">
                                <colgroup>
                                    <col style="width: 180px;"/>
                                    <col style="width: 200px;"/>
                                    <col style="width: 200px;"/>
                                </colgroup>
                                <thead>
                                <tr>
                                    <th>Город</th>
                                    <th>Доля счетов открытых за 24 часа</th>
                                    <th>Динамика относительно предыдущего периода</th>
                                </tr>
                                </thead>
                                <tbody class="chart-container">
                                </tbody>
                            </table>
                        </chart>
                    </div>
                </div>
            </div>
            <div class="chart">
                <chart params="name: 'detailsTable'">
                    <p class="table-header table-header-without-report">Детализированный отчёт</p>
                    <report-button params="group: group, reportUrl: reportUrl"></report-button>
                    <div class="chart-container"></div>
                </chart>
            </div>
        </div>
    </jsp:body>
</t:layout>

