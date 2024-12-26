package org.kleinb.jfun;

@FunctionalInterface
public interface Function1<A, Z> extends java.util.function.Function<A, Z> {

  static <A> Function1<A, A> identity() {
    return a -> a;
  }

  static <A, C> Function1<A, C> constant(C c) {
    return _ -> c;
  }

  @Override
  Z apply(A b);

  default Function1<A, Z> reversed() {
    return this;
  }

  default Function1<A, Z> curried() {
    return this;
  }

  default Function1<Tuple1<A>, Z> tupled() {
    return t -> apply(t._1());
  }

  default <C> Function1<C, Z> compose(Function1<? super C, ? extends A> before) {
    return c -> apply(before.apply(c));
  }

  default <C> Function1<A, C> andThen(Function1<? super Z, ? extends C> after) {
    return b -> after.apply(apply(b));
  }
}
