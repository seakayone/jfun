package org.kleinb.jfun;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
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

    default Try<A> orElse(Supplier<Try<A>> defaultValue) {
        switch (this) {
            case Success<A> _ -> {
                return this;
            }
            case Failure<A> _ -> {
                return defaultValue.get();
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

    default Either<Throwable, A> toEither() {
        return isSuccess() ? Either.right(get()) : Either.left(getFailure());
    }

    default Option<A> toOption() {
        return isSuccess() ? Option.some(get()) : Option.none();
    }

    default Optional<A> toOptional() {
        return isSuccess() ? Optional.of(get()) : Optional.empty();
    }
}

interface TryOps {
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
