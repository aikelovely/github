/**
 * DatePicker builder. Создает следующие observables:
 * app.viewModel.groups.{groupName}.filters.{filterName}.value - текущее значение
 * app.viewModel.groups.{groupName}.filters.{filterName}.enabled - активен/не активен
 *
 * @param viewModel
 * @param config
 * @constructor
 */
app.builders.DatePicker = function (viewModel, config) {
    var self = this;

    var filter = viewModel.createFilter(config),
        value = filter.value = ko.observable();

    var dependencies = [];

    function calculateDependencies() {
        if (config.disableIfNot) {
            dependencies.push(app.createDependencyName(config.disableIf));
        }
        if (config.disableIfNot) {
            dependencies.push(app.createDependencyName(config.disableIfNot));
        }
    }

    function isValidDate(val) {
        return moment(val).isValid();
    }

    function createSupportObservables() {
        filter.enabled = ko.computed(function () {
            var enabled = true;
            if (config.disableIf) {
                enabled = !viewModel.getFilterValue(config.disableIf);
            }
            if (config.disableIfNot) {
                enabled = !!viewModel.getFilterValue(config.disableIfNot);
            }
            return enabled;
        });
    }

    function postInit() {
        if (config.notAfter) {
            value.subscribe(function (currentValue) {
                if (isValidDate(currentValue) && currentValue > viewModel.getFilterValue(config.notAfter)) {
                    viewModel.getFilter(config.notAfter).value(currentValue);
                }
            });
        }
        if (config.notBefore) {
            value.subscribe(function (currentValue) {
                if (isValidDate(currentValue) && currentValue < viewModel.getFilterValue(config.notBefore)) {
                    viewModel.getFilter(config.notBefore).value(currentValue);
                }
            });
        }
        if (config.lastDayOfMonth) {
            value.subscribe(function (currentDate) {
                var current = moment(currentDate),
                    lastDayOfMonth = current.clone().add(1, "months").date(0);
                if (current.date() !== lastDayOfMonth.date()) {
                    setTimeout(function(){
                        value(lastDayOfMonth.toDate());
                    }, 0);
                }
            });
        }
    }

    function getValueToSetup() {
        var valueToSetup = null;

        if (viewModel.cookiesEnabled) {
            valueToSetup = viewModel.cookieData[config.name];
        }

        if (!valueToSetup) {
            valueToSetup = config.defaultValue;
        }

        if (!valueToSetup) {
            valueToSetup = config.lastDayOfMonth
                ? moment().date(0).toDate()
                : new Date();
        }

        return moment(valueToSetup).toDate();
    }

    self.build = function () {
        calculateDependencies();

        var node = new app.DependencyTreeNode(
            app.createDependencyName(config),
            function () {
                var deferred = $.Deferred();

                value(getValueToSetup());

                createSupportObservables();

                deferred.resolve();
                return deferred;
            },
            dependencies,
            postInit);

        return node;
    };
};