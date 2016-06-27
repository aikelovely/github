package ru.alfabank.dmpr.model.cr.ClientTime;

import java.io.Serializable;

/**
 * Иерархия этапов.
 */
public class StageHierarchyDto implements Serializable {
    /**
     * Id эатапа.
      */
    public long stageId;

    /**
     * Название этапа
     */
    public String stageName;

    /**
     * Id бизнес процесса
     */
    public long bpKindId;

    /**
     * Название бизнес процесса
     */
    public String bpKindName;

    /**
     * Id дочернего бизнес процесса
     */
    public long childBpKindId;

    /**
     * Название дочернего бизнес процесса
     */
    public String childBpKindName;
}
