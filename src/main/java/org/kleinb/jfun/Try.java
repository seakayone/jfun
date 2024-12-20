package org.kleinb.jfun;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public sealed interface Try<A> {

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

    A get();

    Throwable getFailure();

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

record Success<A>(A value) implements Try<A> {
    @Override
    public A get() {
        return value;
    }

    @Override
    public Throwable getFailure() {
        return new NoSuchElementException("Success does not have a failure");
    }
}

record Failure<A>(Throwable error) implements Try<A> {
    @Override
    public A get() {
        return TryOps.sneakyThrow(error);
    }

    @Override
    public Throwable getFailure() {
        return error;
    }
}

interface TryOps {
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
