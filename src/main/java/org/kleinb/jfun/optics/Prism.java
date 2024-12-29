package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.None;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Some;

public interface Prism<S, A> extends Optional<S, A> {

  static <S, A> Prism<S, A> of(PartialFunction<S, A> getOption, Function1<A, S> reverseGet) {
    return Prism.of(getOption.lift(), reverseGet);
  }

  static <S, A> Prism<S, A> of(Function1<S, Option<A>> getOption, Function1<A, S> reverseGet) {
    return new Prism<>() {
      @Override
      public Option<A> getOption(S s) {
        return getOption.apply(s);
      }

      @Override
      public S reverseGet(A a) {
        return reverseGet.apply(a);
      }
    };
  }

  Option<A> getOption(S s);

  S reverseGet(A a);

  default Function1<S, S> replace(A a) {
    return s ->
        switch (getOption(s)) {
          case Some<A> _ -> reverseGet(a);
          case None<A> _ -> s;
        };
  }

  default Function1<S, S> modify(Function1<A, A> f) {
    return s ->
        switch (getOption(s)) {
          case Some(A value) -> replace(f.apply(value)).apply(s);
          case None<A> _ -> s;
        };
  }

  default <B> Prism<S, B> andThen(Prism<A, B> other) {
    var self = this;
    return new Prism<>() {
      @Override
      public Option<B> getOption(S s) {
        return self.getOption(s).flatMap(other::getOption);
      }

      @Override
      public S reverseGet(B b) {
        return self.reverseGet(other.reverseGet(b));
      }
    };
  }
}
