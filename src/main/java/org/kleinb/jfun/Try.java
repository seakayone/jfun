package org.kleinb.jfun;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public sealed interface Try<A> permits Failure, Success {

    static <A> Try<A> of(ThrowingSupplier<A> supplier) {
        try {
            return success(supplier.get());
        } catch (Throwable t) {
            return failure(t);
        }
    }

    static <A> Try<A> ofSupplier(Supplier<A> callable) {
        return of(callable::get);
    }

    static <A> Try<A> ofCallable(Callable<A> callable) {
        return of(callable::call);
    }

    static <A> Try<A> success(A value) {
        return new Success<>(value);
    }

    static <A> Try<A> failure(Throwable error) {
        return new Failure<>(error);
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

    default Throwable getFailure() {
        switch (this) {
            case Success<A> _ -> {
                return new NoSuchElementException("Success does not have a failure");
            }
            case Failure(Throwable e) -> {
                return e;
            }
        }
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
