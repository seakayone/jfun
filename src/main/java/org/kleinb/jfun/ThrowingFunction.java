package org.kleinb.jfun;

@FunctionalInterface
public interface ThrowingFunction<A, B> {
    B apply(A a) throws Throwable;
}
