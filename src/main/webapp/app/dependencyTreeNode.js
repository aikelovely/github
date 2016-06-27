/**
 * Узел дерева зависимостей.
 * @param nodeName название узла
 * @param initFunc функция, выполняющая инициализацию узла
 * @param dependencies список зависимотей (массив названий других узлов, не обязательный параметр
 * @param postInitFunc функция, выполняющая пост-обработку, не обязательный параметр
 * @constructor
 */
app.DependencyTreeNode = function (nodeName, initFunc, dependencies, postInitFunc) {
    var self = this;

    self.name = nodeName;
    self.initFunc = initFunc;
    self.dependencies = dependencies || [];
    self.postInitFunc = postInitFunc;
};