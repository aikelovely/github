package ru.alfabank.dmpr.widget.leaderBoard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.BaseEntity;
import ru.alfabank.dmpr.model.leaderBoard.KpiDataItem;
import ru.alfabank.dmpr.model.leaderBoard.LeaderBoardOptions;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardFilterRepository;
import ru.alfabank.dmpr.repository.leaderBoard.LeaderBoardRepository;
import ru.alfabank.dmpr.widget.BaseWidget;

/**
 * Получает мета-данные для отрисовки графиков внутри блока
 */
@Service
public class LeaderBoardBlockChart extends BaseWidget<LeaderBoardOptions, KpiDataItem[]> {
    @Autowired
    LeaderBoardFilterRepository filterRepository;

    @Autowired
    LeaderBoardRepository repository;

    public LeaderBoardBlockChart() {
        super(LeaderBoardOptions.class);
    }

    @Override
    public KpiDataItem[] getData(LeaderBoardOptions options) {
        if (options.divisionGroupId == null) {
            BaseEntity allDivision = filterRepository.getDivisionGroupByCode(options.startDate, options.endDate, "ОБ");
            options.divisionGroupId = allDivision == null ? null : allDivision.id;
        }

        if (options.endDate == null) {
            options.endDate = options.startDate.plusYears(1).plusDays(-1);
        }

        options.kpiIds = new long[]{options.kpiIdForBlockChart};

        LinqWrapper<KpiDataItem> data = LinqWrapper.from(repository.getKpiDataForBlockChart(options))
                .sort(new Selector<KpiDataItem, Comparable>() {
                    @Override
                    public Comparable select(KpiDataItem kpiDataItem) {
                        return kpiDataItem.calcDate;
                    }
                });
        boolean returnEmpty = true;
        for (KpiDataItem kpiDataItem : data) {
            if (kpiDataItem.currentValue != null
                    || kpiDataItem.planValue != null
                    || kpiDataItem.prevValue != null) {
                returnEmpty = false;
                break;
            }
        }
        if (returnEmpty) {
            return new KpiDataItem[0];
        }

        return data.toArray(KpiDataItem.class);
    }
}
