package ru.alfabank.dmpr.infrastructure.linq;

public interface Action<T> {
    void act(T value);
}
