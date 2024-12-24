package org.kleinb.jfun;

@FunctionalInterface
public interface Function7<B, C, D, E, F, G, H, A> {
  A apply(B b, C c, D d, E e, F f, G g, H h);

  default Function1<
          B, Function1<C, Function1<D, Function1<E, Function1<F, Function1<G, Function1<H, A>>>>>>>
      curried() {
    return b -> c -> d -> e -> f -> g -> h -> apply(b, c, d, e, f, g, h);
  }

  default Function1<Tuple7<B, C, D, E, F, G, H>, A> tupled() {
    return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7());
  }

  default <I> Function7<B, C, D, E, F, G, H, I> andThen(Function1<A, I> after) {
    return (b, c, d, e, f, g, h) -> after.apply(apply(b, c, d, e, f, g, h));
  }

  default <I> Function7<I, C, D, E, F, G, H, A> compose(Function1<I, B> before) {
    return (i, c, d, e, f, g, h) -> apply(before.apply(i), c, d, e, f, g, h);
  }
}
