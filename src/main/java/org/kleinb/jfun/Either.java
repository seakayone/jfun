package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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

    default boolean contains(B elem) {
        switch (this) {
            case Left(A _) -> {
                return false;
            }
            case Right(B b) -> {
                return b.equals(elem);
            }
        }
    }

    default boolean exists(Predicate<B> f) {
        switch (this) {
            case Left(A _) -> {
                return false;
            }
            case Right(B value) -> {
                return f.test(value);
            }
        }
    }

    default <C> C fold(Function<B, C> right, Function<A, C> left) {
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

    default Option<B> filterToOption(Predicate<B> p) {
        switch (this) {
            case Left(A _) -> {
                return Option.none();
            }
            case Right(B value) -> {
                return p.test(value) ? Option.some(value) : Option.none();
            }
        }
    }

    default B filterOrElse(Predicate<B> p, B or) {
        switch (this) {
            case Left(A _) -> {
                return or;
            }
            case Right(B value) -> {
                return p.test(value) ? value : or;
            }
        }
    }

    default Either<A, B> tap(Consumer<B> f) {
        if (this instanceof Right(B value)) {
            f.accept(value);
        }
        return this;
    }

    default Either<A, B> tapLeft(Consumer<A> f) {
        if (this instanceof Left(A value)) {
            f.accept(value);
        }
        return this;
    }

    default Either<A, B> tapBoth(Consumer<A> fa, Consumer<B> fb) {
        return tapLeft(fa).tap(fb);
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

    default Optional<B> toOptional() {
        switch (this) {
            case Left(A _) -> {
                return Optional.empty();
            }
            case Right(B value) -> {
                return Optional.of(value);
            }
        }
    }

    default List<B> toList() {
        switch (this) {
            case Left(A _) -> {
                return List.of();
            }
            case Right(B value) -> {
                return List.of(value);
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

    default Validation<A, B> toValidation() {
        switch (this) {
            case Left(A value) -> {
                return Validation.invalid(value);
            }
            case Right(B value) -> {
                return Validation.valid(value);
            }
        }
    }
}
