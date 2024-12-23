package org.kleinb.jfun;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;

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

    default B get() {
        switch (this) {
            case Left(A _) -> {
                throw new NoSuchElementException("get called on Left");
            }
            case Right(B value) -> {
                return value;
            }
        }
    }

    default B getOrElse(B or) {
        switch (this) {
            case Left(A _) -> {
                return or;
            }
            case Right(B value) -> {
                return value;
            }
        }
    }

    default Either<A, B> orElse(Supplier<Either<A, B>> or) {
        switch (this) {
            case Left(A _) -> {
                return or.get();
            }
            case Right(B value) -> {
                return this;
            }
        }
    }

    // conversion methods

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
