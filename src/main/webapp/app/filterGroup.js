/**
 * Группа фильтров. Витрина состоит из одной или нескольких групп фильтров. Каждая группа вкдючает в себя:
 *  - собственно сами фильтры (filters)
 *  - графики (charts)
 *  - владки (tabStrips)
 *  - текущее состояние фильтров (filterData)
 *
 * Между группами может быть отношение master-slave.
 * Обычно, slave группа нужна для реализации следующего функционала:
 *  есть основной набор фильтров (назовем эту групу "main") и есть несколько фильтров ("details"), которые относятся т
 *  только к отдельному графику; этот график должен получать данные не только от группы фильтров "details" но и от
 *  группы "main" в том числе. В таком случае config будет следующим:
 *
 *  { name: "main",
 *    filters: {},
 *    charts: {},
 *    slaves: [{
 *               name: "details",
 *               filters: {},
 *               charts: {}
 *           }]
 *  }
 *
 * @param groupConfig параметры группы. Пример:
 *  {
 *    name: "groupName", // обязательный параметр, если групп больше одной
 *    filters: {},
 *    charts: {},
 *    tabStrips: {},
 *    slaves: []
 *  }
 * @param viewModel
 */
app.FilterGroup = function (groupConfig, viewModel) {
    var self = this;

    self.config = groupConfig;
    self.name = groupConfig.name;
    self.slaves = groupConfig.slaves || [];

    self.isInitialized = ko.observable(false);

    self.filters = {};
    self.tabStrips = {};
    self.charts = [];

    self.processingCharts = ko.observable(false);
    self.visibleCharts = ko.observable(false);
    self.forceDisplayErrors = ko.observable(false).extend({ notify: 'always' });

    self.currentAjaxRequests = ko.observableArray([]);

    self.filterLog = ko.observableArray([]);

    /**
     * Загрузка данных для фильтров. Обновляет список текущих ajax запросов (currentAjaxRequests)
     * и сохраняет отладочную информацию в filterLog.
     * @param filterName
     * @param url - url вида showcaseName/filterName
     * @param options параметры
     * @param observable ko.observable() or ko.observableArray() фильтра
     * @returns {$.Deferred}
     */
    self.getDataForFilter = function (filterName, url, options, observable) {
        self.currentAjaxRequests.push(filterName);

        return app.ajaxUtils.getDataForFilter(url, options, observable)
            .done(function (ajaxResult) {
                // Записываем в лог информацию о загрузке фильтра
                var logEntry = {
                    time: moment().format("HH:mm:ss"),
                    filterName: filterName,
                    queryList: ajaxResult.queryList,
                    errorMessage: ajaxResult.error
                };
                self.filterLog.unshift(logEntry);
            })
            .always(function () {
                self.currentAjaxRequests.remove(filterName);
            });
    };


    /**
     * True, если в данный момент загружаются фильтры или графики.
     * Влияет на доступность кнопки "Обновить"
     */
    self.processing = ko.computed(function () {
        if(!self.isInitialized()) return true;

        var currentAjaxRequests = self.currentAjaxRequests(),
            processingCharts = self.processingCharts();

        return processingCharts || currentAjaxRequests && currentAjaxRequests.length > 0;
    });

    /**
     * Показывает/обновляет графики.
     * @param saveToCookies true, если значение фильтров нужно записывать в cookie
     * @param showOnlyThisGroup true, если не нужно отображать графики подчиненных групп.
     * Не обязательный параметр, false по-умолчанию.
     */
    self.showCharts = function (saveToCookies, showOnlyThisGroup) {
        console.log("Refreshing charts for group \"" + self.name + "\"...");
        self.processingCharts(true);

        _.forEach(self.tabStrips, function (tabStrip) {
            tabStrip.resetTabsState();
        });

        setTimeout(function () {
            var filterData = self.filterData();

            if (saveToCookies) {
                app.cookieManager.filterData(filterData);
            }

            // обрабатываем только видимые графики
            var charts = _(self.charts)
                .filter(function (c) {
                    return c.isVisible() && !c.deferred;
                })
                .toArray(self.charts)
                .value();

            if (charts.length === 0) {
                self.visibleCharts(true);
                self.processingCharts(false);
                return;
            }

            var chartsByUrl = _.groupBy(charts, function (c) {
                if(c.additionalParams === undefined){
                    return c.dataSourceUrl;
                }
                return c.dataSourceUrl + JSON.stringify(c.additionalParams);
            });

            setTimeout(function () {
                var ajaxRequests = [];

                _.forEach(chartsByUrl, function (groupedCharts, url) {
                    // Обновляем состояние графиков, показываем spinner или надпись "загрузка"
                    _.forEach(groupedCharts, function (c) {
                        c.isLoading(true);
                    });

                    var first = groupedCharts[0];
                    var requestData = _.assign({}, filterData, first.additionalParams);

                    var request = app.ajaxUtils.postData(first.dataSourceUrl, JSON.stringify(requestData));

                    request.done(function (ajaxResult) {
                        _.forEach(groupedCharts, function (c) {
                            c.render(filterData, ajaxResult);
                        });
                    });

                    ajaxRequests.push(request);
                });

                self.visibleCharts(true);

                if (ajaxRequests.length > 0) {
                    $.when.apply($, ajaxRequests).always(function () {
                        self.processingCharts(false);
                    });
                } else {
                    self.processingCharts(false);
                }
            }, 10)
        }, 10);

        // Показываем графики у всех зависимых групп
        if (!showOnlyThisGroup) {
            if (self.slaves.length > 0) {
                _.forEach(self.slaves, function (slave) {
                    if (!slave.dontShowAfterMaster) {
                        viewModel.groups[slave.name].showCharts(false);
                    }
                });
            }
        }
    };

    self.showSingleChart = function (chartName) {
        console.log("Refreshing single chart \"" + chartName + "\"...");

        var chart = _.find(self.charts, function(c){
            return c.name === chartName
        });

        if (chart === undefined) {
            console.error("Chart \"" + chartName + "\" not found!");
            return;
        }

        self.processingCharts(true);
        chart.isLoading(true);

        setTimeout(function () {
            var filterData = self.filterData();
            var additionalParams = chart.additionalParams;
            var requestData = _.assign({}, filterData, additionalParams || {});
            var request = app.ajaxUtils.postData(chart.dataSourceUrl, JSON.stringify(requestData));

            request.done(function (ajaxResult) {
                chart.render(filterData, ajaxResult);
                self.processingCharts(false);
            });
        }, 10);
    };

    /*
     drillDownLevel и drillDownDataStorage используются для реализации drill down.
     Drill down может выполняться как "вперед" (при этом увеличивается "level"), так и "назад".
     Обычно, drill down "вперед" происходит с дополнительным параметрами и эти параметры сохраняются
     в drillDownDataStorage, чтобы когда потребуется вернуться на предыдущий уровень их можно было прочитать.
     */
    self.drillDownLevel = ko.observable(0);
    var drillDownDataStorage = ko.observable({"0": {}});

    var maxDrillDownLevel = groupConfig.maxDrillDownLevel;

    /**
     * Функция-обработчик кнопки "Обновить". Выполняет валидацию фильтров, и, если все в порядке,
     * сохраняет текущее состояние фильтров в cookies и обновляет графики.
     */
    self.sendFilterData = function () {
        if (!self.validation.isValid()) {
            self.forceDisplayErrors(true);
            return;
        }

        // Сбрасываем уровень DrillDown
        function resetDrillDownLevel(group) {
            group.drillDownLevel(0);
            ko.utils.arrayForEach(group.slaves, function (slave) {
                resetDrillDownLevel(viewModel.groups[slave.name]);
            });
        }

        resetDrillDownLevel(self);

        self.showCharts(true);
    };

    /**
     * Выпонялет drill down на определенный уровень и обновляет состояние графиков.
     * @param level уровень drill down.
     * @param extensionData дополнительные параметры, расширяют filterData
     * @param showOnlyThisGroup true, если не нужно отображать графики подчиненных групп
     */
    self.drillDown = function (level, extensionData, showOnlyThisGroup) {
        console.log("DrillDown4 by group {0}. Level: {1}. Data: {2}".format(
            self.name,
            level,
            JSON.stringify(extensionData || {})));

        var currentDrillDownLevel = self.drillDownLevel();
        var nextLevel = Math.max(level, currentDrillDownLevel);

        if (nextLevel && maxDrillDownLevel !== undefined
            && level >= currentDrillDownLevel && currentDrillDownLevel >= maxDrillDownLevel) {
            console.log("Max DrillDown level ({0}) for group {1} was reached. Next detalization was prevented.".format(maxDrillDownLevel, self.name));
            return;
        }

        self.drillDownLevel(level);

        var drillDownData = drillDownDataStorage();
        console.log(drillDownData);
        if (level >= currentDrillDownLevel) {
             drillDownData["" + level] = extensionData;
        } 
        else if (level < currentDrillDownLevel) {
            // blev Так и непонял зачем было это услоовие , похоже ошибку кто то заложил специально, раскоментировать и
            // параметры будут передавться и с низу в верх по дереву? тестировать тут через  console.log
              // drillDownData["" + level] = extensionData;
            // а это убрать
            for (var i = currentDrillDownLevel; i > level; i--) {
                delete drillDownData["" + i];
            }
        }

        else  {
            for (var i = currentDrillDownLevel; i > level; i--) {
                delete drillDownData["" + i];
            }
        }

        drillDownDataStorage(drillDownData);

        setTimeout(function () {
            self.showCharts(false, showOnlyThisGroup);
        }, 10);
    };

    /**
     * Возвращает дополнительные параметры заданного уровня drill down (см. extensionData предыдущего метода)
     * @param level уровень drill down. Если не задан, возвращает параметры текущего уровня
     * @returns {*}
     */
    self.drillDownData = function (level) {
        if (level === undefined) {
            level = self.drillDownLevel();
        }
        var drillDownData = drillDownDataStorage();
        return drillDownData["" + level];
    };



    /**
     * Создает filterData. FilterData содержит всю необходимую информвцию для отображения графиков:
     *  - текущее состояние фильтров master групп (если они есть) и текущей группы
     *  - дополнительные параметры drill down
     *  - текущий уровень drill down     *
     * @param masterGroup
     */
    function createFilterData(masterGroup) {
        var filterData = {};

        _.forEach(self.filters, function (filter, name) {
            filterData[name] = filter.value;
        });

        function removeDisabledFilterValues(ownFilterData){
            _.forEach(self.filters, function (filter, name) {
                if(filter.enabled && ko.isObservable(filter.enabled)){
                    if(!filter.enabled()){
                        delete ownFilterData[name];
                    }
                }
            });

            return ownFilterData;
        }

        if (masterGroup) {
            self.filterData = ko.computed(function () {
                self.visibleCharts(false);

                var masterFilterData = masterGroup.filterData(),
                    ownFilterData = removeDisabledFilterValues(ko.toJS(filterData)),
                    currentDrillDownLevel = self.drillDownLevel(),
                    drillDownData = drillDownDataStorage(),
                    currentLevelDrillDownData = drillDownData["" + currentDrillDownLevel];

                return $.extend(
                    {},
                    masterFilterData,
                    ownFilterData,
                    currentLevelDrillDownData,
                    {drillDownLevel: currentDrillDownLevel});
            });
        } else {
            self.filterData = ko.computed(function () {
                self.visibleCharts(false);

                var ownFilterData = removeDisabledFilterValues(ko.toJS(filterData)),
                    currentDrillDownLevel = self.drillDownLevel(),
                    currentLevelDrillDownData = self.drillDownData(currentDrillDownLevel);

                return $.extend(
                    {},
                    ownFilterData,
                    currentLevelDrillDownData,
                    {drillDownLevel: currentDrillDownLevel});
            });
        }
    }

    /**
     * Обработка правил валидации
     */
    function initValidation() {
        var validatedObservable = {};

        _.forEach(groupConfig.filters, function (f) {
            if (f.validationRules && f.validationRules.length) {
                validatedObservable[f.name] = self.filters[f.name].value;
            }
        });

        self.validation = ko.validatedObservable(validatedObservable);
    }

    function processOtherFilterConfiguration() {
        _.forEach(groupConfig.filters, function (config, name) {
            if (config.forceShowCharts === true) {
                self.filters[name].value.subscribe(function () {
                    self.showCharts(false);
                });
            }
        });
    }

    self.initialize = function (masterGroup) {
        initValidation();
        createFilterData(masterGroup);
        processOtherFilterConfiguration();
        self.isInitialized(true);
    };
};