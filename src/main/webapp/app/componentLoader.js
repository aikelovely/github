(function (app, ko) {
    /**
     * Custom component loader. Отвечает за поиск шаблона и создание ViewModel для компонентов.
     * Документацию можно почитать тут: http://knockoutjs.com/documentation/component-loaders.html
     */
    var componentLoader = {
        getConfig: function (name, callback) {
            var componentName = _.camelCase(name), // имя компонента в виде camelCase, т.е. "tab-strip" -> "tabStrip"
                componentViewModelName = capitalizeFirstLetter(componentName),
                viewModelConstructor = app.components[componentViewModelName],
                templateId = componentName + "Template";

            if (viewModelConstructor === undefined) {
                throw new Error("Unable to find view model for component \"" + viewModelConfig + "\"");
            }

            callback({
                viewModel: {
                    createViewModel: function (params, componentInfo) {
                        return new viewModelConstructor(params, componentInfo.element, componentInfo.templateNodes);
                    }
                },
                template: {element: templateId},
                synchronous: true
            });
        }
    };

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }

    ko.components.loaders.unshift(componentLoader);
})(app, ko);