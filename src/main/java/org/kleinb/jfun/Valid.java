package org.kleinb.jfun;

public record Valid<E, A>(A value) implements Validation<E, A> {
}
