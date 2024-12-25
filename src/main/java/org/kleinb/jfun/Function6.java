package org.kleinb.jfun;

@FunctionalInterface
public interface Function6<A, B, C, D, E, F, Z> {

  static <A, B, C, D, E, F, Z> Function6<A, B, C, D, E, F, Z> constant(Z value) {
    return (_, _, _, _, _, _) -> value;
  }

  Z apply(A a, B b, C c, D d, E e, F f);

  default Function1<A, Function1<B, Function1<C, Function1<D, Function1<E, Function1<F, Z>>>>>>
      curried() {
    return a -> b -> c -> d -> e -> f -> apply(a, b, c, d, e, f);
  }

  default Function1<Tuple6<A, B, C, D, E, F>, Z> tupled() {
    return t -> apply(t._1(), t._2(), t._3(), t._4(), t._5(), t._6());
  }

  default <H> Function6<A, B, C, D, E, F, H> andThen(Function1<? super Z, ? extends H> after) {
    return (a, b, c, d, e, f) -> after.apply(apply(a, b, c, d, e, f));
  }

  default <H> Function6<H, B, C, D, E, F, Z> compose(Function1<? super H, ? extends A> before) {
    return (h, b, c, d, e, f) -> apply(before.apply(h), b, c, d, e, f);
  }
}
