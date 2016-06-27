app.components.Chart = function (params, element, templateNodes) {
    var self = this;

    var parsedNameAndGroup = app.parseNameAndGroup(params),
        group = app.viewModel.getGroup(parsedNameAndGroup.group),
        config = group.config.charts[parsedNameAndGroup.name];

    if (config === undefined) {
        throw new Error("Config for chart not found. Name: " + parsedNameAndGroup.name + "\", group: " + parsedNameAndGroup.group)
    }

    self.dataSourceUrl = config.dataSource;
    self.dataSourceAdditionalParams = config.additionalParams;

    var jsFunc = config.jsFunc,
        customParams = _.assign({}, params.customParams, config.customParams),
        $chart = $(element).find(".chart");

    self.isVisible = function () {
        return $(document).find(element).length !== 0;
    };

    self.name = parsedNameAndGroup.name;
    self.isFirstRun = true;
    self.isLoading = ko.observable(false);
    self.containsError = ko.observable(false);
    self.errorMessage = ko.observable();
    self.reportUrl = config.reportUrl;
    self.group = parsedNameAndGroup.group;
    self.customMarkup = false;
    self.deferred = config.deferred;
    self.additionalParams = config.additionalParams;

    self.queryList = ko.observable();

    self.render = function (filterData, ajaxResult) {
        self.queryList(ajaxResult.queryList);

        self.isLoading(false);
        self.isFirstRun = false;
        self.containsError(false);

        if (!ajaxResult.success) {
            self.containsError(true);
            self.errorMessage(ajaxResult.error);
            return;
        }

        // Сохраняем ширину и высоту, чтобы окно браузера не дергалось
        $chart.css({
            'min-width': $chart.width(),
            'min-height': $chart.height()
        });

        var $chartContainer = $chart.find(".chart-container");
        $chartContainer.html("");

        try {
            jsFunc($chartContainer, filterData, ajaxResult.data, customParams, group);
        }
        catch (ex) {
            console.error(ex.stack || ex);
            self.containsError(true);
            self.errorMessage(ex.stack);
        }

        setTimeout(function () {
            $chart.css({
                'min-width': '',
                'min-height': ''
            });
        }, 200);
    };

    function init() {
        if (!_.isFunction(jsFunc)) {
            console.error("Invalid chart config: ", config);
            throw new Error("jsFunc should be function!");
        }

        if (templateNodes && templateNodes.length > 0) {
            if($(templateNodes).filter(".chart-container")[0] === undefined &&
                        $(templateNodes).find(".chart-container")[0] === undefined) {
                console.error("Invalid chart: ", config, "Markup: ", templateNodes);
                throw new Error("Invalid custom chart markup. <div class=\"chart-container\"></div> was not found.")
            }

            self.customMarkup = true;
            self.templateNodes = templateNodes;
        }

        group.charts.push(self);
    }

    init();
};

ko.components.register("chart", {});