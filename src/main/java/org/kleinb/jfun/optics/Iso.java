package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function0;
import org.kleinb.jfun.Function1;

public interface Iso<A, B> extends Lens<A, B> {

  static <A> Iso<A, A> identity() {
    return of(Function1.identity(), Function1.identity());
  }

  static <A> Iso<A, Void> unit(Function0<A> f) {
    return of(_ -> null, _ -> f.get());
  }

  static <A, B> Iso<A, B> of(Function1<A, B> get, Function1<B, A> reverseGet) {
    return new Iso<>() {
      @Override
      public B get(A a) {
        return get.apply(a);
      }

      @Override
      public A reverseGet(B b) {
        return reverseGet.apply(b);
      }
    };
  }

  B get(A a);

  A reverseGet(B b);

  @Override
  default Function1<A, A> replace(B b) {
    return _ -> reverseGet(b);
  }

  default Lens<A, B> asLens() {
    return this;
  }
}
