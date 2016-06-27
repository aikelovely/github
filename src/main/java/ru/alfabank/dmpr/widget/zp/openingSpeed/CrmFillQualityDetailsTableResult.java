package ru.alfabank.dmpr.widget.zp.openingSpeed;

import ru.alfabank.dmpr.model.ChildEntity;
import ru.alfabank.dmpr.model.zp.SubProcessStage;

import java.util.List;
import java.util.Map;

/**
 * Строка таблицы детализированного отчета о качестве заполнения CRM
 */
public class CrmFillQualityDetailsTableResult {
    public List<Map<String, Object>> rows;
    public List<SubProcessStage> subProcessStages;
    public long processId;

    public CrmFillQualityDetailsTableResult(List<Map<String, Object>> rows,
                                            List<SubProcessStage> subProcessStages,
                                            long processId) {
        this.rows = rows;
        this.subProcessStages = subProcessStages;
        this.processId = processId;
    }
}
