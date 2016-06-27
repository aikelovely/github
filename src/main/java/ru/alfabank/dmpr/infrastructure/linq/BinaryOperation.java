package ru.alfabank.dmpr.infrastructure.linq;

public interface BinaryOperation<T> {
    T perform(T first, T second);
}
