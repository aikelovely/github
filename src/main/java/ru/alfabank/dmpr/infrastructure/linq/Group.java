package ru.alfabank.dmpr.infrastructure.linq;

import org.apache.commons.collections4.keyvalue.AbstractKeyValue;

import java.util.Iterator;

public class Group<TKey, TItem> extends AbstractKeyValue<TKey, Iterable<TItem>> implements Iterable<TItem> {
    private final int index;

    public Group(TKey key, Iterable<TItem> value, int index) {
        super(key, value);
        this.index = index;
    }

    public LinqWrapper<TItem> getItems() {
        return LinqWrapper.from(getValue());
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Iterator<TItem> iterator() {
        return getValue().iterator();
    }
}
