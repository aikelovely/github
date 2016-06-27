/**
 * NumericTextBox builder. Создает observable app.viewModel.groups.{groupName}.filters.{filterName}.value.
 * @param viewModel app.viewModel
 * @param config
 * @constructor
 */
app.builders.NumericTextBox = function (viewModel, config) {
    var self = this;

    self.build = function () {
        var filter = viewModel.createFilter(config);

        var value = filter.value = ko.observable();

        var node = new app.DependencyTreeNode(
            app.createDependencyName(config),
            function () {
                var valueToSetup;

                if (viewModel.cookiesEnabled && viewModel.cookieData[config.name] != undefined) {
                    valueToSetup = viewModel.cookieData[config.name];
                } else {
                    valueToSetup = config.defaultValue;
                }

                value(valueToSetup);
            });

        return node;
    };
};
