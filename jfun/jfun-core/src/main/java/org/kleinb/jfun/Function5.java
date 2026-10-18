package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of five arguments. Adds partial application, currying, tupling and composition helpers
 * on top of the plain {@code apply} method.
 *
 * <pre>{@code
 * Function5<Integer, Integer, Integer, Integer, Integer, Integer> sum = (a1, a2, a3, a4, a5) -> a1 + a2 + a3 + a4 + a5;
 * var curried = sum.curried();
 * }</pre>
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <D> the type of the fourth argument
 * @param <E> the type of the fifth argument
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function5<A, B, C, D, E, Z> {

  /**
   * Returns a function that ignores its arguments and always returns the given value.
   *
   * @param value the value to return
   * @param <A> the type of the (ignored) first argument
   * @param <B> the type of the (ignored) second argument
   * @param <C> the type of the (ignored) third argument
   * @param <D> the type of the (ignored) fourth argument
   * @param <E> the type of the (ignored) fifth argument
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <A, B, C, D, E, Z> Function5<A, B, C, D, E, Z> constant(Z value) {
    return (_, _, _, _, _) -> value;
  }

  /**
   * Applies this function to the given arguments.
   *
   * @param a the first argument
   * @param b the second argument
   * @param c the third argument
   * @param d the fourth argument
   * @param e the fifth argument
   * @return the result of applying this function
   */
  Z apply(A a, B b, C c, D d, E e);

  /**
   * Partially applies the first argument, returning a function of the remaining 4 arguments.
   *
   * @param a the first argument
   * @return a function taking the remaining arguments
   */
  default Function4<B, C, D, E, Z> apply(A a) {
    return (b, c, d, e) -> apply(a, b, c, d, e);
  }

  /**
   * Returns a function with the argument order reversed.
   *
   * @return a function taking the arguments in reverse order
   */
  default Function5<E, D, C, B, A, Z> reversed() {
    return (e, d, c, b, a) -> apply(a, b, c, d, e);
  }

  /**
   * Returns the curried form of this function, a chain of single-argument functions.
   *
   * @return the curried function
   */
  default Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Z>>>>> curried() {
    return a -> b -> c -> d -> e -> apply(a, b, c, d, e);
  }

  /**
   * Returns a function that takes its arguments wrapped in a {@link Tuple5}.
   *
   * @return a function taking a {@code Tuple5} of the arguments
   */
  default Function1<Tuple5<A, B, C, D, E>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3(), t._4(), t._5());
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <G> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <G> Function5<A, B, C, D, E, G> andThen(Function1<Z, G> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d, e) -> after.apply(apply(a, b, c, d, e));
  }

  /**
   * Returns a function that applies {@code before} to the first argument, then this function.
   *
   * @param before the function applied to the first argument before this function
   * @param <G> the first argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <G> Function5<G, B, C, D, E, Z> compose(Function1<G, A> before) {
    Objects.requireNonNull(before);
    return (g, b, c, d, e) -> apply(before.apply(g), b, c, d, e);
  }
}
