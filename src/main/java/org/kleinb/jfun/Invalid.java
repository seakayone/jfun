package org.kleinb.jfun;

public record Invalid<E, A>(E error) implements Validation<E, A> {
}
