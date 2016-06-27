<%@ page import="ru.alfabank.dmpr.model.ctq.CTQLayoutItem" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="ru.alfabank.dmpr.filter.ctq.CTQFilter" %>
<%@ page import="ru.alfabank.dmpr.infrastructure.linq.LinqWrapper" %>
<%@ page import="com.fasterxml.jackson.databind.ObjectMapper" %>
<%@ page import="ru.alfabank.dmpr.filter.ctq.CTQFilterHelper" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%
    final String blockGroupId = request.getParameter("blockGroupId") == null ? "-1" : request.getParameter("blockGroupId");
    final String blockId = request.getParameter("blockId") == null ? "-1" : request.getParameter("blockId");

    CTQLayoutItem[] layout = RequestContextUtils.getWebApplicationContext(request).getBean(CTQFilter.class).getLayout();
    LinqWrapper<CTQLayoutItem> layoutWrapper = LinqWrapper.from(layout);

    CTQLayoutItem[] block = CTQFilterHelper.getBlockByIdAndGroupId(layoutWrapper, blockGroupId, blockId);

    request.setAttribute("title", block.length > 0 ? block[0].blockName : "Показатель не определен");
    request.setAttribute("block", new ObjectMapper().writeValueAsString(block));
%>

<t:layout title='${title}'>

<jsp:attribute name="js">
        <script>

          var chartsConfig =_.chain(${block}).groupBy("blockCode").reduce(function(obj, e){
              var first = e[0], result = {
                  jsFunc: createDynamics,
                  dataSource: "CTQDashboardDynamic",
              };

              result.additionalParams = {
                  kpiIds: _.map(e, function(el) { return el.id; })
              };

              result.customParams = {
                  blockGroupId: first.blockGroupId,
                  blockId: first.blockId,
                  blockName: first.blockName,
                  blockDescription: first.blockDescription,
                  blockShowNameFlag: first.blockShowNameFlag === "Y",
                  blockShowNormFlag: first.blockShowNormFlag === "Y",
                  blockShowFactFlag: first.blockShowFactFlag === "Y",
                  blockShowSlFlag: first.blockShowSlFlag === "Y",
                  blockHideCaptionFlag: first.blockHideCaptionFlag === "Y",
                  blockInverseFlag: first.blockZeroTargetFlag === "Y",
                  blockDrillDownUrl: first.blockDrillDownUrl,
                  metrics: _.chain(e).map(function(el){
                      return {
                          id: el.id,
                          alias: el.alias,
                          description: el.description,
                          barColor: el.barColor,
                          normPrefix: el.normPrefix == null ? "" : el.normPrefix,
                          orderNum: el.orderNum
                      }
                  }).sortBy("orderNum").value(),
                  series: []
              };

              obj = result;

              return obj;
          }, {
              jsFunc: createDynamics,
              dataSource: "CTQDashboardDynamic",
              additionalParams: {},
              customParams: {}
          }).value();

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
                          defaultValue: getYearDefaultValue(),
                          width: 90
                      },
                      timeUnitId: {
                          type: "Select",
                          multiple: false,
                          title: "Тип периода",
                          dataSource: {
                              url: "CTQFilter/timeUnits"
                          },
                          defaultValue: 3,
                          width: 150
                      },
                      startDateId: {
                          type: "Select",
                          multiple: false,
                          title: "Период, с",
                          dataSource: {
                              url: "CTQFilter/dates",
                              params: [{name: "startYear", group: "default", required: true},
                                       {name: "timeUnitId",group: "default", required: true}]
                          },
                          //defaultValue: getDateDefaultValue(),
                          width: 265
                      },
                      endDateId: {
                          type: "Select",
                          multiple: false,
                          title: "Период, по",
                          dataSource: {
                              computed: {
                                  jsFunc: createEndPeriodOptions,
                                  params: ["startDateId"]
                              }
                          },
                          width: 265
                      },
                      paramType: {
                          type: "Select",
                          multiple: false,
                          title: "Факт/Ур. качества",
                          dataSource: {
                              data: [
                                  {id: "Fact", name: "Факт"},
                                  {id: "QualityLevel", name: "Уровень качества"}
                              ]
                          },
                          width: 170,
                          defaultValue: "QualityLevel"
                      }
                  },
                  charts: {
                      dynamics: chartsConfig
                  }
              }],
              cookies: false
          };

          app.init(config);

          function getYearDefaultValue() {
              return new Date(moment().startOf('isoweek').year(), 0, 1);
          }

          function getDateDefaultValue() {
              return moment().startOf('isoweek').week();
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

          var highChartsConfigurations = {
              emptyChart: function(title){
                  return {
                      title: {text: title},
                      chart: {
                          height: 100,
                          width: 1184
                      }
                  };
              },
              dynamicChart: function(viewModel, filterData, chart){
                  var bag = chart.bag, series = chart.series,
                      metricConfig = _.find(viewModel.metrics, function(m){ return m.id === parseFloat(bag.id); });

                  if (!metricConfig){
                      return;
                  }

                  var xAxisFilterData = {
                      startDate: moment(_.minBy(series[0].data, function (e) { return e.x; }).x).startOf('day').toDate(),
                      endDate: moment(_.maxBy(series[0].data, function (e) { return e.x; }).x).startOf('day').toDate(),
                      timeUnitId: filterData.timeUnitId
                  };

                  var xAxis = (filterData.timeUnitId == 3 && series[0].data) ? {
                          type: "category",
                          categories: app.chartUtils.getWeekCategoriesByPeriodNum(series[0].data)
                      } : app.chartUtils.createDateTimeXAxis(xAxisFilterData, false), plotLines = [];

                  plotLines.push(
                          {
                              color: 'red',
                              label: {align: 'left', text: '<b>Цель</b>: {0}{1}{2}'.format(
                                      metricConfig.normPrefix || "",
                                      bag.normative, "%"
                              )},
                              value: bag.normative,
                              width: 2,
                              zIndex: 3
                          }
                  );

                  _.each(series[0].data, function(p){
                     p.color = (metricConfig.blockInverseFlag ? (p.y <= bag.normative) : (p.y >= bag.normative)) ? "#95A86D" : "#B05955";
                  });

                  return {
                      chart: {
                          type: "column",
                          width: 1184
                      },
                      title: {text: metricConfig.alias || bag.name},
                      xAxis: xAxis,
                      yAxis: {
                          min: 0,
                          title: {text: ""},
                          labels: {
                              format: '{value}%'
                          },
                          plotLines: plotLines
                      },
                      tooltip: {
                          useHTML: true,
                          pointFormat: "<span style='font-size:10px'>{point.periodName}</span></br><b>{point.y:.2f}%</b><br/>{point.customHTMLTooltip}",
                          headerFormat: ""
                      },
                      plotOptions: {
                          column: {
                              dataLabels: {
                                  enabled: true,
                                  formatter: function() { return this.y.toFixed(2) + "%"; }
                              }
                          }
                      },
                      legend: {
                          enabled: false
                      },
                      series: series
                  };
              }
          };

          function createDynamics($container, filterData, jsonData, customParams){
              var viewModel = customParams;

              if (!jsonData || jsonData.length == 0) {
                  $container.highcharts(highChartsConfigurations.emptyChart(viewModel.blockName));
                  return;
              }

              var atLeastOneChart = false;

              _.each(jsonData, function(chart){
                  var configuration = highChartsConfigurations.dynamicChart(viewModel, filterData, chart);
                  if (configuration) {
                      atLeastOneChart = true;
                      $container.append($(document.createElement('div')).highcharts(configuration));
                  }
              });

              if (!atLeastOneChart){
                  $container.highcharts(highChartsConfigurations.emptyChart(viewModel.blockName));
              }

              $container.children("div:not(:last-child)").css("margin-bottom", '20px');
          }
        </script>
    </jsp:attribute>

  <jsp:body>
    <div class="showcase-title">${title}</div>
    <div class="filter-container">
          <div class="filter-row">
              <div class="filter-element">
                  <filter params="name: 'startYear'"></filter>
              </div>
              <div class="filter-element">
                  <filter params="name: 'timeUnitId'"></filter>
              </div>
              <div class="filter-element">
                  <filter params="name: 'startDateId'"></filter>
              </div>
              <div class="filter-element">
                  <filter params="name: 'endDateId'"></filter>
              </div>
              <div class="filter-element">
                  <filter params="name: 'paramType'"></filter>
              </div>
              <div class="filter-element">
                  <refresh-button></refresh-button>
              </div>
          </div>
          <filter-log></filter-log>
      </div>
      <div data-bind="visible: visibleCharts" class="charts-container">
          <div class="chart">
              <chart params="name: 'dynamics'"></chart>
          </div>
      </div>
  </jsp:body>
</t:layout>
