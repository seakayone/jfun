package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.Function2;
import org.kleinb.jfun.Option;

public interface Lens<WHOLE, PART> extends Optional<WHOLE, PART> {

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

  @Override
  default Option<PART> getOption(WHOLE whole) {
    return Option.some(get(whole));
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
