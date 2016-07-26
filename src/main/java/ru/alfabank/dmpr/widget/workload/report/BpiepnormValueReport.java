package ru.alfabank.dmpr.widget.workload.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.workload.BpiepnormValue;
import ru.alfabank.dmpr.model.workload.WorkloadOptions;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseReport;

/**
 * Created by U_M0NCT on 26.07.2016.
 */

@Service
public class BpiepnormValueReport extends BaseReport <WorkloadOptions> {
@Autowired
    WorkloadRepository rep;
    public BpiepnormValueReport( ) {
        super(WorkloadOptions.class);
    }

    @Override
    protected String getReportName(WorkloadOptions options) {
        return "BpiepnormValue";
    }

    @Override
    protected void configure(ReportBuilder builder, WorkloadOptions options) {
        BpiepnormValue[] data2= rep.getBpiepnormValue();
        builder.addWorksheet(BpiepnormValue.class)
        .bindTo(data2)
         .columns(new ColumnFactoryWrapper() {
             @Override
             public void createColumns(ColumnFactory c) {
                 c.add("BpinnerendProductCcode").title("BpinnerendProductCcode");
                 c.add("BpinnerendProductName").title("BpinnerendProductName");
                 c.add("BporgRegionCcode").title("BporgRegionCcode");
                 c.add("BporgRegionName").title("BporgRegionName");
                 c.add("BpDivisionGroupCcode").title("BpDivisionGroupCcode");
                 c.add("BpDivisionGroupName").title("BpDivisionGroupName");
                 c.add("NormativeMinutes").title("NormativeMinutes");
                 c.add("EffectifFrom").title("EffectifFrom");
                 c.add("EffectifTo").title("EffectifTo");
             }
         });

    }
}
