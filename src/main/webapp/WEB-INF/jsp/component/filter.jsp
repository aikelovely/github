<script id='filterTemplate' type="text/html">
    <div data-bind="validationContainer: {data: observable, config: filterConfig, forceDisplayErrors: forceDisplayErrors}">

        <!-- ko if: (type !== "CheckBox" && title && title.length ) -->
        <strong data-bind="text: title"></strong>
        <!-- /ko -->

        <div data-bind="component: { name: type, params: params}" class="filter"></div>
    </div>
</script>