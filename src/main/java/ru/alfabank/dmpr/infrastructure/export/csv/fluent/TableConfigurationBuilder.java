package ru.alfabank.dmpr.infrastructure.export.csv.fluent;

import ru.alfabank.dmpr.infrastructure.export.csv.TableConfiguration;
import ru.alfabank.dmpr.infrastructure.linq.LinqWrapper;

import java.util.List;

public class TableConfigurationBuilder<T> {
    private final TableConfiguration<T> configuration;
    private final Class<T> dataType;

    public TableConfigurationBuilder(TableConfiguration<T> configuration, Class<T> dataType) {
        this.configuration = configuration;
        this.dataType = dataType;
    }

    public TableConfigurationBuilder<T> columns(ColumnFactoryWrapper factory){
        @SuppressWarnings("unchecked")
       ColumnFactory builder = new ColumnFactory(configuration.getColumns(), dataType);
        factory.createColumns(builder);
        return this;
    }

    public TableConfigurationBuilder<T> bindTo(List<T> data){
        configuration.setData(data);
        return this;
    }

    public TableConfigurationBuilder<T> bindTo(T[] data){
        configuration.setData(LinqWrapper.from(data).toList());
        return this;
    }
}
