// https://github.com/Knockout-Contrib/Knockout-Validation

ko.validation.init({
    insertMessages: false
});

ko.validation.localize({
    required: 'Необходимо заполнить это поле.',
    min: 'Значение должно быть больше или равно {0}.',
    max: 'Значение должно быть меньше или равно {0}.',
    minLength: 'Длина поля должна быть не меньше {0} символов.',
    maxLength: 'Длина поля должна быть не больше {0} символов.',
    pattern: 'Пожалуйста проверьте это поле.',
    step: 'Значение поле должно изменяться с шагом {0}',
    email: 'Введите в поле правильный адрес email',
    date: 'Пожалуйста введите правильную дату',
    dateISO: 'Пожалуйста введите правильную дату в формате ISO',
    number: 'Поле должно содержать число',
    digit: 'Поле должно содержать цифры',
    phoneUS: 'Поле должно содержать правильный номер телефона',
    equal: 'Значения должны быть равны',
    notEqual: 'Пожалуйста выберите другое значение.',
    unique: 'Значение должно быть уникальным.'
});

ko.bindingHandlers.validationContainer = {
    init: function (element, valueAccessor) {
        var options = valueAccessor(),
            $element = $(element);

        var forceDisplayErrors = options.forceDisplayErrors;

        var config = options.config;
        var data = options.data;

        if (config.validationRules) {
            var showError = function () {
                if (data.error() !== null) {
                    $element.find(".filter").qtip({
                        content: {
                            text: function() {
                                return data.error();
                            }
                        },
                        position: {
                            my: 'bottom center',
                            at: 'top center',
                            viewport: $(window)
                        },
                        style: {
                            classes: 'qtip-red',
                            tip: {
                                corner: true
                            }
                        },
                        show: {
                            effect: false
                        },
                        hide: {
                            effect: false
                        }
                    });

                    $element.addClass("validation-error");
                }
            };

            var updateStyle = function (valid) {
                if (!valid && (data.isModified() || forceDisplayErrors())) {
                    showError();
                } else {
                    $element.find(".filter").qtip('destroy', true);
                    $element.removeClass("validation-error");
                }
            };

            _.forEach(config.validationRules, function (rule) {
                switch (rule.type) {
                    case "Date":
                        if (rule.message) {
                            data.extend({date: {message: rule.message}});
                        } else {
                            data.extend({date: true});
                        }

                        break;
                    case "Required":
                        if (rule.message) {
                            data.extend({required: {message: rule.message}});
                        } else {
                            data.extend({required: true});
                        }
                        break;
                    case "Custom":
                        data.extend({
                            validation: {
                                validator: rule.validator,
                                message: rule.message
                            }
                        });
                        break;
                    default:
                        throw new Error("Unknown Validation Rule type: \"" + rule.type + "\"");
                }
            });

            data.isValid.subscribe(updateStyle);
            forceDisplayErrors.subscribe(showError);

        }
    }
};