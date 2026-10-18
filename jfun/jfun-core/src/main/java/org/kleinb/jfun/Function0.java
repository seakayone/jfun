package org.kleinb.jfun;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A function of no arguments. Extends {@link Supplier} and offers the same helpers as the other
 * {@code Function} types, which are trivial or degenerate for zero arguments.
 *
 * <pre>{@code
 * Function0<Integer> answer = () -> 42;
 * Function0<String> text = answer.andThen(i -> "value: " + i);
 * text.apply(); // "value: 42"
 * }</pre>
 *
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function0<Z> extends Supplier<Z> {

  /**
   * Returns a function that always returns the given value.
   *
   * @param value the value to return
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <Z> Function0<Z> constant(Z value) {
    return () -> value;
  }

  /**
   * Computes the result of this function.
   *
   * @return the result
   */
  Z apply();

  /**
   * Computes the result of this function; equivalent to {@link #apply()}.
   *
   * @return the result
   */
  @Override
  default Z get() {
    return apply();
  }

  /**
   * Returns this function; reversing the arguments of a function without arguments is a no-op.
   *
   * @return this function
   */
  default Function0<Z> reversed() {
    return this;
  }

  /**
   * Returns this function; currying a function without arguments is a no-op.
   *
   * @return this function
   */
  default Function0<Z> curried() {
    return this;
  }

  /**
   * Returns a function that takes an empty {@link Tuple0} and ignores it.
   *
   * @param <A> an unused type parameter
   * @return a function taking a {@code Tuple0}
   */
  default <A> Function1<Tuple0, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply();
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <B> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <B> Function0<B> andThen(Function1<? super Z, ? extends B> after) {
    Objects.requireNonNull(after);
    return () -> after.apply(apply());
  }
}
