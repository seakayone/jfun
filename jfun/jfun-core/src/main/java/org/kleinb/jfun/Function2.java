package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of two arguments. Adds partial application, currying, tupling and composition helpers
 * on top of the plain {@code apply} method.
 *
 * <pre>{@code
 * Function2<Integer, Integer, Integer> sum = (a1, a2) -> a1 + a2;
 * var curried = sum.curried();
 * }</pre>
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function2<A, B, Z> extends java.util.function.BiFunction<A, B, Z> {

  /**
   * Returns a function that ignores its arguments and always returns the given value.
   *
   * @param value the value to return
   * @param <A> the type of the (ignored) first argument
   * @param <B> the type of the (ignored) second argument
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <A, B, Z> Function2<A, B, Z> constant(Z value) {
    return (ignored1, ignored2) -> value;
  }

  /**
   * Converts a method reference or lambda to a {@code Function2}.
   *
   * @param methodReference the method reference or lambda
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param <Z> the result type
   * @return the given function, typed as {@code Function2}
   */
  static <A, B, Z> Function2<A, B, Z> of(Function2<A, B, Z> methodReference) {
    return methodReference;
  }

  /**
   * Creates a two-argument function from a curried one.
   *
   * @param f the curried function
   * @param <A> the type of the first argument
   * @param <B> the type of the second argument
   * @param <Z> the result type
   * @return the uncurried function
   */
  static <A, B, Z> Function2<A, B, Z> of(Function1<A, Function1<B, Z>> f) {
    return (a, b) -> f.apply(a).apply(b);
  }

  /**
   * Applies this function to the given arguments.
   *
   * @param a the first argument
   * @param b the second argument
   * @return the result of applying this function
   */
  @Override
  Z apply(A a, B b);

  /**
   * Partially applies the first argument, returning a function of the remaining 1 argument.
   *
   * @param a the first argument
   * @return a function taking the remaining arguments
   */
  default Function1<B, Z> apply(A a) {
    return b -> apply(a, b);
  }

  /**
   * Returns a function with the argument order reversed.
   *
   * @return a function taking the arguments in reverse order
   */
  default Function2<B, A, Z> reversed() {
    return (b, a) -> apply(a, b);
  }

  /**
   * Returns the curried form of this function, a chain of single-argument functions.
   *
   * @return the curried function
   */
  default Function1<A, Function1<B, Z>> curried() {
    return a -> b -> apply(a, b);
  }

  /**
   * Returns a function that takes its arguments wrapped in a {@link Tuple2}.
   *
   * @return a function taking a {@code Tuple2} of the arguments
   */
  default Function1<Tuple2<A, B>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2());
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <D> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <D> Function2<A, B, D> andThen(Function1<? super Z, ? extends D> after) {
    Objects.requireNonNull(after);
    return (a, b) -> after.apply(apply(a, b));
  }

  /**
   * Returns a function that applies {@code before} to the first argument, then this function.
   *
   * @param before the function applied to the first argument before this function
   * @param <D> the first argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <D> Function2<D, B, Z> compose(Function1<? super D, ? extends A> before) {
    Objects.requireNonNull(before);
    return (d, b) -> apply(before.apply(d), b);
  }
}
