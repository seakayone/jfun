package org.kleinb.jfun;

public record Right<A, B>(B value) implements Either<A, B> {}
