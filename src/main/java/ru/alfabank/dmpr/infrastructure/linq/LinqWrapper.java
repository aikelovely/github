package ru.alfabank.dmpr.infrastructure.linq;

import org.apache.commons.collections4.KeyValue;
import ru.alfabank.dmpr.model.BaseEntity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LinqWrapper<T> implements Iterable<T> {
    private final Iterable<T> source;

    private LinqWrapper(Iterable<T> source) {
        this.source = source;
    }

    @Override
    public Iterator<T> iterator() {
        return new BaseIterator<T>() {
            private final Iterator<T> sourceIterator = source.iterator();

            @Override
            public boolean hasNext() {
                return sourceIterator.hasNext();
            }

            @Override
            public T next() {
                return sourceIterator.next();
            }
        };
    }

    public static <TItem> LinqWrapper<TItem> from(Iterable<TItem> source) {
        return new LinqWrapper<>(source);
    }

    public static <TItem> LinqWrapper<TItem> from(TItem[] source) {
        return new LinqWrapper<>(Linq.asIterable(source));
    }

    public static <TKey, TItem> LinqWrapper<KeyValue<TKey, TItem>> from(Map<TKey, TItem> map) {
        return new LinqWrapper<>(Linq.asIterable(map));
    }


    public <TResult> LinqWrapper<TResult> select(Selector<T, TResult> selector) {
        return new LinqWrapper<>(Linq.select(this, selector));
    }

    public LinqWrapper<T> filter(Predicate<T> predicate) {
        return new LinqWrapper<>(Linq.filter(this, predicate));
    }

    public <TValue extends Comparable<? super TValue>> LinqWrapper<T> sort(Selector<T, TValue> selector) {
        return new LinqWrapper<T>(Linq.sort(this, selector));
    }

    public <TValue extends Comparable<? super TValue>> LinqWrapper<T> sortDesc(Selector<T, TValue> selector) {
        return new LinqWrapper<T>(Linq.sortDesc(this, selector));
    }

    public <TKey> LinqWrapper<Group<TKey, T>> group(Selector<T, TKey> keySelector) {
        return new LinqWrapper<>(Linq.group(this, keySelector));
    }

    public LinqWrapper<T> distinct() {
        return new LinqWrapper<>(Linq.distinct(this));
    }

    public <TValue> LinqWrapper<T> distinctBy(Selector<T, TValue> selector) {
        return new LinqWrapper<>(Linq.distinctBy(this, selector));
    }

    public List<T> toList() {
        return Linq.toList(this);
    }

    public T[] toArray(Class<T> aClass) {
        return Linq.toArray(this, aClass);
    }

    public <TKey, TValue> Map<TKey, TValue> toMap(Selector<T, TKey> keySelector, Selector<T, TValue> valueSelector) {
        return Linq.toMap(this, keySelector, valueSelector);
    }

    public <TKey> Map<TKey, T> toMap(Selector<T, TKey> keySelector) {
        return Linq.toMap(this, keySelector);
    }


    public T first() {
        return Linq.first(this);
    }

    public T first(Predicate<T> predicate) {
        return Linq.first(this, predicate);
    }

    public T firstOrNull() {
        return Linq.firstOrNull(this);
    }

    public T firstOrNull(Predicate<T> predicate) {
        return Linq.firstOrNull(this, predicate);
    }


    public boolean any(Predicate<T> predicate) {
        return Linq.any(this, predicate);
    }

    public boolean all(Predicate<T> predicate) {
        return Linq.all(this, predicate);
    }


    public int count() {
        return Linq.count(this);
    }

    public <TValue extends Number> TValue sum(Selector<T, TValue> selector) {
        Number value = 0;
        return sum(selector, (TValue)value);
    }

    public <TValue extends Number> TValue sum(Selector<T, TValue> selector, TValue seed) {
        return Linq.sum(this, selector, seed);
    }

    public <TValue extends Comparable<TValue>> TValue min(Selector<T, TValue> selector) {
        return min(selector, null);
    }

    public <TValue extends Comparable<TValue>> TValue min(Selector<T, TValue> selector, TValue seed) {
        return Linq.min(this, selector, seed);
    }

    public <TValue extends Comparable<TValue>> TValue max(Selector<T, TValue> selector) {
        return max(selector, null);
    }

    public <TValue extends Comparable<TValue>> TValue max(Selector<T, TValue> selector, TValue seed) {
        return Linq.max(this, selector, seed);
    }

    public <TValue> TValue aggregate(Selector<T, TValue> selector, BinaryOperation<TValue> operation, TValue seed) {
        return Linq.aggregate(this, selector, operation, seed);
    }


    public void each(Action<T> action) {
        Linq.each(this, action);
    }
}
