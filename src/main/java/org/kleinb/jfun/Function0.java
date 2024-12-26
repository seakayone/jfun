package org.kleinb.jfun;

import java.util.function.Supplier;

@FunctionalInterface
public interface Function0<Z> extends Supplier<Z> {

  static <Z> Function0<Z> constant(Z value) {
    return () -> value;
  }

  Z apply();

  @Override
  default Z get() {
    return apply();
  }

  default Function0<Z> reversed() {
    return this;
  }

  default Function0<Z> curried() {
    return this;
  }

  default Function1<Tuple0, Z> tupled() {
    return _ -> apply();
  }

  default <B> Function0<B> andThen(Function1<? super Z, ? extends B> after) {
    return () -> after.apply(apply());
  }
}
