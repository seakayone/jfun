package org.kleinb.jfun;

import java.util.function.Function;

public sealed interface Either<A, B> permits Left, Right {
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

    default Either<B, A> swap() {
        switch (this) {
            case Left(A value) -> {
                return right(value);
            }
            case Right(B value) -> {
                return Either.left(value);
            }
        }
    }

    default <C> C fold(Function<A, C> left, Function<B, C> right) {
        switch (this) {
            case Left(A value) -> {
                return left.apply(value);
            }
            case Right(B value) -> {
                return right.apply(value);
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <C> Either<A, C> map(Function<B, C> f) {
        switch (this) {
            case Left(A _) -> {
                return (Left<A, C>) this;
            }
            case Right(B value) -> {
                return Either.right(f.apply(value));
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <C> Either<A, C> flatMap(Function<B, Either<A, C>> f) {
        switch (this) {
            case Left(A _) -> {
                return (Left<A, C>) this;
            }
            case Right(B value) -> {
                return f.apply(value);
            }
        }
    }

    default Option<B> toOption() {
        switch (this) {
            case Left(A _) -> {
                return Option.none();
            }
            case Right(B value) -> {
                return Option.some(value);
            }
        }
    }

    default Try<B> toTry(Function<A, Throwable> e) {
        switch (this) {
            case Left(A value) -> {
                return Try.failure(e.apply(value));
            }
            case Right(B value) -> {
                return Try.success(value);
            }
        }
    }
}
