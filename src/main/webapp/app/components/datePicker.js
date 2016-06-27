app.components.DatePicker = function (params, element) {
    var self = this,
        customOptions = params.config.datepickerOptions || {},
        filter = app.viewModel.getFilter(params);

    self.value = filter.value;
    self.width = params.config.width || 130;
    self.enabled = filter.enabled;

    function init() {
        var $datepickerElement = $(element).find(".input-group.date");

        var options = _.assign({
            language: "ru",
            orientation: "top",
            autoclose: true
        }, customOptions);

        $datepickerElement.datepicker(options);

        $datepickerElement.change(function () {
            var pickerValue = $datepickerElement.datepicker("getDate");
            if (moment(self.value()).isSame(pickerValue)) return;

            self.value(pickerValue);
        });

        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            $datepickerElement.datepicker("remove");
        });

        self.value.subscribe(function (newValue) {
            var current = $datepickerElement.datepicker("getDate");

            if (moment(current).isSame(newValue)) return;

            $datepickerElement.datepicker('update', newValue);
        });

        $datepickerElement.datepicker('update', self.value());
    }

    init();
};