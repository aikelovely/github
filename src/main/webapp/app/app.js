(function (window, _) {
    /*
    Основной модуль приложения. Используется как namespace для всего остального, чтобы не засорять глобальную область
    видимости. Доступен как window.app.
    Основной метод - init, приминает конфигурацию витрины и на ее основе создает viewModel (Knockout.js)
     */
    var App = function () {
        var self = this;

        // Сами builders и components находятся в соответствующих папках
        self.builders = {};
        self.components = {};

        self.defaultGroupName = "default";


        /**
         * Парсит строку или объект и возвращает название группы и имя фильтра или таба.
         * Если имя группы не задано, считается что используется имя группы по-умолчанию (см. defaultGroupName)
         * Строка может быть следующего вида:
         *   "filterX" - функция вернет { name: "filterX", group: "default" }
         *   "groupY.filterX" - { name: "filterX", group: "groupY" }
         * Если параметр объект, то проверяются его свойства "name" (required) и "group".
         */
        self.parseNameAndGroup = function (nameAndGroup) {
            if (_.isString(nameAndGroup)) {
                var parts = nameAndGroup.split(".");

                if (parts.length === 0 || parts.length > 2) {
                    throw new Error("Invalid name: \"" + nameAndGroup + "\"");
                }
                if (parts.length === 2) {
                    return {
                        name: parts[1],
                        group: parts[0]
                    }
                }
                return {
                    name: parts[0],
                    group: self.defaultGroupName
                }
            }
            if (_.has(nameAndGroup, "name")) {
                var group = nameAndGroup.group || self.defaultGroupName;

                return {
                    name: nameAndGroup.name,
                    group: group
                }
            } else {
                console.error(nameAndGroup);
                throw new Error("Invalid filter/tabStrip config.")
            }
        };

        self.createDependencyName = function(config){
            var nameAndGroup = self.parseNameAndGroup(config);
            return nameAndGroup.group + "." + nameAndGroup.name;
        };

        /*
        Показывает модальное окно с сообщением. Принимает 2 параметра: message - текст сообщения и title - заголовок.
        title по-умолчанию - "Произошла ошибка"
        */
        var $alertModalDialog;
        self.showAlert = function (message, title) {
            if ($alertModalDialog === undefined) {
                $('body').append('\
                    <div id="alertModalDialog" class="modal fade">\
                        <div class="modal-dialog modal-lg">\
                            <div class="modal-content">\
                                <div class="modal-header">\
                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">\
                                    <span aria-hidden="true">&times;</span></button>\
                                    <h4 class="modal-title">Произошла ошибка</h4>\
                                </div>\
                                <div class="modal-body">\
                                    <p class="formatted-text"></p>\
                                </div>\
                                <div class="modal-footer">\
                                    <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>\
                                </div>\
                            </div>\
                        </div>\
                    </div>');

                $alertModalDialog = $("#alertModalDialog");
            }

            try {
                var title = title || "Произошла ошибка";
                $alertModalDialog.find('.modal-title').html(title);
                $alertModalDialog.find('.formatted-text').html(message);
                $alertModalDialog.modal();
            } catch (ex) {
                 console.error(message);
            }
        };

        /*
         Перенаправляет на новую витрину (открывается в той-же вкладке)
         */
        self.redirectToShowcase = function (showcaseName, dataToUpdate, forceShowCharts) {
            var key = self.cookieManager.showcaseCookieKey + showcaseName;

            var forceShowChartsValue = forceShowCharts || false;

            var extendedData = $.extend({}, dataToUpdate, {
                forceShowCharts: forceShowChartsValue
            });

            self.cookieManager.updateCookieData(key, extendedData);

            var url = window.location.href;
            var controllerUrl = url.substr(0, url.lastIndexOf("/"));

            window.location = controllerUrl + "/" + showcaseName;
        };

        /*
         Перенаправляет на новую витрину (открывается в другой вкладке)
         */
        self.openNewShowcase = function (showcaseName, dataToUpdate, forceShowCharts) {
            var key = self.cookieManager.showcaseCookieKey + showcaseName;

            var forceShowChartsValue = forceShowCharts || false;

            var extendedData = $.extend({}, dataToUpdate, {
                forceShowCharts: forceShowChartsValue
            });

            self.cookieManager.updateCookieData(key, extendedData);

            var url = window.location.href;
            var controllerUrl = url.substr(0, url.lastIndexOf("/"));

            window.open(controllerUrl + "/" + showcaseName);
        };

        /*
        Основная функция. Создает viewModel (см. DynamicViewModel) на основе config.
        */
        self.init = function (config, extendDynamicViewModel) {
            app.ajaxUtils.ping().done(function () {
                var viewModel = new window.app.DynamicViewModel(config);
                viewModel.init();

                if (extendDynamicViewModel) {
                    if (!_.isFunction(extendDynamicViewModel)) {
                        console.error("extendDynamicViewModel parameter should be function.");
                        return;
                    }
                    extendDynamicViewModel(viewModel);
                }

                self.viewModel = viewModel;

                ko.applyBindings(viewModel);
            });

            // Раз в минуту пингуем сервер чтобы не "умирала" сессия.
            setInterval(function () {
                app.ajaxUtils.ping();
            }, 60000);
        }
        self.getUrlVars = function () {
            var vars = {};
            var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
                function (m, key, value) {
                    vars[key] = value;
                });

            return vars;

        }
        self.getParamValue = function (map, paramName, defaultValue) {

            if (map[paramName]) {
                var arr = map[paramName].split(',');
                if (arr.length > 1) {
                    return arr;
                }
                return map[paramName];
            }
            return defaultValue;


        }

    };

    window.app = new App();

    $(function() {
        var $btn = $(".navbar-support-button");

        if ($btn[0]) {
            var content = $("#supportBtnMarkup").html();

            $btn.popover({
                html: true,
                placement: 'bottom',
                trigger: 'click',
                content: content
            }).text("Нужна помощь?");

            $btn.on("shown.bs.popover", function(){
                $(".btn-close").on("click", function(){
                    $btn.popover("hide");
                    return false;
                })
            })
        }
    });
})(window, _);