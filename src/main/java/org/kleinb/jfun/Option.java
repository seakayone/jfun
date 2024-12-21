package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Option<A> permits None, Some {

    static <A> Option<A> some(A value) {
        return new Some<>(value);
    }

    @SuppressWarnings("unchecked")
    static <A> Option<A> none() {
        return (None<A>) None.INSTANCE;
    }

    default boolean isSome() {
        return this instanceof Some;
    }

    default boolean isNone() {
        return this instanceof None;
    }

    default A get() {
        switch (this) {
            case Some(A value) -> {
                return value;
            }
            case None<A> _ -> {
                throw new NoSuchElementException();
            }
        }
    }

    default <B> Option<B> flatMap(Function<A, Option<B>> f) {
        switch (this) {
            case Some(A value) -> {
                return f.apply(value);
            }
            case None<A> _ -> {
                return none();
            }
        }
    }

    default <B> Option<B> map(Function<A, B> f) {
        switch (this) {
            case Some(A value) -> {
                return some(f.apply(value));
            }
            case None<A> _ -> {
                return none();
            }
        }
    }

    default <C> C fold(Function<A, C> some, Supplier<C> none) {
        switch (this) {
            case Some(A value) -> {
                return some.apply(value);
            }
            case None<A> _ -> {
                return none.get();
            }
        }
    }

    default Option<A> orElse(Supplier<Option<A>> other) {
        switch (this) {
            case Some(A _) -> {
                return this;
            }
            case None<A> _ -> {
                return other.get();
            }
        }
    }

    default A getOrElse(Supplier<A> other) {
        switch (this) {
            case Some(A value) -> {
                return value;
            }
            case None<A> _ -> {
                return other.get();
            }
        }
    }

    default Option<A> filter(Predicate<A> f) {
        return flatMap(a -> f.test(a) ? this : none());
    }

    default Option<A> filterNot(Predicate<A> f) {
        return filter(f.negate());
    }

    default List<A> toList() {
        return fold(List::of, List::of);
    }

    // Conversion methods

    default Optional<A> toOptional() {
        return fold(Optional::of, Optional::empty);
    }

    default <B> Either<B, A> toRight(Supplier<B> left) {
        return isSome() ? Either.right(get()) : Either.left(left.get());
    }

    default Try<A> toTry(Supplier<Throwable> left) {
        return isSome() ? Try.success(get()) : Try.failure(new NoSuchElementException());
    }
}

