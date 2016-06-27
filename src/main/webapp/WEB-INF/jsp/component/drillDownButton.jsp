<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>

<script id='drillDownButtonTemplate' type="text/html">
    <button data-bind="click: onClick, visible: visible" class="btn btn-default pull-right">
        <i class="fa fa-long-arrow-left"></i> <span data-bind="text: btnText"></span>
    </button>
</script>