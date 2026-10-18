package org.kleinb.jfun;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A value of one of two possible types: a {@link Left} (conventionally the failure case) or a
 * {@link Right} (conventionally the success case).
 *
 * <p>{@code Either} is right-biased: {@link #map}, {@link #flatMap}, {@link #get} and the other
 * "happy path" operations act on the {@code Right} value and pass a {@code Left} through unchanged.
 *
 * <pre>{@code
 * Either<String, Integer> parsed = Either.right(21);
 * Either<String, Integer> doubled = parsed.map(i -> i * 2); // Right(42)
 * String text = doubled.fold(err -> "error: " + err, i -> "value: " + i); // "value: 42"
 * }</pre>
 *
 * @param <A> the type of the {@code Left} value
 * @param <B> the type of the {@code Right} value
 */
public sealed interface Either<A, B> extends Iterable<B> permits Either.Left, Either.Right {

  /**
   * The success case of an {@link Either}.
   *
   * @param value the wrapped value
   * @param <A> the type of the (absent) {@code Left} value
   * @param <B> the type of the {@code Right} value
   */
  record Right<A, B>(B value) implements Either<A, B> {}

  /**
   * The failure case of an {@link Either}.
   *
   * @param value the wrapped value
   * @param <A> the type of the {@code Left} value
   * @param <B> the type of the (absent) {@code Right} value
   */
  record Left<A, B>(A value) implements Either<A, B> {}

  /**
   * Creates a {@code Left} holding the given value.
   *
   * @param value the value to wrap
   * @param <A> the type of the {@code Left} value
   * @param <B> the type of the {@code Right} value
   * @return a {@code Left} containing {@code value}
   */
  static <A, B> Either<A, B> left(A value) {
    return new Left<>(value);
  }

  /**
   * Creates a {@code Right} holding the given value.
   *
   * @param value the value to wrap
   * @param <A> the type of the {@code Left} value
   * @param <B> the type of the {@code Right} value
   * @return a {@code Right} containing {@code value}
   */
  static <A, B> Either<A, B> right(B value) {
    return new Right<>(value);
  }

  /**
   * Widens an {@code Either<? extends A, ? extends B>} to an {@code Either<A, B>}. This is safe
   * because {@code Either} is immutable.
   *
   * @param either the either to widen
   * @param <A> the target {@code Left} type
   * @param <B> the target {@code Right} type
   * @return the same instance, viewed as {@code Either<A, B>}
   * @throws NullPointerException if {@code either} is {@code null}
   */
  static <A, B> Either<A, B> narrow(Either<? extends A, ? extends B> either) {
    Objects.requireNonNull(either);
    @SuppressWarnings("unchecked")
    final Either<A, B> narrowed = (Either<A, B>) either;
    return narrowed;
  }

  /**
   * Checks whether this is a {@code Left}.
   *
   * @return {@code true} if this is a {@code Left}, {@code false} otherwise
   */
  default boolean isLeft() {
    return this instanceof Left;
  }

  /**
   * Checks whether this is a {@code Right}.
   *
   * @return {@code true} if this is a {@code Right}, {@code false} otherwise
   */
  default boolean isRight() {
    return this instanceof Right;
  }

  /**
   * Turns a {@code Left} into a {@code Right} and the other way round.
   *
   * @return a {@code Right} holding the {@code Left} value, or a {@code Left} holding the {@code
   *     Right} value
   */
  default Either<B, A> swap() {
    switch (this) {
      case Left(A value) -> {
        return Either.right(value);
      }
      case Right(B value) -> {
        return Either.left(value);
      }
    }
  }

  /**
   * Checks whether this is a {@code Right} whose value equals the given element.
   *
   * @param elem the element to look for
   * @return {@code true} if this is a {@code Right} and its value equals {@code elem}; always
   *     {@code false} for a {@code Left}
   */
  default boolean contains(B elem) {
    return exists(b -> Objects.equals(b, elem));
  }

  /**
   * Checks whether this is a {@code Right} whose value satisfies the given predicate.
   *
   * @param p the predicate to test the {@code Right} value with
   * @return {@code true} if this is a {@code Right} and its value satisfies {@code p}; always
   *     {@code false} for a {@code Left}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default boolean exists(Predicate<? super B> p) {
    Objects.requireNonNull(p);
    return this instanceof Right(B value) && p.test(value);
  }

  /**
   * Reduces this either to a single value by applying the function matching its case.
   *
   * @param ifLeft the function applied to a {@code Left} value
   * @param ifRight the function applied to a {@code Right} value
   * @param <C> the result type
   * @return the result of {@code ifLeft} for a {@code Left}, or of {@code ifRight} for a {@code
   *     Right}
   * @throws NullPointerException if {@code ifLeft} or {@code ifRight} is {@code null}
   */
  default <C> C fold(
      Function<? super A, ? extends C> ifLeft, Function<? super B, ? extends C> ifRight) {
    Objects.requireNonNull(ifLeft);
    Objects.requireNonNull(ifRight);
    switch (this) {
      case Left(A value) -> {
        return ifLeft.apply(value);
      }
      case Right(B value) -> {
        return ifRight.apply(value);
      }
    }
  }

  /**
   * Applies a function to the {@code Right} value.
   *
   * <p>A {@code null} result of {@code f} is wrapped as {@code Right(null)}.
   *
   * @param f the function to apply to the {@code Right} value
   * @param <C> the type of the mapped {@code Right} value
   * @return a {@code Right} holding the result of {@code f}, or this {@code Left} unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <C> Either<A, C> map(Function<? super B, ? extends C> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      return Either.right(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Left<A, C> left = (Left<A, C>) this;
      return left;
    }
  }

  /**
   * Applies a function to the {@code Left} value.
   *
   * @param f the function to apply to the {@code Left} value
   * @param <C> the type of the mapped {@code Left} value
   * @return a {@code Left} holding the result of {@code f}, or this {@code Right} unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <C> Either<C, B> mapLeft(Function<? super A, ? extends C> f) {
    Objects.requireNonNull(f);
    if (this instanceof Left(A value)) {
      return Either.left(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Right<C, B> right = (Right<C, B>) this;
      return right;
    }
  }

  /**
   * Applies one of two functions depending on the case of this either.
   *
   * @param f the function applied to a {@code Left} value
   * @param g the function applied to a {@code Right} value
   * @param <C> the type of the mapped {@code Left} value
   * @param <D> the type of the mapped {@code Right} value
   * @return a {@code Left} holding the result of {@code f}, or a {@code Right} holding the result
   *     of {@code g}
   * @throws NullPointerException if {@code f} or {@code g} is {@code null}
   */
  default <C, D> Either<C, D> bimap(
      Function<? super A, ? extends C> f, Function<? super B, ? extends D> g) {
    Objects.requireNonNull(f);
    Objects.requireNonNull(g);
    return fold(a -> Either.left(f.apply(a)), b -> Either.right(g.apply(b)));
  }

  /**
   * Applies an either-returning function to the {@code Right} value and flattens the result.
   *
   * @param f the function to apply to the {@code Right} value
   * @param <C> the {@code Right} type of the result
   * @return the result of {@code f} for a {@code Right}, or this {@code Left} unchanged
   * @throws NullPointerException if {@code f} is {@code null}, or returns {@code null}
   */
  default <C> Either<A, C> flatMap(
      Function<? super B, ? extends Either<? extends A, ? extends C>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      return narrow(f.apply(value));
    } else {
      @SuppressWarnings("unchecked")
      Left<A, C> left = (Left<A, C>) this;
      return left;
    }
  }

  /**
   * Returns the {@code Right} value.
   *
   * @return the {@code Right} value
   * @throws NoSuchElementException if this is a {@code Left}
   */
  default B get() {
    switch (this) {
      case Right(B value) -> {
        return value;
      }
      case Left(A ignored) -> {
        throw new NoSuchElementException("get called on Left");
      }
    }
  }

  /**
   * Returns the {@code Left} value.
   *
   * @return the {@code Left} value
   * @throws NoSuchElementException if this is a {@code Right}
   */
  default A getLeft() {
    switch (this) {
      case Left(A value) -> {
        return value;
      }
      case Right(B ignored) -> {
        throw new NoSuchElementException("getLeft called on Right");
      }
    }
  }

  /**
   * Returns the {@code Right} value, or a fallback if this is a {@code Left}.
   *
   * @param or the fallback value
   * @return the {@code Right} value, or {@code or} for a {@code Left}
   */
  default B getOrElse(B or) {
    return (this instanceof Right(B value)) ? value : or;
  }

  /**
   * Returns this if it is a {@code Right}, otherwise the lazily evaluated alternative.
   *
   * @param or supplies the alternative; only invoked for a {@code Left}
   * @return this for a {@code Right}, or the result of {@code or}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default Either<A, B> orElse(Supplier<? extends Either<? extends A, ? extends B>> or) {
    Objects.requireNonNull(or);
    if (isRight()) {
      return this;
    } else {
      return narrow(or.get());
    }
  }

  /**
   * Converts to an {@link Option}, keeping the {@code Right} value only if it satisfies the
   * predicate.
   *
   * @param p the predicate to test the {@code Right} value with
   * @return a {@code Some} holding the {@code Right} value if it satisfies {@code p}, otherwise a
   *     {@code None}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default Option<B> filterToOption(Predicate<? super B> p) {
    Objects.requireNonNull(p);
    return (this instanceof Right(B value) && p.test(value)) ? Option.some(value) : Option.none();
  }

  /**
   * Returns the {@code Right} value if it satisfies the predicate, otherwise a fallback. Note that
   * this returns a plain {@code B}, not an {@code Either}.
   *
   * @param p the predicate to test the {@code Right} value with
   * @param or the fallback value
   * @return the {@code Right} value if this is a {@code Right} satisfying {@code p}, otherwise
   *     {@code or}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default B filterOrElse(Predicate<? super B> p, B or) {
    Objects.requireNonNull(p);
    return (this instanceof Right(B value) && p.test(value)) ? value : or;
  }

  /**
   * Runs a side effect on the {@code Right} value.
   *
   * @param f the consumer to run if this is a {@code Right}
   * @return this either, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Either<A, B> tap(Consumer<? super B> f) {
    Objects.requireNonNull(f);
    if (this instanceof Right(B value)) {
      f.accept(value);
    }
    return this;
  }

  /**
   * Runs a side effect on the {@code Left} value.
   *
   * @param f the consumer to run if this is a {@code Left}
   * @return this either, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Either<A, B> tapLeft(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Left(A value)) {
      f.accept(value);
    }
    return this;
  }

  /**
   * Runs the side effect matching the case of this either.
   *
   * @param ifLeft the consumer to run if this is a {@code Left}
   * @param ifRight the consumer to run if this is a {@code Right}
   * @return this either, unchanged
   * @throws NullPointerException if {@code ifLeft} or {@code ifRight} is {@code null}
   */
  default Either<A, B> tapBoth(Consumer<? super A> ifLeft, Consumer<? super B> ifRight) {
    Objects.requireNonNull(ifLeft);
    Objects.requireNonNull(ifRight);
    return tapLeft(ifLeft).tap(ifRight);
  }

  // conversion methods

  /**
   * Converts to an {@link Option}.
   *
   * @return a {@code Some} holding the {@code Right} value, or a {@code None} for a {@code Left}
   */
  default Option<B> toOption() {
    return fold(ignored -> Option.none(), Option::some);
  }

  /**
   * Converts to a {@link java.util.Optional}.
   *
   * @return an {@code Optional} holding the {@code Right} value, or an empty one for a {@code
   *     Left}. As an {@code Optional} cannot hold {@code null}, a {@code Right} holding {@code
   *     null} also yields an empty one.
   */
  default Optional<B> toOptional() {
    return fold(ignored -> Optional.empty(), Optional::ofNullable);
  }

  /**
   * Converts to a {@link java.util.List}.
   *
   * @return a new mutable {@link ArrayList} holding the {@code Right} value, or no elements for a
   *     {@code Left}. A {@code Right} holding {@code null} yields a list containing {@code null}.
   */
  default List<B> toList() {
    final var result = new ArrayList<B>(1);
    forEach(result::add);
    return result;
  }

  /**
   * Converts to a {@link Try}.
   *
   * @param e maps the {@code Left} value to the exception of the failure
   * @return a successful {@code Try} holding the {@code Right} value, or a failed {@code Try}
   *     holding the exception produced by {@code e}
   * @throws NullPointerException if {@code e} is {@code null}
   */
  default Try<B> toTry(Function<? super A, ? extends Throwable> e) {
    Objects.requireNonNull(e);
    return fold(a -> Try.failure(e.apply(a)), Try::success);
  }

  /**
   * Converts to a {@link Validation}.
   *
   * @return a valid {@code Validation} holding the {@code Right} value, or an invalid one holding
   *     the {@code Left} value
   */
  default Validation<A, B> toValidation() {
    return fold(Validation::invalid, Validation::valid);
  }

  /**
   * Iterates over the {@code Right} value.
   *
   * @return an iterator over the single {@code Right} value, or an empty iterator for a {@code
   *     Left}
   */
  @Override
  default Iterator<B> iterator() {
    return fold(ignored -> Iterator.empty(), Iterator::of);
  }
}
