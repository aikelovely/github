package ru.alfabank.dmpr.model;

import java.util.List;

public class SelectOptGroup extends BaseEntity {
    public List<BaseEntity> options;
    public SelectOptGroup(long id, String name, List<BaseEntity> options){
        super(id, name);
        this.options = options;
    }
    public SelectOptGroup(BaseEntity base, List<BaseEntity> options){
        super(base.id, base.name);
        this.options = options;
    }
}
