_.notEmpty = function (value) {
    if (_.isArray) return !_.isEmpty(value);
    return !!value;
};

_.emptyOrContains = function(array, item){
    if (array === undefined || array === null || array.length === 0) return true;
    return _.indexOf(array, item) !== -1;
};

if (!String.prototype.format) {
    String.prototype.format = function() {
        var args = arguments;
        return this.replace(/{(\d+)}/g, function(match, number) {
            return typeof args[number] != 'undefined'
                    ? args[number]
                    : match;
        });
    };
}