(function (app, ko, _, $, window) {
    /**
     * Вкладка. TabStrip содержит одну или несколько вкладок.
     * @param config
     * @constructor
     */
    var Tab = function (config) {
        var self = this;

        var resizeNeeded = true;

        self.title = ko.observable();
        self.visible = ko.observable(true);

        self.active = ko.observable(false);
        self.customParams = {};

        if(_.isString(config)) {
            self.title(config);
        } else {
            if(_.isEmpty(config.title)){
                console.error(config);
                throw new Error("Invalid tab config. It should be String or Object with \"title\" property.")
            }
            self.title(config.title);
            self.customParams = config.customParams;
            self.additionalParams = config.additionalParams;
            self.group = config.group;
        }

        /**
         * Выполняет анимацию отображения вкладки, и, если вкладка отображается в первый раз делает
         * $(window).resize() чтобы обновить ширину и высоту графиков HighCharts
         */
        self.fadeIn = ko.pureComputed(function () {
            var active = self.active();

            if (active && resizeNeeded) {
                $(window).resize();
                resizeNeeded = false;
            }

            return active;
        }).extend({rateLimit: 10});

        self.resetState = function () {
            resizeNeeded = true;
        };
    };

    /**
     * Контейнер вкладок.
     * @param viewModel
     * @param config
     * @constructor
     */
    var TabStrip = function (viewModel, config) {
        var self = this;

        self.tabs = [];
        self.initialized = ko.observable(false);

        _.forEach(config.tabs, function (tab) {
            self.tabs.push(new Tab(tab));
        });

        self.resetTabsState = function(){
            _.forEach(self.tabs, function (tab) {
                tab.resetState();
            });
        }
    };

    /**
     * TabStrip builder. Создает следующие observables:
     *
     * app.viewModel.groups.{groupName}.tabStrips.{tabsStripName}.initialized
     * app.viewModel.groups.{groupName}.tabStrips.{tabsStripName}.tabs
     *
     * @param viewModel
     * @param config
     * @constructor
     */
    app.builders.TabStrip = function (viewModel, config) {
        var self = this;

        var initialized = ko.observable(false);

        self.build = function () {
            var tabStrip = new TabStrip(viewModel, config);
            viewModel.getGroup(config.group).tabStrips[config.name] = tabStrip;

            return new app.DependencyTreeNode(
                app.createDependencyName(config),
                function () {
                    tabStrip.initialized(true);
                },
                calculateDependencies());
        };

        function calculateDependencies() {
            var dependencies = [];
            var dynamicConfig = config.dynamicConfig;

            if (dynamicConfig) {
                _.forEach(dynamicConfig.params, function (param) {
                    dependencies.push(app.createDependencyName(param));
                });
            }

            return dependencies;
        }
    };
})(app, ko, _, $, window);

