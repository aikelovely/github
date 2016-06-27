app.components.DrillDownButton = function (params, element) {
    var self = this;

    var groupName = params.group || app.defaultGroupName,
        group = app.viewModel.getGroup(groupName);

    self.btnText = params.text || "Вернуться на предыдущий уровень детализации";
    self.visible = ko.computed(function(){
        return group.drillDownLevel() > 0;
    });

    self.onClick = function(){
        group.drillDown(group.drillDownLevel() - 1);
    }
};

ko.components.register("drill-down-button", {});