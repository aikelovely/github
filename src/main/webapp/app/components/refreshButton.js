app.components.RefreshButton = function (params, element) {
    var self = this;

    var groupName = params.group || app.defaultGroupName,
        group = app.viewModel.getGroup(groupName),
        btnText = params.text || "Обновить",
        btnProcessingText = params.processingText || "Загрузка";

    self.style = params.style;
    self.processing = group.processing;
    self.sendFilterData = group.sendFilterData;

    self.refreshButtonText = ko.computed(function () {
        var processing = self.processing();
        return processing ? btnProcessingText : btnText;
    });
};

ko.components.register("refresh-button", {});