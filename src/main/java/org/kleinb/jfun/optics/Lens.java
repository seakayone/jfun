package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Function2;
import org.kleinb.jfun.Option;

public interface Lens<S, A> extends Optional<S, A> {

  static <S, A> Lens<S, A> of(Function1<S, A> get, Function2<A, S, S> replace) {
    return new Lens<>() {
      @Override
      public A get(S s) {
        return get.apply(s);
      }

      @Override
      public Function1<S, S> replace(A A) {
        return replace.apply(A);
      }
    };
  }

  A get(S s);

  @Override
  default Option<A> getOption(S s) {
    return Option.some(get(s));
  }

  default <B> Lens<S, B> andThen(Lens<A, B> other) {
    final var self = this;
    return new Lens<>() {
      @Override
      public B get(S s) {
        return other.get(self.get(s));
      }

      @Override
      public Function1<S, S> replace(B b) {
        return self.modify(other.replace(b));
      }
    };
  }
}
