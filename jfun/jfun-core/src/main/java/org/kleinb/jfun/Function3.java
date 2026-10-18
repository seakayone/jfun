package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of three arguments. Adds partial application, currying, tupling and composition
 * helpers on top of the plain {@code apply} method.
 *
 * <pre>{@code
 * Function3<Integer, Integer, Integer, Integer> sum = (a1, a2, a3) -> a1 + a2 + a3;
 * var curried = sum.curried();
 * }</pre>
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function3<A, B, C, Z> {

  /**
   * Returns a function that ignores its arguments and always returns the given value.
   *
   * @param value the value to return
   * @param <A> the type of the (ignored) first argument
   * @param <B> the type of the (ignored) second argument
   * @param <C> the type of the (ignored) third argument
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <A, B, C, Z> Function3<A, B, C, Z> constant(Z value) {
    return (ignored1, ignored2, ignored3) -> value;
  }

  /**
   * Applies this function to the given arguments.
   *
   * @param a the first argument
   * @param b the second argument
   * @param c the third argument
   * @return the result of applying this function
   */
  Z apply(A a, B b, C c);

  /**
   * Partially applies the first argument, returning a function of the remaining 2 arguments.
   *
   * @param a the first argument
   * @return a function taking the remaining arguments
   */
  default Function2<B, C, Z> apply(A a) {
    return (b, c) -> apply(a, b, c);
  }

  /**
   * Returns a function with the argument order reversed.
   *
   * @return a function taking the arguments in reverse order
   */
  default Function3<C, B, A, Z> reversed() {
    return (c, b, a) -> apply(a, b, c);
  }

  /**
   * Returns the curried form of this function, a chain of single-argument functions.
   *
   * @return the curried function
   */
  default Function1<A, Function1<B, Function1<C, Z>>> curried() {
    return a -> b -> c -> apply(a, b, c);
  }

  /**
   * Returns a function that takes its arguments wrapped in a {@link Tuple3}.
   *
   * @return a function taking a {@code Tuple3} of the arguments
   */
  default Function1<Tuple3<A, B, C>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3());
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <E> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <E> Function3<A, B, C, E> andThen(Function1<? super Z, ? extends E> after) {
    Objects.requireNonNull(after);
    return (a, b, c) -> after.apply(apply(a, b, c));
  }

  /**
   * Returns a function that applies {@code before} to the first argument, then this function.
   *
   * @param before the function applied to the first argument before this function
   * @param <E> the first argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <E> Function3<E, B, C, Z> compose(Function1<? super E, ? extends A> before) {
    Objects.requireNonNull(before);
    return (e, b, c) -> apply(before.apply(e), b, c);
  }
}
