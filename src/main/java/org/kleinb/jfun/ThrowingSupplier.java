package org.kleinb.jfun;

@FunctionalInterface
public interface ThrowingSupplier<A> {
    A get() throws Throwable;
}
