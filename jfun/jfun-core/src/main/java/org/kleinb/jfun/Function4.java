package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of four arguments. Adds partial application, currying, tupling and composition helpers
 * on top of the plain {@code apply} method.
 *
 * <pre>{@code
 * Function4<Integer, Integer, Integer, Integer, Integer> sum = (a1, a2, a3, a4) -> a1 + a2 + a3 + a4;
 * var curried = sum.curried();
 * }</pre>
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <D> the type of the fourth argument
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function4<A, B, C, D, Z> {

  /**
   * Returns a function that ignores its arguments and always returns the given value.
   *
   * @param value the value to return
   * @param <A> the type of the (ignored) first argument
   * @param <B> the type of the (ignored) second argument
   * @param <C> the type of the (ignored) third argument
   * @param <D> the type of the (ignored) fourth argument
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <A, B, C, D, Z> Function4<A, B, C, D, Z> constant(Z value) {
    return (_, _, _, _) -> value;
  }

  /**
   * Applies this function to the given arguments.
   *
   * @param a the first argument
   * @param b the second argument
   * @param c the third argument
   * @param d the fourth argument
   * @return the result of applying this function
   */
  Z apply(A a, B b, C c, D d);

  /**
   * Partially applies the first argument, returning a function of the remaining 3 arguments.
   *
   * @param a the first argument
   * @return a function taking the remaining arguments
   */
  default Function3<B, C, D, Z> apply(A a) {
    return (b, c, d) -> apply(a, b, c, d);
  }

  /**
   * Returns a function with the argument order reversed.
   *
   * @return a function taking the arguments in reverse order
   */
  default Function4<D, C, B, A, Z> reversed() {
    return (d, c, b, a) -> apply(a, b, c, d);
  }

  /**
   * Returns the curried form of this function, a chain of single-argument functions.
   *
   * @return the curried function
   */
  default Function1<A, Function1<B, Function1<C, Function1<D, Z>>>> curried() {
    return a -> b -> c -> d -> apply(a, b, c, d);
  }

  /**
   * Returns a function that takes its arguments wrapped in a {@link Tuple4}.
   *
   * @return a function taking a {@code Tuple4} of the arguments
   */
  default Function1<Tuple4<A, B, C, D>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3(), t._4());
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <F> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <F> Function4<A, B, C, D, F> andThen(Function1<? super Z, ? extends F> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d) -> after.apply(apply(a, b, c, d));
  }

  /**
   * Returns a function that applies {@code before} to the first argument, then this function.
   *
   * @param before the function applied to the first argument before this function
   * @param <F> the first argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <F> Function4<F, B, C, D, Z> compose(Function1<? super F, ? extends A> before) {
    Objects.requireNonNull(before);
    return (f, b, c, d) -> apply(before.apply(f), b, c, d);
  }
}
