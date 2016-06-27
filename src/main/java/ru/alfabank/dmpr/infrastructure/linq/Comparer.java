package ru.alfabank.dmpr.infrastructure.linq;

import java.util.Comparator;

public class Comparer {
    public static <TItem, TResult extends Comparable<? super TResult>> Comparator<TItem>
    get(final Selector<TItem, TResult> selector) {
        return new Comparator<TItem>() {
            @Override
            public int compare(TItem item1, TItem item2) {
                TResult value1 = selector.select(item1);
                TResult value2 = selector.select(item2);

                if (value1 == null && value2 == null) return 0;
                if (value1 == null) return -1;
                if (value2 == null) return 1;

                return value1.compareTo(value2);
            }
        };
    }
}
