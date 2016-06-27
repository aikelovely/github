package ru.alfabank.dmpr.infrastructure.linq;

import java.util.Iterator;

public abstract class BaseIterator<T> implements Iterator<T> {
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
