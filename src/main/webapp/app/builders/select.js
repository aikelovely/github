/**
 * Select builder. Создает следующие observables и функции:
 *
 * app.viewModel.groups.{groupName}.filters.{filterName}.value - выбранное значение(-ия)
 * app.viewModel.groups.{groupName}.filters.{filterName}.options - возможные значения
 * app.viewModel.groups.{groupName}.filters.{filterName}.isLoading - находится в ссостоянии загрузки
 * app.viewModel.groups.{groupName}.filters.{filterName}.enabled - активен/не активен
 *
 * app.viewModel.groups.{groupName}.filters.{filterName}.getSelectedOptions() - возвращет выбранные объекты,
 * т.е. в отличии от value эта функция вернет не [1,2], а [{id: 1, name: "value1"], {id: 2, name: "value2"}]
 *
 * @param viewModel
 * @param config
 * @constructor
 */
app.builders.Select = function (viewModel, config) {
    var self = this;

    var filter = viewModel.createFilter(config),
        group = viewModel.getGroup(config.group);

    var dependencies = [];

    var optionsTempStorage = null;
    var valueTempStorage = null;

    function createSupportObservables() {
        var ds = config.dataSource;

        if (ds.url !== undefined && ds.params) {
            _.forEach(ds.params, function (param) {
                dependencies.push(app.createDependencyName(param));
            });
        }

        filter.value = config.multiple
            ? ko.observableArray([]).withPausing()
            : ko.observable().withPausing();

        filter.options = ko.observableArray([]).withPausing();
        filter.isLoading = ko.observable(false);
        filter.disableIfComputedLoaded = ko.observable(false);

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
                    dependencies.push(app.createDependencyName(param));


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

        if(config.disableIfComputed) {
            // Если фильтр находится в состоянии disabled, он не должен содержать никаких значений.

            ko.computed(function () {
                var enabled = filter.enabled();
                var options = filter.options.peek(),
                    value = filter.value.peek();

                if (enabled) {
                    if(optionsTempStorage !== null) {
                        filter.options(optionsTempStorage);

                        setTimeout(function () {
                            var valueToSetup = valueTempStorage;
                            if (valueTempStorage === null || valueTempStorage === undefined) {
                                if (optionsTempStorage && optionsTempStorage.length > 0) {
                                    valueToSetup = optionsTempStorage[0].id;
                                }
                            }

                            filter.value(valueToSetup);
                            optionsTempStorage = null;
                        }, 10);
                    }
                    else {
                        filter.disableIfComputedLoaded(true);
                    }
                }
                else if (!enabled && (optionsTempStorage == null || valueTempStorage == null)) {
                    if (!_.isEmpty(options)) {
                        optionsTempStorage = options;
                        filter.options([]);
                    }
                    if (!_.isEmpty(value)) {
                        valueTempStorage = value;
                        filter.value(null);
                    }
                }
            });
        }

        filter.getSelectedOptions = function () {
            var options = filter.options(),
                value = filter.value();

            if (config.multiple) {
                return _.filter(options, function (i) {
                    return _.indexOf(value, i.id.toString()) !== -1;
                });
            } else {
                return _.filter(options, function (i) {
                    return i.id == value;
                });
            }
        };

        /**
         * Пост-обработка полученных ajax запросом данных.
         */
        var computed = config.dataSource.computed;
        if (computed) {
            var context = {
                loadedData: filter.options,
                params: {}
            };

            _.forEach(computed.params, function (param) {
                var p = typeof param === "object" ? param : { name: param, type: "value"};
                var filter = viewModel.getFilter(p.name);
                context.params[filter.name + (p.type === "value" ? "" : "_" + p.type)] = filter[p.type];
            });

            var jsFunc = computed.jsFunc;
            if (!_.isFunction(jsFunc)) {
                throw new Error("dataSource.computed.jsFunc should be function!");
            }

            filter.computed = ko.computed(function () {
                var contextAsJs = ko.toJS(context);
                var result = jsFunc(contextAsJs);
                if(result){
                    setTimeout(function(){
                        setInitialValue(result);
                    }, 0);
                }
                return result;
            });
        }
    }

    function setInitialValue(options) {
        var valueToSetup;

        if (viewModel.cookiesEnabled) {
            valueToSetup = viewModel.cookieData[config.name];
        }

        // "null" -  значение из cookie
        if (valueToSetup === undefined || valueToSetup === "null") {
            valueToSetup = config.defaultValue || config.defaultValues;
        }

        if (valueToSetup === undefined || valueToSetup === "null") {
            if (!config.multiple && _.isEmpty(config.optionsCaption)) {
                if (!_.isEmpty(options)) {
                    valueToSetup = options[0].id;
                }
            }
        }

        filter.value(valueToSetup);
    }

    function init() {
        if (config.dataSource.url) { // данные получаем ajax запросом
            var params = new app.DSParams(viewModel, config.dataSource.params),
                deferred = $.Deferred();

            function getDataForFilter(requestOptions, onSuccess) {
                return group.getDataForFilter(
                    config.title,
                    config.dataSource.url,
                    requestOptions,
                    function (options) {
                        if (!config.multiple && !_.isEmpty(config.optionsCaption) && options) {
                            options.unshift({id: null, name: config.optionsCaption});
                        }

                        filter.options(options);
                        filter.isLoading(false);

                        onSuccess(options);
                    });
            }

            subscribeToObservables();

            /**
             * Если у фильтра нет никаких зависимостей то просто получаем данные с сервера и устанавливает значения
             * по-умолчанию
             */
            if (dependencies.length === 0) {
                filter.isLoading(true);
                getDataForFilter(params.getRequestOptions(), function (options) {
                    setInitialValue(options);

                    setTimeout(function(){
                        deferred.resolve();
                    }, 10);
                });
            } else {
                /**
                 * Если фильтр зависит от других фильтров, то нужно создать ko.computed, которая будет срабатывать
                 * каждый раз когда любой из этих фильтров изменится.
                 */
                var isInitialized = false;
                ko.computed(function () {
                    var requestOptions = params.getRequestOptions(); // в самом начале для инициализации ko.computed

                    filter.options([]);

                    if (params.testRequiredObservables()) {
                        filter.isLoading(true);
                        getDataForFilter(requestOptions, function (options) {
                            if (!isInitialized) {
                                setInitialValue(options);

                                isInitialized = true;
                            } else if (!config.multiple && _.isEmpty(config.optionsCaption)) {
                                if (!_.isEmpty(options)) {
                                    filter.value(options[0].id);
                                }
                            }

                            setTimeout(function(){
                                deferred.resolve();
                            }, 10);
                        });
                    }
                });
            }

            return deferred.promise();
        } else if (config.dataSource.data) {
            /**
             * Данные заданы локально, в виде [{id: 1, name: "someName}, {..}]
             */
            subscribeToObservables();
            if (!config.multiple && !_.isEmpty(config.optionsCaption)) {
                config.dataSource.data.unshift({id: null, name: config.optionsCaption});
            }
            filter.options(config.dataSource.data);
            setTimeout(function () {
                setInitialValue(config.dataSource.data);
            }, 0);
        } else {
            if(!config.dataSource.computed) {
                console.error(config);
                throw new Error('Data Source should have either "url" or "data" parameters or must be a computed property');
            }
        }

        return null;
    }

    function subscribeToObservables() {
        var emptyValue = config.multiple ? [] : undefined;

        /**
         * Изменение filter.options ознает что фильтр был перезагружен, следовательно нужно сбросить значение
         * filter.value
         */
        filter.options.subscribe(function (currentOptions) {
            var currentValue = filter.value();
            if (!_.isEmpty(currentValue)) {
                filter.value(emptyValue);
            }
        });

        /**
         * Подписываеимся на filter.value чтобы не допустить рассинхронизации данных, в случае если будет попытка
         * задать не существующий id(-s).
         */
        filter.value.subscribe(function (idsToSelect) {

            var currentOptions = filter.options();

            if (!currentOptions || currentOptions.length === 0) {
                return;
            }

            if (!config.multiple) {
                if(idsToSelect === config.optionsCaption){
                    idsToSelect = [];
                }
                else {
                    idsToSelect = [idsToSelect];
                }
            }

            var presentIds = [];
            if (config.withGroups) {
                _.forEach(currentOptions, function (item) {
                    _.forEach(item.options, function (option) {
                        if (idsToSelect) {
                            for (var i = 0; i < idsToSelect.length; i++) {
                                if (idsToSelect[i] == option.id) {
                                    presentIds.push(idsToSelect[i]);
                                }
                            }
                        }
                    });
                });
            } else {
                presentIds = _.filter(idsToSelect, function (id) {
                    for (var i = 0; i < currentOptions.length; i++) {
                        if (currentOptions[i].id == id) {
                            return true;
                        }
                    }
                    return false;
                });
            }

            idsToSelect = config.multiple ? presentIds : presentIds[0];

            filter.value.silentUpdateStart();
            filter.value(idsToSelect);
            filter.value.silentUpdateStop();
        });

        filter.enabled.subscribe(function (isEnabled) {
            if (!isEnabled) {
                filter.value(emptyValue);
            }
        });
    }

    self.build = function () {
        createSupportObservables();

        var postInit = config.postInit || function(){};

        return new app.DependencyTreeNode(
            app.createDependencyName(config),
            init,
            dependencies,
            function() { return postInit(config, filter, viewModel); });
    };

    /**
     * Функция сброса значения на начальное по нажатию на кнопку "Очистить все фильтры"
     */
    self.resetState = function () {
        var defaultValue = config.multiple
            ? config.defaultValues || []
            : config.defaultValue;

        valueTempStorage = null;

        filter.value(defaultValue);
    };
};