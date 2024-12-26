package org.kleinb.jfun;

@FunctionalInterface
public interface Function3<A, B, C, Z> {

  static <A, B, C, Z> Function3<A, B, C, Z> constant(Z value) {
    return (_, _, _) -> value;
  }

  Z apply(A a, B b, C c);

  default Function3<C, B, A, Z> reversed() {
    return (c, b, a) -> apply(a, b, c);
  }

  default Function1<A, Function1<B, Function1<C, Z>>> curried() {
    return a -> b -> c -> apply(a, b, c);
  }

  default Function1<Tuple3<A, B, C>, Z> tupled() {
    return t -> apply(t._1(), t._2(), t._3());
  }

  default <E> Function3<A, B, C, E> andThen(Function1<? super Z, ? extends E> after) {
    return (a, b, c) -> after.apply(apply(a, b, c));
  }

  default <E> Function3<E, B, C, Z> compose(Function1<? super E, ? extends A> before) {
    return (e, b, c) -> apply(before.apply(e), b, c);
  }
}
