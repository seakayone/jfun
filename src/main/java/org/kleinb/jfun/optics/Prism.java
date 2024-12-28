package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.None;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Some;

public interface Prism<WHOLE, PART> {

  static <WHOLE, PART> Prism<WHOLE, PART> of(
      PartialFunction<WHOLE, PART> getOption, Function1<PART, WHOLE> reverseGet) {
    return new Prism<>() {
      @Override
      public Option<PART> getOption(WHOLE whole) {
        return getOption.lift().apply(whole);
      }

      @Override
      public WHOLE reverseGet(PART part) {
        return reverseGet.apply(part);
      }
    };
  }

  Option<PART> getOption(WHOLE whole);

  WHOLE reverseGet(PART part);

  default WHOLE replace(WHOLE whole, PART part) {
    return switch (getOption(whole)) {
      case Some<PART> _ -> reverseGet(part);
      case None<PART> _ -> whole;
    };
  }

  default WHOLE modify(Function1<PART, PART> f, WHOLE whole) {
    return switch (getOption(whole)) {
      case Some(PART value) -> replace(whole, f.apply(value));
      case None<PART> _ -> whole;
    };
  }

  default <NEW_PART> Prism<WHOLE, NEW_PART> andThen(Prism<PART, NEW_PART> other) {
    var self = this;
    return new Prism<>() {
      @Override
      public Option<NEW_PART> getOption(WHOLE whole) {
        return self.getOption(whole).flatMap(other::getOption);
      }

      @Override
      public WHOLE reverseGet(NEW_PART newPart) {
        return self.reverseGet(other.reverseGet(newPart));
      }
    };
  }
}
