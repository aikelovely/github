package ru.alfabank.dmpr.infrastructure.linq;

public interface Selector<TItem, TResult> {
   TResult select(TItem item);
}
