package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function0;
import org.kleinb.jfun.Function1;

public interface Iso<S, A> extends Lens<S, A>, Optional<S, A> {

  static <S> Iso<S, S> identity() {
    return of(Function1.identity(), Function1.identity());
  }

  static <S> Iso<S, Void> unit(Function0<S> f) {
    return of(_ -> null, _ -> f.get());
  }

  static <S, A> Iso<S, A> of(Function1<S, A> get, Function1<A, S> reverseGet) {
    return new Iso<>() {
      @Override
      public A get(S s) {
        return get.apply(s);
      }

      @Override
      public S reverseGet(A a) {
        return reverseGet.apply(a);
      }
    };
  }

  A get(S s);

  S reverseGet(A a);

  default Iso<A, S> reverse() {
    return of(this::reverseGet, this::get);
  }

  @Override
  default Function1<S, S> replace(A a) {
    return _ -> reverseGet(a);
  }

  default Lens<S, A> asLens() {
    return this;
  }

  default <B> Iso<S, B> andThen(Iso<A, B> other) {
    var self = this;
    return Iso.of(s -> other.get(self.get(s)), b -> self.reverseGet(other.reverseGet(b)));
  }
}
