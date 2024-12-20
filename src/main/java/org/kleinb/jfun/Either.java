package org.kleinb.jfun;

import java.util.function.Function;

public sealed interface Either<A, B> {
    static <A, B> Either<A, B> left(A value) {
        return new Left<>(value);
    }

    static <A, B> Either<A, B> right(B value) {
        return new Right<>(value);
    }

    default boolean isLeft() {
        return this instanceof Left;
    }

    default boolean isRight() {
        return this instanceof Right;
    }

    Either<B, A> swap();

    <C> C fold(Function<A, C> left, Function<B, C> right);

    default <C> Either<A, C> map(Function<B, C> f) {
        return flatMap(f.andThen(Either::right));
    }

    default <C> Either<A, C> flatMap(Function<B, Either<A, C>> f) {
        return fold(Either::left, f);
    }

}

record Left<A, B>(A value) implements Either<A, B> {
    @Override
    public Either<B, A> swap() {
        return Either.right(value);
    }

    @Override
    public <C> C fold(Function<A, C> left, Function<B, C> right) {
        return left.apply(value);
    }
}

record Right<A, B>(B value) implements Either<A, B> {
    @Override
    public Either<B, A> swap() {
        return Either.left(value);
    }

    @Override
    public <C> C fold(Function<A, C> left, Function<B, C> right) {
        return right.apply(value);
    }
}
