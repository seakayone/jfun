package org.kleinb.jfun;

import java.util.Objects;

@FunctionalInterface
public interface Function5<A, B, C, D, E, Z> {

  static <A, B, C, D, E, Z> Function5<A, B, C, D, E, Z> constant(Z value) {
    return (_, _, _, _, _) -> value;
  }

  Z apply(A a, B b, C c, D d, E e);

  default Function4<B, C, D, E, Z> apply(A a) {
    return (b, c, d, e) -> apply(a, b, c, d, e);
  }

  default Function5<E, D, C, B, A, Z> reversed() {
    return (e, d, c, b, a) -> apply(a, b, c, d, e);
  }

  default Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Z>>>>> curried() {
    return a -> b -> c -> d -> e -> apply(a, b, c, d, e);
  }

  default Function1<Tuple5<A, B, C, D, E>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2(), t._3(), t._4(), t._5());
    };
  }

  default <G> Function5<A, B, C, D, E, G> andThen(Function1<Z, G> after) {
    Objects.requireNonNull(after);
    return (a, b, c, d, e) -> after.apply(apply(a, b, c, d, e));
  }

  default <G> Function5<G, B, C, D, E, Z> compose(Function1<G, A> before) {
    Objects.requireNonNull(before);
    return (g, b, c, d, e) -> apply(before.apply(g), b, c, d, e);
  }
}
