package org.kleinb.jfun.optics;

import java.util.function.Predicate;
import org.kleinb.jfun.Either;
import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Function2;
import org.kleinb.jfun.Option;

public interface Lens<WHOLE, PART> {

  static <WHOLE, PART> Lens<WHOLE, PART> of(
      Function1<WHOLE, PART> get, Function2<PART, WHOLE, WHOLE> replace) {
    return new Lens<>() {
      @Override
      public PART get(WHOLE PART) {
        return get.apply(PART);
      }

      @Override
      public Function1<WHOLE, WHOLE> replace(PART PART) {
        return replace.apply(PART);
      }
    };
  }

  PART get(WHOLE whole);

  Function1<WHOLE, WHOLE> replace(PART b);

  default Function1<WHOLE, WHOLE> modify(Function1<PART, PART> f) {
    return whole -> replace(f.apply(get(whole))).apply(whole);
  }

  default Either<WHOLE, PART> getOrModify(WHOLE whole) {
    return Either.right(get(whole));
  }

  default Option<PART> getOption(WHOLE whole) {
    return Option.some(get(whole));
  }

  default Function1<WHOLE, Option<PART>> find(Predicate<PART> p) {
    return whole -> Option.some(get(whole)).filter(p);
  }

  default Predicate<WHOLE> exists(Predicate<PART> p) {
    return (Function1.of(this::get)).andThen(Function1.of(p::test))::apply;
  }

  default <PART2> Lens<WHOLE, PART2> andThen(Lens<PART, PART2> other) {
    final var self = this;
    return new Lens<>() {
      @Override
      public PART2 get(WHOLE whole) {
        return other.get(self.get(whole));
      }

      @Override
      public Function1<WHOLE, WHOLE> replace(PART2 part2) {
        return self.modify(other.replace(part2));
      }
    };
  }
}
