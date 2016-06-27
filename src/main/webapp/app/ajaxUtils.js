(function (app, $) {
    // Отключаем cache для GET запросов
    $.ajaxSetup({ cache: false });

    /*
     * Wrapper поверх $.ajax. Все методы возвращают $.Deferred.
    */
    var AjaxUtils = function () {
        var self = this;

        /**
         * Загрузка данных для фильтров.
         * @param url - url вида showcaseName/filterName
         * @param options - параметры
         * @param observable
         * @returns {$.Deferred} JSON (см. FrontApiController.Result)
         */
        self.getDataForFilter = function (url, options, observable) {
            var ajax = $.ajax({
                type: "GET",
                url: app.rootUrl + "api/" + url,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                async: true,
                data: options
            });

            ajax.fail(function (ajaxResult) {
                var error;
                try {
                    error = ajaxResult.responseText;
                } catch (ex) {
                    error = ajaxResult;
                }

                console.error(error);
            });

            ajax.done(function (ajaxResult) {
                if (ajaxResult.success) {
                    observable(ajaxResult.data);
                }
                else {
                    console.error("Url: \"" + url + "\". Error message: ", ajaxResult.error);
                }
            });

            return ajax;
        };

        /**
         * Отправляет данные на сервер GET запросом и получает JSON
         * @param url
         * @param data
         * @returns {$.Deferred} JSON (см. FrontApiController.Result)
         */
        self.getData = function (url, data) {
            return $.ajax({
                type: "GET",
                url: app.rootUrl + "api/" + url,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: data
            });
        };

        /**
         * Отправляет данные на сервер POST запросом и получает JSON
         * @param url
         * @param data
         * @returns {$.Deferred} JSON (см. FrontApiController.Result)
         */
        self.postData = function (url, data) {
            return $.ajax({
                type: "POST",
                url: app.rootUrl + "api/" + url,
                dataType: 'json',
                contentType: "application/json; charset=utf-8",
                data: data
            });
        };

        /**
         * Выполняет ajax запрос, для работы авторизации
         */
        self.ping = function(){
            return $.ajax({
                type: "GET",
                url: app.rootUrl + "api/ping",
                dataType: 'json',
                contentType: "application/json; charset=utf-8"
            });
        };

        /**
         * Очищает кэш MyBatis.
         * @returns {$.Deferred} true, если кэш был очищен, false - если произошла ошибка.
         */
        self.clearDatabaseCache = function(){
            return $.ajax({
                type: "POST",
                url: app.rootUrl + "admin/clearCaches",
                dataType: 'json',
                contentType: "application/json; charset=utf-8"
            });
        }
    };

    app.ajaxUtils = new AjaxUtils();
})(app, $);
