<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<script id='filterLogTemplate' type="text/html">
    <!-- ko if: $root.showSQL -->

    <button data-bind="click: toggleCollapse" class="btn btn-info btn-sm btn-filter-log">
        <i class="fa fa-eye"></i> SQL запросы фильтров
    </button>

    <div class="collapse filter-log">
        <table class="table table-fixed">
            <thead>
            <tr>
                <th>Время</th>
                <th>Фильтр</th>
                <th>SQL запрос</th>
                <th>Статус</th>
            </tr>
            </thead>
            <colgroup>
                <col width="100px" />
                <col width="220px" />
                <col width="600px" />
            </colgroup>
            <tbody data-bind="foreach: filterLog">
            <tr>
                <td data-bind="text: time"></td>
                <td data-bind="text: filterName"></td>
                <td data-bind="foreach: queryList">
                    <h4>
                        <!-- ko if: fromCache -->
                        <span class="label label-primary">Данные получены из кэша</span>
                        <!-- /ko -->
                        <!-- ko ifnot: fromCache -->
                        <span class="label label-default">Запрос к бд</span>
                        <!-- /ko -->
                    </h4>
                    <p data-bind="html: sql" class="formatted-text"></p>
                </td>
                <td>
                    <!-- ko if: errorMessage-->
                    <p class="label label-danger">Произошла ошибка</p>
                    <div style="margin-top: 10px;">
                        <button class="btn btn-default" data-bind="click: $component.showErrorMessage">
                            Показать текст ошибки
                        </button>
                    </div>
                    <!-- /ko -->
                    <!-- ko ifnot: errorMessage -->
                    <span class="label label-success">OK</span>
                    <!-- /ko -->
                </td>
            </tr>
            </tbody>
        </table>
    </div>

    <!-- /ko -->
</script>