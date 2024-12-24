package org.kleinb.jfun;

public record Left<A, B>(A value) implements Either<A, B> {}
