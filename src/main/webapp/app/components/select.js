// Localization
$.extend($.fn.selectpicker.defaults, {
    noneSelectedText: 'Ничего не выбрано',
    countSelectedText: 'Выбрано {0} из {1}',
    clearLinkText: 'Очистить',
    selectAllText: 'Выбрать все'
});

app.components.Select = function (params, element) {
    var self = this;

    var filter = app.viewModel.getFilter(params),
        config = params.config;

    self.data = filter.value;
    self.multiple = config.multiple;
    self.options = filter.computed ? filter.computed : filter.options;
    self.withGroups = config.withGroups;

    self.afterRender = function() {
        var selector = self.multiple ? ".multi-select" : ".single-select";
        if(self.withGroups){
            selector+= "-with-groups"
        }

        var $select = $(element).find(selector);

        $select.selectpicker({
            title: config.optionsCaption === false
                ? undefined
                : config.optionsCaption || config.multiple ? "Все" : undefined,
            selectedTextFormat: config.selectedTextFormat || "count>1",
            size: config.size || 10,
            width: config.width || 250,
            liveSearch: true,
            clearLink: config.enableClear,
            selectAllLink: config.enableSelectAll
        });

        if( /Android|webOS|iPhone|iPad|iPod|BlackBerry/i.test(navigator.userAgent) ) {
            $select.selectpicker('mobile');
        }

        if(config.onHide){
            var $selectpicker = $select.data('selectpicker').$newElement;
            $selectpicker.on('hide.bs.dropdown', config.onHide);
        }


        function refreshSelectPicker() {
            $select.selectpicker('refresh');
        }

        self.options.subscribe(function () {
            refreshSelectPicker();
        });

        self.data.subscribe(function (idsToSelect) {
            if(idsToSelect == config.optionsCaption){
                idsToSelect = null;
            }
            self.data.silentUpdateStart();
            $select.selectpicker('val', idsToSelect);
            refreshSelectPicker();
            self.data.silentUpdateStop();
        });

        function refreshEnabledState(enabled) {
            $select.prop('disabled', !enabled);

            refreshSelectPicker();
        }

        refreshSelectPicker();
        refreshEnabledState(filter.enabled());
        filter.enabled.subscribe(refreshEnabledState);
    };
};