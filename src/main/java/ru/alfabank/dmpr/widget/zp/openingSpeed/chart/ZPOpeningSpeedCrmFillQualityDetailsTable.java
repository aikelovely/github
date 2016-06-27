package ru.alfabank.dmpr.widget.zp.openingSpeed.chart;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.helper.MathHelper;
import ru.alfabank.dmpr.infrastructure.linq.*;
import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.zp.ProcessStage;
import ru.alfabank.dmpr.model.zp.SubProcessStage;
import ru.alfabank.dmpr.model.zp.ZPOpeningSpeedOptions;
import ru.alfabank.dmpr.model.zp.ZPQualityInfo;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPOpeningSpeedRepository;
import ru.alfabank.dmpr.widget.BaseWidget;
import ru.alfabank.dmpr.widget.zp.openingSpeed.CrmFillQualityDetailsTableResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Таблица детализированный отчет о качестве заполнения CRM
 */
@Service
public class ZPOpeningSpeedCrmFillQualityDetailsTable extends BaseWidget<ZPOpeningSpeedOptions, CrmFillQualityDetailsTableResult[]> {
    @Autowired
    ZPOpeningSpeedRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPOpeningSpeedCrmFillQualityDetailsTable() {
        super(ZPOpeningSpeedOptions.class);
    }

    @Override
    public CrmFillQualityDetailsTableResult[] getData(final ZPOpeningSpeedOptions options) {

        LinqWrapper<ZPQualityInfo> data = LinqWrapper.from(repository.getCrmFillQualityDetails(options));

        final List<Long> processStageIds = LinqWrapper.from(filterRepository.getProcessStages())
                .filter(new Predicate<ProcessStage>() {
                    @Override
                    public boolean check(ProcessStage item) {
                        return item.code == 1 || item.code == 2;
                    }
                })
                .sort(new Selector<ProcessStage, Comparable>() {
                    @Override
                    public Comparable select(ProcessStage item) {
                        return item.code;
                    }
                })
                .select(new Selector<ProcessStage, Long>() {
                    @Override
                    public Long select(ProcessStage item) {
                        return item.id;
                    }
                })
                .toList();

        LinqWrapper<SubProcessStage> subProcessStageIds = LinqWrapper.from(filterRepository.getSubProcessStages())
                .filter(new Predicate<SubProcessStage>() {
                    @Override
                    public boolean check(SubProcessStage item) {
                        return processStageIds.contains(item.parentId);
                    }
                });

        return new CrmFillQualityDetailsTableResult[]{
                createGroup(data, subProcessStageIds, processStageIds.get(0)),
                createGroup(data, subProcessStageIds, processStageIds.get(1))
        };
    }

    private CrmFillQualityDetailsTableResult createGroup(LinqWrapper<ZPQualityInfo> data,
                                                         LinqWrapper<SubProcessStage> subProcessStages,
                                                         final long processId) {
        subProcessStages = subProcessStages
                .filter(new Predicate<SubProcessStage>() {
                    @Override
                    public boolean check(SubProcessStage item) {
                        return item.parentId == processId;
                    }
                });

        final List<Long> subProcessStageIds = subProcessStages
                .select(new Selector<SubProcessStage, Long>() {
                    @Override
                    public Long select(SubProcessStage item) {
                        return item.id;
                    }
                }).toList();

        subProcessStageIds.add(-1L);

        data = data.filter(new Predicate<ZPQualityInfo>() {
            @Override
            public boolean check(ZPQualityInfo item) {
                return subProcessStageIds.contains(item.subStageId);
            }
        });

        List<Map<String, Object>> rows = data
                .group(new Selector<ZPQualityInfo, Pair<String, String>>() {
                    @Override
                    public Pair<String, String> select(ZPQualityInfo item) {
                        return Pair.of(item.unitCode, item.unitName);
                    }
                })
                .select(new Selector<Group<Pair<String, String>, ZPQualityInfo>, Map<String, Object>>() {
                    @Override
                    public Map<String, Object> select(Group<Pair<String, String>, ZPQualityInfo> unitGroup) {
                        return createRow(unitGroup, subProcessStageIds);
                    }
                })
                .toList();

        if(rows.size() > 1){
            rows.add(createTotalsRow(data, subProcessStages));
        }

        return new CrmFillQualityDetailsTableResult(rows, subProcessStages.toList(), processId);
    }

    private Map<String, Object> createTotalsRow(final LinqWrapper<ZPQualityInfo> data,
                                                final LinqWrapper<SubProcessStage> subProcessStages){
        final Map<String, Object> row = new HashMap<>();

        final int totalCompanyCount = data.filter(new Predicate<ZPQualityInfo>() {
            @Override
            public boolean check(ZPQualityInfo item) {
                return item.subStageId == -1L;
            }
        }).sum(new Selector<ZPQualityInfo, Integer>() {
            @Override
            public Integer select(ZPQualityInfo item) {
                return item.companyCount;
            }
        });

        final AtomicInteger totalCountByStage = new AtomicInteger(0);

        subProcessStages.each(new Action<SubProcessStage>() {
            @Override
            public void act(final SubProcessStage stage) {
                String columnName = "C" + stage.id;

                LinqWrapper<ZPQualityInfo> dataByStage = data.filter(new Predicate<ZPQualityInfo>() {
                    @Override
                    public boolean check(ZPQualityInfo item) {
                        return item.subStageId == stage.id;
                    }
                });
                int companyCountByStage = 0;

                if(dataByStage.count() > 0){
                    companyCountByStage = dataByStage.sum(new Selector<ZPQualityInfo, Integer>() {
                        @Override
                        public Integer select(ZPQualityInfo item) {
                            return item.companyCount;
                        }
                    });
                }

                totalCountByStage.addAndGet(companyCountByStage);

                row.put(columnName, MathHelper.safeDivide(companyCountByStage * 100, totalCompanyCount));
            }
        });

        double averageValue = MathHelper.safeDivide(totalCountByStage.get() * 100,
                totalCompanyCount * subProcessStages.count());
        row.put("average", averageValue);

        return row;
    }

    private Map<String, Object> createRow(Group<Pair<String, String>, ZPQualityInfo> unitGroup,
                                          List<Long> subProcessStageIds) {
        Pair<String, String> key = unitGroup.getKey();
        LinqWrapper<ZPQualityInfo> items = unitGroup.getItems();

        final Map<String, Object> row = new HashMap<>();
        row.put("unitCode", key.getLeft());
        row.put("unitName", key.getRight());

        final Map<Long, ZPQualityInfo> index = items
                .toMap(new Selector<ZPQualityInfo, Long>() {
                    @Override
                    public Long select(ZPQualityInfo item) {
                        return item.subStageId;
                    }
                });

        ZPQualityInfo totalCompanyInfo = index.get(-1L);
        final int totalCompanyCount = totalCompanyInfo == null ? 0 : totalCompanyInfo.companyCount;

        final ZPQualityInfo sumValue = new ZPQualityInfo();
        LinqWrapper.from(subProcessStageIds)
                .each(new Action<Long>() {
                    @Override
                    public void act(Long subProcessId) {
                        if(subProcessId == -1L) return;;

                        ZPQualityInfo item = index.get(subProcessId);
                        String columnName = "C" + subProcessId;

                        double value = 0d;
                        if (item != null){
                            sumValue.companyCount += item.companyCount;
                            value = MathHelper.safeDivide(item.companyCount, totalCompanyCount) * 100;
                        }

                        row.put(columnName, value);
                    }
                });

        double averageValue = MathHelper.safeDivide(sumValue.companyCount * 100,
                (subProcessStageIds.size() - 1) * totalCompanyCount);
        row.put("average", averageValue);

        return row;
    }
}