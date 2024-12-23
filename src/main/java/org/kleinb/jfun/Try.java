package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Try<A> permits Failure, Success {

    static <A> Try<A> of(ThrowingSupplier<A> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable t) {
            return Try.failure(t);
        }
    }

    static <A> Try<A> ofSupplier(Supplier<A> callable) {
        return of(callable::get);
    }

    static <A> Try<A> ofCallable(Callable<A> callable) {
        return of(callable::call);
    }

    static Try<Void> ofRunnable(Runnable runnable) {
        return of(() -> {
            runnable.run();
            return null;
        });
    }

    static <A> Try<A> success(A value) {
        return new Success<>(value);
    }

    static <A> Try<A> failure(Throwable t) {
        return new Failure<>(t);
    }

    default boolean isSuccess() {
        return this instanceof Success;
    }

    default boolean isFailure() {
        return this instanceof Failure;
    }

    default A get() {
        switch (this) {
            case Success(A value) -> {
                return value;
            }
            case Failure<A> _ -> {
                return TryOps.sneakyThrow(getFailure());
            }
        }
    }

    default A getOrElse(A defaultValue) {
        switch (this) {
            case Success(A value) -> {
                return value;
            }
            case Failure<A> _ -> {
                return defaultValue;
            }
        }
    }

    default Throwable getFailure() {
        switch (this) {
            case Success<A> _ -> {
                throw new NoSuchElementException("getFailure called on Success");
            }
            case Failure(Throwable e) -> {
                return e;
            }
        }
    }

    default Try<A> orElse(Supplier<Try<A>> or) {
        switch (this) {
            case Success<A> _ -> {
                return this;
            }
            case Failure<A> _ -> {
                return or.get();
            }
        }
    }

    default boolean contains(A value) {
        switch (this) {
            case Success(A v) -> {
                return v.equals(value);
            }
            case Failure<A> _ -> {
                return false;
            }
        }
    }

    default boolean exists(Predicate<A> f) {
        switch (this) {
            case Success(A value) -> {
                return f.test(value);
            }
            case Failure<A> _ -> {
                return false;
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Try<B> map(Function<A, B> f) {
        switch (this) {
            case Success(A value) -> {
                return Try.success(f.apply(value));
            }
            case Failure<A> failure -> {
                return (Failure<B>) failure;
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Try<B> mapTry(ThrowingFunction<A, B> f) {
        switch (this) {
            case Success(A value) -> {
                return Try.of(() -> f.apply(value));
            }
            case Failure<A> failure -> {
                return (Failure<B>) failure;
            }
        }
    }

    @SuppressWarnings("unchecked")
    default <B> Try<B> flatMap(Function<A, Try<B>> f) {
        switch (this) {
            case Success(A value) -> {
                return f.apply(value);
            }
            case Failure<A> failure -> {
                return (Failure<B>) failure;
            }
        }
    }

    default Try<A> recover(Function<Throwable, A> f) {
        switch (this) {
            case Success<A> success -> {
                return success;
            }
            case Failure(Throwable t) -> {
                return Try.success(f.apply(t));
            }
        }
    }

    default Try<A> recoverWith(Function<Throwable, Try<A>> f) {
        switch (this) {
            case Success<A> success -> {
                return success;
            }
            case Failure(Throwable t) -> {
                return f.apply(t);
            }
        }
    }

    default Try<A> filter(Predicate<A> f) {
        switch (this) {
            case Success(A value) -> {
                return f.test(value) ? this : Try.failure(new NoSuchElementException("Predicate does not hold for " + value));
            }
            case Failure<A> _ -> {
                return this;
            }
        }
    }

    default Try<A> filterNot(Predicate<A> f) {
        return filter(f.negate());
    }

    default Try<A> tap(Consumer<A> f) {
        switch (this) {
            case Success(A value) -> {
                f.accept(value);
            }
            case Failure<A> _ -> {
            }
        }
        return this;
    }

    default Try<A> tapFailure(Consumer<Throwable> f) {
        switch (this) {
            case Success<A> _ -> {
            }
            case Failure(Throwable t) -> {
                f.accept(t);
            }
        }
        return this;
    }

    // conversion methods

    default Either<Throwable, A> toEither() {
        switch (this) {
            case Success(A value) -> {
                return Either.right(value);
            }
            case Failure(Throwable t) -> {
                return Either.left(t);
            }
        }
    }

    default Option<A> toOption() {
        switch (this) {
            case Success(A value) -> {
                return Option.some(value);
            }
            case Failure<A> _ -> {
                return Option.none();
            }
        }
    }

    default Optional<A> toOptional() {
        switch (this) {
            case Success(A value) -> {
                return Optional.of(value);
            }
            case Failure<A> _ -> {
                return Optional.empty();
            }
        }
    }

    default List<A> toList() {
        switch (this) {
            case Success(A value) -> {
                return List.of(value);
            }
            case Failure<A> _ -> {
                return List.of();
            }
        }
    }
}

interface TryOps {
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
