app.components.Select2 = function (params, element) {
    var self = this;

    var defaultConfig = {
        optionsCaption: "Все",
        width: 300,
        enableClear: false,
        delay: 250
    };

    var parsedNameAndGroup = app.parseNameAndGroup(params),
        group = app.viewModel.getGroup(parsedNameAndGroup.group);

    var filter = app.viewModel.getFilter(params),
        config = _.assign(defaultConfig, params.config);

    self.btnText = ko.observable(config.optionsCaption);
    self.width = config.width;

    self.showClearBtn = ko.observable(false);
    self.value = filter.value;

    var $select = $(element).find("select"),
        $button = $(element).find("button");

    $select.width(config.width + "px");

    $dropdown = $(element).find(".dropdown-menu");

    $dropdown.click(function (e) {
        e.stopPropagation();
    });

    $select.select2({
        multiple: true,
        width: "style",
        ajax: {
            url: app.rootUrl + "api/" + config.dataSource.url,
            dataType: 'json',
            delay: config.delay,
            data: filter.ajaxData,
            processResults: function (ajaxResult) {
                // Записываем в лог информацию о загрузке фильтра
                var logEntry = {
                    time: moment().format("HH:mm:ss"),
                    filterName: config.title,
                    queryList: ajaxResult.queryList,
                    errorMessage: ajaxResult.error
                };
                group.filterLog.unshift(logEntry);

                if(ajaxResult.data){
                    return {
                        results: ajaxResult.data.results,
                        pagination: {
                            more: ajaxResult.data.more
                        }
                    }
                }
                return {
                    results: [],
                    pagination: {
                        more: false
                    }
                }
            }
        }
    });

    $dropdown.on('hide.bs.dropdown', function () {
        $select.select2("close");
    });

    $button.on('click', function() {
        if(!$(this.parentNode).hasClass('open')){
            setTimeout(function() {
                $select.select2("open");
            }, 100);
        }
    });

    self.value.subscribe(function(ids){
        if(ids && ids.length > 0){
            self.showClearBtn(true);

            if(ids.length === 1){
                var data = $select.select2("data");
                self.btnText(data[0].text);
            } else {
                self.btnText("Выбрано " + ids.length);
            }
        }
        else {
            self.btnText(config.optionsCaption);
            self.showClearBtn(false);
        }
    });

    self.clear = function () {
        $select.val([]);
        $select.trigger("change");
    };

    filter.clear = self.clear;
};