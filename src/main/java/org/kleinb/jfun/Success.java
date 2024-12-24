package org.kleinb.jfun;

public record Success<A>(A value) implements Try<A> {}
