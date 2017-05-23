<%@ tag import="ru.alfabank.dmpr.infrastructure.spring.security.UserContext" %>
<%@ tag import="java.util.Arrays" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Overall Page template" pageEncoding="UTF-8" %>
<%@attribute name="title" required="true" %>
<%@attribute name="css" fragment="true" %>
<%@attribute name="js" fragment="true" %>
<!DOCTYPE html>
<!--[if lt IE 10]>    <html class="old-ie"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="ru"> <!--<![endif]-->

<html lang="ru">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width"/>

    <title>${title}</title>

    <link rel="stylesheet" href="<c:url value="/libs/kendo-ui/css/kendo.common.min.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/kendo-ui/css/kendo.silver.min.css" />" />
    <%--<link rel="stylesheet" href="<c:url value="/libs/kendo-ui/css/kendo.common-material.min.css" />" />--%>
    <%--<link rel="stylesheet" href="<c:url value="/libs/kendo-ui/css/kendo.material.min.css" />" />--%>

    <link rel="stylesheet" href="<c:url value="/libs/bootstrap/css/bootstrap.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-datepicker/css/bootstrap-datepicker3.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/font-awesome/css/font-awesome.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/bootstrap-select/css/bootstrap-select.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/select2/css/select2.css" />" />
    <link rel="stylesheet" href="<c:url value="/libs/qtip/css/jquery.qtip.css" />" />
    <link rel="stylesheet" href="<c:url value="/styles/site.css" />" />

    <jsp:invoke fragment="css"/>

    <!--[if lt IE 9]>
    <script>
        document.createElement("chart");
        document.createElement("filter");
        document.createElement("filter-log");
        document.createElement("refresh-button");
        document.createElement("report-button");
        document.createElement("csv-report-button");
        document.createElement("reset-filter--button");
        document.createElement("sql-button");
        document.createElement("tab-strip");
    </script>
    <![endif]-->

</head>
<body>
<jsp:include page="/WEB-INF/jsp/shared/navigation.jsp"/>

<div class="container">
    <jsp:doBody/>
</div>

<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/jquery/jquery.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/json2/json2.js" />"></script>

<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/jszip/jszip.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/jszip/jszip.min.js" />"></script>

<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/bootstrap/js/bootstrap.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/knockout/knockout.debug.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/knockout-validation/knockout.validation.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/highcharts/highcharts.src.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/highcharts/modules/highcharts-more.src.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/highcharts/modules/no-data-to-display.src.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/highcharts/modules/exporting.src.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/highcharts/modules/offline-exporting.src.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/jquery-cookie/jquery-cookie.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/lodash/lodash.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/bootstrap-datepicker/js/bootstrap-datepicker.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/bootstrap-datepicker/locales/bootstrap-datepicker.ru.min.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/bootstrap-select/js/bootstrap-select.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/select2/js/select2.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/select2/js/i18n/ru.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/qtip/js/jquery.qtip.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/moment/moment.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/moment/ru.js" />"></script>

<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/kendo-ui/js/kendo.web.min.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/kendo-ui/js/kendo.culture.ru-RU.min.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/kendo-ui/js/kendo.messages.ru-RU.min.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/libs/kendo-ui/js/kendo.all.min.js" />"></script>

<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/app.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/checkBox.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/datePicker.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/numericTextBox.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/select.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/select2.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/builders/tabStrip.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/checkBox.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/datePicker.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/drillDownButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/filter.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/numericTextBox.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/select.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/select2.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/tabStrip.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/refreshButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/resetFilterButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/reportButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/csvReportButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/chart.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/sqlButton.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/components/filter-log.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/ajaxUtils.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/cookieManager.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/dependencyTreeNode.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/dsParams.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/dynamicViewModel.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/filterGroup.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/knockoutUtils.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/chartUtils.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/lodashExtensions.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/componentLoader.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/ko.validation.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/ie-polyfill.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/knockout-bindings/ko.bootstrapPopOver.js" />"></script>
<script type="text/javascript" charset="UTF-8" src="<c:url value="/app/knockout-bindings/ko.bootstrapTooltip.js" />"></script>

<script type="text/javascript">
    app.rootUrl = '<c:url value="/" />';
    app.isSqlViewer = <%= UserContext.isSqlViewerRole() %>;
</script>

<jsp:include page="/WEB-INF/jsp/component/checkBox.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/filter.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/drillDownButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/datePicker.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/numericTextBox.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/select.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/select2.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/tabStrip.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/refreshButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/resetFilterButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/reportButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/csvReportButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/chart.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/sqlButton.jsp"/>
<jsp:include page="/WEB-INF/jsp/component/filter-log.jsp"/>

<jsp:invoke fragment="js"/>

</body>
</html>