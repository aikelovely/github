<script id='selectTemplate' type="text/html">
    <!-- ko template: { afterRender: afterRender } -->

    <!-- ko if: (multiple && withGroups) -->
    <select data-bind="foreach: options, selectedOptions: data" multiple="multiple" class="show-menu-arrow multi-select-with-groups">
        <optgroup data-bind="attr: { label: name, value: id }, foreach: options" label="">
            <option data-bind="text: name, attr: { value: id }"></option>
        </optgroup>
    </select>
    <!-- /ko -->

    <!-- ko if: (multiple && !withGroups) -->
    <select data-bind="foreach: options, selectedOptions: data" multiple="multiple" class="show-menu-arrow multi-select">
        <option data-bind="text: name, attr: { value: id }"></option>
    </select>
    <!-- /ko -->

    <!-- ko if: (!multiple && withGroups) -->
    <select data-bind="foreach: options, value: data" class="show-menu-arrow single-select-with-groups">
        <optgroup data-bind="attr: { label: name, value: id }, foreach: options" label="">
            <option data-bind="text: name, attr: { value: id }"></option>
        </optgroup>
    </select>
    <!-- /ko -->

    <!-- ko if: (!multiple && !withGroups) -->
    <select data-bind="foreach: options, value: data" class="show-menu-arrow single-select">
        <option data-bind="text: name, attr: { value: id }"></option>
    </select>
    <!-- /ko -->

    <!-- /ko -->
</script>