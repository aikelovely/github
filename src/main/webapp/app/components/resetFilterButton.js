app.components.ResetFilterButton = function (params, element) {
    var self = this;

    self.resetFilterState = app.viewModel.resetFilterState;
    self.style = params.style;
};

ko.components.register("reset-filter-button", {});