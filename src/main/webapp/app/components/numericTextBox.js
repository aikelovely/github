app.components.NumericTextBox = function (params, element) {
    var self = this;

    self.value = app.viewModel.getFilter(params).value;

    function init() {
        var $input = $(element).find("input");

        var config = params.config;

        $input.width(config.width || 80);

        var options = {
            min: config.min || 0,
            max: config.max || 100,
            step: config.step || 1,
            format: config.format || "0",
            decimals: config.decimals || 0
        };

        $input.kendoNumericTextBox(options);

        var numericTextBox = $input.data("kendoNumericTextBox");

        // Устанавливаем значение по-умолчанию, если оно задано.
        numericTextBox.value(self.value());

        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            numericTextBox.destroy();
        });

        self.value.subscribe(function (newValue) {
            var current = numericTextBox.value();

            if (current == newValue) return;

            numericTextBox.value(newValue);
        });
    }

    init();
};
