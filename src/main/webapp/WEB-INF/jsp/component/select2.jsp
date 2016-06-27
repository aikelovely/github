<script id='select2Template' type="text/html">
    <div class="btn-group">
        <button data-bind="style: {width: width + 'px'}" type="button"
                class="btn btn-default dropdown-toggle btn-select2"
                data-toggle="dropdown">
            <div class="btn-select2-text" data-bind="text: btnText"></div>
            <span class="caret"></span>
        </button>
        <ul class="dropdown-menu select2">
            <!-- ko if: showClearBtn -->
            <a data-bind="click: clear" href="javascript:void(0)" class="btn-clear-select2">Очистить</a>
            <!-- /ko -->
            <select data-bind="selectedOptions: value"></select>
        </ul>
    </div>
</script>