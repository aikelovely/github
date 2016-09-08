package ru.alfabank.dmpr.widget.nom.report;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.export.excel.Column;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactory;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.ColumnFactoryWrapper;
import ru.alfabank.dmpr.infrastructure.helper.ArrayHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodSelectHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.Period;
import ru.alfabank.dmpr.model.PeriodSelectOption;
import ru.alfabank.dmpr.model.Week;
import ru.alfabank.dmpr.model.nom.NomCountFinProd;
import ru.alfabank.dmpr.model.nom.NomDetailsReportRow;
import ru.alfabank.dmpr.model.nom.NomOptions;
import ru.alfabank.dmpr.model.nom.NomQueryOptions;
import ru.alfabank.dmpr.repository.nom.NomFilterRepository;
import ru.alfabank.dmpr.repository.nom.NomRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import java.text.SimpleDateFormat;

/**
 * Выгрузка рядом с графиком "Детализация ВКП"
 */
@Service
public class NomReport extends BaseReport<NomOptions> {
    // Класс для листа верхнего уровня (продукты)
    class NomDetailsProductRecordRow extends NomDetailsReportRow {
        public boolean operationsAbnormalEmissions;

        public NomDetailsProductRecordRow(NomDetailsReportRow row){
            valueDay = row.valueDay;
            valueDayAsString = row.valueDayAsString;
            direction = row.direction;
            divGrp = row.divGrp;
            kpCCode = row.kpCCode;
            kpName = row.kpName;
            ooDoc = row.ooDoc;
            coRp = row.coRp;
            region = row.region;
        }
    }

    @Autowired
    private NomRepository repository;

    @Autowired
    private NomFilterRepository filterRepository;

    public NomReport() {
        super(NomOptions.class);
    }

    @Override
    protected String getReportName(NomOptions options) {
        return "KP_Details";
    }

    private static ColumnFactoryWrapper getColumns(final boolean withDate, final boolean isAbnormalList, final boolean isProductList){
        return new ColumnFactoryWrapper() {
            @Override
            public void createColumns(ColumnFactory c) {
                if (withDate) {
                    c.add("valueDayAsString").title("Период");
                }
                c.add("direction").title("Дирекция").width(15);
                c.add("divGrp").title("Подразделение").width(15);
                c.add("kpCCode").title("Код КП").width(15);
                c.add("kpName").title("Наименование КП");

                if (!isProductList) {
                    c.add("kp2CCode").title("Код ПодКП").width(15);
                    c.add("kp2Name").title("Наименование ПодКП");
                }

                c.add("ooDoc").title("ОО/ДО");
                c.add("region").title("Куст");
                c.add("coRp").title("ЦО/РП").width(8);

                if (!isAbnormalList) {
                    c.add("blk").title("Блок");
                }


                Column cntColumn = new Column(NomDetailsReportRow.class, "cnt");
                ColumnBuilder cntColumnBuilder = new ColumnBuilder(cntColumn).title("Количество").dynamicBackgroundByRow(new Selector<Object, Color>() {
                    @Override
                    public Color select(Object o) {
                        NomDetailsReportRow row = (NomDetailsReportRow) o;
                        return row.isAbnormalEmission() ? Color.RedColor : null;
                    }
                });

                if (isProductList){
                    cntColumnBuilder.dynamicFormat(new Selector<Object, String>() {
                        @Override
                        public String select(Object o) {
                            NomDetailsProductRecordRow row = (NomDetailsProductRecordRow) o;
                            return !row.isAbnormalEmission() && row.operationsAbnormalEmissions ? "0\\*" : null;
                        }
                    });
                }

                c.add(cntColumn);

                if (!isAbnormalList) {
                    c.add("portfolioFlag").title("Флаг портфельности КП").width(20);
                }
            }
        };
    }

    @Override
    protected void configure(ReportBuilder builder, NomOptions options) {
        final int secondWorksheetTimeUnitId = options.timeUnitId == 3 ? 2 : options.timeUnitId;
        int thirdWorksheetTimeUnitId = 3;

        final NomQueryOptions queryOptions = new NomQueryOptions(options, filterRepository.getWeeks());
        NomDetailsReportRow[] groupData = repository.getReport(queryOptions);
        queryOptions.timeUnitId = secondWorksheetTimeUnitId;
        NomDetailsReportRow[] periodData = repository.getReport(queryOptions);
        queryOptions.timeUnitId = thirdWorksheetTimeUnitId;
        NomDetailsReportRow[] periodDataByWeek = repository.getReport(queryOptions);
        NomDetailsReportRow[] abnormalEmissionsData;

        NomCountFinProd[] countKP = repository.getCountKP(queryOptions);

        if ((groupData == null || groupData.length == 0
                || periodData == null || periodData.length == 0
                || periodDataByWeek == null || periodDataByWeek.length == 0) &&
                (countKP == null || countKP.length == 0)) {
            builder.addNoDataWorksheet();
            return;
        }

        final Week[] weeks = filterRepository.getWeeks();

        // Подписи для 2-ой вкладки (обычные недели/месяцы)
        LinqWrapper.from(periodData).each(new Action<NomDetailsReportRow>() {
            @Override
            public void act(final NomDetailsReportRow value) {
                PeriodSelectOption periodEntity = secondWorksheetTimeUnitId == Period.month.getValue() ?
                        PeriodSelectHelper.getMonthByDate(value.valueDay) : PeriodSelectHelper.getWeekByDate(value.valueDay, weeks);

                if (periodEntity != null) {
                    value.valueDayAsString = periodEntity.name;
                } else {
                    value.valueDayAsString = value.valueDay.toString();
                }
            }
        });

        // Подписи для переходящих недель
        LinqWrapper.from(periodDataByWeek).each(new Action<NomDetailsReportRow>() {
            @Override
            public void act(final NomDetailsReportRow value) {
                PeriodSelectOption periodEntity = PeriodSelectHelper.getWeekByDate(value.valueDay, weeks);

                if (periodEntity != null) {
                    LocalDate transitionWeekStart = periodEntity.startDate.getMonthOfYear() == value.valueDay.getMonthOfYear() ?
                            periodEntity.startDate : value.valueDay.withDayOfMonth(1);
                    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

                    value.valueDayAsString = "Неделя " + periodEntity.periodNum +
                            " (" + df.format(transitionWeekStart.toDate()) + "-" + df.format(value.valueDay.toDate()) + ")";
                } else {
                    value.valueDayAsString = value.valueDay.toString();
                }
            }
        });

        if (options.timeUnitId == Period.week.getValue()) {
            abnormalEmissionsData = periodData;
        } else {
            queryOptions.timeUnitId = 2;
            abnormalEmissionsData = repository.getReport(queryOptions);

            // Подписи для вкладки выбросов
            LinqWrapper.from(abnormalEmissionsData).each(new Action<NomDetailsReportRow>() {
                @Override
                public void act(final NomDetailsReportRow value) {
                    PeriodSelectOption periodEntity = PeriodSelectHelper.getWeekByDate(value.valueDay, weeks);

                    if (periodEntity != null) {
                        value.valueDayAsString = periodEntity.name;
                    } else {
                        value.valueDayAsString = value.valueDay.toString();
                    }
                }
            });
        }

        // Формируем обычные листы
        builder.addWorksheet(NomDetailsReportRow.class)
                .bindTo(groupData)
                .title("За весь период")
                .columns(getColumns(false, false, false));

        builder.addWorksheet(NomDetailsReportRow.class)
                .bindTo(periodData)
                .title("В разрезе периодов")
                .columns(getColumns(true, false, false));

        builder.addWorksheet(NomDetailsReportRow.class)
                .bindTo(periodDataByWeek)
                .title("В разрезе периодов(Переходящие)")
                .columns(getColumns(true, false, false));



        // Группированные данные по операциям
        NomDetailsReportRow[] operationalAbnormalEmissionsData =
                LinqWrapper.from(abnormalEmissionsData).group(new Selector<NomDetailsReportRow, String>() {
            @Override
            public String select(NomDetailsReportRow nomDetailsReportRow) {
                return nomDetailsReportRow.getOperationDateKey();
            }
        }).select(new Selector<Group<String, NomDetailsReportRow>, NomDetailsReportRow>() {
                    @Override
                    public NomDetailsReportRow select(Group<String, NomDetailsReportRow> nomDetailsReportRows) {
                        LinqWrapper<NomDetailsReportRow> items = nomDetailsReportRows.getItems();

                        NomDetailsReportRow operationRow = items.first().cloneWithoutAdditionalFields();

                        operationRow.cnt = items.sum(new Selector<NomDetailsReportRow, Double>() {
                            @Override
                            public Double select(NomDetailsReportRow nomDetailsReportRow) {
                                return nomDetailsReportRow.cnt;
                            }
                        });

                        return operationRow;
                    }
                }).toArray(NomDetailsReportRow.class);

        // Заполняем выбросы у операций
        fillAbnormalEmissions(operationalAbnormalEmissionsData);

        // Группированные данные по продуктам
        NomDetailsProductRecordRow[] productAbnormalEmissionsData =
                LinqWrapper.from(operationalAbnormalEmissionsData).group(new Selector<NomDetailsReportRow, String>() {
            @Override
            public String select(NomDetailsReportRow nomDetailsReportRow) {
                return nomDetailsReportRow.getProductDateKey();
            }
        }).select(new Selector<Group<String, NomDetailsReportRow>, NomDetailsProductRecordRow>() {
            @Override
            public NomDetailsProductRecordRow select(Group<String, NomDetailsReportRow> nomDetailsReportRows) {
                LinqWrapper<NomDetailsReportRow> items = nomDetailsReportRows.getItems();

                NomDetailsProductRecordRow productRow = new NomDetailsProductRecordRow(items.first());

                productRow.cnt = items.sum(new Selector<NomDetailsReportRow, Double>() {
                    @Override
                    public Double select(NomDetailsReportRow nomDetailsReportRow) {
                        return nomDetailsReportRow.cnt;
                    }
                });

                NomDetailsReportRow abnormalItem = nomDetailsReportRows.getItems().firstOrNull(new Predicate<NomDetailsReportRow>() {
                    @Override
                    public boolean check(NomDetailsReportRow item) {
                        return item.isAbnormalEmission();
                    }
                });

                productRow.operationsAbnormalEmissions = abnormalItem != null;

                return productRow;
            }
        }).toArray(NomDetailsProductRecordRow.class);

        // Заполняем выбросы у продуктов
        fillAbnormalEmissions(productAbnormalEmissionsData);

        // Формируем листы по выбросам
        builder.addWorksheet(NomDetailsProductRecordRow.class)
                .bindTo(LinqWrapper.from(productAbnormalEmissionsData).filter(new Predicate<NomDetailsProductRecordRow>() {
                    @Override
                    public boolean check(NomDetailsProductRecordRow item) {
                        return item.isAbnormalEmission() || item.operationsAbnormalEmissions;
                    }
                }).toArray(NomDetailsProductRecordRow.class))
                .title("Аномальные выбросы по КП")
                .columns(getColumns(true, true, true));

        builder.addWorksheet(NomDetailsReportRow.class)
                .bindTo(LinqWrapper.from(operationalAbnormalEmissionsData).filter(new Predicate<NomDetailsReportRow>() {
                    @Override
                    public boolean check(NomDetailsReportRow item) {
                        return item.isAbnormalEmission();
                    }
                }).toArray(NomDetailsReportRow.class))
                .title("Аномальные выбросы по операциям")
                .columns(getColumns(true, true, false));

        builder.addWorksheet(NomCountFinProd.class)
                .bindTo(countKP)
                .title("CountKP")
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("prdStartDay").title("Дата начала периода").format("dd.MM.yyyy");
                        c.add("valueDate").title("Дата конца периода").format("dd.MM.yyyy");
                        c.add("directorate").title("Дирекция");
                        c.add("codeFinProd").title("Код КП");
                        c.add("nameFinProd").title("Наименование КП");
                        c.add("codeToFinProd").title("Код ПодКП");
                        c.add("nameToFinProd").title("Наименование ПодКП");
                        c.add("shortName").title("ОО/ДО");
                        c.add("countAgg").title("Количество");
                        c.add("portfolioFlg").title("Флаг портфельности КП");
                    }
                });
    }

    private void fillAbnormalEmissions(NomDetailsReportRow[] data){
        LinqWrapper<NomDetailsReportRow> dataWrapper = LinqWrapper.from(data);

        dataWrapper.group(new Selector<NomDetailsReportRow, String>() {
            @Override
            public String select(NomDetailsReportRow nomDetailsReportRow) {
                return nomDetailsReportRow.getKey();
            }
        }).each(new Action<Group<String, NomDetailsReportRow>>() {
            @Override
            public void act(Group<String, NomDetailsReportRow> nomDetailsReportRows) {
                final LinqWrapper<NomDetailsReportRow> items = nomDetailsReportRows.getItems();

                Double[] counts = items.select(new Selector<NomDetailsReportRow, Double>() {
                    @Override
                    public Double select(NomDetailsReportRow nomDetailsReportRow) {
                        return nomDetailsReportRow.cnt;
                    }
                }).toArray(Double.class);

                final double firstQuartile = ObjectUtils.firstNonNull(ArrayHelper.QuartileInc(counts, 1), 0d);
                final double thirdQuartile = ObjectUtils.firstNonNull(ArrayHelper.QuartileInc(counts, 3), 0d);

                items.each(new Action<NomDetailsReportRow>() {
                    @Override
                    public void act(NomDetailsReportRow item) {
                        item.setIsAbnormalEmission(false);
                        if ((item.cnt < firstQuartile - 3 * (thirdQuartile - firstQuartile)) ||
                                (item.cnt > thirdQuartile + 3 * (thirdQuartile - firstQuartile))) {
                            item.setIsAbnormalEmission(true);
                        }
                    }
                });
            }
        });
    }
}