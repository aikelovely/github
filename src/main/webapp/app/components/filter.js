app.components.Filter = function (params, element) {
    var self = this;

    var parsedNameAndGroup = app.parseNameAndGroup(params),
        group = app.viewModel.getGroup(parsedNameAndGroup.group),
        config = group.config.filters[parsedNameAndGroup.name];

    if (config === undefined) {
        throw new Error("Config for filter not found. Name: " + parsedNameAndGroup.name + "\", group: " + parsedNameAndGroup.group)
    }

    params.config = config;

    self.params = params;
    self.type = params.config.type;
    self.title = params.config.title;
    self.filterConfig = config;

    self.forceDisplayErrors = group.forceDisplayErrors;
    self.observable = app.viewModel.getFilter(params).value;
};

ko.components.register("filter", {});