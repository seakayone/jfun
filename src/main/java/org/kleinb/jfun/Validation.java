package org.kleinb.jfun;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public sealed interface Validation<E, A> permits Invalid, Valid {
    static <E, A> Validation<E, A> valid(A value) {
        return new Valid<>(value);
    }

    static <E, A> Validation<E, A> invalid(E error) {
        return new Invalid<>(error);
    }

    static <E, A> Validation<E, A> fromEither(Either<E, A> either) {
        return either.fold(Validation::valid, Validation::invalid);
    }

    static <A> Validation<Throwable, A> fromTry(Try<A> t) {
        return t.fold(Validation::valid, Validation::invalid);
    }

    default boolean isValid() {
        return this instanceof Valid;
    }

    default boolean isInvalid() {
        return this instanceof Invalid;
    }

    default A get() {
        switch (this) {
            case Valid(A value) -> {
                return value;
            }
            case Invalid<E, A> _ -> {
                throw new NoSuchElementException();
            }
        }
    }

    default E getError() {
        switch (this) {
            case Invalid(E error) -> {
                return error;
            }
            case Valid<E, A> _ -> {
                throw new NoSuchElementException();
            }
        }
    }

    default A getOrElse(A defaultValue) {
        switch (this) {
            case Valid(A value) -> {
                return value;
            }
            case Invalid<E, A> _ -> {
                return defaultValue;
            }
        }
    }

    default A getOrElse(Supplier<A> defaultValue) {
        switch (this) {
            case Valid(A value) -> {
                return value;
            }
            case Invalid<E, A> _ -> {
                return defaultValue.get();
            }
        }
    }

    default Validation<E, A> orElse(Validation<E, A> or) {
        switch (this) {
            case Valid<E, A> valid -> {
                return valid;
            }
            case Invalid<E, A> _ -> {
                return or;
            }
        }
    }

    default Validation<E, A> orElse(Supplier<Validation<E, A>> or) {
        switch (this) {
            case Valid<E, A> valid -> {
                return valid;
            }
            case Invalid<E, A> _ -> {
                return or.get();
            }
        }
    }

    default Validation<A, E> swap() {
        switch (this) {
            case Valid(A value) -> {
                return invalid(value);
            }
            case Invalid(E error) -> {
                return valid(error);
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Validation<E, B> map(Function<A, B> f) {
        switch (this) {
            case Valid(A value) -> {
                return valid(f.apply(value));
            }
            case Invalid<E, A> invalid -> {
                return (Invalid<E, B>) invalid;
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Validation<B, A> mapError(Function<E, B> f) {
        switch (this) {
            case Valid<E, A> valid -> {
                return (Validation<B, A>) valid;
            }
            case Invalid(E error) -> {
                return invalid(f.apply(error));
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Validation<E, B> flatMap(Function<A, Validation<E, B>> f) {
        switch (this) {
            case Valid(A value) -> {
                return f.apply(value);
            }
            case Invalid<E, A> invalid -> {
                return (Invalid<E, B>) invalid;
            }
        }
    }

    default <B> B fold(Function<A, B> valid, Function<E, B> invalid) {
        switch (this) {
            case Valid(A value) -> {
                return valid.apply(value);
            }
            case Invalid(E error) -> {
                return invalid.apply(error);
            }
        }
    }

    default Validation<E, A> tap(Consumer<A> f) {
        if (this instanceof Valid(A value)) {
            f.accept(value);
        }
        return this;
    }

    default Validation<E, A> tapError(Consumer<E> f) {
        if (this instanceof Invalid(E error)) {
            f.accept(error);
        }
        return this;
    }

    default Validation<E, A> tapBoth(Consumer<A> valid, Consumer<E> invalid) {
        return tap(valid).tapError(invalid);
    }

    default Either<E, A> toEither() {
        switch (this) {
            case Valid(A value) -> {
                return Either.right(value);
            }
            case Invalid<E, A>(E error) -> {
                return Either.left(error);
            }
        }
    }

}

