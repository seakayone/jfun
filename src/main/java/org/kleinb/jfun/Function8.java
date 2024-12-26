package org.kleinb.jfun;

@FunctionalInterface
public interface Function8<A, B, C, D, E, F, G, H, Z> {

  static <A, B, C, D, E, F, G, H, Z> Function8<A, B, C, D, E, F, G, H, Z> constant(Z value) {
    return (_, _, _, _, _, _, _, _) -> value;
  }

  Z apply(A a, B b, C c, D d, E e, F f, G g, H h);

  default Function7<B, C, D, E, F, G, H, Z> apply(A a) {
    return (b, c, d, e, f, g, h) -> apply(a, b, c, d, e, f, g, h);
  }

  default Function8<H, G, F, E, D, C, B, A, Z> reversed() {
    return (h, g, f, e, d, c, b, a) -> apply(a, b, c, d, e, f, g, h);
  }

  default Function1<
          A,
          Function1<
              B,
              Function1<
                  C, Function1<D, Function1<E, Function1<F, Function1<G, Function1<H, Z>>>>>>>>
      curried() {
    return a -> b -> c -> d -> e -> f -> g -> h -> apply(a, b, c, d, e, f, g, h);
  }

  default Function1<Tuple8<A, B, C, D, E, F, G, H>, Z> tupled() {
    return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6(), t._7(), t._8());
  }

  default <K> Function8<A, B, C, D, E, F, G, H, K> andThen(
      Function1<? super Z, ? extends K> after) {
    return (a, b, c, d, e, f, g, h) -> after.apply(apply(a, b, c, d, e, f, g, h));
  }

  default <K> Function8<K, B, C, D, E, F, G, H, Z> compose(
      Function1<? super K, ? extends A> before) {
    return (k, b, c, d, e, f, g, h) -> apply(before.apply(k), b, c, d, e, f, g, h);
  }
}
