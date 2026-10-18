package org.kleinb.jfun;

import java.util.Objects;

/**
 * A function of seven arguments. Adds partial application, currying, tupling and composition
 * helpers on top of the plain {@code apply} method.
 *
 * <pre>{@code
 * Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> sum = (a1, a2, a3, a4, a5, a6, a7) -> a1 + a2 + a3 + a4 + a5 + a6 + a7;
 * var curried = sum.curried();
 * }</pre>
 *
 * @param <A> the type of the first argument
 * @param <B> the type of the second argument
 * @param <C> the type of the third argument
 * @param <D> the type of the fourth argument
 * @param <E> the type of the fifth argument
 * @param <F> the type of the sixth argument
 * @param <G> the type of the seventh argument
 * @param <Z> the result type
 */
@FunctionalInterface
public interface Function7<A, B, C, D, E, F, G, Z> {

  /**
   * Returns a function that ignores its arguments and always returns the given value.
   *
   * @param value the value to return
   * @param <A> the type of the (ignored) first argument
   * @param <B> the type of the (ignored) second argument
   * @param <C> the type of the (ignored) third argument
   * @param <D> the type of the (ignored) fourth argument
   * @param <E> the type of the (ignored) fifth argument
   * @param <F> the type of the (ignored) sixth argument
   * @param <G> the type of the (ignored) seventh argument
   * @param <Z> the result type
   * @return a function that always returns {@code value}
   */
  static <A, B, C, D, E, F, G, Z> Function7<A, B, C, D, E, F, G, Z> constant(Z value) {
    return (_, _, _, _, _, _, _) -> value;
  }

  /**
   * Applies this function to the given arguments.
   *
   * @param a the first argument
   * @param b the second argument
   * @param c the third argument
   * @param d the fourth argument
   * @param e the fifth argument
   * @param f the sixth argument
   * @param g the seventh argument
   * @return the result of applying this function
   */
  Z apply(A a, B b, C c, D d, E e, F f, G g);

  /**
   * Partially applies the first argument, returning a function of the remaining 6 arguments.
   *
   * @param a the first argument
   * @return a function taking the remaining arguments
   */
  default Function6<B, C, D, E, F, G, Z> apply(A a) {
    return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
  }

  /**
   * Returns a function with the argument order reversed.
   *
   * @return a function taking the arguments in reverse order
   */
  default Function7<G, F, E, D, C, B, A, Z> reversed() {
    return (g, f, e, d, c, b, a) -> apply(a, b, c, d, e, f, g);
  }

  /**
   * Returns the curried form of this function, a chain of single-argument functions.
   *
   * @return the curried function
   */
  default Function1<
          A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Z>>>>>>>
      curried() {
    return a -> b -> c -> d -> e -> f -> g -> apply(a, b, c, d, e, f, g);
  }

  /**
   * Returns a function that takes its arguments wrapped in a {@link Tuple7}.
   *
   * @return a function taking a {@code Tuple7} of the arguments
   */
  default Function1<Tuple7<A, B, C, D, E, F, G>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7());
    };
  }

  /**
   * Returns a function that applies this function first, then {@code after}.
   *
   * @param after the function applied to the result of this function
   * @param <I> the result type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code after} is {@code null}
   */
  default <I> Function7<A, B, C, D, E, F, G, I> andThen(Function1<? super Z, ? extends I> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d, e, f, g) -> after.apply(apply(a, b, c, d, e, f, g));
  }

  /**
   * Returns a function that applies {@code before} to the first argument, then this function.
   *
   * @param before the function applied to the first argument before this function
   * @param <I> the first argument type of the composed function
   * @return the composed function
   * @throws NullPointerException if {@code before} is {@code null}
   */
  default <I> Function7<I, B, C, D, E, F, G, Z> compose(Function1<? super I, ? extends A> before) {
    Objects.requireNonNull(before);
    return (i, b, c, d, e, f, g) -> apply(before.apply(i), b, c, d, e, f, g);
  }
}
