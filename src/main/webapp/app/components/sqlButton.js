app.components.SqlButton = function (params, element) {
    var self = this;

    var queryList = params.queryList;
    self.buttonText = params.buttonText || "SQL";

    var separator = "\r\n",
        fromCacheLabel = '<h4><span class="label label-primary">Данные получены из кэша</span></h4>',
        fromDbLabel = '<h4><span class="label label-default">Данные получены из бд</span></h4>';

    function getHtml() {
        var queryHtml = [];

        _.forEach(queryList(), function (queryInfo) {
            var label = queryInfo.fromCache ? fromCacheLabel : fromDbLabel,
                html = label + separator + queryInfo.sql;
            queryHtml.push(html);
        });

        return queryHtml.join(separator + "<hr/>");
    }

    self.showSQLText = function () {
        app.showAlert(getHtml(), "SQL запросы");
    };
};

ko.components.register("sql-button", {});