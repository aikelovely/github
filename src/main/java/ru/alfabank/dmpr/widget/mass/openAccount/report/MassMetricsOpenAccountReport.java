package ru.alfabank.dmpr.widget.mass.openAccount.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.Dynamic;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.DynamicReportRow;
import ru.alfabank.dmpr.model.cr.TTYAndTTM.TTYAndTTMOptions;
import ru.alfabank.dmpr.model.mass.openAccount.KpiDataItem;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountDetailsReportRow;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountDetailsTableRow;
import ru.alfabank.dmpr.model.mass.openAccount.MassOpenAccountOptions;
import ru.alfabank.dmpr.repository.cr.CRFilterRepository;
import ru.alfabank.dmpr.repository.cr.TTYAndTTMRepository;
import ru.alfabank.dmpr.repository.mass.MassFilterRepository;
import ru.alfabank.dmpr.repository.mass.MassOpenAccountRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Выгрузка в Excel
 */
@Service
public class MassMetricsOpenAccountReport extends BaseReport<MassOpenAccountOptions> {
    @Autowired
    private MassOpenAccountRepository repository;

    @Autowired
    private MassFilterRepository filterRepository;

    public MassMetricsOpenAccountReport() {
        super(MassOpenAccountOptions.class);
    }

    @Override
    protected String getReportName(final MassOpenAccountOptions options) {
        return "DetailsReport";
    }

    @Override
    protected void configure(ReportBuilder builder, final MassOpenAccountOptions options) {
        if(options.systemUnitIds != null && options.systemUnitIds.length > 0){
            options.rcbUnitId = options.systemUnitIds[0];
        } else {
            options.rcbUnitId = 2;
        }

        final BaseEntity firstLevelUnit = LinqWrapper.from(filterRepository.getSystemUnits())
                .first(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(BaseEntity item) {
                        return item.id == options.rcbUnitId;
                    }
                });

        final boolean showSecondLevel = options.systemUnitIds != null && options.systemUnitIds.length > 1;

        KpiDataItem[] data = repository.getDetailsTable(options);
        final KpiDataItem summation = new KpiDataItem();
        final List<MassOpenAccountDetailsReportRow> rows = new ArrayList<>();


        LinqWrapper.from(data).each(new Action<KpiDataItem>() {
            @Override
            public void act(final KpiDataItem item) {
                summation.bpCount += item.bpCount;

                summation.bpCountGrp1 += item.bpCountGrp1;
                summation.bpCountGrp2 += item.bpCountGrp2;
                summation.bpCountGrp3 += item.bpCountGrp3;
                summation.bpCountGrp4 += item.bpCountGrp4;
                summation.bpCountGrp5 += item.bpCountGrp5;

                summation.sumDurationInHours += item.sumDurationInHours;
                if (summation.maxDurationInHours < item.maxDurationInHours) {
                    summation.maxDurationInHours = item.maxDurationInHours;
                }

                rows.add(new MassOpenAccountDetailsReportRow(item, item.unitName, ""));

                if(showSecondLevel){
                    options.rcbUnitId = options.systemUnitIds[1];
                    switch (options.systemUnitIds[0]){
                        case 1:
                            options.cityIds = new long[]{item.unitId};
                            break;
                        case 2:
                            options.salesChannelIds = new long[]{item.unitId};
                            break;
                        case 3:
                            options.bpTypeIds = new long[]{item.unitId};
                            break;
                        case 4:
                            options.dopOfficeIds = new long[]{item.unitId};
                            break;
                    }
                    KpiDataItem[] secondLvlData = repository.getDetailsTable(options);
                    LinqWrapper.from(secondLvlData).each(new Action<KpiDataItem>() {
                        @Override
                        public void act(KpiDataItem childItem) {
                            rows.add(new MassOpenAccountDetailsReportRow(
                                    childItem,
                                    item.unitName,
                                    childItem.unitName));
                        }
                    });
                }
            }
        });

        rows.add(new MassOpenAccountDetailsReportRow(summation, "Итоги", ""));

        final String percentFormat = "0.0\\%";

        builder.addWorksheet(MassOpenAccountDetailsReportRow.class)
                .bindTo(rows)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("firstLvlUnitName").title(firstLevelUnit.name);

                        if(showSecondLevel){
                            BaseEntity secondLevelUnit = LinqWrapper.from(filterRepository.getSystemUnits())
                                    .first(new Predicate<BaseEntity>() {
                                        @Override
                                        public boolean check(BaseEntity item) {
                                            return item.id == options.systemUnitIds[1];
                                        }
                                    });

                            c.add("secondLvlUnitName").title(secondLevelUnit.name);
                        }
                        c.add("clientInn").title("ИНН").width(15);
                        c.add("clientPin").title("ПИН EQ").width(15);
                        c.add("branchName").title("Отделение").width(15);
                        c.add("branchMnemonic").title("Мнемоника отделения").width(15);
                        c.add("bpCount").title("Кол-во счетов").width(15);
                        c.add("bpCountGrp1").title("Кол-во счетов открытых за 24 часа").width(15);
                        c.add("bpCountGrp2").title("Более 24 часов (кол-во)").width(15);
                        c.add("bpCountGrp3").title("От 24 часов до 3 дней (кол-во)").width(15);
                        c.add("bpCountGrp4").title("От 3 до 5 дней (кол-во)").width(15);
                        c.add("bpCountGrp5").title("Более 5 дней (кол-во)").width(15);
                        c.add("bpGrp1Percent").title("Доля открытых за 24 часа (доля)").format(percentFormat).width(15);
                        c.add("bpGrp2Percent").title("Более 24 часов (доля)").format(percentFormat).width(15);
                        c.add("bpGrp3Percent").title("От 24 часов до 3 дней (доля)").format(percentFormat).width(15);
                        c.add("bpGrp4Percent").title("От 3 до 5 дней (доля)").format(percentFormat).width(15);
                        c.add("bpGrp5Percent").title("Более 5 дней (доля)").format(percentFormat).width(15);
                        c.add("avgDurationInDays").title("Средний срок открытия").width(15);
                        c.add("maxDurationInDays").title("Макс. срок открытия").width(15);
                    }
                });
    }
}
