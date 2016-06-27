<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="Отчет по выполнению KPI на длительность скорингов"/>

<t:layout title="${title}">

    <jsp:attribute name="js">
        <script>
            var config = {
                groups: [{
                    filters: {
                        startDate: {
                            type: "DatePicker",
                            title: "Период, с",
                            notAfter: "endDate",
                            defaultValue: moment().add(-1, "w").toDate()
                        },
                        endDate: {
                            type: "DatePicker",
                            title: "Период, по",
                            notBefore: "startDate"
                        },
                        sourceSystemIds: {
                            type: "Select",
                            multiple: true,
                            title: "Система-источник",
                            dataSource: {
                                url: "pilAndCCFilter/Modules"
                            },
                            width: 175
                        },
                        timeUnitId: {
                            type: "Select",
                            multiple: false,
                            title: "Период агрегации",
                            dataSource: {
                                url: "operKpiFilter/TimeUnits"
                            },
                            width: 130
                        },
                        sevenFieldsCheckId: {
                            type: "Select",
                            multiple: false,
                            title: "Учет проверок по 7 полям",
                            dataSource: {
                                url: "operKpiFilter/SevenFieldsCheck"
                            },
                            width: 200
                        }
                    },
                    charts: {
                        table: {
                            jsFunc: createTable,
                            dataSource: "OperKpiScorTable"
                        },
                        dynamic: {
                            jsFunc: createDynamic,
                            dataSource: "OperKpiScorDynamicTable"
                        }
                    },
                    tabStrips: {
                        main: {
                            sameMarkup: true,
                            tabs: [
                                {
                                    title: "Total",
                                    customParams: {reportTypeId: 0},
                                    group: "total"
                                },
                                {
                                    title: "Internet",
                                    customParams: {reportTypeId: 1},
                                    group: "internet"
                                },
                                {
                                    title: "DSA",
                                    customParams: {reportTypeId: 2},
                                    group: "dsa"
                                },
                                {
                                    title: "Branch",
                                    customParams: {reportTypeId: 3},
                                    group: "branch"
                                }
                            ],
                            csvReportUrl: "OperKpiScorCsvReport"
                        }
                    }
                }],
                cookies: true
            };

            function createTable($container, filterData, jsonData, customParams) {
                var filteredData = _.filter(jsonData, function (row) {
                    return row.reportTypeId == customParams.reportTypeId;
                });

                var gridConfig = {
                    dataSource: {
                        data: filteredData
                    },
                    sortable: true,
                    columns: [
                        {field: "durGroupName", title: "Группа длительности", width: 200},
                        {
                            title: "TTY Окончательное решение",
                            columns: [
                                {field: "finalFactValuePIL", title: "Кредит наличными", format: "{0:##0.##}%"},
                                {field: "finalFactValueCC", title: "Кредитная карта", format: "{0:##0.##}%"}]
                        },
                        {
                            title: "TTY Предварительное решение",
                            columns: [
                                {field: "preliminarilyFactValuePIL", title: "Кредит наличными", format: "{0:##0.##}%"},
                                {field: "preliminarilyFactValueCC", title: "Кредитная карта", format: "{0:##0.##}%"}]
                        }
                    ]
                };

                $container.kendoGrid(gridConfig);
            }

            function createDynamic($container, filterData, jsonData, customParams) {
                var filteredData = _.filter(jsonData, function (row) {
                    return row.reportTypeId == customParams.reportTypeId;
                });

                var columns = app.chartUtils.createDateTimeColumns(filterData, null, function (data) {
                    if (data == null) return "";
                    return Math.round(data * 100) / 100 + "%";
                });

                // Задаем первую колонку
                columns.unshift({
                    field: "durGroupName",
                    title: "Группа длительности",
                    width: 200,
                    locked: true
                });

                $container.kendoGrid({
                    dataSource: {
                        data: filteredData
                    },
                    height: _.isEmpty(filteredData) ? 100 : 460,
                    sortable: true,
                    columns: columns
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
                    <filter params="name: 'startDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'endDate'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'sourceSystemIds'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'timeUnitId'"></filter>
                </div>
                <div class="filter-element">
                    <filter params="name: 'sevenFieldsCheckId'"></filter>
                </div>
                <div class="filter-element">
                    <refresh-button></refresh-button>
                </div>
            </div>

            <filter-log></filter-log>
        </div>

        <div data-bind="visible: visibleCharts" class="charts-container">
            <tab-strip params="name: 'main'">
                <tab>
                    <chart params="name: 'table', group: 'default'"></chart>
                    <chart params="name: 'dynamic', group: 'default'"></chart>
                </tab>
            </tab-strip>
        </div>
    </jsp:body>
</t:layout>

