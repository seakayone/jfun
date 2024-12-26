package org.kleinb.jfun;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Try<A> permits Failure, Success {

  static <A> Try<A> of(ThrowingSupplier<? extends A> supplier) {
    try {
      return success(supplier.get());
    } catch (Throwable t) {
      return Try.failure(t);
    }
  }

  static <A> Try<A> ofSupplier(Supplier<? extends A> callable) {
    return of(callable::get);
  }

  static <A> Try<A> ofCallable(Callable<? extends A> callable) {
    return of(callable::call);
  }

  static Try<Void> ofRunnable(Runnable runnable) {
    return of(
        () -> {
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
    if (this instanceof Success(A value)) {
      return value;
    } else {
      return TryOps.sneakyThrow(getFailure());
    }
  }

  default A getOrElse(A or) {
    return isSuccess() ? get() : or;
  }

  default Throwable getFailure() {
    if (this instanceof Failure(Throwable t)) {
      return t;
    } else {
      throw new NoSuchElementException("getFailure called on Success");
    }
  }

  default Try<A> orElse(Supplier<? extends Try<? extends A>> or) {
    if (this instanceof Success<A> success) {
      return success;
    } else {
      @SuppressWarnings("unchecked")
      Try<A> aTry = (Try<A>) or.get();
      return aTry;
    }
  }

  default boolean contains(A value) {
    return exists(a -> Objects.equals(a, value));
  }

  default boolean exists(Predicate<? super A> f) {
    return (this instanceof Success(A value)) && f.test(value);
  }

  default <B> Try<B> map(Function<? super A, ? extends B> f) {
    if (this instanceof Success(A value)) {
      return Try.success(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Failure<B> aFailure = (Failure<B>) this;
      return aFailure;
    }
  }

  default <B> Try<B> mapTry(ThrowingFunction<? super A, ? extends B> f) {
    if (this instanceof Success(A value)) {
      return Try.of(() -> f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Failure<B> failure = (Failure<B>) this;
      return failure;
    }
  }

  default <B> Try<B> flatMap(Function<? super A, ? extends Try<? extends B>> f) {
    if (this instanceof Success(A value)) {
      @SuppressWarnings("unchecked")
      Try<B> aTry = (Try<B>) f.apply(value);
      return aTry;
    } else {
      @SuppressWarnings("unchecked")
      Try<B> aTry = (Try<B>) this;
      return aTry;
    }
  }

  default <B> B fold(
      Function<? super A, ? extends B> success, Function<? super Throwable, ? extends B> failure) {
    switch (this) {
      case Success(A value) -> {
        return success.apply(value);
      }
      case Failure(Throwable t) -> {
        return failure.apply(t);
      }
    }
  }

  default Try<A> recover(Function<? super Throwable, ? extends A> f) {
    return (this instanceof Failure(Throwable t)) ? Try.success(f.apply(t)) : this;
  }

  default Try<A> recoverWith(Function<? super Throwable, ? extends Try<? extends A>> f) {
    if (this instanceof Failure(Throwable t)) {
      @SuppressWarnings("unchecked")
      Try<A> recovered = (Try<A>) f.apply(t);
      return recovered;
    } else {
      return this;
    }
  }

  default Try<A> filter(Predicate<? super A> f) {
    switch (this) {
      case Success(A value) -> {
        return f.test(value)
            ? this
            : Try.failure(new NoSuchElementException("Predicate does not hold for " + value));
      }
      case Failure<A> failure -> {
        return failure;
      }
    }
  }

  default Try<A> filterNot(Predicate<? super A> f) {
    return filter(f.negate());
  }

  default Try<A> tap(Consumer<? super A> f) {
    if (this instanceof Success(A value)) {
      f.accept(value);
    }
    return this;
  }

  default Try<A> tapFailure(Consumer<? super Throwable> f) {
    if (this instanceof Failure(Throwable t)) {
      f.accept(t);
    }
    return this;
  }

  default Try<A> tapBoth(Consumer<? super A> fa, Consumer<? super Throwable> ft) {
    return tap(fa).tapFailure(ft);
  }

  // conversion methods

  default Either<Throwable, A> toEither() {
    return fold(Either::right, Either::left);
  }

  default Option<A> toOption() {
    return fold(Option::some, _ -> Option.none());
  }

  default Optional<A> toOptional() {
    return fold(Optional::of, _ -> Optional.empty());
  }

  default List<A> toList() {
    return fold(List::of, _ -> List.of());
  }
}

interface TryOps {
  static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
    @SuppressWarnings("unchecked")
    T t1 = (T) t;
    throw t1;
  }
}
