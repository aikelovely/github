<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="title" value="LeaderBoard"/>

<t:layout title="${title}">
    <jsp:attribute name="css">
        <style scoped>
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

          .tab-old-2 {
            background-color: rgba(141, 179, 150, 0.75) !important;
          }

          .active .tab-old-2 {
            background-color: #179433 !important;
          }

          .tab-2 {
            background-color: rgba(187, 191, 135, 0.75) !important;
          }

          .active .tab-2 {
            background-color: #DEC53E !important;
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

          .tab-content {
            margin-bottom: 20px;
          }

          .tab-content .chart:last-child {
            margin-bottom: 0;
          }

          hr {
            border-color: #B0B0B0;
          }

          .kpi7 {
            position: relative;
          }

          .kpi7 .link {
            position: absolute;
            top: 7px;
            right: 5px;
            font-size: 18px;
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
                   // "Клиенты",
                   // "Процессы и технологии",
                   // "Эффективность",
                    "Риски"
                  ]
                }
              },
              charts: {
                kpi11Chart: {
                  jsFunc: createKpi11Chart,
                  dataSource: "LeaderBoardKpi11Chart"
                }
              },
              slaves: [{
                name: "byYear",
                filters: {
                  startDate: {
                    type: "DatePicker",
                    defaultValue: moment().startOf("year").toDate()
                  },
                  endDate: {
                    type: "DatePicker",
                    defaultValue: moment().endOf("year").toDate()
                  }
                },
                charts: {
                  kpi13Chart: {
                    jsFunc: createKpi13Chart,
                    dataSource: "LeaderBoardKpi13Chart"
                  }
                }
              }]
            }]
          };

          function createKpi11Chart($container, filterData, jsonData) {
            var chart = jsonData[0],
                    series = chart.series,
                    title = chart.bag.kpiName;

            if (series.length === 0 || series[0].data.length === 0) {
              $container.empty();
              return;
            }

            var defaultColors = Highcharts.getOptions().colors;

            _.forEach(series, function (s) {
              switch (s.bag.kpiCode) {
                case "KPIOB~11~1":
                  s.color = color.currentValue;
                  s.yAxis = 0;
                  s.type = "column";
                  break;
                case "KPIOB~11~2":
                  s.yAxis = 1;
                  s.type = "line";
                  s.zIndex = 2;
                  s.color = defaultColors[2];
                  break;
                case "KPIOB~11~3":
                  s.yAxis = 0;
                  s.color = defaultColors[3];
                  s.type = "line";
                  s.zIndex = 2;
                  s.dashStyle = "ShortDash";
                  s.marker = {
                    enabled: false
                  };
                  s.tooltip = {
                    pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
                  };
                  break;
              }
            });

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
              yAxis: [{
                title: {text: ""},
                min: 3400,
                tickInterval: 75
              }, {
                title: {text: ""},
                labels: {
                  format: '{value}%'
                },
                opposite: true,
                min: 60
              }
              ],
              tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: "<tr><td style=\"padding:2px;\">{series.name}: </td>" +
                '<td style="padding:0"><b>{point.y:.2f}%</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
              },
              plotOptions: {
                column: {
                  tooltip: {
                    pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
                  }
                }
              },
              series: series
            });
          }

          function createKpi13Chart($container, filterData, jsonData) {
            var chart = jsonData[0],
                    series = chart.series,
                    title = chart.bag.kpiName;

            if (series.length === 0 || series[0].data.length === 0) {
              $container.empty();
              return;
            }

            var defaultColors = Highcharts.getOptions().colors;

            _.forEach(series, function (s) {
              if (s.bag.isMainValue) {
                s.color = color.currentValue;
                s.yAxis = 0;
                s.type = "column";
              } else {
                s.zIndex = 2;
                s.yAxis = 1;
                s.type = "line";
                s.marker = {
                  symbol: "circle"
                }
              }

              if (s.bag.isPlanValue) {
                s.dashStyle = "ShortDash";
                s.marker = {
                  enabled: false
                }
              }

              switch (s.bag.kpiCode) {
                case "KPIOB~13~1":
                  s.color = defaultColors[2];
                  break;
                case "KPIOB~13~2":
                  s.color = defaultColors[4];
                  break;
                case "KPIOB~13~3":
                  s.color = defaultColors[3];
                  break;
                case "KPIOB~13~4":
                  s.color = defaultColors[7];
                  break;
              }
            });

            $container.highcharts({
              chart: {
                type: 'column',
                height: 500
              },
              title: {
                text: title
              },
              xAxis: {
                categories: app.chartUtils.getWeekCategories(chart.bag.categories)
              },
              yAxis: [{
                title: {text: ""},
                maxPadding: 0
              }, {
                title: {text: ""},
                labels: {
                  format: '{value}%'
                },
                opposite: true,
                maxPadding: 0
              }],
              tooltip: {
                headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                pointFormat: "<tr><td style=\"padding:2px;\">{series.name}: </td>" +
                '<td style="padding:0"><b>{point.y:.2f}%</b></td></tr>',
                footerFormat: '</table>',
                shared: true,
                useHTML: true
              },
              plotOptions: {
                column: {
                  tooltip: {
                    pointFormat: '<tr><td style="padding:2px">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.2f}</b></td></tr>',
                  }
                }
              },
              //legend: {enabled: false},
              series: series
            });
          }

          app.init(config, function (viewModel) {
            var defaultGroup = viewModel.getGroup("default");

            var byYearStartDate = viewModel.getFilter("byYear.startDate"),
                byYearEndDate = viewModel.getFilter("byYear.endDate");

            defaultGroup.filters.startDate.value.subscribe(function (newValue) {
              var year = moment(newValue).year(),
                  byYearGroupYear = moment(byYearStartDate.value()).year();

              if (year !== byYearGroupYear) {
                byYearStartDate.value(moment(newValue).startOf("year").toDate());
                byYearEndDate.value(moment(newValue).endOf("year").toDate());
              }
            });

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
    </div>

    <filter-log></filter-log>

    <div data-bind="visible: groups.default.visibleCharts" class="charts-container">
      <tab-strip params="name: 'showcases'">
        <tab>
          <chart params="name: 'kpi11Chart'"></chart>
        </tab>
        <tab>
          <chart params="name: 'kpi13Chart', group: 'byYear'"></chart>
        </tab>
      </tab-strip>
    </div>
  </jsp:body>
</t:layout>