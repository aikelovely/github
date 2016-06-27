app.components.CsvReportButton = function (params, element) {
    var self = this;

    var groupName = params.group || app.defaultGroupName,
        group = app.viewModel.getGroup(groupName),
        reportUrl = params.reportUrl,
        btnText = params.text || "Выгрузить в CSV",
        btnProcessingText = params.processingText || "Создание CSV...";

    if (_.isEmpty(reportUrl)) {
        console.error("Invalid report button params:", params, element);
        throw new Error("ReportUrl cannot be null or empty");
    }

    self.style = params.style;
    self.processing = ko.observable();

    self.disabled = ko.pureComputed(function () {
        return group.processing() || self.processing();
    });

    self.reportButtonText = ko.computed(function () {
        var processing = self.processing();
        return processing ? btnProcessingText : btnText;
    });

    self.reportCreated = ko.observable(false);
    self.queryList = ko.observable();

    self.generateReport = function () {
        console.log("Generating CSV report by url \"" + reportUrl + "\"...");

        self.processing(true);

        var filterData = group.filterData();
        var requestData = JSON.stringify(_.assign(filterData, params.additionalParams || {}));

        app.ajaxUtils.postData(reportUrl, requestData).done(function (ajaxResult) {
            self.processing(false);
            self.reportCreated(true);
            self.queryList(ajaxResult.queryList);

            if (!ajaxResult.success) {
                app.showAlert(ajaxResult.error);
                return;
            }

            var reportName = ajaxResult.data.reportName,
                id = ajaxResult.data.id,
                reportUrl = app.rootUrl + "csv/getById?id=" + id + "&reportName=" + reportName;

            setTimeout(function () {
                window.location = reportUrl;
            }, 100);
        });
    };
};

ko.components.register("csv-report-button", {});