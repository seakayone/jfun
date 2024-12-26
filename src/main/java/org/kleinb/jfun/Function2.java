package org.kleinb.jfun;

@FunctionalInterface
public interface Function2<A, B, Z> extends java.util.function.BiFunction<A, B, Z> {

  static <A, B, Z> Function2<A, B, Z> constant(Z value) {
    return (_, _) -> value;
  }

  @Override
  Z apply(A a, B b);

  default Function2<B, A, Z> reversed() {
    return (b, a) -> apply(a, b);
  }

  default Function1<A, Function1<B, Z>> curried() {
    return a -> b -> apply(a, b);
  }

  default Function1<Tuple2<A, B>, Z> tupled() {
    return t -> apply(t._1(), t._2());
  }

  default <D> Function2<A, B, D> andThen(Function1<? super Z, ? extends D> after) {
    return (a, b) -> after.apply(apply(a, b));
  }

  default <D> Function2<D, B, Z> compose(Function1<? super D, ? extends A> before) {
    return (d, b) -> apply(before.apply(d), b);
  }
}
