/**
 * Расширение для ko.observable. Позволяет выполнять обновление значения ko.observable без информирования ее
 * подписчиков. Пример:
 *
 * var myObs = ko.observable().withPausing();
 * myObs("newValue"); // информирует подписчиков
 *
 * myObs.silentUpdateStart();
 * myObs("newValue2"); // не информирует подписчиков
 * myObs.silentUpdateStop();
 */
ko.observable.fn.withPausing = function () {
    this.notifySubscribers = function () {
        if (!this.pauseNotifications) {
            ko.subscribable.fn.notifySubscribers.apply(this, arguments);
        }
    };

    this.silentUpdateStart = function (newValue) {
        this.pauseNotifications = true;
    };

    this.silentUpdateStop = function () {
        this.pauseNotifications = false;
    };

    return this;
};