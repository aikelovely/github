package ru.alfabank.dmpr.widget.zp.institution.chart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.alfabank.dmpr.infrastructure.chart.ChartResult;
import ru.alfabank.dmpr.infrastructure.linq.Action;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;
import ru.alfabank.dmpr.infrastructure.linq.Predicate;
import ru.alfabank.dmpr.infrastructure.linq.Selector;
import ru.alfabank.dmpr.model.ChildEntityWithInfo;
import ru.alfabank.dmpr.model.zp.ProcessStage;
import ru.alfabank.dmpr.model.zp.SubProcessStage;
import ru.alfabank.dmpr.model.zp.ZPInstitutionOptions;
import ru.alfabank.dmpr.model.zp.ZPTile;
import ru.alfabank.dmpr.repository.zp.ZPFilterRepository;
import ru.alfabank.dmpr.repository.zp.ZPInstitutionRepository;
import ru.alfabank.dmpr.widget.BaseChart;
import ru.alfabank.dmpr.widget.BaseWidget;

import java.util.HashMap;
import java.util.Map;

/**
 * График-плитка.
 */
@Service
public class ZPInstitutionTiles extends BaseWidget<ZPInstitutionOptions, ZPTile[]> {
    @Autowired
    ZPInstitutionRepository repository;

    @Autowired
    ZPFilterRepository filterRepository;

    public ZPInstitutionTiles() {
        super(ZPInstitutionOptions.class);
    }

    @Override
    public ZPTile[] getData(ZPInstitutionOptions options) {
        LinqWrapper<ZPTile> data = LinqWrapper.from(repository.getTiles(options));

        if (data.count() == 0) return new ZPTile[0];

        final ProcessStage[] stages = filterRepository.getProcessStages();
        final SubProcessStage[] subStages = filterRepository.getSubProcessStages();

        data.each(new Action<ZPTile>() {
            @Override
            public void act(final ZPTile tile) {
                ProcessStage stage = LinqWrapper.from(stages).firstOrNull(new Predicate<ProcessStage>() {
                    @Override
                    public boolean check(ProcessStage stage) {
                        return tile.stageId == stage.id;
                    }
                });
                if(stage != null){
                    tile.subStageCode = stage.code;
                }

                SubProcessStage subStage = LinqWrapper.from(subStages).firstOrNull(new Predicate<SubProcessStage>() {
                    @Override
                    public boolean check(SubProcessStage subStage) {
                        return tile.subStageId == subStage.id;
                    }
                });
                if(subStage != null){
                    tile.subStageCode = subStage.code;
                }
            }
        });

        return data.toArray(ZPTile.class);
    }
}
