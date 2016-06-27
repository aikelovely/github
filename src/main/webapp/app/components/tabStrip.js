app.components.TabStrip = function (params, element, templateNodes) {
    var self = this;

    var parsedNameAndGroup = app.parseNameAndGroup(params),
        config = app.viewModel.getGroup(parsedNameAndGroup.group).config.tabStrips[parsedNameAndGroup.name];

    if (config === undefined) {
        throw new Error("Config for TabStrip not found. Name: " + parsedNameAndGroup.name + "\", group: \"" + parsedNameAndGroup.group + "\"")
    }

    var tabStrip = app.viewModel.getGroup(parsedNameAndGroup.group).tabStrips[parsedNameAndGroup.name],
        initialized = tabStrip.initialized;

    self.group = parsedNameAndGroup.group;
    self.header = config.header;
    self.tabs = tabStrip.tabs;
    self.reportUrl = config.reportUrl;
    self.csvReportUrl = config.csvReportUrl;

    self.activateTab = function (tab) {
        _.forEach(self.tabs, function (t) {
            t.active(false);
        });

        tab.active(true);

        if (config.onActivated) {
            if (!_.isFunction(config.onActivated)) {
                throw new Error("config.onActivated parameter should be function");
            }

            config.onActivated(tab);
        }
    };

    function updateTabMarkup(tab, $markup){
        // Обработка графиков
        _.forEach($markup.find("chart"), function (chartElement) {
            var $chart = $(chartElement);
            var origParams = $chart.attr("params");
            var paramsAttr = origParams + ", customParams: "
                + JSON.stringify(tab.customParams || {});
            if (tab.group && origParams.indexOf("group:") === -1) {
                paramsAttr += ", group: " + JSON.stringify(tab.group);
            }
            $chart.attr("params", paramsAttr);
        });

        // Обработка кнопок выгрузить в Excel/CSV
        _.forEach($markup.find("report-button, csv-report-button"), function (btn) {
            var $btn = $(btn);
            var origParams = $btn.attr("params");
            var paramsAttr = origParams + ", additionalParams: "
                + JSON.stringify(tab.additionalParams || {});
            $btn.attr("params", paramsAttr);
        });

        if (tab.group){
            $markup.html($markup.html().replace(/{groupName}/g, tab.group));
        }

    }

    function init() {
        // Парсим разметку табов. Вытаскиваем разметку только из <tab></tab>, все остальное игнорируется
        var tabsMarkup = $(templateNodes).filter("tab");

        if (config.sameMarkup) {
            if(tabsMarkup.length !== 1){
                throw new Error("TabStrip with \"sameMarkup\" config should contains the only one tab element");
            }

            var markup = tabsMarkup[0];

            _.forEach(self.tabs, function(tab){
                var $markup = $(markup).clone();

                updateTabMarkup(tab, $markup);
                tab.content = $markup.html();
            })
        } else {
            if (self.tabs.length !== tabsMarkup.length) {
                // Сравниваем количество заданных табов в разметке, с заданными в конфиге
                throw new Error("Number of tabs in config and markup should match. TabStrip: \"" + params.name + "\".");
            }

            _.forEach(self.tabs, function(tab, index){
                var $markup = $(tabsMarkup[index]);
                updateTabMarkup(tab, $markup);
                tab.content = $markup.html();
            });
        }

        if (config.dynamicConfig) {

            self.visible = ko.observable(true);

            ko.computed(function () {
                var computedParams = {};

                _.forEach(config.dynamicConfig.params, function (param) {
                    var filter = app.viewModel.getFilter(param);
                    computedParams[filter.name] = filter.value;
                });

                var paramsAsJs = ko.toJS(computedParams);

                // Ждем пока все фильтры, от которых зависят табы, не загрузятся
                if (!initialized()) return;

                var jsFunc = config.dynamicConfig.jsFunc;
                if (!_.isFunction(jsFunc)) {
                    throw new Error("dynamicConfig.jsFunc should be function!");
                }

                console.log("Recalculating tabs for \"" + config.name + "\", group: \"" + config.group + "\"");
                jsFunc(self.tabs, paramsAsJs);

                var visibleTabs = _.filter(self.tabs, function (t) {
                    return t.visible();
                });

                // Активируем первый по порядку таб
                activateFirstTab(visibleTabs);

                // Определяем видимость контейнера
                self.visible(visibleTabs && visibleTabs.length > 0);
            });
        } else {
            // Табы статичны и никогда не меняются
            self.visible = true;
            activateFirstTab(self.tabs);
        }
    }

    function activateFirstTab(tabs) {
        // Активируем первый по порядку таб
        if (tabs && tabs.length > 0) {
            self.activateTab(tabs[0]);
        }
    }

    init();
};

ko.components.register("tab-strip", {});

ko.bindingHandlers['tabHtml'] = {
    'init': function () {
        return {'controlsDescendantBindings': true};
    },
    'update': function (element, valueAccessor) {
        // setHtml will unwrap the value if needed
        ko.utils.setHtml(element, valueAccessor());
        ko.applyBindingsToDescendants(app.viewModel, element);
    }
};