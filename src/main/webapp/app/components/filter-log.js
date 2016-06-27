app.components.FilterLog = function (params, element) {
    var self = this;

    var groupName = params.group || app.defaultGroupName,
        group = app.viewModel.getGroup(groupName);

    self.filterLog = group.filterLog;

    self.showErrorMessage = function(logEntry){
        app.showAlert(logEntry.errorMessage, "Ошибка при загрузке фильтра \"{0}\"".format(logEntry.filterName));
    };

    self.toggleCollapse = function(){
        $(element).find('.collapse').collapse('toggle');
    };
};

ko.components.register("filter-log", {});