/**
 * DataSource parameters. Парсит и обрабатывает параметры dataSource.
 * @param viewModel
 * @param params массив, список параметров. Каждый параметр может быть задан как виде строки
 * ("filterX" или "groupY.filterX"), так и в виде объекта:
 * {
 *   name: "filterX",
 *   group: "groupY",
 *   required: true // если true, считаем что этот параметр обязательный - если фильтр filterX не задан, то ajax запрос
 *                  // не будет выполняться
 * }
 *
 */
app.DSParams = function (viewModel, params) {
    var self = this;

    var requestOptions = {},
        requiredObservables = [],
        dateTimeObservables = [];

    function initialize() {
        if (params) {

            if(!_.isArray(params)){
                console.error("Invalid params:", params);
                throw new Error("dataSource.params should be an array of strings.")
            }

            _.forEach(params, function (param) {
                var filter = viewModel.getFilter(param);

                requestOptions[filter.name] = filter.value;

                // Если параметр задан как строка, то считаем что он необязательный
                if(_.isPlainObject(param)) {
                    if(param.required){
                        requiredObservables.push(param);
                    }
                }

                var parsedNameAndGroup = app.parseNameAndGroup(param);
                var config = viewModel.getGroup(parsedNameAndGroup.group).config.filters[parsedNameAndGroup.name];
                if (config === undefined) {
                    throw new Error("Undefined parameter value. Filter with name \"" + parsedNameAndGroup.name
                        + "\" and group \"" + parsedNameAndGroup.group + "\"  not found.")
                }
                if (config.type === "DatePicker") { // TODO: Добавить в условие MonthPicker & YearPicker, если потребуется
                    dateTimeObservables.push(config.name);
                }
            });
        }
    }

    /**
     * Возвращает список параметров для ajax запроса. Если параметр типа Date, то преобразовывает его в строку
     * формата ISO.
     */
    self.getRequestOptions = function () {
        var optionsAsJs = ko.toJS(requestOptions);

        _.forEach(dateTimeObservables, function (p) {
            if (optionsAsJs[p]) {
                optionsAsJs[p] = moment(optionsAsJs[p]).format("YYYY-MM-DD");
            }
        });

        return optionsAsJs;
    };

    /**
     * Проверка на то, что все required параметры заданы
     * @returns {boolean} true или false
     */
    self.testRequiredObservables = function () {
        for (var i = 0, j = requiredObservables.length; i < j; i++) {
            var param = requiredObservables[i];
            var paramValue = viewModel.getFilterValue(param);

            if (paramValue === undefined || paramValue === null) return false;
            if (paramValue.length !== undefined && paramValue.length === 0) return false;
        }

        return true;
    };

    /**
     * Проверка на то что есть хоть один required параметр
     * @returns {boolean} true или false
     */
    self.anyRequiredObservables = function () {
        return requiredObservables.length > 0;
    };

    initialize();
};

