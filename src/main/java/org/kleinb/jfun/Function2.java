package org.kleinb.jfun;

import java.util.Objects;

@FunctionalInterface
public interface Function2<A, B, Z> extends java.util.function.BiFunction<A, B, Z> {

  static <A, B, Z> Function2<A, B, Z> constant(Z value) {
    return (_, _) -> value;
  }

  static <A, B, Z> Function2<A, B, Z> of(Function2<A, B, Z> methodReference) {
    return methodReference;
  }

  static <A, B, Z> Function2<A, B, Z> of(Function1<A, Function1<B, Z>> f) {
    return (a, b) -> f.apply(a).apply(b);
  }

  @Override
  Z apply(A a, B b);

  default Function1<B, Z> apply(A a) {
    return b -> apply(a, b);
  }

  default Function2<B, A, Z> reversed() {
    return (b, a) -> apply(a, b);
  }

  default Function1<A, Function1<B, Z>> curried() {
    return a -> b -> apply(a, b);
  }

  default Function1<Tuple2<A, B>, Z> tupled() {
    return t -> {
      Objects.requireNonNull(t);
      return apply(t._1(), t._2());
    };
  }

  default <D> Function2<A, B, D> andThen(Function1<? super Z, ? extends D> after) {
    Objects.requireNonNull(after);
    return (a, b) -> after.apply(apply(a, b));
  }

  default <D> Function2<D, B, Z> compose(Function1<? super D, ? extends A> before) {
    Objects.requireNonNull(before);
    return (d, b) -> apply(before.apply(d), b);
  }
}
