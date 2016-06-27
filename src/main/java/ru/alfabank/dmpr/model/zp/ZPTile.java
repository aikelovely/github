package ru.alfabank.dmpr.model.zp;

/**
 * Набор данных для отображения графика-плитки на витрине "Заведение зарплатного проекта"
 */
public class ZPTile {
    public Long stageId;
    public int stageCode;
    public String stageName;

    public Long subStageId;
    public int subStageCode;
    public String subStageName;

    public Integer inKpiCount;
    public Integer totalCount;
    public Double totalDuration;
    public Double kpiNorm;
    public Double inKpiNorm;
}
