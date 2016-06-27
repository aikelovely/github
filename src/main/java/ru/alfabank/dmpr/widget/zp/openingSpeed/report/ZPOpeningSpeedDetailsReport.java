package ru.alfabank.dmpr.widget.zp.openingSpeed.report;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.Color;
import ru.alfabank.dmpr.infrastructure.export.excel.ReportBuilder;
import ru.alfabank.dmpr.infrastructure.export.excel.fluent.*;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.ParamType;
import ru.alfabank.dmpr.model.zp.*;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseReport;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Выгрузка в Excel.
 */
@Service
public class ZPOpeningSpeedDetailsReport extends BaseReport<ZPOpeningSpeedOptions> {
    @Autowired
    private ZPOpeningSpeedRepository repository;

    @Autowired
    private ZPFilterRepository filterRepository;

    public ZPOpeningSpeedDetailsReport() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    protected String getReportName(ZPOpeningSpeedOptions options) {
        return "DetailsReport";
    }

    @Override
    protected void configure(ReportBuilder builder, ZPOpeningSpeedOptions options) {
        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        if (options.systemUnitId == 3) // отображать по городам, если выбрана детализация по городам/компаниям
        {
            options.systemUnitId = 1;
        }

        LinqWrapper<ZPKPIDataItem> data = LinqWrapper.from(repository.getDetails(options));

        createDataWorksheet(builder, options, dates, data);
        createUlCountWorksheet(builder, options, dates, data);
        createCrmWorksheet(builder, options, dates);

        if (options.openingTypeId == 1) {
            createDetailsWorksheet(builder, options);
        } else {
            createRoDetailsWorksheet(builder, options);
        }
    }

    private void createDataWorksheet(ReportBuilder builder,
                                     final ZPOpeningSpeedOptions options,
                                     final List<LocalDate> dates,
                                     final LinqWrapper<ZPKPIDataItem> data) {
        String title = options.paramType == ParamType.AvgDuration
                ? String.format("KPI, %s", options.subProcessStageId == null ? "дн" : "ч")
                : "KPI, %";

        if (data.count() == 0) {
            builder.addNoDataWorksheet().title(title);
            return;
        }

        final double inKpiThreshold = repository.getInKPIThreshold(options);
        final double durationThreshold = data.count() > 0 ? data.first().kpiNorm : 0;

        List<Map<String, Object>> rows = data
                .group(new Selector<ZPKPIDataItem, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(ZPKPIDataItem item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, ZPKPIDataItem>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, ZPKPIDataItem> unitGroup) {
                        return createDataWorksheetRow(unitGroup, dates, inKpiThreshold, options);
                    }
                })
                .sort(new Selector<Map<String, Object>, String>() {
                    @Override
                    public String select(Map<String, Object> row) {
                        return (String) row.get("unitName");
                    }
                })
                .toList();

        BaseEntity systemUnit = LinqWrapper.from(filterRepository.getSystemUnits())
                .firstOrNull(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(BaseEntity item) {
                        return item.id == options.systemUnitId;
                    }
                });

        final String unitName = systemUnit == null ? "Единица сети" : systemUnit.name;

        final Selector<Object, Color> dynamicBackgroundFunc = new Selector<Object, Color>() {
            @Override
            public Color select(Object o) {
                if (o == null) return null;

                double value = (double) o;
                if (options.paramType == ParamType.Percent) {
                    return value >= inKpiThreshold
                            ? Color.GreenColor
                            : value >= 80
                            ? Color.OrangeColor
                            : Color.RedColor;
                }

                return value <= durationThreshold
                        ? Color.GreenColor
                        : Color.RedColor;
            }
        };

        builder.addWorksheetWithDictionaryData()
                .bindTo(rows)
                .title(title)
                .columns(new ColumnDictionaryFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnDictionaryFactory c) {
                        c.add("unitName", String.class).title(unitName);

                        for (LocalDate currentDate : dates) {
                            String title = PeriodHelper.dateAsStringByTimeUnitId(currentDate, options);

                            String columnName = DateHelper.createDateName(currentDate);

                            ColumnBuilder column = c.add(columnName, Double.class).title(title);
                            if (options.paramType == ParamType.Percent) {
                                column.format("0").dynamicBackground(dynamicBackgroundFunc);
                            } else {
                                column.format("#0.0").dynamicBackground(dynamicBackgroundFunc);
                            }
                        }

                        if (dates.size() > 1) {
                            ColumnBuilder column = c.add("totals", Double.class).title("Итого");
                            if (options.paramType == ParamType.Percent) {
                                column.format("0").dynamicBackground(dynamicBackgroundFunc);
                            } else {
                                column.format("#0.0").dynamicBackground(dynamicBackgroundFunc);
                            }
                        }
                    }
                });
    }


    private Map<String, Object> createDataWorksheetRow(Group<Pair<String, String>, ZPKPIDataItem> unitGroup,
                                                       final List<LocalDate> dates,
                                                       final double inKpiThreshold,
                                                       final ZPOpeningSpeedOptions options) {
        Pair<String, String> key = unitGroup.getKey();

        final Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitCode", key.getLeft());
        row.put("unitName", key.getRight());

        LinqWrapper<ZPKPIDataItem> items = unitGroup.getItems();
        final Map<LocalDate, ZPKPIDataItem> index = items.toMap(new Selector<ZPKPIDataItem, LocalDate>() {
            @Override
            public LocalDate select(ZPKPIDataItem item) {
                return item.calcDate;
            }
        });

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        ZPKPIDataItem item = index.get(date);
                        String columnName = DateHelper.createDateName(date);

                        Double value = null;
                        if (item != null) {
                            if (options.paramType == ParamType.AvgDuration) {
                                value = item.avgDuration;
                            } else {
                                value = MathHelper.safeDivide(100 * item.inKpiCount, item.totalCount);
                            }
                        }

                        row.put(columnName, value);
                    }
                });

        final ZPKPIDataItem summation = new ZPKPIDataItem();
        summation.inKpiCount = 0;
        summation.totalCount = 0;
        summation.totalDuration = 0d;
        summation.kpiNorm = 0d;

        items.each(new Action<ZPKPIDataItem>() {
            @Override
            public void act(ZPKPIDataItem item) {
                summation.inKpiCount += item.inKpiCount;
                summation.totalCount += item.totalCount;
                summation.totalDuration += item.totalDuration;
            }
        });

        summation.avgDuration = summation.totalDuration / summation.totalCount;

        Double totalValue;
        if (options.paramType == ParamType.AvgDuration) {
            totalValue = summation.avgDuration;
        } else {
            totalValue = MathHelper.safeDivide(100 * summation.inKpiCount, summation.totalCount);
        }

        row.put("totals", totalValue);

        return row;
    }

    private void createUlCountWorksheet(ReportBuilder builder,
                                        final ZPOpeningSpeedOptions options,
                                        final List<LocalDate> dates,
                                        final LinqWrapper<ZPKPIDataItem> data) {
        String title = "Количество ЮЛ";

        if (data.count() == 0) {
            builder.addNoDataWorksheet().title(title);
            return;
        }

        builder.addCustomWorksheet().title(title).configure(new Action<Sheet>() {
            @Override
            public void act(Sheet sheet) {
                int rowIndex = 0;
                createComplexHeaders(sheet, rowIndex, dates, options, "Всего ЮЛ", "В KPI");
                rowIndex += 2;

                List<Group<Pair<String, String>, ZPKPIDataItem>> dataByUnit = data
                        .group(new Selector<ZPKPIDataItem, Pair<String, String>>() {
                            @Override
                            public Pair<String, String> select(ZPKPIDataItem item) {
                                return Pair.of(item.unitCode, item.unitName);
                            }
                        })
                        .sort(new Selector<Group<Pair<String, String>, ZPKPIDataItem>, Comparable>() {
                            @Override
                            public Comparable select(Group<Pair<String, String>, ZPKPIDataItem> group) {
                                return group.getKey().getRight();
                            }
                        }).toList();



                for (Group<Pair<String, String>, ZPKPIDataItem> group : dataByUnit) {
                    int columnIndex = 0;
                    Row row = sheet.createRow(rowIndex);

                    final Map<LocalDate, ZPKPIDataItem> index = group.getItems().toMap(new Selector<ZPKPIDataItem, LocalDate>() {
                        @Override
                        public LocalDate select(ZPKPIDataItem item) {
                            return item.calcDate;
                        }
                    });

                    row.createCell(columnIndex).setCellValue(group.getKey().getRight());
                    columnIndex++;

                    for (LocalDate currentDate : dates) {
                        ZPKPIDataItem valueByDate = index.get(currentDate);

                        if(valueByDate == null){
                            columnIndex += 2;
                            continue;
                        }

                        row.createCell(columnIndex).setCellValue(valueByDate.totalCount);
                        columnIndex++;

                        row.createCell(columnIndex).setCellValue(valueByDate.inKpiCount);
                        columnIndex++;
                    }

                    if (dates.size() > 1) {
                        Integer totalCount = group.getItems().sum(new Selector<ZPKPIDataItem, Integer>() {
                            @Override
                            public Integer select(ZPKPIDataItem item) {
                                return item.totalCount;
                            }
                        });

                        Integer inKpiCount = group.getItems().sum(new Selector<ZPKPIDataItem, Integer>() {
                            @Override
                            public Integer select(ZPKPIDataItem item) {
                                return item.inKpiCount;
                            }
                        });

                        row.createCell(columnIndex).setCellValue(totalCount);
                        columnIndex++;

                        row.createCell(columnIndex).setCellValue(inKpiCount);
                    }

                    rowIndex++;
                }

                sheet.autoSizeColumn(0);
            }
        });
    }

    private void createCrmWorksheet(ReportBuilder builder,
                                        final ZPOpeningSpeedOptions options,
                                        final List<LocalDate> dates) {
        String title = "Заполнение CRM";

        final LinqWrapper<ZPProjectDynamic> data = LinqWrapper.from(repository.getProjectDetails(options));

        if (data.count() == 0) {
            builder.addNoDataWorksheet().title(title);
            return;
        }

        builder.addCustomWorksheet().title(title).configure(new Action<Sheet>() {
            @Override
            public void act(Sheet sheet) {
                int rowIndex = 0;
                createComplexHeaders(sheet, rowIndex, dates, options, "Всего ЮЛ", "С данными CRM");
                rowIndex += 2;

                List<Group<Pair<String, String>, ZPProjectDynamic>> dataByUnit = data
                        .group(new Selector<ZPProjectDynamic, Pair<String, String>>() {
                            @Override
                            public Pair<String, String> select(ZPProjectDynamic item) {
                                return Pair.of(item.unitCode, item.unitName);
                            }
                        })
                        .sort(new Selector<Group<Pair<String, String>, ZPProjectDynamic>, Comparable>() {
                            @Override
                            public Comparable select(Group<Pair<String, String>, ZPProjectDynamic> group) {
                                return group.getKey().getRight();
                            }
                        }).toList();


                for (Group<Pair<String, String>, ZPProjectDynamic> group : dataByUnit) {
                    int columnIndex = 0;
                    Row row = sheet.createRow(rowIndex);

                    final Map<LocalDate, ZPProjectDynamic> index = group.getItems()
                            .toMap(new Selector<ZPProjectDynamic, LocalDate>() {
                                @Override
                                public LocalDate select(ZPProjectDynamic item) {
                                    return item.calcDate;
                                }
                            });

                    row.createCell(columnIndex).setCellValue(group.getKey().getRight());
                    columnIndex++;

                    for (LocalDate currentDate : dates) {
                        ZPProjectDynamic valueByDate = index.get(currentDate);

                        if (valueByDate == null) {
                            columnIndex += 2;
                            continue;
                        }

                        row.createCell(columnIndex).setCellValue(valueByDate.companyCount);
                        columnIndex++;
                        row.createCell(columnIndex).setCellValue(valueByDate.startedOkCount);
                        columnIndex++;
                    }

                    if (dates.size() > 1) {
                        Integer totalCount = group.getItems().sum(new Selector<ZPProjectDynamic, Integer>() {
                            @Override
                            public Integer select(ZPProjectDynamic item) {
                                return item.companyCount;
                            }
                        });

                        Integer startedOkCount = group.getItems().sum(new Selector<ZPProjectDynamic, Integer>() {
                            @Override
                            public Integer select(ZPProjectDynamic item) {
                                return item.startedOkCount;
                            }
                        });

                        row.createCell(columnIndex).setCellValue(totalCount);
                        columnIndex++;

                        row.createCell(columnIndex).setCellValue(startedOkCount);
                    }

                    rowIndex++;
                }

                sheet.autoSizeColumn(0);
            }
        });
    }

    private void createComplexHeaders(Sheet sheet,
                                      int rowIndex,
                                      final List<LocalDate> dates,
                                      final ZPOpeningSpeedOptions options,
                                      String firstColumnName,
                                      String secondColumnName) {

        BaseEntity systemUnit = LinqWrapper.from(filterRepository.getSystemUnits())
                .firstOrNull(new Predicate<BaseEntity>() {
                    @Override
                    public boolean check(BaseEntity item) {
                        return item.id == options.systemUnitId;
                    }
                });

        final String unitName = systemUnit == null ? "Единица сети" : systemUnit.name;

        Workbook book = sheet.getWorkbook();

        XSSFCellStyle style = (XSSFCellStyle) book.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(new XSSFColor(Color.LightBlueColor.bytes));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setWrapText(true);

        XSSFFont font = (XSSFFont) book.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setColor(new XSSFColor(Color.WhiteColor.bytes));
        style.setFont(font);

        Row firstRow = sheet.createRow(rowIndex);
        Row secondRow = sheet.createRow(rowIndex + 1);

        int columnIndex = 0;

        Cell cell = firstRow.createCell(columnIndex);
        cell.setCellValue(unitName);
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, columnIndex, columnIndex));

        columnIndex++;
        for (LocalDate currentDate : dates) {
            String title = PeriodHelper.dateAsStringByTimeUnitId(currentDate, options);
            CreateComplexHeader(sheet, style, firstRow, secondRow, columnIndex, title,
                    firstColumnName, secondColumnName);

            columnIndex += 2;
        }

        if (dates.size() > 1) {
            String title = "Итого";
            CreateComplexHeader(sheet, style, firstRow, secondRow, columnIndex, title,
                    firstColumnName, secondColumnName);
        }
    }

    private void CreateComplexHeader(Sheet sheet,
                                     CellStyle style,
                                     Row firstRow,
                                     Row secondRow,
                                     int columnIndex,
                                     String mergedColumnTitle,
                                     String firstColumnName,
                                     String secondColumnName) {


        Cell cell = firstRow.createCell(columnIndex);
        cell.setCellValue(mergedColumnTitle);
        cell.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(firstRow.getRowNum(), firstRow.getRowNum(), columnIndex, columnIndex + 1));

        cell = secondRow.createCell(columnIndex);

        cell.setCellValue(firstColumnName);
        cell.setCellStyle(style);


        cell = secondRow.createCell(columnIndex + 1);

        cell.setCellValue(secondColumnName);
        cell.setCellStyle(style);


        sheet.setColumnWidth(columnIndex, 12 * 256);
        sheet.setColumnWidth(columnIndex + 1, 12 * 256);
    }

    private void createDetailsWorksheet(ReportBuilder builder, final ZPOpeningSpeedOptions options) {
        final ZPReportDetailsItem[] data = repository.getReportDetails(options);

        String title = "Детализация по компаниям";

        if (data == null || data.length == 0) {
            builder.addNoDataWorksheet().title(title);
            return;
        }

        final String unitText = options.subProcessStageId != null ? " ч" : " дн";

        for (ZPReportDetailsItem item : data) {
            item.isInKpiTitle = item.isInKpi ? "Да" : "Нет";
        }

        final long firstStageId = filterRepository.getProcessStageIdByCode(1);
        final long secondStageId = filterRepository.getProcessStageIdByCode(2);
        final long thirdStageId = filterRepository.getProcessStageIdByCode(3);

        LinqWrapper.from(data).each(new Action<ZPReportDetailsItem>() {
            @Override
            public void act(ZPReportDetailsItem item) {
                item.totalDuration = 0d;
                if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, firstStageId)) {
                    item.totalDuration += item.s1Duration;
                }
                if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, secondStageId)) {
                    item.totalDuration += item.s2Duration;
                }
                if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, thirdStageId)) {
                    item.totalDuration += item.s3Duration;
                }
            }
        });

        builder.addWorksheet(ZPReportDetailsItem.class)
                .title(title)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("companyName").title("Наименование ЮЛ");
                        c.add("INN").title("ИНН");
                        c.add("bushName").title("Куст");
                        c.add("operationOfficeName").title("Операционный офис");
                        c.add("cityName").title("Город");
                        c.add("managerName").title("ФИО Менеджера");

                        if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, firstStageId)) {
                            c.add("s1StartDate").title("Дата начала этапа \"Заведение ЮЛ\"")
                                    .format("dd.MM.yyyy HH:mm:ss").width(24);
                            c.add("s1Duration").title("Длительность этапа \"Заведение ЮЛ\", " + unitText).width(24);
                        }

                        if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, secondStageId)) {
                            c.add("s2StartDate").title("Дата начала этапа \"Открытие счета 1-му ФЛ\"")
                                    .format("dd.MM.yyyy HH:mm:ss").width(24);
                            c.add("s2Duration").title("Длительность этапа \"Открытие счета 1-му ФЛ\", " + unitText).width(24);
                        }

                        if (options.processStageIds.length == 0 || ArrayUtils.contains(options.processStageIds, thirdStageId)) {
                            c.add("s3StartDateText").title("Дата начала этапа \"Выдача карты ФЛ\"").width(24);
                            c.add("s3Duration").title("Длительность этапа \"Выдача карты ФЛ\", " + unitText).width(24);
                        }

                        if (options.processStageIds.length != 1) {
                            c.add("totalDuration").title("Общая длительность").width(20);
                        }

                        c.add("isInKpiTitle").title("Данные в CRM").width(15);
                    }
                });

    }

    private void createRoDetailsWorksheet(ReportBuilder builder, ZPOpeningSpeedOptions options) {
        final ZPReportDetailsItem[] data = repository.getRoReportDetails(options);

        String title = "Детализация по компаниям";

        if (data == null || data.length == 0) {
            builder.addNoDataWorksheet().title(title);
            return;
        }

        builder.addWorksheet(ZPReportDetailsItem.class)
                .title(title)
                .bindTo(data)
                .columns(new ColumnFactoryWrapper() {
                    @Override
                    public void createColumns(ColumnFactory c) {
                        c.add("companyName").title("Наименование ЮЛ");
                        c.add("INN").title("ИНН");
                        c.add("bushName").title("Куст");
                        c.add("operationOfficeName").title("Операционный офис");
                        c.add("cityName").title("Город");
                        c.add("managerName").title("ФИО Менеджера");

                        c.add("roStartDate").title("Дата начала дооткрытия")
                                .format("dd.MM.yyyy HH:mm:ss").width(24);
                        c.add("roFinishDate").title("Дата завершения  дооткрытия")
                                .format("dd.MM.yyyy HH:mm:ss").width(24);

                        c.add("totalDuration").title("Общая длительность").width(20);
                    }
                });

    }
}
