package org.kleinb.jfun;

@FunctionalInterface
public interface Function1<B, A> {

  static <A> Function1<A, A> identity() {
    return a -> a;
  }

  static <B, A> Function1<B, A> constant(A a) {
    return _ -> a;
  }

  A apply(B b);

  default Function1<B, Function0<A>> curried() {
    return b -> () -> apply(b);
  }

  default Function1<Tuple1<B>, A> tupled() {
    return t -> apply(t._1());
  }

  default <C> Function1<C, A> compose(Function1<C, B> before) {
    return c -> apply(before.apply(c));
  }

  default <C> Function1<B, C> andThen(Function1<A, C> after) {
    return b -> after.apply(apply(b));
  }
}
