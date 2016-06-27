package ru.alfabank.dmpr.infrastructure.linq;

import org.apache.commons.collections4.keyvalue.AbstractKeyValue;

class LinqKeyValue<TKey, TItem> extends AbstractKeyValue<TKey, TItem> {
    public LinqKeyValue(TKey key, TItem value) {
        super(key, value);
    }
}
