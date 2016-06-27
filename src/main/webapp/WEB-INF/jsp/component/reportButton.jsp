<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script id='reportButtonTemplate' type="text/html">
    <button data-bind="click: generateReport, disable: disabled, style: style" class="btn btn-default btn-mini">
        <i class="icon-excel"></i>
        <span data-bind="text: reportButtonText"></span>
    </button>

    <!-- ko if: ($root.showSQL() && reportCreated()) -->
    <sql-button params="queryList: queryList, buttonText: 'SQL (Excel)'"></sql-button>
    <!-- /ko -->
</script>