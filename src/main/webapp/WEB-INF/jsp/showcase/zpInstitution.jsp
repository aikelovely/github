<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Заведение зарплатного проекта"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
            .tile-container {
                background-color: white;
                text-align: center;
                height: 105px;
                width: 100%;
                font-family: "Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif;
                color: #333333;
                line-height: normal;
                margin-bottom: 20px;
                position: relative;
            }

            .stage-info {
                font-weight: bold !important;
            }

            .tile-title {
                position: relative;
                font-size: 17px;
                padding-top: 15px;
                z-index: 2;
            }

            .half-size-tile .tile-title {
                font-size: 14px;
                padding-top: 10px;
            }

            .tile-value {
                font-size: 12px;
                position: absolute;
                bottom: 5px;
                left: 0;
                right: 0;
            }

            .triangle {
                border-style: solid;
                border-width: 0 70px 70px 0;
                position: absolute;
                top: 0;
                right: 0;
            }

            .triangle.green {
                border-color: transparent #C3D69B transparent transparent;
            }

            .triangle.white {
                border-color: transparent #ffffff transparent transparent;
            }

            .triangle.red {
                border-color: transparent #FDA6A2 transparent transparent;
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
                                params: ["bushIds", "operationOfficeIds"]
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
                                params: ["bushIds", "operationOfficeIds", "cityIds"]
                            },
                            pageSize: 100
                        },
                        companyIds: {
                            type: "Select2",
                            multiple: true,
                            title: "Компания",
                            enableClear: true,
                            dataSource: {
                                url: "ZpCommonFilter/Companies",
                                params: ["bushIds", "operationOfficeIds", "cityIds", "managerIds"]
                            },
                            width: 330
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
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Единица времени",
                            dataSource: {
                                url: "ZpCommonFilter/TimeUnits"
                            },
                            width: 120
                        }
                    },
                    charts: {
                        countDynamic: {
                            jsFunc: createCountDynamic,
                            dataSource: "ZPInstitutionDynamic"
                        },
                        percentOrDurationDynamic: {
                            jsFunc: createPercentOrDurationDynamic,
                            dataSource: "ZPInstitutionDynamic"
                        },
                        tile1: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {stageId: 1}},
                        tile2: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {stageId: 2}},
                        tile3: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {stageId: 3}},

                        tile101: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 101}},
                        tile102: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 102}},
                        tile104: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 104}},
                        tile105: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 105}},
                        tile106: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 106}},
                        tile107: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 107}},

                        tile201: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 201}},
                        tile202: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 202}},
                        tile203: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 203}},

                        tile301: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 301}},
                        tile302: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 302}},
                        tile303: {jsFunc: createTile, dataSource: "ZPInstitutionTiles", customParams: {subStageId: 303}}
                    }
                }],
                cookies: true
            };

            function arrayContainsOrEmpty(array, item) {
                if (array === undefined || array === null || array.length === 0) return true;

                return $.inArray(value, array);
            }

            function openingSchemaTypesComputed(context) {
                var data = context.loadedData,
                        cityIds = context.params.cityIds || [];

                if (cityIds.length > 0 && viewModel) {
                    var cityIdsOptions = viewModel.cityIds_options(),
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

            function createCountDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[0],
                        title = "";

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

                var legendValue = "" + chart.bag.total;

                $container.highcharts({
                    chart: {
                        height: 300,
                        marginTop: 30,
                        type: "area"
                    },
                    title: {text: title},
                    tooltip: {
                        pointFormat: "<span style=\"color:{series.color}\">{series.name}</span>:<b>{point.y}</b>"
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            format: '{value}'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        layout: "vertical",
                        align: "left",
                        verticalAlign: "middle",
                        margin: 50,
                        itemWidth: 250,
                        useHtml: true,
                        itemStyle: {
                            "fontSize": "14px",
                            "width": "250px"
                        },
                        labelFormat: "{name}: " + legendValue
                    }
                });
            }

            function createPercentOrDurationDynamic($container, filterData, jsonData, customParams) {
                var chart = jsonData[1],
                        title = "";

                if (chart === undefined || chart.series === undefined || chart.series.length === 0 || chart.series[0].data.length === 0) {
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

                var legendValue = chart.bag.total;
                var pointFormat;
                if (filterData.paramType == "AvgDuration") {
                    legendValue = legendValue.toFixed(0) + " дн.";
                    pointFormat = "<span style=\"color:{series.color}\">{series.name}</span>:<b>{point.y:.0f} дн. </b>"
                } else {
                    legendValue = legendValue.toFixed(1) + "%";
                    pointFormat = "<span style=\"color:{series.color}\">{series.name}</span>:<b>{point.y:.2f}%. </b>"
                }

                $container.highcharts({
                    chart: {
                        height: 300,
                        marginTop: 30,
                        type: "area"
                    },
                    title: {text: title},
                    tooltip: {
                        pointFormat: pointFormat
                    },
                    xAxis: xAxis,
                    yAxis: {
                        title: {text: ''},
                        labels: {
                            format: '{value}'
                        }
                    },
                    series: series,
                    legend: {
                        enabled: true,
                        layout: "vertical",
                        align: "left",
                        verticalAlign: "middle",
                        margin: 50,
                        itemWidth: 260,
                        useHtml: true,
                        itemStyle: {
                            "fontSize": "14px",
                            "width": "250px"
                        },
                        labelFormat: "{name}: " + legendValue
                    }
                });
            }

            function createTile($container, filterData, jsonData, customParams, group) {
                var chart;
                try {
                    chart = _.find(jsonData, function (data) {
                        if (customParams.stageId) {
                            return data.stageCode == customParams.stageId;
                        } else {
                            return data.subStageCode == customParams.subStageId;
                        }
                    });
                } catch (ex) {
                    return;
                }

                if(chart === null || chart === undefined) return;

                var tileClass = "tile-container";
                if(customParams.stageId){
                    tileClass += " stage-info";
                } else if(customParams.subStageId == 105 || customParams.subStageId == 106) {
                    tileClass += " half-size-tile";
                }

                var $tile = $("<div/>", {
                    class: tileClass
                });

                var $title = $("<div />", {
                    class: "tile-title",
                    text: chart.subStageName
                }).appendTo($tile);

                var value = "", triangleClass = "green";
                if(chart.totalCount === null || chart.totalCount === undefined || chart.totalCount == 0){
                    value = "Нет данных";
                    triangleClass = "white"
                }
                else if(filterData.paramType == "AvgDuration"){
                    var unitText;
                    if(customParams.stageId){
                        unitText = "дн.";
                    } else {
                        unitText = "ч.";
                        chart.totalDuration *= 9;
                        chart.kpiNorm *= 9;
                    }
                    var avgDuration = chart.totalDuration / chart.totalCount;

                    value = "Средняя длительность {0} {1}".format(avgDuration.toFixed(0), unitText)

                    if(avgDuration > chart.kpiNorm) {
                        triangleClass = "red";
                    }
                } else {
                    var inKpiPercent = chart.inKpiCount * 100 / chart.totalCount;
                    inKpiPercent = inKpiPercent.toFixed(2) * 100 / 100;

                    value = "Процент в KPI {0}".format(inKpiPercent + "%");

                    if(inKpiPercent < chart.inKpiNorm) {
                        triangleClass = "red";
                    }
                }

                var $triangle = $("<div />", {
                    class: "triangle " + triangleClass
                }).appendTo($tile);

                var $valueText = $("<div />", {
                    class: "tile-value",
                    text: value
                }).appendTo($tile);

                $tile.appendTo($container);
            }

            app.init(config);
        </script>
    </jsp:attribute>

    <jsp:body>
        <a class="btn btn-default btn-xs support-button" href="<c:url value="/files/zpInstitution.pdf" />">Инструкция пользователя</a>

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
                    <filter params="name: 'cityIds'"></filter>
                </div>

            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'managerIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'companyIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'schemaTypeIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'paramType'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
            </div>
            <div>
                <refresh-button params="style: {marginRight: '10px'}"></refresh-button>
                <reset-filter-button></reset-filter-button>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <div class="chart">
                <chart params="name: 'countDynamic'"></chart>
            </div>
            <div class="chart">
                <chart params="name: 'percentOrDurationDynamic'"></chart>
            </div>
            <div class="chart row">
                <div class="col-xs-4">
                    <chart params="name: 'tile1'"></chart>
                    <chart params="name: 'tile101'"></chart>
                    <chart params="name: 'tile102'"></chart>
                    <chart params="name: 'tile104'"></chart>
                    <div class="row">
                        <div class="col-xs-6">
                            <chart params="name: 'tile105'"></chart>
                        </div>
                        <div class="col-xs-6">
                            <chart params="name: 'tile106'"></chart>
                        </div>
                    </div>
                    <chart params="name: 'tile107'"></chart>
                </div>
                <div class="col-xs-4">
                    <chart params="name: 'tile2'"></chart>
                    <chart params="name: 'tile201'"></chart>
                    <chart params="name: 'tile202'"></chart>
                    <chart params="name: 'tile203'"></chart>
                </div>
                <div class="col-xs-4">
                    <chart params="name: 'tile3'"></chart>
                    <chart params="name: 'tile301'"></chart>
                    <chart params="name: 'tile302'"></chart>
                    <chart params="name: 'tile303'"></chart>
                </div>
            </div>
        </div>
    </jsp:body>
</t:layout>