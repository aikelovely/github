package ru.alfabank.dmpr.infrastructure.linq;

import org.apache.commons.collections4.KeyValue;
import ru.alfabank.dmpr.model.SelectOptGroup;

import java.lang.reflect.Array;
import java.util.*;

public class Linq {
    private Linq() {
    }

    public static <TSource, TResult> Iterable<TResult> select(
            final Iterable<TSource> source, final Selector<TSource, TResult> selector) {
        return new Iterable<TResult>() {
            @Override
            public Iterator<TResult> iterator() {
                return new BaseIterator<TResult>() {
                    private Iterator<TSource> sourceIterator = source.iterator();

                    @Override
                    public boolean hasNext() {
                        return sourceIterator.hasNext();
                    }

                    @Override
                    public TResult next() {
                        return selector.select(sourceIterator.next());
                    }
                };
            }
        };
    }

    public static <TItem> Iterable<TItem> filter(
            final Iterable<TItem> source, final Predicate<TItem> predicate) {
        return new Iterable<TItem>() {
            @Override
            public Iterator<TItem> iterator() {
                return new BaseIterator<TItem>() {
                    private Iterator<TItem> sourceIterator = source.iterator();
                    private TItem preview;

                    @Override
                    public boolean hasNext() {
                        if (preview != null)
                            return true;

                        preview = previewNext();
                        return preview != null;
                    }

                    @Override
                    public TItem next() {
                        if (preview != null) {
                            TItem temp = preview;
                            preview = null;
                            return temp;
                        }

                        TItem next = previewNext();
                        if (next != null)
                            return next;

                        throw new NoSuchElementException();
                    }

                    private TItem previewNext() {
                        while (sourceIterator.hasNext()) {
                            TItem current = sourceIterator.next();
                            if (predicate.check(current))
                                return current;
                        }

                        return null;
                    }
                };
            }
        };
    }

    public static <TItem, TValue extends Comparable<? super TValue>> Iterable<TItem> sort(
            final Iterable<TItem> source, final Selector<TItem, TValue> selector) {
        return new Iterable<TItem>() {
            @Override
            public Iterator<TItem> iterator() {
                return new BaseIterator<TItem>() {
                    private Iterator<TItem> iterator;

                    @Override
                    public boolean hasNext() {
                        ensureSorted();
                        return iterator.hasNext();
                    }

                    @Override
                    public TItem next() {
                        ensureSorted();
                        return iterator.next();
                    }

                    private void ensureSorted() {
                        if (iterator != null)
                            return;

                        List<TItem> list = new ArrayList<>();
                        for (TItem item : source) {
                            list.add(item);
                        }

                        Collections.sort(list, Comparer.get(selector));
                        iterator = list.iterator();
                    }
                };
            }
        };
    }

    public static <TItem, TValue extends Comparable<? super TValue>> Iterable<TItem> sortDesc(
            final Iterable<TItem> source, final Selector<TItem, TValue> selector) {
        return new Iterable<TItem>() {
            @Override
            public Iterator<TItem> iterator() {
                return new BaseIterator<TItem>() {
                    private Iterator<TItem> iterator;

                    @Override
                    public boolean hasNext() {
                        ensureSorted();
                        return iterator.hasNext();
                    }

                    @Override
                    public TItem next() {
                        ensureSorted();
                        return iterator.next();
                    }

                    private void ensureSorted() {
                        if (iterator != null)
                            return;

                        List<TItem> list = new ArrayList<>();
                        for (TItem item : source) {
                            list.add(item);
                        }

                        Collections.sort(list, Collections.reverseOrder(Comparer.get(selector)));
                        iterator = list.iterator();
                    }
                };
            }
        };
    }

    public static <TItem, TKey> Iterable<Group<TKey, TItem>> group(
            final Iterable<TItem> source, final Selector<TItem, TKey> keySelector) {
        return new Iterable<Group<TKey, TItem>>() {
            @Override
            public Iterator<Group<TKey, TItem>> iterator() {
                return new BaseIterator<Group<TKey, TItem>>() {
                    private Iterator<KeyValue<TKey, List<TItem>>> iterator;
                    private int index;

                    @Override
                    public boolean hasNext() {
                        ensureGrouped();
                        return iterator.hasNext();
                    }

                    @Override
                    public Group<TKey, TItem> next() {
                        ensureGrouped();
                        KeyValue<TKey, List<TItem>> next = iterator.next();
                        return new Group<>(next.getKey(), next.getValue(), index++);
                    }

                    private void ensureGrouped() {
                        if (iterator != null)
                            return;

                        Map<TKey, List<TItem>> map = new LinkedHashMap<>();
                        for (TItem item : source) {
                            TKey key = keySelector.select(item);
                            List<TItem> list = map.get(key);
                            if (list == null)
                                map.put(key, list = new ArrayList<>());
                            list.add(item);
                        }
                        iterator = asIterable(map).iterator();
                    }
                };
            }
        };
    }

    public static <TItem> Iterable<TItem> distinct(final Iterable<TItem> source) {
        return new Iterable<TItem>() {
            @Override
            public Iterator<TItem> iterator() {
                return new BaseIterator<TItem>() {
                    private Iterator<TItem> sourceIterator = source.iterator();
                    private TItem preview;
                    Set<TItem> set = new HashSet<>();

                    @Override
                    public boolean hasNext() {
                        if (preview != null)
                            return true;

                        preview = previewNext();
                        return preview != null;
                    }

                    @Override
                    public TItem next() {
                        if (preview != null) {
                            TItem temp = preview;
                            preview = null;
                            return temp;
                        }

                        TItem next = previewNext();
                        if (next != null)
                            return next;

                        throw new NoSuchElementException();
                    }

                    private TItem previewNext() {
                        while (sourceIterator.hasNext()) {
                            TItem current = sourceIterator.next();
                            if (!set.contains(current)) {
                                set.add(current);
                                return current;
                            }
                        }

                        return null;
                    }
                };
            }
        };
    }

    public static <TItem, TValue> Iterable<TItem> distinctBy(final Iterable<TItem> source,
                                                             final Selector<TItem, TValue> selector) {
        return new Iterable<TItem>() {
            @Override
            public Iterator<TItem> iterator() {
                return new BaseIterator<TItem>() {
                    private Iterator<TItem> sourceIterator = source.iterator();
                    private TItem preview;
                    Set<TValue> valueSet = new HashSet<>();
                    Set<TItem> set = new HashSet<>();

                    @Override
                    public boolean hasNext() {
                        if (preview != null)
                            return true;

                        preview = previewNext();
                        return preview != null;
                    }

                    @Override
                    public TItem next() {
                        if (preview != null) {
                            TItem temp = preview;
                            preview = null;
                            return temp;
                        }

                        TItem next = previewNext();
                        if (next != null)
                            return next;

                        throw new NoSuchElementException();
                    }

                    private TItem previewNext() {
                        while (sourceIterator.hasNext()) {
                            TItem current = sourceIterator.next();
                            TValue currentValue = selector.select(current);
                            if (!valueSet.contains(currentValue)) {
                                valueSet.add(currentValue);
                                set.add(current);
                                return current;
                            }
                        }

                        return null;
                    }
                };
            }
        };
    }

    public static <T> Iterable<T> asIterable(final T[] array) {
        if (array == null)
            throw new NullPointerException("array");

        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new BaseIterator<T>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < array.length;
                    }

                    @Override
                    public T next() {
                        return array[index++];
                    }
                };
            }
        };
    }

    public static <TKey, TItem> Iterable<KeyValue<TKey, TItem>> asIterable(final Map<TKey, TItem> map) {
        if (map == null)
            throw new NullPointerException("map");

        return new Iterable<KeyValue<TKey, TItem>>() {
            @Override
            public Iterator<KeyValue<TKey, TItem>> iterator() {
                return new BaseIterator<KeyValue<TKey, TItem>>() {
                    private Iterator<TKey> keyIterator = map.keySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return keyIterator.hasNext();
                    }

                    @Override
                    public KeyValue<TKey, TItem> next() {
                        TKey key = keyIterator.next();
                        return new LinqKeyValue<>(key, map.get(key));
                    }
                };
            }
        };
    }


    public static <T> List<T> toList(Iterable<T> source) {
        ArrayList<T> list = new ArrayList<>();
        for (T item : source) {
            list.add(item);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Iterable<T> source, Class<T> aClass) {
        List<T> list = toList(source);

        return list.toArray((T[])Array.newInstance(aClass, list.size()));
    }

    public static <TItem, TKey, TValue> Map<TKey, TValue> toMap(
            Iterable<TItem> source, Selector<TItem, TKey> keySelector, Selector<TItem, TValue> valueSelector) {
        Map<TKey, TValue> map = new HashMap<>();
        for (TItem item : source) {
            TKey key = keySelector.select(item);
            TValue value = valueSelector.select(item);
            map.put(key, value);
        }
        return map;
    }

    public static <TItem, TKey> Map<TKey, TItem> toMap(Iterable<TItem> source, Selector<TItem, TKey> keySelector) {
        Map<TKey, TItem> map = new HashMap<>();
        for (TItem item : source) {
            TKey key = keySelector.select(item);
            map.put(key, item);
        }
        return map;
    }


    public static <TItem> TItem first(Iterable<TItem> source) {
        TItem item = firstOrNull(source);
        if (item != null)
            return item;

        throw new IndexOutOfBoundsException();
    }

    public static <TItem> TItem firstOrNull(Iterable<TItem> source) {
        Iterator<TItem> iterator = source.iterator();
        if (iterator.hasNext())
            return iterator.next();

        return null;
    }

    public static <TItem> TItem first(Iterable<TItem> source, Predicate<TItem> predicate) {
        TItem item = firstOrNull(source, predicate);
        if (item == null)
            throw new NullPointerException();

        return item;
    }

    public static <TItem> TItem firstOrNull(Iterable<TItem> source, Predicate<TItem> predicate) {
        for (TItem item : source) {
            if (predicate.check(item))
                return item;
        }
        return null;
    }


    public static <TItem> boolean any(Iterable<TItem> source, Predicate<TItem> predicate) {
        for (TItem item : source) {
            if (predicate.check(item))
                return true;
        }
        return false;
    }

    public static <TItem> boolean all(Iterable<TItem> source, Predicate<TItem> predicate) {
        for (TItem item : source) {
            if (!predicate.check(item))
                return false;
        }
        return true;
    }


    public static <T> int count(Iterable<T> source) {
        Iterator<T> iterator = source.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    public static <TItem, TValue extends Number> TValue sum(
            Iterable<TItem> source, Selector<TItem, TValue> selector, TValue seed) {
        return aggregate(source,
                selector != null ? selector : new Selector<TItem, TValue>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public TValue select(TItem item) {
                        return (TValue) item;
                    }
                }, new BinaryOperation<TValue>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public TValue perform(TValue first, TValue second) {
                        return first == null ? second : (TValue) addNumbers(first, second);
                    }

                    private Number addNumbers(Number a, Number b) {
                        if (a instanceof Double || b instanceof Double) {
                            return a.doubleValue() + b.doubleValue();
                        } else if (a instanceof Float || b instanceof Float) {
                            return a.floatValue() + b.floatValue();
                        } else if (a instanceof Long || b instanceof Long) {
                            return a.longValue() + b.longValue();
                        } else {
                            return a.intValue() + b.intValue();
                        }
                    }
                }, seed);
    }

    public static <TItem, TValue extends Comparable<TValue>> TValue min(
            Iterable<TItem> source, Selector<TItem, TValue> selector, TValue seed) {
        return aggregate(source, selector != null ? selector : new Selector<TItem, TValue>() {
            @Override
            @SuppressWarnings("unchecked")
            public TValue select(TItem item) {
                return (TValue) item;
            }
        }, new BinaryOperation<TValue>() {
            @Override
            public TValue perform(TValue first, TValue second) {
                return first == null
                        ? second
                        : first.compareTo(second) >= 0 ? second : first;
            }
        }, seed);
    }

    public static <TItem, TValue extends Comparable<TValue>> TValue max(
            Iterable<TItem> source, Selector<TItem, TValue> selector, TValue seed) {
        return aggregate(source, selector != null ? selector : new Selector<TItem, TValue>() {
            @Override
            @SuppressWarnings("unchecked")
            public TValue select(TItem item) {
                return (TValue) item;
            }
        }, new BinaryOperation<TValue>() {
            @Override
            public TValue perform(TValue first, TValue second) {
                return first == null
                        ? second
                        : first.compareTo(second) >= 0 ? first : second;
            }
        }, seed);
    }

    public static <TItem, TValue> TValue aggregate(
            Iterable<TItem> source, Selector<TItem, TValue> selector, BinaryOperation<TValue> operation, TValue seed) {
        for (TItem item : source) {
            seed = operation.perform(seed, selector.select(item));
        }
        return seed;
    }


    public static <TItem> void each(Iterable<TItem> source, Action<TItem> action) {
        for (TItem item : source) {
            action.act(item);
        }
    }
}
