/**
 * ViewModel, основная модель витрины. Каждая модель содержит одну или несколько групп фильтров (см. FilterGroup)
 * @param config JSON-конфигурация модели. Пример:
 * {
 *  groups: [], // см. filterGroup.js
 *  cookies: true,
 *  forceShowCharts: false
 * }
 */
app.DynamicViewModel = function (config) {
    var self = this;

    // список групп фильтров
    self.groups = {};

    var dependencyTree = [], // дерево зависимостей фильтров
        builders = [],
        selfInitAwait; // $.Deferred, resolve() выполняется после полной инициализации

    // параметры фильтров из cookies
    self.cookieData = app.cookieManager.filterData();
    // если true, то начальные значения фильтров будут на основе данных из cookies
    self.cookiesEnabled = false;

    // отображение отладочной информации на витрине (текст SQL запросов)
    self.showSQL = ko.observable(app.cookieManager.sqlVisibility());
    if (!app.isSqlViewer) {
        self.showSQL(false);
        app.cookieManager.sqlVisibility(false);
    }
    self.toggleSQLVisibility = function () {
        var isVisible = !self.showSQL();

        self.showSQL(isVisible);
        app.cookieManager.sqlVisibility(isVisible);
    };

    // функция выполняющая очистку кэша MyBatis
    self.clearDatabaseCache = function () {
        app.ajaxUtils.clearDatabaseCache().done(function (isOk) {
            if (isOk) {
                app.showAlert("Кэш был успешно очищен", "Кэш");
            }
            else {
                app.showAlert("Произошла ошибка при попытке очистить кэш. Попробуйте еще раз...");
            }
        });
    };

    /**
     * Поиск группы фильтров по имени
     * @param groupName
     * @returns {FilterGroup}
     */
    self.getGroup = function (groupName) {
        var group = self.groups[groupName];
        if (group === undefined) {
            throw new Error('Group "' + groupName + ' not found.');
        }

        return group;
    };

    /**
     * Поиск фильтра по имени
     * @param nameAndGroup строка или объект, см. метод app.parseNameAndGroup
     * @returns {filter}
     */
    self.getFilter = function (nameAndGroup) {
        var parsedNameAndGroup = app.parseNameAndGroup(nameAndGroup);
        var group = self.getGroup(parsedNameAndGroup.group);

        var filter = group.filters[parsedNameAndGroup.name];
        if (filter === undefined) {
            throw new Error("Filter not found! Name: \"{0}\" , group: \"{1}\".".format(
                parsedNameAndGroup.name,
                parsedNameAndGroup.group));
        }
        return filter;
    };

    /**
     * Создает фильтр в соответсвующей группе фильтров
     * @param nameAndGroup строка или объект, см. метод app.parseNameAndGroup
     * @returns {*} ссылка на фильтр
     */
    self.createFilter = function (nameAndGroup) {
        var parsedNameAndGroup = app.parseNameAndGroup(nameAndGroup);
        var group = self.getGroup(parsedNameAndGroup.group);

        var filter = group.filters[parsedNameAndGroup.name] = {};

        filter.name = parsedNameAndGroup.name;

        return filter;
    };

    /**
     * Возвращает текущее значение фильтра
     * @param nameAndGroup nameAndGroup строка или объект, см. метод app.parseNameAndGroup
     * @returns {*}
     */
    self.getFilterValue = function (nameAndGroup) {
        return self.getFilter(nameAndGroup).value();
    };

    /**
     * Обрабатока конфигурации фильтров и вкладок. Строит дерево зависимостей.
     */
    function buildDependencyTree(groupConfig) {
        _.forEach(groupConfig.filters, function (config, name) {

            config.name = name;
            config.group = groupConfig.name;

            var builderConstructor = app.builders[config.type];

            if (builderConstructor === undefined) {
                console.error("Invalid config:", config);
                throw new Error("Unknown filter type");
            }

            var builder = new builderConstructor(self, config);

            var node = builder.build();
            dependencyTree.push(node);

            builders.push(builder);
        });

        _.forEach(groupConfig.tabStrips, function (config, name) {
            config.name = name;
            config.group = groupConfig.name;

            var builder = new app.builders.TabStrip(self, config);

            var node = builder.build();
            dependencyTree.push(node);

            builders.push(builder);
        });
    }

    function createFakeDeferred() {
        var deferred = $.Deferred();
        deferred.resolve();
        return deferred.promise();
    }

    /**
     * Решает дерево зависимостей и выполняет метод init() и postInit() у каждого узла дерева.
     */
    function resolveDependencyTree() {
        var globalDeferredList = {},
            groupWithoutDependencies = [],
            groupWithDependencies = [];

        _.forEach(dependencyTree, function (node) {
            if (node.dependencies.length > 0) {
                groupWithDependencies.push(node);

                // 0 - White, 1 - Gray, 2 - Black http://en.wikipedia.org/wiki/Topological_sort
                node.color = 0;
            } else {
                groupWithoutDependencies.push(node);

                globalDeferredList[node.name] = node.initFunc() || createFakeDeferred();
            }
        });

        var groupWithDependenciesInitOrder = [];

        function getEdges(nodeName) {
            return _.filter(dependencyTree, function (n) {
                return n.name !== nodeName &&
                    _.indexOf(n.dependencies, nodeName) !== -1;
            });
        }

        function dfs(node) {
            if (node.color === 1) return true;
            if (node.color === 2) return false;

            node.color = 1;

            var edges = getEdges(node.name);
            for (var i = 0; i < edges.length; i++) {
                if (dfs(edges[i])) return true;
            }

            groupWithDependenciesInitOrder.push(node);
            node.color = 2;
            return false;
        }

        for (var i = 0; i < groupWithDependencies.length; i++) {
            var n = groupWithDependencies[i];
            var circle = dfs(n);
            if (circle) {
                throw new Error("Circular dependency is detected." + n.name);
            }
        }

        _.forEach(groupWithDependenciesInitOrder.reverse(), function (node) {
            var deferredList = [];
            _.forEach(node.dependencies, function (dependecy) {
                deferredList.push(globalDeferredList[dependecy]);
            });

            var deferred = $.Deferred();

            $.when.apply(null, deferredList).done(function () {
                var await = node.initFunc() || createFakeDeferred();

                $.when(await).done(function () {
                    deferred.resolve();
                });
            });

            globalDeferredList[node.name] = deferred.promise();
        });

        // Пост-обработка, порядок выполнения функций postInit не важен.
        $.when.apply(null, _.toArray(globalDeferredList)).done(function () {
            _.forEach(dependencyTree, function (node) {
                if (node.postInitFunc) {
                    node.postInitFunc();
                }
            });

            selfInitAwait.resolve();
        });
    }

    function createFilterGroups() {
        var groupConfigs = config.groups;

        // рекурсивно создает группу и все подчиненные ей группы
        function createDependencyTreeGroups(currentGroupConfigs, masterGroup) {
            _.forEach(currentGroupConfigs, function (config) {
                var group = self.groups[config.name] = new app.FilterGroup(config, self);


                buildDependencyTree(config);
                if (group.slaves && group.slaves.length > 0) {
                    createDependencyTreeGroups(group.slaves, group);
                }
            });
        }

        // если группа всего одна и ее имя не задано, используем имя группы по-умолчанию
        if (groupConfigs.length == 1 && groupConfigs[0].name === undefined) {
            groupConfigs[0].name = app.defaultGroupName;
        }

        createDependencyTreeGroups(groupConfigs, null);
        resolveDependencyTree();

        function initGroups(currentGroupConfigs, masterGroup) {
            _.forEach(currentGroupConfigs, function (config) {
                var group = self.groups[config.name];

                console.log("Initializing group \"" + config.name + "\"...");
                group.initialize(masterGroup);

                if (group.slaves && group.slaves.length > 0) {
                    console.log("Creating slaves for group: " + config.name);
                    initGroups(group.slaves, group);
                }
            })
        }

        selfInitAwait.done(function () {
            initGroups(groupConfigs, null);

            setTimeout(showChartsIfNeeded, 0);
        });

        var visibleChartsComputed = {};

        _.forEach(groupConfigs, function (config) {
            visibleChartsComputed[config.name] = self.groups[config.name].visibleCharts;
        });

        self.visibleCharts = ko.computed(function () {
            var groupVisibility = ko.toJS(visibleChartsComputed);
            var visible = false;

            for (var groupName in groupVisibility) {
                if (groupVisibility[groupName]) {
                    visible = true;
                    break;
                }
            }

            return visible;
        });
    }

    /**
     * Показываекм графики сразу после загрузки витрины если задан параметр forceShowCharts = true в
     * cookies или config.
     */
    function showChartsIfNeeded() {
        if (self.cookieData.forceShowCharts || config.forceShowCharts) {
            _.forEach(self.groups, function (g) {
                g.showCharts(true);
            });
        }
    }

    function getQueryParameters() {
        var match,
            pl     = /\+/g,  // Regex for replacing addition symbol with a space
            search = /([^&=]+)=?([^&]*)/g,
            decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
            query  = window.location.search.substring(1);

        var urlParams = {};
        while (match = search.exec(query))
            urlParams[decode(match[1])] = decode(match[2]);
        return urlParams;
    }

    /**
     * Функция-обработчик кнопки "Очистить все фильтры". Сбрасывает состояние фильтров на начальное.
     */
    self.resetFilterState = function () {
        _.forEach(builders, function (builder) {
            if (builder.resetState) {
                builder.resetState();
            }
        });
    };

    /**
     * Основная функция, инициализаует модель.
     */
    self.init = function () {
        console.log("Initializing dynamic view model...");
        selfInitAwait = $.Deferred();

        var urlParams = getQueryParameters();

        if(!_.isEmpty(urlParams)){
            self.cookiesEnabled = true;
            self.cookieData = urlParams;
        }
        else {
            switch (config.cookies) {
                case true:
                    self.cookiesEnabled = true;
                    break;
                case false:
                    self.cookiesEnabled = false;
                    break;
                case "OnlyDrillDown":
                default:
                    self.cookiesEnabled = self.cookieData.forceShowCharts === true;
            }
        }


        createFilterGroups();
    };
};