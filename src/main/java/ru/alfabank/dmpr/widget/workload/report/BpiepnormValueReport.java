package ru.alfabank.dmpr.widget.workload.report;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.BaseOptions;
import ru.alfabank.dmpr.model.workload.*;
import ru.alfabank.dmpr.repository.workload.WorkloadRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import static org.joda.time.LocalDateTime.*;

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
        return "Детальный слой "+ now();
    }

    @Override
    protected void configure(ReportBuilder builder, WorkloadOptions options) {
        BpiepnormValue[] data2= rep.getBpiepnormValue();
        BpworkWeekReport[] data3=rep.getBpworkWeek();
        ConvertCountReport[] data4=rep.getConvertCount();
        WorkloadReport[] data5=rep.getWorkload();


        builder.addWorksheet(BpiepnormValue.class)
        .bindTo(data2)
                .title("BpiepnormValue")
         .columns(new ColumnFactoryWrapper() {
             @Override
             public void createColumns(ColumnFactory c) {
                 c.add("BpinnerendProductCcode").title("BpinnerendProductCcode").width(20);

                 c.add("BpinnerendProductName").title("BpinnerendProductName").width(20);
                 c.add("BporgRegionCcode").title("BporgRegionCcode").width(20);
                 c.add("BporgRegionName").title("BporgRegionName").width(20);
                 c.add("BpDivisionGroupCcode").title("BpDivisionGroupCcode").width(20);
                 c.add("BpDivisionGroupName").title("BpDivisionGroupName").width(20);
                 c.add("NormativeMinutes").title("NormativeMinutes").width(20);
                 c.add("EffectifFrom").title("EffectifFrom").format("dd.MM.yyyy");
                 c.add("EffectifTo").title("EffectifTo").format("dd.MM.yyyy");
                 c.add("DateNow").title("Дата выгрузки").format("dd.MM.yyyy HH:mm:ss");
             }
         }
         );

        builder.addWorksheet(BpworkWeekReport.class)
                .bindTo(data3)
                .title("BpworkWeek")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("Ccode").title("Ccode").width(20);
                                 c.add("Name").title("Name").width(20);
                                 c.add("NumberOfWeek").title("NumberOfWeek").width(20);
                                 c.add("YearWeek").title("YearWeek").width(20);
                                 c.add("ValueDay").title("ValueDay").format("dd.MM.yyyy").width(20);
                                 c.add("WorkminCnt").title("WorkminCnt").width(20);
                                 c.add("DateNow").title("Дата выгрузки").format("dd.MM.yyyy HH:mm:ss").width(20);
                             }
                         }
                );

        builder.addWorksheet(ConvertCountReport.class)
                .bindTo(data4)
                .title("ConvertCountReport")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("ValueDay").title("ValueDay").format("dd.MM.yyyy").width(20);
                                 c.add("BpDivisionGroupCcode").title("BpDivisionGroupCcode").width(20);
                                 c.add("BpinnerendProductName").title("BpinnerendProductName").width(20);
                                 c.add("BpinnerendProductCcode").title("BpinnerendProductCcode").width(20);
                                 c.add("NormativeMinutes").title("NormativeMinutes").width(20);
                                 c.add("TotalbpiepquantityCnt").title("TotalbpiepquantityCnt").width(20);
                                 c.add("TotallaborvalueCnt").title("TotallaborvalueCnt").width(20);
                                 c.add("Worktime4staffunitCnt").title("Worktime4staffunitCnt").width(20);
                                 c.add("EstimatedstaffunitCnt").title("EstimatedstaffunitCnt").width(20);
                                 c.add("DateNow").title("Дата выгрузки").format("dd.MM.yyyy HH:mm:ss").width(20);
                             }
                         }
                );

        builder.addWorksheet(WorkloadReport.class)
                .bindTo(data5)
                .title("Workload")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("ValueDay").title("ValueDay").format("dd.MM.yyyy");
                                 c.add("BpdivisiongroupNameLvl1").title("BpdivisiongroupNameLvl1").width(20);
                                 c.add("BpdivisiongroupNameLvl2").title("BpdivisiongroupNameLvl2").width(20);
                                 c.add("BpdivisiongroupNameLvl3").title("BpdivisiongroupNameLvl3").width(20);
                                 c.add("BpDivisionGroupCcode").title("BpDivisionGroupCcode").width(20);
                                 c.add("BporgRegionCcodeLvl2").title("BporgRegionCcodeLvl2").width(20);
                                 c.add("BporgRegionCcodeLvl3").title("BporgRegionCcodeLvl3").width(20);
                                 c.add("TotalfactvalueCnt").title("TotalfactvalueCnt").width(20);
                                 c.add("SlafactvalueCnt").title("SlafactvalueCnt").width(20);
                                 c.add("SlafactvaluedoubleCnt").title("SlafactvaluedoubleCnt").width(20);
                                 c.add("SlafactvacancytempCnt").title("SlafactvacancytempCnt").width(20);
                                 c.add("NoslafactvalueCnt").title("NoslafactvalueCnt").width(20);
                                 c.add("NoslafactvaluedoubleCnt").title("NoslafactvaluedoubleCnt").width(20);
                                 c.add("NoslafactvacancyCnt").title("NoslafactvacancyCnt").width(20);
                                 c.add("EstimatedstaffunitW8Cnt").title("EstimatedstaffunitW8Cnt").width(20);
                                 c.add("Quartile1W8Cnt").title("Quartile1W8Cnt").width(20);
                                 c.add("Quartile3W8Cnt").title("Quartile3W8Cnt").width(20);
                                 c.add("LowlimitW8Cnt").title("LowlimitW8Cnt").width(20);
                                 c.add("HighlimitW8Cnt").title("HighlimitW8Cnt").width(20);
                                 c.add("CriteriaW8Cnt").title("CriteriaW8Cnt").width(20);
                                 c.add("Quartile1W4Cnt").title("Quartile1W4Cnt").width(20);
                                 c.add("Quartile3W4Cnt").title("Quartile3W4Cnt").width(20);
                                 c.add("LowlimitW4Cnt").title("LowlimitW4Cnt").width(20);
                                 c.add("HighlimitW4Cnt").title("HighlimitW4Cnt").width(20);
                                 c.add("CriteriaW4Cnt").title("CriteriaW4Cnt").width(20);
                                 c.add("TotalcriteriaCnt").title("TotalcriteriaCnt").width(20);
                                 c.add("WorkloadFactor").title("WorkloadFactor").width(20);
                                 c.add("DateNow").title("Дата выгрузки").format("dd.MM.yyyy HH:mm:ss").width(20);
                             }
                         }
                );

    }
}
