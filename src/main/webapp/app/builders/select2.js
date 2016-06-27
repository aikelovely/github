/**
 * Select2 builder. Создает следующие observables и функции:
 *
 * app.viewModel.groups.{groupName}.filters.{filterName}.value - выбранное значение(-ия)
 * app.viewModel.groups.{groupName}.filters.{filterName}.options - возможные значения
 * app.viewModel.groups.{groupName}.filters.{filterName}.isLoading - находится в ссостоянии загрузки
 * app.viewModel.groups.{groupName}.filters.{filterName}.enabled - активен/не активен
 *
 *
 * @param viewModel
 * @param config
 * @constructor
 */
app.builders.Select2 = function (viewModel, config) {
    var self = this;

    var filter = viewModel.createFilter(config);

    var dependencies = [];

    function createSupportObservables(){
        var ds = config.dataSource;

        if (ds.url !== undefined && ds.params) {
            _.forEach(ds.params, function (param) {
                dependencies.push(app.createDependencyName(param));
            });
        }

        var dsParams = new app.DSParams(viewModel, ds.params);

        filter.value = ko.observableArray([]).withPausing();

        filter.isLoading = ko.observable(false);

        filter.ajaxData = function(params){
            var data = _.assign(params,
                dsParams.getRequestOptions(), {
                pageSize: config.pageSize || 10
            });
            return data;
        };

        ko.computed(function(){
            var options = dsParams.getRequestOptions();
            if(filter.clear){
                filter.clear();
            }
        });

        // Состояние фильтра зависит от disableIfNull, disableIfComputed и isLoading
        filter.enabled = ko.computed(function () {
            var enabled = true;

            if (config.disableIfNull) {
                enabled = _.notEmpty(viewModel.getFilterValue(config.disableIfNull));
            }

            if (config.disableIfComputed) {
                var disableIfComputedContext = {
                    params: {}
                };

                _.forEach(config.disableIfComputed.params, function (param) {
                    var filter = viewModel.getFilter(param);
                    disableIfComputedContext.params[filter.name] = filter.value;
                });

                var jsFunc = config.disableIfComputed.jsFunc;
                if (!_.isFunction(jsFunc)) {
                    throw new Error("disableIfComputed.jsFunc should be function!");
                }

                var contextAsJs = ko.toJS(disableIfComputedContext);

                enabled = enabled && !jsFunc(contextAsJs);
            }

            var isLoading = filter.isLoading();

            return enabled && !isLoading;
        });
    }

    self.build = function () {
        createSupportObservables();

        return new app.DependencyTreeNode(
            app.createDependencyName(config),
            function(){},
            dependencies);
    };

    /**
     * Функция сброса значения на начальное по нажатию на кнопку "Очистить все фильтры"
     */
    self.resetState = function () {
        var defaultValue = config.multiple
            ? config.defaultValues || []
            : config.defaultValue;

        filter.value(defaultValue);
    };
};