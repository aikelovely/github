package ru.alfabank.dmpr.widget.mass.decomposition.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import ru.alfabank.dmpr.infrastructure.helper.DateHelper;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.helper.PeriodHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.mass.decomposition.KpiDataItem;
import ru.alfabank.dmpr.model.mass.decomposition.MassDecompositionOptions;
import ru.alfabank.dmpr.model.mass.decomposition.TresholdCode;
import ru.alfabank.dmpr.model.mass.decomposition.TresholdItem;
import ru.alfabank.dmpr.repository.mass.MassDecompositionRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class MassDecompositionTableByStageBase extends BaseWidget<MassDecompositionOptions, List<Map<String, Object>>> {
    @Autowired
    MassDecompositionRepository repository;

    public MassDecompositionTableByStageBase() {
        super(MassDecompositionOptions.class);
        _avgDurationTreshold = null;
        _percentTreshold = null;
    }

    private TresholdItem _avgDurationTreshold;
    private TresholdItem _percentTreshold;

    protected abstract void fixOptions(MassDecompositionOptions options);

    @Override
    public List<Map<String, Object>> getData(final MassDecompositionOptions options) {
        options.stageDetalization = 1;
        fixOptions(options);

        _percentTreshold = repository.getTresholdByCode(TresholdCode.ByDay);
        _avgDurationTreshold = repository.getTresholdByCode(TresholdCode.AvgDuration);

        KpiDataItem[] items = LinqWrapper.from(repository.getKpiData(options)).filter(new Predicate<KpiDataItem>() {
            @Override
            public boolean check(KpiDataItem item) {
                return item.stageId == options.stageId;
            }
        }).toArray(KpiDataItem.class);

        LinqWrapper<KpiDataItem> sortedItems = LinqWrapper.from(items)
                .sort(new Selector<KpiDataItem, LocalDate>() {
                    @Override
                    public LocalDate select(KpiDataItem item) {
                        return item.calcDate;
                    }
                });

        final List<LocalDate> dates = PeriodHelper.getTicks(options);

        List<Map<String, Object>> rows = sortedItems.group(new Selector<KpiDataItem, Pair<Long, String>>() {
            @Override
            public Pair<Long, String> select(KpiDataItem item) {
                return Pair.of(item.unitId, item.unitName);
            }
        }).select(new Selector<Group<Pair<Long, String>, KpiDataItem>, Map<String, Object>>() {
            @Override
            public Map<String, Object> select(Group<Pair<Long, String>, KpiDataItem> unitGroup) {
                return createUnitRow(unitGroup, dates);
            }
        }) .sort(new Selector<Map<String, Object>, String>() {
            @Override
            public String select(Map<String, Object> row) {
                return (String) row.get("unitName");
            }
        }).toList();

        LinqWrapper<Group<LocalDate, KpiDataItem>> dateGroups = sortedItems.group(new Selector<KpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(KpiDataItem item) {
                return item.calcDate;
            }
        });

        rows.add(createTotalsRow(dateGroups, dates));

        return rows;
    }

    // row - строка с данными для одного подразделения (unit)
    private Map<String, Object> createUnitRow(
            Group<Pair<Long, String>, KpiDataItem> unitGroup, List<LocalDate> dates) {
        Pair<Long, String> key = unitGroup.getKey();
        final Map<String, Object> row = createEmptyRow(key);

        LinqWrapper<KpiDataItem> items = unitGroup.getItems();
        final Map<LocalDate, KpiDataItem> index = items.toMap(new Selector<KpiDataItem, LocalDate>() {
            @Override
            public LocalDate select(KpiDataItem item) {
                return item.calcDate;
            }
        });

        LinqWrapper.from(dates)
                .each(new Action<LocalDate>() {
                    @Override
                    public void act(LocalDate date) {
                        KpiDataItem item = index.get(date);
                        String columnName = DateHelper.createDateName(date);
                        addNewCell(row, columnName, item);
                    }
                });

        final KpiDataItem summer = new KpiDataItem();

        items.each(new Action<KpiDataItem>() {
            @Override
            public void act(KpiDataItem item) {
                summer.totalDuration += item.totalDuration;
                summer.bpCount += item.bpCount;
                summer.inLimitBpCount += item.inLimitBpCount;
            }
        });

        addNewCell(row, "average", summer);

        return row;
    }

    private Map<String, Object> createTotalsRow(
            LinqWrapper<Group<LocalDate, KpiDataItem>> dateGroups, List<LocalDate> dates) {
        Map<LocalDate, KpiDataItem> index = new HashMap<>();
        for (Group<LocalDate, KpiDataItem> dateGroup : dateGroups) {
            KpiDataItem summer = new KpiDataItem();
            final List<KpiDataItem> items = dateGroup.getItems().toList();
            for (KpiDataItem item : items) {
                summer.totalDuration += item.totalDuration;
                summer.inLimitBpCount += item.inLimitBpCount;
                summer.bpCount += item.bpCount;
            }
            index.put(dateGroup.getKey(), summer);
        }

        Map<String, Object> row = createEmptyRow(Pair.of((Long) null, "totals"));

        for (LocalDate date : dates) {
            KpiDataItem item = index.get(date);

            String columnName = DateHelper.createDateName(date);
            addNewCell(row, columnName, item);
        }

        addNewCell(row, "average", null);
        return row;
    }

    private void addNewCell(Map<String, Object> row, String columnName, KpiDataItem item) {
        class RichCell {
            public double avgDurationValue;
            public double percentValue;
            public TresholdItem avgDurationTreshold;
            public TresholdItem percentTreshold;

            public RichCell(KpiDataItem item) {
                avgDurationValue = MathHelper.safeDivide(item.totalDuration, item.bpCount);
                percentValue = MathHelper.safeDivide(item.inLimitBpCount, item.bpCount);
                avgDurationTreshold = _avgDurationTreshold;
                percentTreshold = _percentTreshold;
            }
        }

        RichCell cell = null;
        if (item != null)
            cell = new RichCell(item);

        row.put(columnName, cell);
    }

    private Map<String, Object> createEmptyRow(Pair<Long, String> key) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("unitId", key.getLeft());
        row.put("unitName", key.getRight());
        return row;
    }
}
