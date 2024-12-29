package org.kleinb.jfun.optics;

import java.util.function.Predicate;
import org.kleinb.jfun.Either;
import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Function2;
import org.kleinb.jfun.Option;

public interface Optional<S, A> {

  static <S, A> Optional<S, A> of(Function1<S, Option<A>> getOption, Function2<A, S, S> replace) {
    return new Optional<>() {
      @Override
      public Option<A> getOption(S s) {
        return getOption.apply(s);
      }

      @Override
      public Function1<S, S> replace(A a) {
        return replace.apply(a);
      }
    };
  }

  Function1<S, S> replace(A a);

  Option<A> getOption(S s);

  default Either<S, A> getOrModify(S s) {
    return getOption(s).toRight(() -> s);
  }

  default Function1<S, S> modify(Function1<A, A> f) {
    return s -> getOrModify(s).fold(Function1.identity(), a -> replace(f.apply(a)).apply(s));
  }

  default Function1<S, Option<S>> modifyOption(Function1<A, A> f) {
    return b -> getOption(b).map(a -> replace(f.apply(a)).apply(b));
  }

  default Function1<S, Option<S>> replaceOption(A a) {
    return modifyOption(_ -> a);
  }

  default Boolean isEmpty(S s) {
    return getOption(s).isNone();
  }

  default Boolean nonEmpty(S s) {
    return getOption(s).isSome();
  }

  default Function1<S, Option<A>> find(Predicate<A> p) {
    return s -> getOption(s).filter(p);
  }

  default Predicate<S> exist(Predicate<A> p) {
    return s -> getOption(s).filter(p).isSome();
  }

  default <B> Optional<S, B> andThen(Optional<A, B> other) {
    final var self = this;
    return Optional.of(
        s -> self.getOption(s).flatMap(other::getOption),
        Function2.of(b -> self.modify(other.replace(b))));
  }
}
