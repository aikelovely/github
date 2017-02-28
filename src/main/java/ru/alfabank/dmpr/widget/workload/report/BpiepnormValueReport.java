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
import java.io.FileInputStream;
import java.io.InputStream;
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
        return "WorkloadDetail "+ now();
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
                 c.add("NormativeMinutes").title("NormativeMinutes").format("#,##0.0000").width(20);
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
                                 c.add("ValueDay").title("Отчетная дата").format("dd.MM.yyyy").width(20);
                                 c.add("BpDivisionGroupCcode").title("Группа подразделений").width(20);
                                 c.add("BpinnerendProductName").title("Внутренний конечный продукт").width(20);
                                 c.add("BpinnerendProductCcode").title("Внутренний конечный продукт_код").width(20);
                                 c.add("PortfolioFlag").title("Признак портфельного КП").width(20);
                                 c.add("BporgRegionCcode").title("Регион присутствия").width(20);
                                 c.add("NormativeMinutes").title("Нормативное время исполнения для ВКП").format("#,##0.0000").width(20);
                                 c.add("TotalbpiepquantityCnt").title("Общее фактическое количество внутренних конечных продуктов в аналитических разрезах").format("#,##0.0000").width(20);
                                 c.add("TotallaborvalueCnt").title("Общее количество фактических трудозатрат").format("#,##0.0000").width(20);
                                 c.add("Worktime4staffunitCnt").title("Количество времени одной штатной единицы в отчетном периоде, которое может затратить на выполнение работ").format("#,##0.0000").width(20);
                                 c.add("EstimatedstaffunitCnt").title("Количество штатных единиц необходимых для выполнения фактической трудоемкости за период").format("#,##0.0000").width(20);
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
                                 c.add("ValueDay").title("Отчетная дата").format("dd.MM.yyyy");
                                 c.add("BpdivisiongroupNameLvl1").title("Бизнес линия").width(20);
                                 c.add("BpdivisiongroupNameLvl2").title("Дирекция").width(20);
                                 c.add("BpdivisiongroupNameLvl3").title("Управление/направление").width(20);
                                 c.add("BpDivisionGroupCcode").title("Код группы подразделенеий").width(20);
                                 c.add("BporgRegionCcodeLvl2").title("Родитель региона присутствия").width(20);
                                 c.add("BporgRegionCcodeLvl3").title("Регион присутствия").width(20);
                                 c.add("TotalfactvalueCnt").title("Численность штатных должностей в подразделении").format("#,##0.0000").width(20);
                                 c.add("SlafactvalueCnt").title("Численность штатных должностей с занятой позицей, создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("SlafactvaluedoubleCnt").title("Численность дублирования штатных должностей,  создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("SlafactvacancyCnt").title("Численность штатных должностей с вакантной позицей, создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("SlafactvacancytempCnt").title("Численность штатных должностей с декретной вакантной позицей, создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("NoslafactvalueCnt").title("Численность штатных должностей с занятой позицей, не создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("NoslafactvaluedoubleCnt").title("Численность дублирования штатных должностей,  не создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("NoslafactvacancyCnt").title("Численность штатных должностей с вакантной позицей, не создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("NoslafactvacancytempCnt").title("Численность штатных должностей с декретной вакантной позицей, не создающие ВКП в подразделении").format("#,##0.0000").width(20);
                                 c.add("EstimatedstaffunitW8Cnt").title("Количество штатных единиц необходимых для выполнения фактической трудоемкости на 8 неделю (на отчетную дату)").format("#,##0.0000").width(20);
                                 c.add("Quartile1W8Cnt").title("1-ый квартиль за 8 недель. Нижняя граница интервала принятия решения").format("#,##0.0000").width(20);
                                 c.add("Quartile3W8Cnt").title("3-ий квартиль за 8 недель. Верхняя граница интервала принятия решения").format("#,##0.0000").width(20);
                                 c.add("LowlimitW8Cnt").title("Нижняя граница коридора принятия решения за 8 недель").format("#,##0.0000").width(20);
                                 c.add("HighlimitW8Cnt").title("Верхняя граница коридора принятия решения за 8 недель").format("#,##0.0000").width(20);
                                 c.add("CriteriaW8Cnt").title("Критерий ввода/вывода штатных единиц за 8 недель").format("#,##0.0000").width(20);
                                 c.add("Quartile1W4Cnt").title("1-ый квартиль за 4 недели. Нижняя граница интервала принятия решения").format("#,##0.0000").width(20);
                                 c.add("Quartile3W4Cnt").title("3-ий квартиль за 4 недели. Верхняя граница интервала принятия решения").format("#,##0.0000").width(20);
                                 c.add("LowlimitW4Cnt").title("Нижняя граница коридора принятия решения за 4 недели").format("#,##0.0000").width(20);
                                 c.add("HighlimitW4Cnt").title("Верхняя граница коридора принятия решения за 4 недели").format("#,##0.0000").width(20);
                                 c.add("CriteriaW4Cnt").title("Критерий ввода/вывода штатных единиц за 4 недели").format("#,##0.0000").width(20);
                                 c.add("TotalcriteriaCnt").title("Совокупный критерий ввода/вывода").format("#,##0.0000").width(20);
                                 c.add("WorkloadFactor").title("Нагрузка на подразделение на 8 неделю (на отчетную дату)").format("#,##0.0000").width(20);
                                 c.add("Description").title("Комментарий").width(20);
                                 c.add("DateNow").title("Дата выгрузки").format("dd.MM.yyyy HH:mm:ss").width(20);
                             }
                         }
                );

    }
}

