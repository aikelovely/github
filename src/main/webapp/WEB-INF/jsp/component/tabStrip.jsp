<script id='tabStripTemplate' type="text/html">
    <div data-bind="if: visible" class="chart-tabs">
        <!-- ko if: header -->
        <p data-bind="text: header" class="table-header"></p>
        <!-- /ko -->

        <ul class="nav nav-tabs" role="tablist">
            <!-- ko foreach: tabs -->
            <!-- ko if: visible -->
            <li data-bind="css: {'active': active}">
                <a data-bind="click: $parent.activateTab, text: title" href="javascript:;"></a>
            </li>
            <!-- /ko -->
            <!-- /ko -->

            <!-- ko if: reportUrl -->
            <li class="pull-right">
                <report-button params="group: group, reportUrl: reportUrl"></report-button>
            </li>
            <!-- /ko -->

            <!-- ko if: csvReportUrl -->
            <li class="pull-right">
                <csv-report-button params="group: group, reportUrl: csvReportUrl"></csv-report-button>
            </li>
            <!-- /ko -->
        </ul>
        <div data-bind="foreach: tabs" class="tab-content">
            <!-- ko if: visible -->
            <div data-bind="tabHtml: content, css: {'active': active, 'in': fadeIn}" class="tab-pane fade">
            </div>
            <!-- /ko -->
        </div>
    </div>

</script>