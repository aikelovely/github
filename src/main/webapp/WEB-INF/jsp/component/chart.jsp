<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script id='chartTemplate' type="text/html">
    <div class="chart">
        <!-- ko if: $root.showSQL -->
        <sql-button params="queryList: queryList"></sql-button>
        <!-- /ko -->

        <!-- ko if: isLoading -->
        <!-- ko if: isFirstRun -->
        <div class='roller'></div>
        <!-- /ko -->
        <!-- ko ifnot: isFirstRun -->
        <div class='chart-overlay'></div>
        <div class='invalidate'>Загрузка...</div>
        <!-- /ko -->
        <!-- /ko -->

        <!-- ko ifnot: containsError -->

        <!-- ko if: customMarkup -->
        <div data-bind="template: { nodes: templateNodes }"></div>
        <!-- /ko -->

        <!-- ko ifnot: customMarkup -->
        <!-- ko if: reportUrl -->
        <report-button params="group: group, reportUrl: reportUrl"></report-button>
        <!-- /ko -->
        <div class="chart-container"></div>
        <div class='chart-url'></div>
        <!-- /ko -->

        <!-- /ko -->

        <!-- ko if: containsError -->
        <div class="panel panel-danger">
            <div class="panel-heading">При загрузке графика произошла ошибка</div>
            <div data-bind="html: errorMessage" class="panel-body chart-error-message">
            </div>
        </div>
        <!-- /ko -->
    </div>
</script>