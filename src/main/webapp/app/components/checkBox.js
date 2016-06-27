app.components.CheckBox = function (params, element) {
    var self = this;

    self.title = params.config.title;
    self.value = app.viewModel.getFilter(params).value;
};
