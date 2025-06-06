package org.kleinb.jfun;

import java.util.Objects;

@FunctionalInterface
public interface Function7<A, B, C, D, E, F, G, Z> {

  static <A, B, C, D, E, F, G, Z> Function7<A, B, C, D, E, F, G, Z> constant(Z value) {
    return (_, _, _, _, _, _, _) -> value;
  }

  Z apply(A a, B b, C c, D d, E e, F f, G g);

  default Function6<B, C, D, E, F, G, Z> apply(A a) {
    return (b, c, d, e, f, g) -> apply(a, b, c, d, e, f, g);
  }

  default Function7<G, F, E, D, C, B, A, Z> reversed() {
    return (g, f, e, d, c, b, a) -> apply(a, b, c, d, e, f, g);
  }

  default Function1<
          A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Z>>>>>>>
      curried() {
    return a -> b -> c -> d -> e -> f -> g -> apply(a, b, c, d, e, f, g);
  }

  default Function1<Tuple7<A, B, C, D, E, F, G>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7());
    };
  }

  default <I> Function7<A, B, C, D, E, F, G, I> andThen(Function1<? super Z, ? extends I> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d, e, f, g) -> after.apply(apply(a, b, c, d, e, f, g));
  }

  default <I> Function7<I, B, C, D, E, F, G, Z> compose(Function1<? super I, ? extends A> before) {
    Objects.requireNonNull(before);
    return (i, b, c, d, e, f, g) -> apply(before.apply(i), b, c, d, e, f, g);
  }
}
