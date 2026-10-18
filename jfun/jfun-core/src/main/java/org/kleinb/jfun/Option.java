package org.kleinb.jfun;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A container for an optional value: a {@link Some} holds a value, {@link None} is the absence of
 * one. As a sealed interface it works with pattern matching.
 *
 * <p>Unlike {@link java.util.Optional}, {@code Option} allows {@code null} as a value: {@code
 * Option.some(null)} is a {@code Some} holding {@code null}, and {@link #map} wraps a {@code null}
 * result of its function in a {@code Some} instead of returning {@code None}. Use {@link #of} (or
 * {@code flatMap(a -> Option.of(f(a)))}) when {@code null} should mean "absent".
 *
 * <pre>{@code
 * Option<String> name = Option.of(user.nickname());
 * String greeting = name.map(n -> "Hello, " + n).getOrElse("Hello, stranger");
 * }</pre>
 *
 * @param <A> the type of the value
 */
public sealed interface Option<A> extends Iterable<A> permits Option.None, Option.Some {

  /**
   * The present case of an {@link Option}.
   *
   * @param value the wrapped value, which may be {@code null}
   * @param <A> the type of the value
   */
  record Some<A>(A value) implements Option<A> {}

  /**
   * The absent case of an {@link Option}. Use {@link Option#none()} to obtain the singleton.
   *
   * @param <A> the type of the (absent) value
   */
  record None<A>() implements Option<A> {
    static final None<?> INSTANCE = new None<>();
  }

  /**
   * Creates a {@code Some} holding the given value. {@code null} is allowed; use {@link #of} to map
   * {@code null} to {@code None}.
   *
   * @param value the value to wrap, which may be {@code null}
   * @param <A> the type of the value
   * @return a {@code Some} containing {@code value}
   */
  static <A> Option<A> some(A value) {
    return new Some<>(value);
  }

  /**
   * Returns {@code None}, ignoring the argument. This overload allows {@code Option::none} to be
   * used where a function taking a {@code Void} is expected.
   *
   * @param ignored ignored
   * @param <A> the type of the (absent) value
   * @return the {@code None} singleton
   */
  static <A> Option<A> none(Void ignored) {
    return none();
  }

  /**
   * Returns the {@code None} singleton.
   *
   * @param <A> the type of the (absent) value
   * @return {@code None}
   */
  static <A> Option<A> none() {
    @SuppressWarnings("unchecked")
    final var none = (None<A>) None.INSTANCE;
    return none;
  }

  /**
   * Wraps a possibly-{@code null} value: {@code None} for {@code null}, otherwise a {@code Some}.
   *
   * @param value the value to wrap, which may be {@code null}
   * @param <A> the type of the value
   * @return {@code None} if {@code value} is {@code null}, otherwise a {@code Some} containing it
   */
  static <A> Option<A> of(A value) {
    return value == null ? none() : some(value);
  }

  /**
   * Converts a {@link java.util.Optional} to an {@code Option}.
   *
   * @param optional the optional to convert
   * @param <A> the type of the value
   * @return a {@code Some} holding the value of {@code optional}, or {@code None} if it is empty
   * @throws NullPointerException if {@code optional} is {@code null}
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  static <A> Option<A> ofOptional(Optional<? extends A> optional) {
    Objects.requireNonNull(optional);
    return optional.<Option<A>>map(Option::some).orElseGet(Option::none);
  }

  /**
   * Turns an iterable of options into an option of a list.
   *
   * @param options the options to combine
   * @param <A> the type of the values
   * @return a {@code Some} holding an unmodifiable list of all values if every element is a {@code
   *     Some}, otherwise {@code None}
   * @throws NullPointerException if {@code options} is {@code null}
   */
  static <A> Option<List<A>> sequence(Iterable<? extends Option<? extends A>> options) {
    Objects.requireNonNull(options);
    var result = new ArrayList<A>();
    for (Option<? extends A> option : options) {
      if (option instanceof None) {
        return none();
      }
      result.add(option.get());
    }
    return some(Collections.unmodifiableList(result));
  }

  /**
   * Widens an {@code Option<? extends A>} to an {@code Option<A>}. This is safe because {@code
   * Option} is immutable.
   *
   * @param option the option to widen
   * @param <A> the target type
   * @return the same instance, viewed as {@code Option<A>}
   * @throws NullPointerException if {@code option} is {@code null}
   */
  static <A> Option<A> narrow(Option<? extends A> option) {
    Objects.requireNonNull(option);
    @SuppressWarnings("unchecked")
    final Option<A> narrowed = (Option<A>) option;
    return narrowed;
  }

  /**
   * Checks whether this is a {@code Some}.
   *
   * @return {@code true} if this is a {@code Some}, {@code false} otherwise
   */
  default boolean isSome() {
    return this instanceof Some;
  }

  /**
   * Checks whether this is {@code None}.
   *
   * @return {@code true} if this is {@code None}, {@code false} otherwise
   */
  default boolean isNone() {
    return this instanceof None;
  }

  /**
   * Checks whether this is a {@code Some} whose value equals the given value.
   *
   * @param value the value to look for
   * @return {@code true} if this is a {@code Some} and its value equals {@code value}; always
   *     {@code false} for {@code None}
   */
  default boolean contains(A value) {
    return exists(a -> Objects.equals(a, value));
  }

  /**
   * Checks whether this is a {@code Some} whose value satisfies the given predicate.
   *
   * @param p the predicate to test the value with
   * @return {@code true} if this is a {@code Some} and its value satisfies {@code p}; always {@code
   *     false} for {@code None}
   * @throws NullPointerException if {@code p} is {@code null}
   */
  default boolean exists(Predicate<? super A> p) {
    Objects.requireNonNull(p);
    return (this instanceof Some(A value)) && p.test(value);
  }

  /**
   * Returns the value.
   *
   * @return the value, which may be {@code null} for {@code Some(null)}
   * @throws NoSuchElementException if this is {@code None}
   */
  default A get() {
    if (this instanceof Some(A value)) {
      return value;
    } else {
      throw new NoSuchElementException("No element present");
    }
  }

  /**
   * Combines this option with another into an option of a {@link Tuple2}.
   *
   * @param that the other option
   * @param <B> the type of the other value
   * @return a {@code Some} holding both values if both options are a {@code Some}, otherwise {@code
   *     None}
   * @throws NullPointerException if {@code that} is {@code null}
   */
  default <B> Option<Tuple2<A, B>> zip(Option<B> that) {
    Objects.requireNonNull(that);
    return zipWith(that, Tuple::of);
  }

  /**
   * Combines this option with another using the given function.
   *
   * @param that the other option
   * @param f the function combining both values
   * @param <B> the type of the other value
   * @param <C> the type of the combined value
   * @return a {@code Some} holding the result of {@code f} if both options are a {@code Some},
   *     otherwise {@code None}
   * @throws NullPointerException if {@code that} or {@code f} is {@code null}
   */
  default <B, C> Option<C> zipWith(Option<B> that, Function2<A, B, C> f) {
    Objects.requireNonNull(that);
    Objects.requireNonNull(f);
    return flatMap(a -> that.map(b -> f.apply(a, b)));
  }

  /**
   * Applies an option-returning function to the value and flattens the result.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the value of the result
   * @return the result of {@code f} for a {@code Some}, or {@code None}
   * @throws NullPointerException if {@code f} is {@code null} or returns {@code null}
   */
  default <B> Option<B> flatMap(Function<? super A, ? extends Option<? extends B>> f) {
    Objects.requireNonNull(f);
    if (this instanceof Some(A value)) {
      return narrow(f.apply(value));
    } else {
      return none();
    }
  }

  /**
   * Applies a function to the value of a {@code Some}.
   *
   * <p>Note: if {@code f} returns {@code null}, the result is {@code Some(null)}, not {@code None}.
   * This differs from {@link java.util.Optional#map}. To treat a {@code null} result as absent, use
   * {@code flatMap(a -> Option.of(f.apply(a)))}.
   *
   * @param f the function to apply to the value
   * @param <B> the type of the mapped value
   * @return a {@code Some} holding the result of {@code f}, or {@code None} if this is {@code None}
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default <B> Option<B> map(Function<? super A, ? extends B> f) {
    Objects.requireNonNull(f);
    return (this instanceof Some(A value)) ? some(f.apply(value)) : none();
  }

  /**
   * Reduces this option to a single value by applying the function matching its case.
   *
   * @param ifNone supplies the result for {@code None}
   * @param ifSome the function applied to the value of a {@code Some}
   * @param <C> the result type
   * @return the result of {@code ifNone} for {@code None}, or of {@code ifSome} for a {@code Some}
   * @throws NullPointerException if {@code ifNone} or {@code ifSome} is {@code null}
   */
  default <C> C fold(Supplier<? extends C> ifNone, Function<? super A, ? extends C> ifSome) {
    Objects.requireNonNull(ifNone);
    Objects.requireNonNull(ifSome);
    return (this instanceof Some(A value)) ? ifSome.apply(value) : ifNone.get();
  }

  /**
   * Returns this if it is a {@code Some}, otherwise the lazily evaluated alternative.
   *
   * @param or supplies the alternative; only invoked for {@code None}
   * @return this for a {@code Some}, or the result of {@code or}
   * @throws NullPointerException if {@code or} is {@code null}
   */
  default Option<A> orElse(Supplier<? extends Option<? extends A>> or) {
    Objects.requireNonNull(or);
    if (this instanceof Some<A> some) {
      return some;
    } else {
      return narrow(or.get());
    }
  }

  /**
   * Returns the value, or {@code null} for {@code None}. Because {@code Some(null)} is allowed, a
   * {@code null} result does not imply that this is {@code None}.
   *
   * @return the value, or {@code null} if this is {@code None}
   */
  default A getOrNull() {
    return (this instanceof Some(A value)) ? value : null;
  }

  /**
   * Returns the value, or a fallback if this is {@code None}.
   *
   * @param a the fallback value
   * @return the value, or {@code a} for {@code None}
   */
  default A getOrElse(A a) {
    return (this instanceof Some(A value)) ? value : a;
  }

  /**
   * Returns the value, or a lazily evaluated fallback if this is {@code None}.
   *
   * @param a supplies the fallback value; only invoked for {@code None}
   * @return the value, or the result of {@code a} for {@code None}
   * @throws NullPointerException if {@code a} is {@code null}
   */
  default A getOrElse(Supplier<A> a) {
    Objects.requireNonNull(a);
    return (this instanceof Some(A value)) ? value : a.get();
  }

  /**
   * Keeps the value only if it satisfies the predicate.
   *
   * @param f the predicate to test the value with
   * @return this if it is a {@code Some} whose value satisfies {@code f}, otherwise {@code None}
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Option<A> filter(Predicate<? super A> f) {
    Objects.requireNonNull(f);
    return (this instanceof Some(A value) && f.test(value)) ? this : none();
  }

  /**
   * Keeps the value only if it does not satisfy the predicate.
   *
   * @param f the predicate to test the value with
   * @return this if it is a {@code Some} whose value does not satisfy {@code f}, otherwise {@code
   *     None}
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Option<A> filterNot(Predicate<? super A> f) {
    Objects.requireNonNull(f);
    return filter(f.negate());
  }

  /**
   * Runs a side effect on the value of a {@code Some}.
   *
   * @param f the consumer to run if this is a {@code Some}
   * @return this option, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Option<A> tap(Consumer<? super A> f) {
    Objects.requireNonNull(f);
    if (this instanceof Some(A value)) {
      f.accept(value);
    }
    return this;
  }

  /**
   * Runs a side effect if this is {@code None}.
   *
   * @param f the action to run if this is {@code None}
   * @return this option, unchanged
   * @throws NullPointerException if {@code f} is {@code null}
   */
  default Option<A> tapNone(Runnable f) {
    Objects.requireNonNull(f);
    if (this instanceof None) {
      f.run();
    }
    return this;
  }

  /**
   * Runs the side effect matching the case of this option.
   *
   * @param ifNone the action to run if this is {@code None}
   * @param ifSome the consumer to run if this is a {@code Some}
   * @return this option, unchanged
   * @throws NullPointerException if {@code ifNone} or {@code ifSome} is {@code null}
   */
  default Option<A> tapBoth(Runnable ifNone, Consumer<? super A> ifSome) {
    Objects.requireNonNull(ifNone);
    Objects.requireNonNull(ifSome);
    if (this instanceof Some(A value)) {
      ifSome.accept(value);
    } else {
      ifNone.run();
    }
    return this;
  }

  // Conversion methods

  /**
   * Converts to a {@link java.util.stream.Stream}.
   *
   * @return a single-element stream for a {@code Some}, or an empty stream for {@code None}
   */
  default Stream<A> toStream() {
    return fold(Stream::empty, Stream::of);
  }

  /**
   * Converts to a {@link java.util.List}.
   *
   * @return an immutable single-element list for a {@code Some}, or an empty list for {@code None}.
   *     A {@code Some} holding {@code null} yields a list containing {@code null}.
   */
  default List<A> toList() {
    return fold(List::of, Collections::singletonList);
  }

  /**
   * Converts to a {@link java.util.Optional}.
   *
   * @return an {@code Optional} holding the value, or an empty one for {@code None}. As an {@code
   *     Optional} cannot hold {@code null}, a {@code Some} holding {@code null} also yields an
   *     empty one, so the conversion cannot tell it from {@code None}.
   */
  default Optional<A> toOptional() {
    return fold(Optional::empty, Optional::ofNullable);
  }

  /**
   * Converts to an {@link Either}, using the given supplier for the {@code Left} if this is {@code
   * None}.
   *
   * @param left supplies the {@code Left} value; only invoked for {@code None}
   * @param <B> the type of the {@code Left} value
   * @return a {@code Right} holding the value, or a {@code Left} holding the result of {@code left}
   * @throws NullPointerException if {@code left} is {@code null}
   */
  default <B> Either<B, A> toRight(Supplier<? extends B> left) {
    Objects.requireNonNull(left);
    return fold(() -> Either.left(left.get()), Either::right);
  }

  /**
   * Converts to a {@link Try}.
   *
   * @return a successful {@code Try} holding the value, or a failed one holding a {@link
   *     NoSuchElementException} for {@code None}
   */
  default Try<A> toTry() {
    return fold(() -> Try.failure(new NoSuchElementException()), Try::success);
  }

  /**
   * Iterates over the value.
   *
   * @return an iterator over the single value of a {@code Some}, or an empty iterator for {@code
   *     None}
   */
  @Override
  default Iterator<A> iterator() {
    return fold(Iterator::empty, Iterator::of);
  }
}
