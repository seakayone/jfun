package org.kleinb.jfun;

@FunctionalInterface
public interface ThrowingSupplier<T> {
    T get() throws Throwable;
}
