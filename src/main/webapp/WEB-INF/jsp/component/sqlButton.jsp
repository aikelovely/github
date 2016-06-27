<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script id='sqlButtonTemplate' type="text/html">
    <button data-bind="click: showSQLText" class="btn btn-info btn-sm btn-show-sql">
        <i class="fa fa-eye"></i> <span data-bind="text: buttonText"></span>
    </button>
</script>