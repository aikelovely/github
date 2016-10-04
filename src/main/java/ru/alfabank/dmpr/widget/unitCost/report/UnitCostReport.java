package ru.alfabank.dmpr.widget.unitCost.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.model.unitCost.*;
import ru.alfabank.dmpr.repository.unitCost.UnitCostRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import static org.joda.time.LocalDateTime.now;

/**
 * Created by U_M0NCT on 26.07.2016.
 */

@Service
public class UnitCostReport extends BaseReport <UnitCostPeriodOptions> {
@Autowired
    UnitCostRepository rep;
    public UnitCostReport( ) {
        super(UnitCostPeriodOptions.class);
    }

    @Override
    protected String getReportName(UnitCostPeriodOptions options) {
        return "UnitCost "+ now();
    }

    @Override
    protected void configure(ReportBuilder builder, UnitCostPeriodOptions options) {
        UnitCost[] data2= rep.getunitCost(options);
        Pl[] data3= rep.getPl(options);
        DetailreportUc[] data4= rep.getDetailreportUc(options);

        Bpiep2profitcenterShist[] data5= rep.getBpiep2profitcenterShist(options);
        UcBpplallocrulesShist[] data6= rep.getBpplallocrulesShist(options);
        BpucplrptstringShist[] data7= rep.getBpucplrptstringShist(options);

        builder.addWorksheet(UnitCost.class)
        .bindTo(data2)
                .title("UnitCost")
         .columns(new ColumnFactoryWrapper() {
             @Override
             public void createColumns(ColumnFactory c) {
                 c.add("prdtypeCcode").title("Тип периода").width(20);
                 c.add("lvl").title("Уровень").width(20);
                 c.add("valueDay").title("Дата").width(20).format("dd.MM.yyyy");
                 c.add("bpinnerendproductName").title("Наименование ВКП").width(20);
                 c.add("bpinnerendproductCcode").title("Код ВКП").width(20);
                 c.add("bporgregionCcode").title("Регион").width(20);
                 c.add("bpdivisiongroupIepCcode").title("Дирекция ВКП").width(20);
                 c.add("totalBpiepQuantityCnt").title("Количество ВКП").format("#,##0.00").width(20);
                 c.add("totallaborvalueCnt").title("Трудозатраты ВКП(мин)").format("#,##0.00").width(20);
                 c.add("totalcostusdCnt").title("TOTALCOST в USD").format("#,##0.00").width(20);
                 c.add("totalcostrurCnt").title("TOTALCOST в RUR").format("#,##0.00").width(20);
                 c.add("ucUsd").title("UnitCost в USD").format("#,##0.00").width(20);
                 c.add("ucRur").title("UnitCost в RUR").format("#,##0.00").width(20);
             }
         }
         );
        builder.addWorksheet(Pl.class)
                .bindTo(data3)
                .title("PL")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("valueDay").title("Дата").format("dd.MM.yyyy");
                                 c.add("ccode").title("Регион").width(20);
                                 c.add("rusCcode").title("ПЦ").width(20);
                                 c.add("ncodePl").title("Код строки отчета PL").width(20);
                                 c.add("namePl").title("Строка отчета PL").width(20);
                                 c.add("ncodeGr").title("Код группировки").width(20);
                                 c.add("nameGr").title("Наименование группировки").width(20);
                                 c.add("sumRurAmt").title("Сумма RUR").format("#,##0.00").width(20);
                                 c.add("sumUsdAmt").title("Сумма USD").format("#,##0.00").width(20);

                             }
                         }
                );
        builder.addWorksheet(DetailreportUc.class)
                .bindTo(data4)
                .title("DETAILREPORT_UC")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("valueDay").title("Отчетная дата").format("dd.MM.yyyy");
                                 c.add("profitcenterRusCcode").title("Расход по профит-центру").width(20);
                                 c.add("bporgregionCostCcode").title("Расход по региону").width(20);
                                 c.add("totalcostvaluepcUsdAmt2").title("Расход ПЦ/региона в USD").width(20);
                                 c.add("bpdivisiongroupIepCcode").title("Дирекция ВКП").width(20);
                                 c.add("bpinnerendproductName").title("Наименование ВКП").width(20);
                                 c.add("bpinnerendproductCcode").title("Код ВКП").width(20);
                                 c.add("bporgregionlvl4ccode").title("Регион 4-го уровня").width(20);
                                 c.add("bporgregionlvl3ccode").title("Регион 3-го уровня").width(20);
                                 c.add("bporgregionlvl2ccode").title("Регион 2-го уровня").width(20);
                                 c.add("bporgregionlvl1ccode").title("Регион 1-го уровня").width(20);
                                 c.add("totalbpiepquantitycnt").title("Количество ВКП").format("#,##0.00").width(20);
                                 c.add("totallaborvaluecnt").title("Трудозатраты ВКП(мин)").format("#,##0.00").width(20);
                                 c.add("normativ").title("Норматив").format("#,##0.00").width(20);
                                 c.add("totallaborvaluepccnt").title("Трудозатраты ПЦ/региона").format("#,##0.00").width(20);
                                 c.add("iep2workloadrate").title("Пропорция тз ВКП к тз ПЦ/рег").format("#,##0.00").width(20);
                                 c.add("totalcostvalueusdamt").title("TOTALCOST на КП в USD").format("#,##0.00").width(20);
                                 c.add("totalcostvalueruramt").title("TOTALCOST на КП в RUR").format("#,##0.00").width(20);
                                 c.add("totalcostvaluepcusdamt").title("TOTALCOST ПЦ/Региона в USD").format("#,##0.00").width(20);
                                 c.add("totalcostvaluepcruramt").title("TOTALCOST ПЦ/Региона в RUR").format("#,##0.00").width(20);
                             }
                         }
                );

        builder.addWorksheet(Bpiep2profitcenterShist.class)
                .bindTo(data5)
                .title("BPIEP2PROFITCENTER_SHIST")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("pc").title("ПЦ").width(20);
                                 c.add("startDate").title("Дата начала ПЦ").format("dd.MM.yyyy");
                                 c.add("ccodepc").title("Код КП").width(20);
                                 c.add("kp").title("КП").width(20);
                                 c.add("ccodereg").title("Код региона").width(20);
                                 c.add("namereg").title("Регион").width(20);
                                 c.add("ccodedr").title("Код драйвера").width(20);
                                 c.add("namedr").title("Дрйавер").width(20);
                                 c.add("headcountAmt").title("Коэффициент").format("#,##0.00").width(20);
                                 c.add("effectiveFrom").title("Дата начала").format("dd.MM.yyyy");
                                 c.add("effectiveTo").title("Дата окончания").format("dd.MM.yyyy");
                             }
                         }
                );


        builder.addWorksheet(UcBpplallocrulesShist.class)
                .bindTo(data6)
                .title("BPPLALLOCRULES_SHIST")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("pc").title("ПЦ").width(20);
                                 c.add("startDate1").title("Дата начала ПЦ").format("dd.MM.yyyy");
                                 c.add("pc1").title("ПЦ, на который аллоцируется").width(20);
                                 c.add("startDate2").title("Дата нач.ПЦ, на кот.алл-ся").format("dd.MM.yyyy");
                                 c.add("ncodeupc").title("Код группировки УПО").width(20);
                                 c.add("nameupc").title("Группировка УПО").width(20);
                                 c.add("ccode").title("Код драйвера").width(20);
                                 c.add("name").title("Дрйавер").width(20);
                                 c.add("ruramt").title("Сумма в RUR").format("#,##0.0000").width(20);
                                 c.add("usdamt").title("Сумма в USD").format("#,##0.00").width(20);
                                 c.add("bpdriverallocFactor").title("Коэффициент аллокации").format("#,##0.00").width(20);
                                 c.add("corrValueDay").title("Отчетная дата").format("dd.MM.yyyy");
                                 c.add("effectiveFrom").title("Дата начала").format("dd.MM.yyyy");
                                 c.add("effectiveTo").title("Дата окончания").format("dd.MM.yyyy");
                             }
                         }
                );


        builder.addWorksheet(BpucplrptstringShist.class)
                .bindTo(data7)
                .title("BPUCPLRPTSTRING_SHIST")
                .columns(new ColumnFactoryWrapper() {
                             @Override
                             public void createColumns(ColumnFactory c) {
                                 c.add("ncodepl").title("Код строки отчета PL").width(20);
                                 c.add("namepl").title("Строка отчета PL").width(20);
                                 c.add("ncodeupc").title("Код гркппировки УПО").width(20);
                                 c.add("nameupc").title("Группировка УПО").width(20);
                                 c.add("effectiveFrom").title("Дата начала").format("dd.MM.yyyy");
                                 c.add("effectiveTo").title("Дата окончания").format("dd.MM.yyyy");
                             }
                         }
                );

///---/


//        Bpiep2profitcenterShist[] getUcDetailreportUc(UnitCostPeriodOptions options);
//        UcBpplallocrulesShist[] getUcBpplallocrulesShist(UnitCostPeriodOptions options);
//        BpucplrptstringShist[] getBpucplrptstringShist(UnitCostPeriodOptions options);
///---/
    }
}
