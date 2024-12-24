package org.kleinb.jfun;

@FunctionalInterface
public interface Function5<B, C, D, E, F, A> {
  A apply(B b, C c, D d, E e, F f);

  default Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, A>>>>> curried() {
    return b -> c -> d -> e -> f -> apply(b, c, d, e, f);
  }

  default Function1<Tuple5<B, C, D, E, F>, A> tupled() {
    return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5());
  }

  default <G> Function5<B, C, D, E, F, G> andThen(Function1<A, G> after) {
    return (b, c, d, e, f) -> after.apply(apply(b, c, d, e, f));
  }

  default <G> Function5<G, C, D, E, F, A> compose(Function1<G, B> before) {
    return (g, c, d, e, f) -> apply(before.apply(g), c, d, e, f);
  }
}
