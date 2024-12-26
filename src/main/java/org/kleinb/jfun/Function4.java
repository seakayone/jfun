package org.kleinb.jfun;

@FunctionalInterface
public interface Function4<A, B, C, D, Z> {

  static <A, B, C, D, Z> Function4<A, B, C, D, Z> constant(Z value) {
    return (_, _, _, _) -> value;
  }

  Z apply(A a, B b, C c, D d);

  default Function4<D, C, B, A, Z> reversed() {
    return (d, c, b, a) -> apply(a, b, c, d);
  }

  default Function1<A, Function1<B, Function1<C, Function1<D, Z>>>> curried() {
    return a -> b -> c -> d -> apply(a, b, c, d);
  }

  default Function1<Tuple4<A, B, C, D>, Z> tupled() {
    return t -> apply(t._1(), t._2(), t._3(), t._4());
  }

  default <F> Function4<A, B, C, D, F> andThen(Function1<? super Z, ? extends F> after) {
    return (a, b, c, d) -> after.apply(apply(a, b, c, d));
  }

  default <F> Function4<F, B, C, D, Z> compose(Function1<? super F, ? extends A> before) {
    return (f, b, c, d) -> apply(before.apply(f), b, c, d);
  }
}
