package org.kleinb.jfun;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The result of a computation that either succeeded with a value ({@link Success}) or failed with a
 * {@link Throwable} ({@link Failure}). Exceptions thrown inside {@link #of} and its variants are
 * captured instead of propagated.
 *
 * <pre>{@code
 * int port = Try.of(() -> Integer.parseInt(text)).getOrElse(8080);
 * }</pre>
 *
 * @param <A> the type of the value
 */
public sealed interface Try<A> extends Iterable<A> permits Try.Failure, Try.Success {
  /**
   * The success case of a {@link Try}.
   *
   * @param value the computed value
   * @param <A> the type of the value
   */
  record Success<A>(A value) implements Try<A> {}

  /**
   * The failure case of a {@link Try}.
   *
   * @param error the captured throwable
   * @param <A> the type of the (absent) value
   */
  record Failure<A>(Throwable error) implements Try<A> {}

  /**
   * Runs the given supplier and captures its outcome. Any {@link Throwable} thrown, including
   * {@link Error}s, results in a {@code Failure}.
   *
   * @param supplier the computation to run
   * @param <A> the type of the value
   * @return a {@code Success} holding the result, or a {@code Failure} holding the thrown throwable
   * @throws NullPointerException if {@code supplier} is {@code null}
   */
  static <A> Try<A> of(ThrowingSupplier<? extends A> supplier) {
    Objects.requireNonNull(supplier);
    try {
      return success(supplier.get());
    } catch (Throwable t) {
      return Try.failure(t);
    }
  }

  /**
   * Runs the given {@link Supplier} and captures its outcome.
   *
   * @param supplier the computation to run
   * @param <A> the type of the value
   * @return a {@code Success} holding the result, or a {@code Failure} holding the thrown throwable
   * @throws NullPointerException if {@code supplier} is {@code null}
   */
  static <A> Try<A> ofSupplier(Supplier<? extends A> supplier) {
    Objects.requireNonNull(supplier);
    return of(supplier::get);
  }

  /**
   * Runs the given {@link Callable} and captures its outcome.
   *
   * @param callable the computation to run
   * @param <A> the type of the value
   * @return a {@code Success} holding the result, or a {@code Failure} holding the thrown throwable
   * @throws NullPointerException if {@code callable} is {@code null}
   */
  static <A> Try<A> ofCallable(Callable<? extends A> callable) {
    Objects.requireNonNull(callable);
    return of(callable::call);
  }

  /**
   * Runs the given {@link Runnable} and captures its outcome.
   *
   * @param runnable the action to run
   * @return a {@code Success} holding {@code null} if the action completes, or a {@code Failure}
   *     holding the thrown throwable
   * @throws NullPointerException if {@code runnable} is {@code null}
   */
  static Try<Void> ofRunnable(Runnable runnable) {
    Objects.requireNonNull(runnable);
    return of(
        () -> {
          runnable.run();
          return null;
        });
  }

  /**
   * Creates a {@code Success} holding the given value.
   *
   * @param value the value to wrap
   * @param <A> the type of the value
   * @return a {@code Success} containing {@code value}
   */
  static <A> Try<A> success(A value) {
    return new Success<>(value);
  }

  /**
   * Creates a {@code Failure} holding the given throwable.
   *
   * @param t the throwable to wrap
   * @param <A> the type of the (absent) value
   * @return a {@code Failure} containing {@code t}
   * @throws NullPointerException if {@code t} is {@code null}
   */
  static <A> Try<A> failure(Throwable t) {
    Objects.requireNonNull(t);
    return new Failure<>(t);
  }

  /**
   * Widens a {@code Try<? extends A>} to a {@code Try<A>}. This is safe because {@code Try} is
   * immutable.
   *
   * @param t the try to widen
   * @param <A> the target type
   * @return the same instance, viewed as {@code Try<A>}
   * @throws NullPointerException if {@code t} is {@code null}
   */
  static <A> Try<A> narrow(Try<? extends A> t) {
    Objects.requireNonNull(t);
    @SuppressWarnings("unchecked")
    Try<A> narrowed = (Try<A>) t;
    return narrowed;
  }

  /**
   * Checks whether this is a {@code Success}.
   *
   * @return {@code true} if this is a {@code Success}, {@code false} otherwise
   */
  default boolean isSuccess() {
    return this instanceof Success;
  }

  /**
   * Checks whether this is a {@code Failure}.
   *
   * @return {@code true} if this is a {@code Failure}, {@code false} otherwise
   */
  default boolean isFailure() {
    return this instanceof Failure;
  }

  /**
   * Returns the value of a {@code Success}. For a {@code Failure}, the captured throwable is
   * rethrown as is, even if it is a checked exception ("sneaky throw").
   *
   * @return the value
   */
  default A get() {
    if (this instanceof Success(A value)) {
      return value;
    } else {
      return TryOps.sneakyThrow(getFailure());
    }
  }

  /**
   * Returns the value, or a fallback if this is a {@code Failure}.
   *
   * @param or the fallback value
   * @return the value, or {@code or} for a {@code Failure}
   */
  default A getOrElse(A or) {
    return isSuccess() ? get() : or;
  }

  /**
   * Returns the captured throwable.
   *
   * @return the throwable of a {@code Failure}
   * @throws NoSuchElementException if this is a {@code Success}
   */
  default Throwable getFailure() {
    if (this instanceof Failure(Throwable t)) {
      return t;
    } else {
      throw new NoSuchElementException("getFailure called on Success");
    }
  }

  /**
   * Returns this if it is a {@code Success}, otherwise the lazily evaluated alternative.
   *
   * @param or supplies the alternative; only invoked for a {@code Failure}
   * @return this for a {@code Success}, or the result of {@code or}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default Try<A> orElse(Supplier<? extends Try<? extends A>> or) {
    Objects.requireNonNull(or);
    if (this instanceof Success<A> success) {
      return success;
    } else {
      return narrow(or.get());
    }
  }

  /**
   * Checks whether this is a {@code Success} whose value equals the given value.
   *
   * @param value the value to look for
   * @return {@code true} if this is a {@code Success} and its value equals {@code value}; always
   *     {@code false} for a {@code Failure}
   */
  default boolean contains(A value) {
    return exists(a -> Objects.equals(a, value));
  }

  /**
   * Checks whether this is a {@code Success} whose value satisfies the given predicate.
   *
   * @param p the predicate to test the value with
   * @return {@code true} if this is a {@code Success} and its value satisfies {@code p}; always
   *     {@code false} for a {@code Failure}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return (this instanceof Success(A value)) && p.test(value);
  }

  /**
   * Applies a function to the value of a {@code Success}. Exceptions thrown by {@code f} are not
   * captured; use {@link #mapTry} for that. A {@code null} result is wrapped as {@code
   * Success(null)}.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the mapped value
   * @return a {@code Success} holding the result of {@code f}, or this {@code Failure} unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> Try<B> map(Function<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Success(A value)) {
      return Try.success(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Failure<B> aFailure = (Failure<B>) this;
      return aFailure;
    }
  }

  /**
   * Applies a function to the value of a {@code Success}, capturing any throwable it throws.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the mapped value
   * @return a {@code Success} holding the result of {@code f}, a {@code Failure} holding the
   *     throwable thrown by {@code f}, or this {@code Failure} unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> Try<B> mapTry(ThrowingFunction<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Success(A value)) {
      return Try.of(() -> f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Failure<B> failure = (Failure<B>) this;
      return failure;
    }
  }

  /**
   * Applies a try-returning function to the value of a {@code Success} and flattens the result.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the value of the result
   * @return the result of {@code f} for a {@code Success}, or this {@code Failure} unchanged
   * @throws NullPointerException if {@code f} is {@code null} or returns {@code null}
   */
  default <B> Try<B> flatMap(Function<? super A, ? extends Try<? extends B>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Success(A value)) {
      return narrow(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Try<B> aTry = (Try<B>) this;
      return aTry;
    }
  }

  /**
   * Reduces this try to a single value by applying the function matching its case.
   *
   * @param ifFailure the function applied to the throwable of a {@code Failure}
   * @param ifSuccess the function applied to the value of a {@code Success}
   * @param <B> the result type
   * @return the result of {@code ifFailure} for a {@code Failure}, or of {@code ifSuccess} for a
   *     {@code Success}
   * @throws NullPointerException if {@code ifFailure} or {@code ifSuccess} is {@code null}
   */
  default <B> B fold(
      Function<? super Throwable, ? extends B> ifFailure,
      Function<? super A, ? extends B> ifSuccess) {
    Objects.requireNonNull(ifSuccess);
    Objects.requireNonNull(ifFailure);
    switch (this) {
      case Success(A value) -> {
        return ifSuccess.apply(value);
      }
      case Failure(Throwable t) -> {
        return ifFailure.apply(t);
      }
    }
  }

  /**
   * Maps a {@code Failure} to a {@code Success}.
   *
   * @param f the function mapping the throwable to a replacement value
   * @return a {@code Success} holding the result of {@code f} for a {@code Failure}, otherwise this
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Try<A> recover(Function<? super Throwable, ? extends A> f) {
    Objects.requireNonNull(f);
    return (this instanceof Failure(Throwable t)) ? Try.success(f.apply(t)) : this;
  }

  /**
   * Maps a {@code Failure} to another {@code Try}.
   *
   * @param f the function mapping the throwable to a replacement {@code Try}
   * @return the result of {@code f} for a {@code Failure}, otherwise this
   * @throws NullPointerException if {@code f} is {@code null} or returns {@code null}
   */
  default Try<A> recoverWith(Function<? super Throwable, ? extends Try<? extends A>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Failure(Throwable t)) {
      return narrow(f.apply(t));
    } else {
      return this;
    }
  }

  /**
   * Keeps the value only if it satisfies the predicate.
   *
   * @param p the predicate to test the value with
   * @return this if it is a {@code Success} whose value satisfies {@code p}; a {@code Failure}
   *     holding a {@link NoSuchElementException} if it does not; this {@code Failure} unchanged
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default Try<A> filter(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    switch (this) {
      case Success(A value) -> {
        return p.test(value)
            ? this
            : Try.failure(new NoSuchElementException("Predicate does not hold for " + value));
      }
      case Failure<A> failure -> {
        return failure;
      }
    }
  }

  /**
   * Keeps the value only if it does not satisfy the predicate.
   *
   * @param p the predicate to test the value with
   * @return this if it is a {@code Success} whose value does not satisfy {@code p}; a {@code
   *     Failure} holding a {@link NoSuchElementException} if it does; this {@code Failure}
   *     unchanged
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default Try<A> filterNot(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return filter(p.negate());
  }

  /**
   * Runs a side effect on the value of a {@code Success}.
   *
   * @param f the consumer to run if this is a {@code Success}
   * @return this try, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Try<A> tap(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Success(A value)) {
      f.accept(value);
    }
    return this;
  }

  /**
   * Runs a side effect on the throwable of a {@code Failure}.
   *
   * @param f the consumer to run if this is a {@code Failure}
   * @return this try, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Try<A> tapFailure(Consumer<? super Throwable> f) {
    Objects.requireNonNull(f);
    if (this instanceof Failure(Throwable t)) {
      f.accept(t);
    }
    return this;
  }

  /**
   * Runs the side effect matching the case of this try.
   *
   * @param ifFailure the consumer to run if this is a {@code Failure}
   * @param ifSuccess the consumer to run if this is a {@code Success}
   * @return this try, unchanged
   * @throws NullPointerException if {@code ifFailure} or {@code ifSuccess} is {@code null}
   */
  default Try<A> tapBoth(Consumer<? super Throwable> ifFailure, Consumer<? super A> ifSuccess) {
    Objects.requireNonNull(ifSuccess);
    Objects.requireNonNull(ifFailure);
    return tap(ifSuccess).tapFailure(ifFailure);
  }

  // conversion methods

  /**
   * Converts to an {@link Either}.
   *
   * @return a {@code Right} holding the value, or a {@code Left} holding the throwable
   */
  default Either<Throwable, A> toEither() {
    return fold(Either::left, Either::right);
  }

  /**
   * Converts to an {@link Option}.
   *
   * @return a {@code Some} holding the value, or {@code None} for a {@code Failure}
   */
  default Option<A> toOption() {
    return fold(_ -> Option.none(), Option::some);
  }

  /**
   * Converts to a {@link Validation}.
   *
   * @return a valid {@code Validation} holding the value, or an invalid one holding the throwable
   */
  default Validation<Throwable, A> toValidation() {
    return fold(Validation::invalid, Validation::valid);
  }

  /**
   * Converts to a {@link java.util.Optional}.
   *
   * @return an {@code Optional} holding the value, or an empty one for a {@code Failure}. As an
   *     {@code Optional} cannot hold {@code null}, a {@code Success} holding {@code null} also
   *     yields an empty one.
   */
  default Optional<A> toOptional() {
    return fold(_ -> Optional.empty(), Optional::ofNullable);
  }

  /**
   * Converts to a {@link java.util.List}.
   *
   * @return an immutable single-element list for a {@code Success}, or an empty list for a {@code
   *     Failure}. A {@code Success} holding {@code null} yields a list containing {@code null}.
   */
  default List<A> toList() {
    return fold(_ -> List.of(), Collections::singletonList);
  }

  /**
   * Iterates over the value.
   *
   * @return an iterator over the single value of a {@code Success}, or an empty iterator for a
   *     {@code Failure}
   */
  @Override
  default Iterator<A> iterator() {
    return fold(_ -> Iterator.empty(), Iterator::of);
  }
}

interface TryOps {
  static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
    @SuppressWarnings("unchecked")
    T t1 = (T) t;
    throw t1;
  }
}
