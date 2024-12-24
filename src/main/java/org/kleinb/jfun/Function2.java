package org.kleinb.jfun;

@FunctionalInterface
public interface Function2<B, C, A> {

  A apply(B b, C c);

  default Function1<B, Function1<C, A>> curried() {
    return b -> c -> apply(b, c);
  }

  default Function1<Tuple2<B, C>, A> tupled() {
    return t -> apply(t._1(), t._2());
  }

  default <D> Function2<B, C, D> andThen(Function1<A, D> after) {
    return (b, c) -> after.apply(apply(b, c));
  }

  default <D> Function2<D, C, A> compose(Function1<D, B> before) {
    return (d, c) -> apply(before.apply(d), c);
  }
}
