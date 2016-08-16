<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Скорость заведения зарплатного проекта"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .modal .k-grid .k-grid-content {
                max-height: 400px;
            }

            .modal-dialog {
                width: 900px;
            }

            .large-modal .modal-dialog {
                width: 1300px;
            }

            chart[params$="'rating'"] .highcharts-tooltip span {
                height: auto;
                min-width: 200px;
                overflow: auto;
                white-space: normal !important;
            }
        </style>
    </jsp:attribute>

    <jsp:attribute name="js">
        <script>
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
                        openingTypeId: {
                            type: "Select",
                            multiple: false,
                            title: "Тип открытия",
                            dataSource: {
                                url: "ZpCommonFilter/OpeningTypes"
                            },
                            width: 175
                        },
                        bushIds: {
                            type: "Select",
                            multiple: true,
                            title: "Куст",
                            enableClear: true,
                            dataSource: {
                                url: "ZpCommonFilter/Bushes"
                            }
                        },
                        operationOfficeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Операционный офис",
                            enableSearch: true,
                            enableClear: true,
                            dataSource: {
                                url: "ZpCommonFilter/OperationOffices",
                                params: ["bushIds"]
                            }
                        },
                        cityIds: {
                            type: "Select",
                            multiple: true,
                            title: "Город",
                            enableSearch: true,
                            enableClear: true,
                            withGroups: true,
                            dataSource: {
                                url: "ZpCommonFilter/Cities",
                                params: ["bushIds", "operationOfficeIds", "openingTypeId"]
                            }
                        },
                        managerIds: {
                            type: "Select2",
                            multiple: true,
                            title: "Менеджер",
                            enableSearch: true,
                            enableClear: true,
                            dataSource: {
                                url: "ZpCommonFilter/Managers",
                                params: ["bushIds", "operationOfficeIds", "cityIds", "openingTypeId"]
                            },
                            pageSize: 100,
                            width: 250
                        },
                        companyIds: {
                            type: "Select2",
                            multiple: true,
                            title: "Компания",
                            enableClear: true,
                            dataSource: {
                                url: "ZpCommonFilter/Companies",
                                params: ["bushIds", "operationOfficeIds", "cityIds", "managerIds", "openingTypeId"]
                            },
                            width: 350
                        },
                        topCount: {
                            title: "Топ",
                            type: "NumericTextBox",
                            defaultValue: 5,
                            min: 3,
                            max: 10
                        },
                        schemaTypeIds: {
                            type: "Select",
                            multiple: true,
                            title: "Тип схемы",
                            dataSource: {
                                url: "ZpCommonFilter/SchemaTypes"
                            },
                            width: 160
                        },
                        processStageIds: {
                            type: "Select",
                            multiple: true,
                            title: "Этап",
                            dataSource: {
                                url: "ZpCommonFilter/ProcessStages"
                            },
                            disableIfComputed: {
                                params: ["openingTypeId"],
                                jsFunc: function (context) {
                                    return context.params.openingTypeId != 1
                                }
                            },
                            width: 215,
                            validationRules: [
                                {
                                    type: "Custom",
                                    validator: function (val) {
                                        // Нельзя выбрать только 1 и 3 этап
                                        return !(val && val.length == 2 && val[0] == getStageIdByCode(1) && val[1] == getStageIdByCode(3));
                                    },
                                    message: "Неправильный выбор этапов"
                                }
                            ]
                        },
                        subProcessStageId: {
                            type: "Select",
                            multiple: false,
                            title: "Подэтап",
                            dataSource: {
                                url: "ZpCommonFilter/SubProcessStages",
                                params: ["processStageIds"]
                            },
                            disableIfComputed: {
                                params: ["openingTypeId", "processStageIds"],
                                jsFunc: function (context) {
                                    var stageIds = context.params.processStageIds || [];
                                    return context.params.openingTypeId != 1 ||
                                            stageIds.length !== 1;
                                }
                            },
                            width: 215,
                            optionsCaption: "Все"
                        },
                        paramType: {
                            type: "Select",
                            multiple: false,
                            title: "Отображение параметра",
                            dataSource: {
                                data: [
                                    {id: "Percent", name: "Доля в KPI"},
                                    {id: "AvgDuration", name: "Время/Проекты"}
                                ]
                            },
                            width: 160
                        },
                        withClientWait: {
                            type: "Select",
                            multiple: false,
                            title: "Клиентское ожидание",
                            dataSource: {
                                data: [
                                    {id: 0, name: "Без клиентского ожидания"},
                                    {id: 1, name: "С клиентским ожиданием"}
                                ]
                            },
                            disableIfComputed: {
                                params: ["processStageIds", "subProcessStageId"],
                                jsFunc: function (context) {
                                    var processStageIds = context.params.processStageIds || [],
                                            subProcessStageId = context.params.subProcessStageId;
                                    try {
                                        var enabled = processStageIds.indexOf(getStageIdByCode(1)) >= 0
                                                || processStageIds.indexOf(getStageIdByCode(2)) >= 0 ||
                                                [getSubStageIdByCode(107),
                                                    getSubStageIdByCode(106),
                                                    getSubStageIdByCode(203)].indexOf(subProcessStageId) >= 0;
                                        return !enabled;
                                    } catch (e) {
                                        return false;
                                    }
                                }
                            },
                            width: 220
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "ZpCommonFilter/TimeUnits"
                            },
                            width: 120
                        },
                        systemUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Детализация",
                            dataSource: {
                                url: "ZpCommonFilter/SystemUnits"
                            },
                            width: 110
                        }
                    },
                    slaves: [{
                        name: "emission",

                        charts: {
                            emissionTable: {
                                deferred: true,
                                jsFunc: createEmissionTable,
                                dataSource: "ZPOpeningSpeedEmission"
                            }
                        }
                    }],
                    tabStrips: {
                        dynamicWithPie: {
                            tabs: ["", "Доля компаний с данными CRM"],
                            dynamicConfig: {
                                jsFunc: function (tabs, params) {
                                    var paramType = params.paramType,
                                            openingTypeId = params.openingTypeId;

                                    var firstTabTitle = paramType === "Percent"
                                            ? "Динамика доли проектов в пределах KPI"
                                            : "Динамика средней скорости";

                                    tabs[0].title(firstTabTitle);
                                    tabs[1].visible(openingTypeId != 2);
                                },
                                params: ["paramType", "openingTypeId"]
                            }
                        },
                        details: {
                            header: "Сводный отчет",
                            tabs: ["", "Количество ЮЛ", "Доля компаний с данными CRM, %"],
                            dynamicConfig: {
                                jsFunc: function (tabs, params) {
                                    var paramType = params.paramType,
                                            openingTypeId = params.openingTypeId,
                                            subProcessStageId = params.subProcessStageId;

                                    var firstTabTitle = paramType === "Percent"
                                            ? "KPI, %"
                                            : "KPI, " + (subProcessStageId ? "ч" : "дн");

                                    var thirdTabTitle = paramType === "Percent"
                                            ? "Доля компаний с данными CRM, %"
                                            : "Количество компаний в АЗОН";

                                    tabs[0].title(firstTabTitle);

                                    tabs[1].visible(openingTypeId != 2);

                                    tabs[2].title(thirdTabTitle);
                                    tabs[2].visible(openingTypeId != 2);
                                },
                                params: ["paramType", "openingTypeId", "subProcessStageId"]
                            },
                            reportUrl: "ZPOpeningSpeedDetailsReport"
                        },
                        quality: {
                            tabs: ["Заведение ЮЛ", "Открытие счета 1-му ФЛ"],
                            dynamicConfig: {
                                jsFunc: function (tabs, params) {
                                    var openingTypeId = params.openingTypeId,
                                            processStageIds = params.processStageId;

                                    if (openingTypeId == 2) {
                                        tabs[0].visible(false);
                                        tabs[1].visible(false);
                                        return;
                                    }

                                    tabs[0].visible(arrayContainsOrEmpty(processStageIds, getStageIdByCode(1)));
                                    tabs[1].visible(arrayContainsOrEmpty(processStageIds, getStageIdByCode(2)));
                                },
                                params: ["openingTypeId", "processStageIds"]
                            }
                        }
                    },
                    charts: {
                        dynamicWithPie: {
                            jsFunc: createDynamicWithPie,
                            dataSource: "ZPOpeningSpeedDynamicWithPie"
                        },
                        projectDynamicWithPie: {
                            jsFunc: createProjectDynamicWithPie,
                            dataSource: "ZPOpeningSpeedProjectDynamicWithPie"
                        },
                        rating: {
                            jsFunc: createRating,
                            dataSource: "ZPOpeningSpeedRating"
                        },
                        topNCount: {
                            jsFunc: createTopOrWorstNCount,
                            dataSource: "ZPOpeningSpeedTopOrWorstNCount",
                            customParams: {isTop: true}
                        },
                        worstNCount: {
                            jsFunc: createTopOrWorstNCount,
                            dataSource: "ZPOpeningSpeedTopOrWorstNCount",
                            customParams: {isTop: false}
                        },
                        detailsKpiTable: {
                            jsFunc: createDetailsTable,
                            dataSource: "ZPOpeningSpeedDetailsTable",
                            customParams: {isKpiTable: true}
                        },
                        detailsULCountTable: {
                            jsFunc: createDetailsTable,
                            dataSource: "ZPOpeningSpeedDetailsTable",
                            customParams: {ulCount: true}

                        },
                        projectDetailsTable: {
                            jsFunc: createDetailsTable,
                            dataSource: "ZPOpeningSpeedProjectDetailsTable",
                            customParams: {projectDetails: true}
                        },
                        crmFillQualityDetails1: {
                            jsFunc: createCrmFillQualityDetailsTable,
                            dataSource: "ZPOpeningSpeedCrmFillQualityDetailsTable",
                            customParams: {processStageCode: 1}
                        },
                        crmFillQualityDetails2: {
                            jsFunc: createCrmFillQualityDetailsTable,
                            dataSource: "ZPOpeningSpeedCrmFillQualityDetailsTable",
                            customParams: {processStageCode: 2}
                        },
                        crmFillQualityDynamic: {
                            jsFunc: createCrmFillQualityDynamic,
                            dataSource: "ZPOpeningSpeedCrmFillQualityDynamic"
                        }
                    }
                }],
                cookies: true
            };

            function arrayContainsOrEmpty(array, value) {
                if (array === undefined || array === null || array.length === 0) return true;
                return $.inArray(value, array) !== -1;
            }

            function getStageIdByCode(code) {
                if (app.viewModel) {
                    var filter = app.viewModel.getFilter("processStageIds");
                    if (filter) {
                        var processStage = _.find(filter.options(), function (option) {
                            return option.code == code;
                        });
                        if (processStage) {
                            return processStage.id.toString();
                        }
                    }
                }
                return null;
            }

            function getStageCodeById(id) {
                if (app.viewModel) {
                    var filter = app.viewModel.getFilter("processStageIds");
                    if (filter) {
                        var processStage = _.find(filter.options(), function (option) {
                            return option.id == id;
                        });
                        if (processStage) {
                            return processStage.code.toString();
                        }
                    }
                }
                return null;
            }

            function getSubStageIdByCode(code) {
                if (app.viewModel) {
                    var filter = app.viewModel.getFilter("subProcessStageId");
                    var processStage = _.find(filter.options(), function (option) {
                        return option.code == code;
                    });
                    if (processStage) {
                        return processStage.id.toString();
                    }
                }
                return null;
            }

            function openingSchemaTypesComputed(context) {
                var data = context.loadedData,
                        cityIds = context.params.cityIds || [],
                        viewModel = app.viewModel;

                if (cityIds.length > 0 && viewModel) {
                    var cityIdsOptions = viewModel.getFilter('cityIds').options(),
                            containsMoscow = false,
                            containsRegions = false,
                            indexToRemove = -1,
                            i,
                            id;

                    function checkCityOptions(options) {
                        for (i = 0; i < options.length; i++) {
                            id = options[i].id;
                            if (ko.utils.arrayIndexOf(cityIds, id) !== -1) {
                                return true;
                            }
                        }
                        return false;
                    }

                    ko.utils.arrayForEach(cityIdsOptions, function (options) {
                        if (options.name === "Москва и Московская Область") {
                            containsMoscow = checkCityOptions(options.options);
                            return;
                        }
                        if (options.name === "Регионы") {
                            containsRegions = checkCityOptions(options.options);
                        }
                    });

                    if (!containsMoscow) {
                        // схему 5 нужно выводить в фильтр, только если выбрана Москва
                        for (i = 0; i < data.length; i++) {
                            if (data[i].id == 5) {
                                indexToRemove = i;
                                break;
                            }
                        }
                    }

                    if (!containsRegions) {
                        // схему 6, только если выбраны Регионы
                        for (i = 0; i < data.length; i++) {
                            if (data[i].id == 6) {
                                indexToRemove = i;
                                break;
                            }
                        }
                    }

                    if (indexToRemove > 0) {
                        data.splice(indexToRemove, 1);
                    }
                }

                return data;
            }

            function legendFormatter() {
                var legendItem = this;

                function formatPieLegnd(item, divAttr) {
                    return "<div " + divAttr + ">" + item.name + " <p style='font-weight: normal'>" + item.y + " проектов </p><div>";
                }

                switch (this.name) {
                    case "В KPI":
                    case "С данными CRM":
                        return formatPieLegnd(legendItem, "");
                    case "За пределами KPI":
                    case "Без данных CRM":
                        return formatPieLegnd(legendItem, "style='margin-bottom: 50px'");

                    default:
                        return legendItem.name;
                }
            }

            function createDynamicWithPie($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var title = chart.bag.title,
                        width = 1184;

                $container.siblings().find(".table-header").html(title);

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
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false),
                        isPercent = filterData.paramType === "Percent";

                var averageValue = chart.bag.averageValue;
                var kpiValue = chart.bag.kpiValue;
                var plotLines;


                if (!isPercent) {
                    var unitText = _.isEmpty(filterData.subProcessIds) ? " дн" : " ч";

                    plotLines = [{
                        color: 'lightgray',
                        label: {
                            align: 'left',
                            text: '<b>Среднее</b>: {0}{1}'.format(averageValue.toFixed(0), unitText),
                            zIndex: 8,
                            x: 0
                        },
                        value: averageValue,
                        width: 2,
                        zIndex: 7
                    }, {
                        color: 'red',
                        label: {
                            align: 'right',
                            text: '<b>KPI</b>: {0}{1}'.format(kpiValue.toFixed(0), unitText),
                            zIndex: 8,
                            x: 15
                        },
                        value: kpiValue,
                        width: 2,
                        zIndex: 7
                    }];

                    var tooltip = "<span>{series.name}</span>: <b>{point.y:.1f}" + unitText;

                    if (filterData.withClientWait == 0) {
                        tooltip += "</b><br/>Клиентское время выделено для <b>{point.totalCwCount}</b> проектов";
                    }

                    $container.highcharts({
                        chart: {
                            type: "column",
//                            width: width,
                            marginRight: 220,
                            marginTop: 50
                        },
                        title: {text: ''},
                        plotOptions: {
                            column: {
                                showInLegend: false,
                                dataLabels: {
                                    enabled: xAxis.ticks.length < 25,
                                    inside: true,
                                    formatter: function () {
                                        return this.y.toFixed(1);
                                    },
                                    zIndex: 4
                                },
                                tooltip: {
                                    pointFormat: tooltip
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
                                    pointFormat: "<b>{point.y}</b> проектов ({point.percentage:.2f}%)"
                                }
                            },
                            line: {
                                color: "#0F79D4",
                                marker: {
                                    enabled: false
                                },
                                tooltip: {
                                    pointFormat: "<span>{series.name}</span>: <b>{point.y:.1f}" + unitText + "</b>"
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: xAxis,
                        yAxis: {
                            endOnTick: false,
                            min: 0,
                            minRange: kpiValue,
                            title: {text: ''},
                            labels: {
                                format: '{value}' + unitText
                            },
                            plotLines: plotLines
                        },
                        series: series,
                        legend: {
                            enabled: true,
                            x: -20,
                            y: 200,
                            align: "right",
                            verticalAlign: "top",
                            layout: "vertical",
                            useHTML: true,
                            labelFormatter: legendFormatter
                        }
                    });
                }
                else {
                    plotLines = [{
                        color: 'lightgray',
                        label: {
                            align: 'left',
                            text: '<b>Среднее</b>: {0}%'.format(averageValue.toFixed(1)),
                            zIndex: 8,
                            x: 0
                        },
                        value: averageValue,
                        width: 2,
                        zIndex: 7
                    }, {
                        color: 'red',
                        label: {
                            align: 'right',
                            text: '<b>KPI</b>: {0}%'.format(kpiValue.toFixed(1)),
                            zIndex: 8,
                            x: 15
                        },
                        value: kpiValue,
                        width: 2,
                        zIndex: 7
                    }];

                    $container.highcharts({
                        chart: {
                            type: "column",
//                            width: width,
                            marginRight: 220,
                            marginTop: 50
                        },
                        title: {text: ''},
                        plotOptions: {
                            column: {
                                showInLegend: false,
                                dataLabels: {
                                    enabled: xAxis.ticks.length < 31,
                                    inside: true,
                                    formatter: function () {
                                        return this.y < 3 ? '' : this.y.toFixed(1) + "%";
                                    },
                                    zIndex: 4
                                },
                                tooltip: {
                                    pointFormat: "В KPI: <b>{point.inKpiCount}</b> ({point.y:.2f}%)<br/>" +
                                    "За пределами KPI: <b>{point.outKpiCount}</b> ({point.outKpiPercent:.2f}%)"
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
                                    pointFormat: "<b>{point.y}</b> проектов ({point.percentage:.2f}%)"
                                }
                            },
                            line: {
                                color: "#0F79D4",
                                marker: {
                                    enabled: false
                                },
                                tooltip: {
                                    pointFormat: "<span>{series.name}</span>: <b>{point.y:.2f}%</b>"
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        xAxis: xAxis,
                        yAxis: {
                            min: 0,
                            max: 100,
                            title: {text: ''},
                            labels: {
                                format: '{value}%'
                            },
                            plotLines: plotLines
                        },
                        series: series,
                        legend: {
                            enabled: true,
                            x: -20,
                            y: 200,
                            align: "right",
                            verticalAlign: "top",
                            layout: "vertical",
                            useHTML: true,
                            labelFormatter: legendFormatter
                        }
                    });
                }
            }

            function createProjectDynamicWithPie($container, filterData, jsonData, customParams) {
                var chart = jsonData[0];

                var width = 1184;

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
                        type: "column",
//                        width: width,
                        marginRight: 220
                    },
                    title: {text: ''},
                    plotOptions: {
                        column: {
                            color: "rgb(124, 181, 236)",
                            showInLegend: false,
                            dataLabels: {
                                enabled: xAxis.ticks.length < 31,
                                inside: true,
                                formatter: function () {
                                    return this.y < 3 ? '' : this.y.toFixed(0) + "%";
                                },

                                zIndex: 4
                            },
                            tooltip: {
                                pointFormat: "{series.name}: <b>{point.y:.2f}%</b><br/>" +
                                "Количество ЮЛ: <b>{point.startedOkCount}</b><br/>" +
                                "Количество компаний в АЗОН: <b>{point.companyCount}</b>"
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
                                pointFormat: "<b>{point.y}</b> проектов ({point.percentage:.2f}%)"
                            }
                        }
                    },
                    tooltip: {
                        useHTML: true
                    },
                    xAxis: xAxis,
                    yAxis: {
                        min: 0,
                        max: 100,
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        x: -20,
                        y: 200,
                        align: "right",
                        verticalAlign: "top",
                        layout: "vertical",
                        useHTML: true,
                        labelFormatter: legendFormatter
                    }
                });
            }

            function createRating($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        rowCount = chart.series[0].data.length,
                        categories = chart.bag.categories,
                        width = 440,
                        kpiValue = chart.bag.kpiValue,
                        title = filterData.paramType === "AvgDuration"
                                ? "Средняя скорость"
                                : "Доля проектов в пределах KPI";

                $container.siblings(".scrollable-chart-title").text(title);

                if (rowCount === 0) {
                    $container.highcharts({
                        title: {text: ''},
                        chart: {
                            height: 100,
//                            width: width
                        }
                    });
                    return;
                }

                var marginLeft = null;
                if (filterData.systemUnitId == 3) marginLeft = 250;
                if (filterData.systemUnitId == 2) marginLeft = 200;


                var emissionGroup = app.viewModel.getGroup("emission");
                var $modal = $(".modal");

                var markers = chart.series[1];
                if (markers) {
                    _.each(markers.data, function (point) {
                        point.events = {
                            click: function () {
                                var drillDownOptions = {};
                                switch (filterData.systemUnitId) {
                                    case "1":
                                        drillDownOptions.cityIds = [point.tag];
                                        break;
                                    case "2":
                                        drillDownOptions.managerIds = [point.tag];
                                        break;
                                    case "3":
                                        drillDownOptions.companyIds = [point.tag];
                                        break;
                                    case "11":
                                        drillDownOptions.bushIds = [point.tag];
                                        break;
                                    case "12":
                                        drillDownOptions.operationOfficeIds = [point.tag];
                                        break;
                                }

                                $modal.modal("show");
                                emissionGroup.drillDown(1, drillDownOptions);
                                emissionGroup.showSingleChart("emissionTable");
                            }
                        }
                    });
                }

                if (filterData.paramType === "AvgDuration") {
                    var unitText = filterData.subProcessStageId ? " ч" : " дн";

                    var yMax = _.maxBy(chart.series[0].data, "y");
                    var plotLineX = 8;
                    if(yMax && yMax.y){
                        plotLineX = kpiValue < yMax.y / 2 ? 8 : -65;
                    }

                    $container.highcharts({
                        chart: {
                            type: 'bar',
                            height: rowCount * 35 + 200,
//                            width: width,
                            marginLeft: marginLeft,
                            marginRight: 40,
                            events: {
                                load: function () {
                                    this.reflow();
                                }
                            }
                        },
                        plotOptions: {
                            bar: {
                                dataLabels: {
                                    enabled: true,
                                    inside: true,
                                    align: "right",
                                    formatter: function () {
                                        return this.point.y.toFixed(1) + unitText;
                                    },
                                    zIndex: 3
                                },
                                tooltip: {
                                    headerFormat: "{point.x}<br/>",
                                    pointFormat: "{point.name}"
                                },
                                turboThreshold: 0
                            },
                            line: {
                                lineWidth: 0,
                                tooltip: {
                                    headerFormat: "",
                                    pointFormat: "{series.name}"
                                },
                                marker: {
                                    symbol: "url({0})".format(app.rootUrl + "images/warning-triangle.png")
                                },
                                states: {
                                    hover: {
                                        lineWidthPlus: 0
                                    }
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        title: {text: ""},
                        xAxis: {
                            categories: categories,
                            title: {text: null}
                        },
                        yAxis: {
                            title: {text: null},
                            endOnTick: false,
                            plotLines: [{
                                color: 'red',
                                value: kpiValue,
                                width: 2,
                                zIndex: 5,
                                label: {
                                    text: '<b>KPI</b>: {0}{1}'.format(kpiValue, unitText),
                                    zIndex: 8,
                                    x: plotLineX,
                                    y: -1,
                                    rotation: 0
                                }
                            }]
                        },
                        legend: {enabled: false},
                        series: chart.series
                    });
                } else {
                    $container.highcharts({
                        chart: {
                            type: 'bar',
                            height: rowCount * 35 + 200,
//                            width: width,
                            marginLeft: marginLeft,
                            marginRight: 40,
                            events: {
                                load: function () {
                                    this.reflow();
                                }
                            }
                        },
                        plotOptions: {
                            bar: {
                                dataLabels: {
                                    enabled: true,
                                    inside: true,
                                    formatter: function () {
                                        if (this.key.indexOf("Пропущенно") !== -1) return "";
                                        return "{0}% ({1}/{2})".format(
                                                this.point.y.toFixed(2),
                                                this.point.inKpiCount,
                                                this.point.totalCount);
                                    },
                                    zIndex: 3
                                },
                                tooltip: {
                                    headerFormat: "{point.x}<br/>",
                                    pointFormat: "{point.y:.2f}% ({point.inKpiBpCount}/{point.totalCount})"
                                },
                                turboThreshold: 0
                            },
                            line: {
                                lineWidth: 0,
                                tooltip: {
                                    headerFormat: "",
                                    pointFormat: "{series.name}"
                                },
                                marker: {
                                    symbol: "url({0})".format(app.rootUrl + "images/warning-triangle.png")
                                },
                                states: {
                                    hover: {
                                        lineWidthPlus: 0
                                    }
                                }
                            }
                        },
                        tooltip: {
                            useHTML: true
                        },
                        title: {text: ""},
                        xAxis: {
                            categories: categories,
                            title: {text: null}
                        },
                        yAxis: {
                            title: {text: null},
                            max: filterData.systemUnitId != 3 ? 115 : 100,
                            endOnTick: false,
                            plotLines: [{
                                color: 'red',
                                value: kpiValue,
                                width: 2,
                                zIndex: 5,
                                label: {
                                    text: '<b>KPI</b>: {0}%'.format(kpiValue.toFixed(2)),
                                    zIndex: 8,
                                    x: kpiValue < 50 ? 8 : -65,
                                    y: -1,
                                    rotation: 0
                                }
                            }]
                        },
                        legend: {enabled: false},
                        series: chart.series
                    });
                }
            }

            function createTopOrWorstNCount($container, filterData, jsonData, customParams) {
                var title = customParams.isTop
                                ? "Топ {0} лучших".format(filterData.topCount)
                                : "Топ {0} худших".format(filterData.topCount),
                        data = _.find(jsonData, function (x) {
                            return x.isTop == customParams.isTop;
                        }),
                        unitText = filterData.subProcessStageId ? " ч" : " дн";

                $container.siblings(".scrollable-chart-title").text(title);

                var systemUnitOptions = app.viewModel.getFilter("systemUnitId").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети";

                var $table = $("<table/>", {
                    class: "table table-condensed"
                }).append(
                        $('<colgroup><col>' +
                        '<col style="width: 15%">' +
                        '<col style="width: 20%"></colgroup>'),
                        $('<thead/>'),
                        $('<tbody/>')
                );

                var $tableHeader = $("<tr/>");
                $tableHeader.append($("<th/>", {text: systemUnitName}));
                $tableHeader.append($("<th/>", {text: "Доля в KPI"}));
                $tableHeader.append($("<th/>", {text: "Средняя скорость"}));

                $("thead", $table).append($tableHeader);

                _.each(data.rows, function (item) {

                    $("tbody", $table).append(
                            $('<tr/>').append(
                                    $("<td />", {
                                        text: item.unitName,
                                        css: {'word-break': 'break-all'}
                                    }),
                                    $("<td/>", {text: (item.inKpiRatioAvg).toFixed(2) + "%"}),
                                    $("<td/>", {text: item.avgDuration.toFixed(1) + unitText})
                            )
                    );
                });

                $container.append($table);
            }

            function createDetailsTable($container, filterData, jsonData, customParams) {
                var greenCss = "label-success",
                        yellowCss = "label-warning",
                        redCss = "label-danger";

                var paramType = filterData.paramType,
                        isULCount = customParams.ulCount,
                        isKpiTable = customParams.isKpiTable,
                        isProjectDetails = customParams.projectDetails; // Третий таб

                var cellTemplate = "<span class='label label-cell {0}' title='{1}'>{2}</span>";
                var mainValueField = "";

                if (isULCount) {
                    mainValueField = "totalCount";
                }
                // Средняя длительность для Третьего таба
                else if (paramType == "AvgDuration" && isProjectDetails) {
                    mainValueField = "companyCount";
                }
                else {
                    if (isKpiTable) { // Первый таб, процент
                        jsonData = _.map(jsonData, function (dataItem) {
                            _.forOwn(dataItem, function (value, key) {
                                if (value !== null && _.isObject(value)) {
                                    dataItem[key].mainValue = (value.inKpiCount * 100 / value.totalCount).toFixed(0);
                                }
                            });
                            return dataItem;
                        })
                    }
                    else { // Третий таб, процент
                        jsonData = _.map(jsonData, function (dataItem) {
                            _.forOwn(dataItem, function (value, key) {
                                if (value !== null && _.isObject(value)) {
                                    dataItem[key].mainValue = value.startedOkCount * 100 / value.companyCount;
                                }
                            });
                            return dataItem;
                        })
                    }

                    mainValueField = "mainValue";
                }

                function formatCellValue(data, roundDecimals) {
                    if (data == null) return "";

                    // Второй таб "Количество ЮЛ"
                    if (isULCount) {
                        return data.totalCount;
                    }
                    // Средняя длительность для Третьего таба
                    if (paramType == "AvgDuration" && isProjectDetails) {
                        return data.companyCount;
                    }

                    var value = "", css = "", title = "";

                    if (isKpiTable && paramType == "AvgDuration") { // Первый таб, средняя длительность
                        value = Math.round(data.avgDuration * 100) / 100;
                        css = value <= data.durationThreshold ? greenCss : redCss;
                    } else {
                        if (isKpiTable) { // Первый таб, процент
                            value = (data.inKpiCount * 100 / data.totalCount).toFixed(0);
                            title = "{0}/{1}".format(data.inKpiCount, data.totalCount);
                            css = value >= data.inKpiThreshold
                                    ? greenCss
                                    : value >= 80
                                    ? yellowCss
                                    : redCss;
                        }
                        else { // Третий таб, процент
                            value = (data.startedOkCount * 100 / data.companyCount);
                            value = Math.round(value * 100) / 100;
                            css = value >= 90
                                    ? greenCss
                                    : value >= 70
                                    ? yellowCss
                                    : redCss;
                        }
                    }

                    return cellTemplate.format(css, title,
                            roundDecimals >= 0 ? Math.round(value * Math.pow(10, roundDecimals)) / Math.pow(10, roundDecimals) : value);
                }

                var systemUnitOptions = app.viewModel.getFilter("systemUnitId").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети",
                        columns = app.chartUtils.createDateTimeColumns(
                                filterData,
                                mainValueField,
                                formatCellValue);

                // Если больше одной колонки с датами
                if (columns.length > 1) {
                    // Задаем колонку "Итого"
                    columns.push({
                        field: "totals",
                        title: "Итого",
                        template: function (dataItem) {
                            return formatCellValue(dataItem.totals, 1);
                        },
                        width: columns[0].width,
                        sortable: {
                            compare: function (a, b) {
                                if (a["totals"] == null) return -1;
                                if (b["totals"] == null) return 1;

                                return a["totals"][mainValueField] - b["totals"][mainValueField];
                            }
                        }
                    });
                }

                var firstColumnWidth = filterData.systemUnitId == 2 ||
                filterData.systemUnitId == 11 ||
                filterData.systemUnitId == 12 ? 300 : 200;

                // Задаем первую колонку
                columns.unshift({
                    field: "unitName",
                    title: systemUnitName,
                    width: firstColumnWidth,
                    locked: true
                });

                $container.kendoGrid({
                    dataSource: {
                        data: jsonData
                    },
                    height: _.isEmpty(jsonData) ? 100 : 460,
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

            function createCrmFillQualityDetailsTable($container, filterData, jsonData, customParams) {
                var data = _.find(jsonData, function (x) {
                    return x.processId == getStageIdByCode(customParams.processStageCode);
                });

                var rows = data.rows || [],
                        subProcessStages = data.subProcessStages,
                        systemUnitOptions = app.viewModel.getFilter("systemUnitId").getSelectedOptions(),
                        systemUnitName = systemUnitOptions.length > 0 ? systemUnitOptions[0].name : "Единица сети";

                function formatCellValue(value) {
                    return (value.toFixed(2) * 100) / 100 + "%";
                }

                var columns = _.map(subProcessStages, function (stage) {
                    var columnName = "C" + stage.id;

                    return {
                        field: columnName,
                        title: stage.name,
                        template: function (dataItem) {
                            return formatCellValue(dataItem[columnName]);
                        }
                    };
                });

                var firstColumnWidth = filterData.systemUnitId == 2 ||
                filterData.systemUnitId == 11 ||
                filterData.systemUnitId == 12 ? 300 : 200;

                // Задаем первую колонку
                columns.unshift({
                    field: "unitName",
                    title: systemUnitName,
                    width: firstColumnWidth
                });

                // Задаем колонку "Среднее"
                columns.push({
                    field: "average",
                    title: "Общий % качества заполнения CRM",
                    template: function (dataItem) {
                        return formatCellValue(dataItem["average"]);
                    }
                });

                if (rows && rows.length > 1) {
                    // Footer
                    columns[0].footerTemplate = "Итоги";

                    var footerRow = rows.splice(-1, 1)[0];
                    _.forEach(columns, function (c) {
                        var value = _.get(footerRow, c.field);
                        if (value === null || value === undefined) return;
                        c.footerTemplate = formatCellValue(value);
                    });
                }

                $container.kendoGrid({
                    dataSource: {
                        data: rows
                    },
                    height: _.isEmpty(rows) ? 150 : 460,
                    sortable: true,
                    scrollable: true,
                    columns: columns
                });

                var grid = $container.data("kendoGrid");
                $(window).resize(function () {
                    grid.resize();
                });
            }

            function createCrmFillQualityDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        title = "Динамика качества заполнения CRM";

                if (chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
                    $container.highcharts({
                        title: {text: title},
                        chart: {
                            height: 100
                        }
                    });
                    return;
                }

                var series = chart.series,
                        xAxis = app.chartUtils.createDateTimeXAxis(filterData, false);

                xAxis.crosshair = true;

                $container.highcharts({
                    chart: {
                        type: "column"
                    },
                    title: {text: title},
                    plotOptions: {
                        column: {
                            dataLabels: {
                                enabled: xAxis.ticks.length < 10,
                                format: "{y:.1f}",
                                zIndex: 4
                            }
                        }
                    },
                    tooltip: {
                        shared: true,
                        useHTML: true,
                        headerFormat: "{point.key}<table>",
                        pointFormat: "<tr><td><span style=\"color:{series.color}\">\u25CF</span> {series.name}: </td>" +
                        "<td style=\"text-align: right\"><b>{point.y:.2f}%</b></td></tr>",
                        footerFormat: "</table>"
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            format: '{value}%'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true
                    }
                });
            }

            function createEmissionTable($container, filterData, jsonData, customParams) {
                var showManager = filterData.systemUnitId == 1
                        || filterData.systemUnitId == 11
                        || filterData.systemUnitId == 12;

                var columns = [];

                if (showManager) {
                    columns.push({
                        field: "managerName",
                        title: "Менеджер",
                        width: 180
                    });
                }

                columns.push({
                    field: "companyName",
                    title: "Компания",
                    width: 180
                }, {
                    field: "companyINN",
                    title: "ИНН",
                    width: 110
                });


                var stageIds = filterData.processStageIds,
                        stageColumnWidth = 130,
                        dateTemplate = '#= kendo.toString(new Date({0}), "dd.MM.yy" ) #',
                        unitText = _.isEmpty(filterData.subProcessIds) ? " дн" : " ч";

                var $modal = $(".modal");
                $modal.removeClass("large-modal");
                if (stageIds.length === 1) {
                    var field = "s{0}StartDate".format(getStageCodeById(stageIds[0]));
                    columns.push({
                        field: field,
                        title: "Дата начала",
                        width: stageColumnWidth,
                        template: dateTemplate.format(field)
                    })
                } else {
                    $modal.addClass("large-modal");

                    if (arrayContainsOrEmpty(stageIds, getStageIdByCode(1))) {
                        columns.push({
                            field: "s1StartDate",
                            title: "Дата начала этапа \"Заведение ЮЛ\"",
                            width: stageColumnWidth,
                            template: dateTemplate.format("s1StartDate")
                        }, {
                            field: "s1Duration",
                            title: "Длительность этапа \"Заведение ЮЛ\"",
                            width: stageColumnWidth
                        })
                    }
                    if (arrayContainsOrEmpty(stageIds, getStageIdByCode(2))) {
                        columns.push({
                            field: "s2StartDate",
                            title: "Дата начала этапа \"Открытие счета 1-му ФЛ\"",
                            width: stageColumnWidth,
                            template: dateTemplate.format("s2StartDate")
                        }, {
                            field: "s2Duration",
                            title: "Длительность этапа \"Открытие счета 1-му ФЛ\"",
                            width: stageColumnWidth
                        })
                    }
                    if (arrayContainsOrEmpty(stageIds, getStageIdByCode(3))) {
                        columns.push({
                            field: "s3StartDate",
                            title: "Дата начала этапа \"Выдача карты 1-му ФЛ\"",
                            width: stageColumnWidth,
                            template: dateTemplate.format("s3StartDate")
                        }, {
                            field: "s3Duration",
                            title: "Длительность этапа \"Выдача карты 1-му ФЛ\"",
                            width: stageColumnWidth
                        })
                    }
                }

                columns.push({
                    field: "avgDuration",
                    title: "Длительность, " + unitText,
                    width: 140
                });

                $container.kendoGrid({
                    dataSource: {
                        data: jsonData
                    },
                    sortable: true,
                    scrollable: true,
                    columns: columns
                });
            }

            app.init(config);
        </script>
    </jsp:attribute>

    <jsp:body>
        <a class="btn btn-default btn-xs support-button" href="<c:url value="/files/zpOpeningSpeed.pdf" />">Инструкция
            пользователя</a>

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
                    <filter params="name: 'bushIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'operationOfficeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'openingTypeId'"></filter>
                </div>
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'cityIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'managerIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'companyIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'topCount'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'schemaTypeIds'"></filter>
                </div>

            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'processStageIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'subProcessStageId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'paramType'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'withClientWait'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'systemUnitId'"></filter>
                </div>
            </div>
            <div>
                <refresh-button params="style: {marginRight: '5px'}"></refresh-button>
                <reset-filter-button></reset-filter-button>
            </div>
            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <tab-strip params="name: 'dynamicWithPie'">
                <tab>
                    <chart params="name: 'dynamicWithPie'">
                        <div style="margin: 10px auto 15px;">
                            <p class="table-header"></p>
                        </div>
                        <div class="chart-container"></div>
                    </chart>
                </tab>
                <tab>
                    <chart params="name: 'projectDynamicWithPie'"></chart>
                </tab>
            </tab-strip>
            <div class="row" style="margin: 20px -15px 10px">
                <div class="col-xs-12 col-sm-12 col-lg-5" style="min-width:450px;">
                    <div class="chart">
                        <chart params="name: 'rating'">
                            <div class="scrollable-chart">
                                <p class="scrollable-chart-title"></p>

                                <div class="chart-container" style="height: 385px;"></div>
                            </div>
                        </chart>
                    </div>
                </div>
                <div class="col-xs-12 col-sm-12 col-lg-7">
                    <div class="row">
                        <div class="col-xs-6">
                            <chart params="name: 'topNCount'">
                                <div class="scrollable-chart">
                                    <p class="scrollable-chart-title"></p>

                                    <div class="chart-container" style="height: 385px;">

                                    </div>
                                </div>
                            </chart>
                        </div>
                        <div class="col-xs-6">
                            <chart params="name: 'worstNCount'">
                                <div class="scrollable-chart">
                                    <p class="scrollable-chart-title"></p>

                                    <div class="chart-container" style="height: 385px;"></div>
                                </div>
                            </chart>
                        </div>
                    </div>
                </div>
            </div>
            <div class="chart">
                <tab-strip params="name: 'details'">
                    <tab>
                        <chart params="name: 'detailsKpiTable'"></chart>
                    </tab>
                    <tab>
                        <chart params="name: 'detailsULCountTable'"></chart>
                    </tab>
                    <tab>
                        <chart params="name: 'projectDetailsTable'"></chart>
                    </tab>
                </tab-strip>
            </div>
            <div class="chart">
                <tab-strip params="name: 'quality'">
                    <tab>
                        <chart params="name: 'crmFillQualityDetails1'"></chart>
                    </tab>
                    <tab>
                        <chart params="name: 'crmFillQualityDetails2'"></chart>
                    </tab>
                </tab-strip>
            </div>
            <div data-bind="if: groups.default.filters.openingTypeId.value() != 2" class="chart">
                <chart params="name: 'crmFillQualityDynamic'"></chart>
            </div>
        </div>

        <div class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title">Аномальная длительность процесса</h4>
                    </div>
                    <div class="modal-body">
                        <chart params="name: 'emissionTable', group: 'emission'"></chart>
                    </div>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>