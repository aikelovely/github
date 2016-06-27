(function (app) {
    //Turn on automatic storage of JSON objects passed as the cookie value
    $.cookie.json = true;

    /**
     * Отвечает за работу с cookies.
     */
    var CookieManager = function () {
        var self = this;

        self.showcaseCookieKey = "filterData/showcase/";

        var updateCookieData = function (cookieKey, dataToUpdate) {
            $.cookie(cookieKey, dataToUpdate, {expires: 7, path: "/"});
        };

        self.updateCookieData = function(cookieKey, dataToUpdate) {
            return updateCookieData(cookieKey, dataToUpdate);
        };

        /**
         * Читает/записывает состояние фильтров в cookies
         * @param dataToUpdate есди параметр не задан, то возвращает данные из cookie, в противном случае записывает
         * @returns {cookieData}
         */
        self.filterData = function (dataToUpdate) {
            var key = "filterData" + window.location.pathname;

            // write
            if (dataToUpdate !== undefined) {
                updateCookieData(key, dataToUpdate);
                return undefined;
            }
            // read
            return $.cookie(key) || {};
        };

        /**
         * Записывает/читает флаг, который влияет на отображение отладочной информации о SQL запросах
         * @param dataToUpdate
         * @returns {*} true or false
         */
        self.sqlVisibility = function(dataToUpdate){
            var key = "showSQL";

            // write
            if (dataToUpdate !== undefined) {
                updateCookieData(key, dataToUpdate);
                return;
            }
            // read
            return $.cookie(key);
        }
    };

    app.cookieManager = new CookieManager();
})(app);