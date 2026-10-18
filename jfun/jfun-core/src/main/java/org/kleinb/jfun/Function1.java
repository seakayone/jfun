package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of one argument. Extends {@link java.util.function.Function} and adds currying,
 * tupling and composition helpers that are shared by the {@code Function0} to {@code Function7}
 * family.
 *
 * <pre>{@code
 * Function1<Integer, Integer> inc = i -> i + 1;
 * Function1<Integer, String> show = inc.andThen(i -> "value: " + i);
 * show.apply(41); // "value: 42"
 * }</pre>
 *
 * @param <A> the argument type
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function1<A, Z> extends java.util.function.Function<A, Z> {

  /**
   * Returns the identity function.
   *
   * @param <A> the argument and result type
   * @return a function that returns its argument unchanged
   */
  static <A> Function1<A, A> identity() {
    return a -> a;
  }

  /**
   * Returns a function that ignores its argument and always returns the given value.
   *
   * @param c the value to return
   * @param <A> the (ignored) argument type
   * @param <C> the result type
   * @return a function that always returns {@code c}
   */
  static <A, C> Function1<A, C> constant(C c) {
    return ignored -> c;
  }

  /**
   * Converts a method reference or lambda to a {@code Function1}.
   *
   * @param methodReference the method reference or lambda
   * @param <A> the argument type
   * @param <Z> the result type
   * @return the given function, typed as {@code Function1}
   */
  static <A, Z> Function1<A, Z> of(Function1<A, Z> methodReference) {
    return methodReference;
  }

  /**
   * Applies this function to the given argument.
   *
   * @param b the argument
   * @return the result of applying this function to {@code b}
   */
  @Override
  Z apply(A b);

  /**
   * Returns this function; reversing the arguments of a single-argument function is a no-op.
   *
   * @return this function
   */
  default Function1<A, Z> reversed() {
    return this;
  }

  /**
   * Returns this function; currying a single-argument function is a no-op.
   *
   * @return this function
   */
  default Function1<A, Z> curried() {
    return this;
  }

  /**
   * Returns a function that takes its argument wrapped in a {@link Tuple1}.
   *
   * @return a function taking a {@code Tuple1} of the argument
   */
  default Function1<Tuple1<A>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1());
    };
  }

  /**
   * Returns a function that applies {@code before} first, then this function.
   *
   * @param before the function applied to the argument before this function
   * @param <C> the argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <C> Function1<C, Z> compose(Function1<? super C, ? extends A> before) {
    Objects.requireNonNull(before);
    return c -> apply(before.apply(c));
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <C> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <C> Function1<A, C> andThen(Function1<? super Z, ? extends C> after) {
    Objects.requireNonNull(after);
    return b -> after.apply(apply(b));
  }
}
