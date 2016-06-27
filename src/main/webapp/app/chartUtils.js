(function (app, $, _, Highcharts) {
    /**
     * Настройка графиков: параметры по-умолчанию, докализация и т.д.
     */

    var isOldIE = $('html').is(".old-ie");

    Highcharts.setOptions({
        credits: {enabled: false},
        lang: {
            months: ['Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь', 'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'],
            weekdays: ['Воскресенье', 'Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница', 'Суббота'],
            shortMonths: ['Янв', 'Фев', 'Мар', 'Апр', 'Май', 'Июн', 'Июл', 'Авг', 'Сен', 'Окт', 'Ноя', 'Дек'],
            resetZoom: 'Показать всё',
            resetZoomTitle: 'Сбросить увеличение',
            excelButtonTitle: 'Выгрузка в Excel',
            rangeSelectorZoom: 'Увеличение',
            rangeSelectorFrom: 'с',
            rangeSelectorTo: 'по',
            // Убираем разделитель (Decimal mark)
            thousandsSep: ' ',
            numericSymbols: [],
            noData: "Нет данных",
            printChart: "Распечатать",
            downloadPNG: "Загрузить изображение",
            downloadSVG: "Загрузить векторное изображение"
        },
        tooltip: {
            dateTimeLabelFormats: {
                millisecond: '%A, %b %e, %H:%M:%S.%L',
                second: '%A, %b %e, %H:%M:%S',
                minute: '%A, %b %e, %H:%M',
                hour: '%A, %b %e, %H:%M',
                day: '%A, %b %e, %Y',
                week: 'Неделя с %A, %b %e, %Y',
                month: '%B %Y',
                year: '%Y'
            }
        },
        noData: {
            style: {
                fontWeight: 'normal',
                fontSize: '14px'
            }
        },
        navigation: {
            menuItemStyle: {
                fontSize: '14px',
                padding: '5px 10px'
            }
        },
        exporting: {
            enabled: !isOldIE,
            fallbackToExportServer: false,
            scale: 1
        },
        colors: [
            "#7cb5ec",
            "#91e8e1",
            "#90ed7d",
            "#f7a35c",
            "#8085e9",
            "#f15c80",
            "#e4d354",
            "#5be8e0",
            "#8d4653",
            "#434348",
            "#4B966E",
            "#D46A6A"
        ]
    });

    /**
     * Возвращзает номер недели по производственнному календарю. Если четверг относится к прошлому году, то и неделя
     * считается последней за прошлый год.
     * @returns {number}
     */
    Date.prototype.getWeek = function () {
        var target  = new Date(this.valueOf());
        var dayNr   = (this.getDay() + 6) % 7;
        target.setDate(target.getDate() - dayNr + 3);
        var firstThursday = target.valueOf();
        target.setMonth(0, 1);
        if (target.getDay() != 4) {
            target.setMonth(0, 1 + ((4 - target.getDay()) + 7) % 7);
        }
        return 1 + Math.ceil((firstThursday - target) / 604800000);
    };


    // Поддержка отображения детализации (TimeUnit) по Кварталам
    Highcharts.dateFormats = {
        Q: function (timestamp) {
            var date = new Date(timestamp),
                year = date.getFullYear(),
                month = date.getMonth();

            var quarterNumber = Math.round((month - 1) / 3) + 1;
            return "Q" + quarterNumber + " '" + year.toString().substr(2, 2);
        }
    };

    kendo.culture("ru-RU");
    moment.locale("ru");

    /**
     * Преобразует дату в локальное время, добавляя offset.
     * @param date - дата в UTC формате
     * @returns {moment} - дата в локальном времени
     */
    function toLocalDate(date) {
        var offset = moment(date).clone().utcOffset();
        return moment(date).add(offset, "m");
    }

    var TimeUnit = {
        1: "Hour",
        2: "Day",
        3: "Week",
        4: "Month",
        5: "Quarter",
        6: "Year",
        7: "All"
    };

    function getTimeUnitById(id) {
        return TimeUnit[id] || "None";
    }

    /**
     * Формирует список ticks для HighCharts xAxis
     * @param startDate период, с
     * @param endDate период, по
     * @param timeUnit TimeUnit
     * @returns {Array}
     */
    function getTicks(startDate, endDate, timeUnit) {
        var ticks = [],
            from = startDate.clone(), // обнуляем время, т.к. возможна проблема с timezone
            to = endDate.clone(),
            delta;

        // Вычисляем delta для соответвующего timeUnit
        switch (timeUnit) {
            case "Hour":
                to.add(1, "d");
                delta = {
                    value: 1,
                    unit: "h"
                };
                break;

            case "Day":
                delta = {
                    value: 1,
                    unit: "d"
                };
                break;

            case "Week":
                from.startOf('week');
                delta = {
                    value: 7,
                    unit: "d"
                };
                break;

            case "Month":
                from = from.startOf('month');
                to.startOf('month').add(1, "M").add(-1, "d");
                delta = {
                    value: 1,
                    unit: "M"
                };
                break;

            case "Quarter":
                from.startOf('quarter');
                to.startOf('quarter');
                delta = {
                    value: 3,
                    unit: "M"
                };
                break;

            case "Year":
                from.startOf('year');
                to.startOf('year');
                delta = {
                    value: 1,
                    unit: "y"
                };
                break;

            case "All":
                // "магическая" константа
                return [moment("2001-01-01")];
        }

        for (var dt = from.clone(); dt <= to; dt.add(delta.value, delta.unit)) {
            ticks.push(dt.clone());
        }

        return ticks;
    }

    /**
     * Создает конфигурацию колонок для Kendo UI Grid. Используется в том случае, если колонки представляют собой
     * набор дат.
     * @param options параметры фильтров
     * @param fieldName имя поля строки таблицы (объекта, который передается в dataSource.data) для отображения в ячейке
     * @param cellFormatter функция форматирования значения ячейки таблицы
     *  Получает на вход значение строки по ключу fieldName.
     *  Не обязательный параметр.
     * @param customColumnConfig конфиг, который расширяет стандартные параметры колонки. Не обязательный параметр
     * @returns {Array} Список колонок
     */
    function createDateTimeColumns(options, fieldName, cellFormatter, customColumnConfig) {
        var startDate = moment(options.startDate),
            endDate = moment(options.endDate),
            timeUnit = getTimeUnitById(options.timeUnitId),
            ticks = getTicks(startDate, endDate, timeUnit),
            columns = [],
            width = 95,
            titleFormatter;

        switch (timeUnit) {
            case "Hour":
                titleFormatter = function (value) {
                    return value.format("HH:mm");
                };
                break;

            case "Day":
                titleFormatter = function (value) {
                    return value.format("DD.MM.YY");
                };
                break;

            case "Week":
                width = 160;
                titleFormatter = function (value) {
                    return value.format("DD.MM.YY") + " - " +
                        value.clone().add(6, "d").format("DD.MM.YY");
                };
                break;

            case "Month":
                titleFormatter = function (value) {
                    return _.capitalize(value.format("MMMM[<br/>] YYYY"));
                };
                break;

            case "Quarter":
                titleFormatter = function (value) {
                    return "Q" + value.format("Q 'YY");
                };
                break;

            case "Year":
                titleFormatter = function (value) {
                    return value.format("YYYY");
                };
                break;

            case "All":
                width = 180;
                titleFormatter = function (value) {
                    return startDate.format("DD.MM.YYYY") + " - " +
                        endDate.format("DD.MM.YYYY");
                };
                break;
        }

        _.forEach(ticks, function (t) {
            var columnName = "D" + t.format("YYYYMMDDHH"),
                title = titleFormatter(t),
                field = fieldName ? columnName + "." + fieldName : columnName;

            var columnConfig = {
                field: field,
                title: title,
                width: width
            };

            if (cellFormatter) {
                columnConfig.template = function (dataItem) {
                    return cellFormatter(dataItem[columnName]);
                }
            }

            if (fieldName) {
                // define custom compare function
                columnConfig.sortable = {
                    compare: function (a, b) {
                        if (a[columnName] == null) return -1;
                        if (b[columnName] == null) return 1;

                        return a[columnName][fieldName] - b[columnName][fieldName];
                    }
                }
            }

            columns.push(_.assign(columnConfig, customColumnConfig));
        });

        return columns;
    }

    /**
     * Представляет собой конфигурацию XAxis для HighCharts.
     * @param options параметры фильтров
     * @param useMassStyleFormatter фдаг, указывающий на то, что нужно использовать форматированние дат "в стиле МАСС"
     * @constructor возвращает объект, который можно использовать при создании HighCharts
     */
    var DateTimeXAxis = function (options, useMassStyleFormatter) {
        var self = this;

        var startDate = moment(options.startDate),
            endDate = moment(options.endDate),
            timeUnit = getTimeUnitById(options.timeUnitId);

        function init() {
            if (timeUnit === "None" || timeUnit === "All") {
                self.labels = {
                    formatter: function () {
                        return "{0} - {1}".format(startDate.format("DD.MM.YYYY"), endDate.format("DD.MM.YYYY"));
                    }
                };
                // TODO: Расчитывать self.min & self.max чтобы единственный столбик не был слишком толстым
                self.ticks = [];
                return;
            }

            self.type = "datetime";
            fillTicks();


            var unitName = timeUnit.toLowerCase();

            if (timeUnit == "Quarter") {
                // Используем уже существующий unitName, чтобы не создавать новый. В данном случае month.
                unitName = "month";
                // Реализация формата см. выше
                self.dateTimeLabelFormats = {month: '%Q'}
            }

            //self.tickPositioner = function () {
            //    var ticks = self.ticks;
            //
            //    ticks.info = {
            //        unitName: unitName,
            //        higherRanks: {}
            //    };
            //
            //    return ticks;
            //};

            fillMinMaxAndXDateFormat();

            // Если нужно, задаем собственный label formatter
            if (useMassStyleFormatter) {
                self.labels = {
                    formatter: massStyleFormatter,
                    useHTML: true
                };
            }
        }

        /**
         * Заполняет поле ticks.
         */
        function fillTicks() {
            var ticks = getTicks(startDate, endDate, timeUnit);
            self.ticks = [];

            _.forEach(ticks, function (t) {
                self.ticks.push(toLocalDate(t).valueOf());
            });
        }

        /*
         Заполняет поля min, max и xDateFormat
         */
        function fillMinMaxAndXDateFormat() {
            var min = startDate.clone(),
                max = endDate.clone(),
                xDateFormat,
                tickInterval;

            var singleTick = self.ticks.length === 1;

            switch (timeUnit) {
                case "Hour":
                    xDateFormat = "%A, %b %e, %H:%M";
                    tickInterval = 3600 * 1000;
                    break;

                case "Day":
                    max.add(12, 'h');
                    xDateFormat = "%A, %b %e, %Y";
                    tickInterval = 24 * 3600 * 1000;
                    break;

                case "Week":
                    min.startOf('week').add(-3, "d");
                    max.startOf('week').add(3, "d");

                    xDateFormat = "Неделя с %A, %b %e, %Y";
                    tickInterval = 7 * 24 * 3600 * 1000;
                    break;

                case "Month":
                    if (singleTick) {
                        min.startOf('month').add(-5, "d");
                        max.startOf('month').add(5, "d");
                    } else {
                        min.startOf('month').add(-10, "d");
                        max.startOf('month').add(10, "d");
                    }

                    xDateFormat = "%B %Y";
                    tickInterval = 30 * 24 * 3600 * 1000;
                    break;

                case "Quarter":
                    min.startOf('quarter').add(-1, "M");
                    max.startOf('quarter').add(1, "M");
                    xDateFormat = "Квартал с %A, %b %e, %Y";
                    tickInterval = 3 * 30 * 24 * 3600 * 1000;
                    break;

                case "Year":
                    min.startOf('year').add(-1, "y");
                    max.startOf('year').add(1, "y");
                    xDateFormat = "%Y";
                    tickInterval = 12 * 30 * 24 * 3600 * 1000;
                    break;
            }

            self.min = min.valueOf();
            self.max = max.valueOf();
            self.xDateFormat = xDateFormat;
            self.tickInterval = tickInterval;
        }

        /**
         * Форматирование дат "в стиле МАСС". Отличие от стандартного форматирования только для timeUnit "Week".
         * Логика следующая: для каждого tick отображаем начало недели и конец недели. Для первой недели начало не
         * может быть ранее фильтра "Период, с", а для последней - окончание не позднее фильтра "Период, по"
         */
        function massStyleFormatter() {
            var obj = this,
                format = obj.dateTimeLabelFormat;

            if (timeUnit === "All") {
                return "{0} - {1}".format(startDate.format("DD.MM.YYYY"), endDate.format("DD.MM.YYYY"));
            }

            if (timeUnit === "Week") {
                var from, to;

                if (obj.isFirst) { // первый tick
                    from = startDate.clone();
                    to = startDate.clone().endOf("week");
                }
                else if (obj.isLast) { // последний tick
                    from = endDate.clone().startOf('week');
                    to = endDate.clone();
                }
                else {
                    from = moment(obj.value);
                    to = from.clone().add(6, "d");
                }

                return '<span title="{0} - {1}">{0} -<br/>{1}</span>'.format(
                    Highcharts.dateFormat(format, toLocalDate(from).toDate()),
                    Highcharts.dateFormat(format, toLocalDate(to).toDate()));
            }

            // default
            return Highcharts.dateFormat(format, obj.value);
        }

        init();
    };

    /**
     * Singleton, для удобства
     * @constructor
     */
    var ChartUtils = function () {
        var self = this;

        /**
         * Создает XDateTimeAxis для HighCharts
         * @param options параметры фильтров
         * @param useMassStyleFormatter true or false
         * @returns {DateTimeXAxis}
         */
        self.createDateTimeXAxis = function (options, useMassStyleFormatter) {
            return new DateTimeXAxis(options, useMassStyleFormatter);
        };

        /**
         * Создает конфигурацию колонок для Kendo UI Grid. Используется в том случае, если колонки представляют собой
         * набор дат.
         */
        self.createDateTimeColumns = createDateTimeColumns;

        /**
         * Возвращает дату в винительном падеже.
         * @param date
         * @returns {*}
         */
        self.getMonthAccusativeName = function (date) {
            return moment(date).format("D MMMM").split(" ")[1];
        };

        self.getWeekCategories = function (points) {
            var categories = [];
            _.forEach(points, function (p) {
                var weekOfYear = moment(p).week();
                if (weekOfYear <= 2) {
                    categories.push("1+2/" + moment(p).format("YYYY"));
                    return;
                }
                categories.push(moment(p).format("w/YYYY"));
            });
            return categories;
        };

        self.getWeekCategoriesByPeriodNum = function (points) {
            var categories = [];
            var index = 0;
            _.forEach(points, function (p) {
                var weekOfYear = p.periodNum;
                var title = "{0}/{1}".format(weekOfYear, moment(p.x).format("YYYY"));
                p.x = index++; // перезаписываем x чтобы данные HighChart корректно отрисовал xAxis
                categories.push(title);
            });
            return categories;
        };

        self.getMonthCategories = function (points) {
            var categories = [];
            _.forEach(points, function (p) {
                categories.push(_.capitalize(moment(p).format("MMMM")));
            });
            return categories;
        };

        self.getQuarterCategories = function (points) {
            var categories = [];
            _.forEach(points, function (p) {
                categories.push(_.capitalize(moment(p).format("[Q]Q 'YY")));
            });
            return categories;
        };

        self.daysToDDHH = function(valueInDays){
            var days = Math.floor(valueInDays),
                hours = Math.floor((valueInDays - days) * 24);

            return (days > 0 ? days + "д " : "") + Math.floor(hours) + "ч";
        };


        self.hoursToDDHH = function(value) {
            var days = Math.floor(value / 24);
            var hours = value - days * 24;
            return (days > 0 ? days + "д " : "") + Math.floor(hours) + "ч";
        };

        self.hoursToHHMM = function(value){
            var hours = Math.floor(value),
                minutes = Math.floor((value - hours) * 60);

            return (hours > 0 ? hours + "ч " : "") + Math.floor(minutes) + "мин";
        };

        self.getPeriodFromTimestamp = function (timeStamp, timeUnitId, minStartDate, maxEndDate) {
            var startDate = moment(timeStamp).startOf("day"),
                endDate = startDate.clone();

            switch (parseInt(timeUnitId)) {
                case 3:
                    endDate.add(6, "days");
                    break;
                case 4:
                    endDate.add(1, 'months').add(-1, "days");
                    break;
                case 5:
                    var month = endDate.month();
                    var quarterNumber = Math.round((month - 1) / 3) + 1;
                    endDate = moment({year: endDate.year(), month: quarterNumber * 3, day: 1});
                    endDate.add(-1, "days");
                    break;
            }

            if (startDate < minStartDate) {
                startDate = minStartDate;
            }
            if (endDate > maxEndDate) {
                endDate = maxEndDate;
            }

            return {
                startDate: startDate['toDate'] ? startDate.toDate() : startDate,
                endDate: endDate['toDate'] ? endDate.toDate() : endDate
            };
        };

        self.getPeriodText = function (sD, eD) {
            var startDate = moment(sD),
                endDate = moment(eD);

            return "c {0} {1} по {2}".format(
                app.chartUtils.getMonthAccusativeName(startDate),
                startDate.format("YYYY"),
                endDate.format("MMMM YYYY"));
        };

        self.getRealWeekNoByProductionNo = function(year, productionWeekNo) {
            var firstDayOfYear = moment(year).startOf('year'),
                weekDayYearStartsOn = firstDayOfYear.isoWeekday(), firstProductionWeekMonday;
            if (weekDayYearStartsOn > 4) {
                firstProductionWeekMonday = firstDayOfYear.add(8 - weekDayYearStartsOn,'days');
            } else {
                firstProductionWeekMonday = firstDayOfYear.startOf('week');
            }
            return firstProductionWeekMonday.add(7 * (productionWeekNo - 1), 'days').week();
        };
    };

    app.chartUtils = new ChartUtils();
})(app, $, _, Highcharts);