<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="title" value='Выгрузка показателей CTQ'/>

<t:layout title='${title}'>

<jsp:attribute name="js">
        <script>
            var config = {
                groups: [{
                    name: "default",
                    filters: {
                        startYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: getStartYearDefaultValue(),
                            notAfter: "endYear",
                            width: 90
                        },
                        startDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, с",
                            dataSource: {
                                url: "CTQFilter/startDates",
                                params: [{name: "startYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            width: 265,
                            postInit: createStartDateIdSubscriptions
                        },
                        endYear: {
                            type: "DatePicker",
                            title: "Год",
                            datepickerOptions: {
                                minViewMode: 2,
                                format: 'yyyy'
                            },
                            defaultValue: getEndYearDefaultValue(),
                            notBefore: "startYear",
                            width: 90
                        },
                        endDateId: {
                            type: "Select",
                            multiple: false,
                            title: "Период, по",
                            dataSource: {
                                url: "CTQFilter/endDates",
                                params: [{name: "endYear", group: "default", required: true}, {
                                    name: "timeUnitId",
                                    group: "default",
                                    required: true
                                }]
                            },
                            width: 265,
                            postInit: createEndDateIdSubscriptions
                        },
                        kpiIds: {
                            type: "Select",
                            multiple: true,
                            title: "Показатели",
                            enableSearch: true,
                            enableClear: true,
                            dataSource: {
                                url: "CTQFilter/KPIs"
                            },
                            width: 780
                        }
                    }
                }],
                cookies: false
            };

            app.init(config);

            function getStartYearDefaultValue() {
                return new Date(moment().startOf('isoweek').subtract(6, 'weeks').year(), 0, 1);
            }

            function getEndYearDefaultValue() {
                return new Date(moment().startOf('isoweek').subtract(1, 'weeks').year(), 0, 1);
            }

            function getStartDateDefaultValue() {
                return moment().startOf('isoweek').subtract(6, 'weeks').week();
            }

            function getEndDateDefaultValue() {
                return moment().startOf('isoweek').subtract(1, 'weeks').week();
            }

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

            function createEndPeriodOptions(context) {
                var startDateId = context.params.startDateId;
                if (app.viewModel) {
                    var startDateIdFilter = app.viewModel.getFilter("startDateId");
                    if (startDateIdFilter.options().length) {
                        return _.filter(startDateIdFilter.options(), function (opt) {
                            return opt.id >= startDateId;
                        });
                    }
                }
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
            </div>
            <div class="filter-row">
                <div class="filter-element">
                    <filter params="name: 'kpiIds'"></filter>
                </div>
            </div>
            <div class="filter-row">
                <report-button params="reportUrl: 'CTQDashboardReport', group: 'default'"></report-button>
            </div>
            <filter-log></filter-log>
        </div>
    </jsp:body>
</t:layout>
