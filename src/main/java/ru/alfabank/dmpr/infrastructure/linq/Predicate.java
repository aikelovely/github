package ru.alfabank.dmpr.infrastructure.linq;

public interface Predicate<T> {
    boolean check(T item);
}
