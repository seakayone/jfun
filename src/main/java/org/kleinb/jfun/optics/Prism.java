package org.kleinb.jfun.optics;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.None;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Some;

public interface Prism<WHOLE, PART> {

  static <WHOLE, PART> Prism<WHOLE, PART> of(
      PartialFunction<WHOLE, PART> getOption, Function1<PART, WHOLE> reverseGet) {
    return Prism.of(getOption.lift(), reverseGet);
  }

  static <WHOLE, PART> Prism<WHOLE, PART> of(
      Function1<WHOLE, Option<PART>> getOption, Function1<PART, WHOLE> reverseGet) {
    return new Prism<>() {
      @Override
      public Option<PART> getOption(WHOLE whole) {
        return getOption.apply(whole);
      }

      @Override
      public WHOLE reverseGet(PART part) {
        return reverseGet.apply(part);
      }
    };
  }

  Option<PART> getOption(WHOLE whole);

  WHOLE reverseGet(PART part);

  default Function1<WHOLE, WHOLE> replace(PART part) {
    return whole ->
        switch (getOption(whole)) {
          case Some<PART> _ -> reverseGet(part);
          case None<PART> _ -> whole;
        };
  }

  default Function1<WHOLE, WHOLE> modify(Function1<PART, PART> f) {
    return whole ->
        switch (getOption(whole)) {
          case Some(PART value) -> replace(f.apply(value)).apply(whole);
          case None<PART> _ -> whole;
        };
  }

  default Function1<WHOLE, Option<WHOLE>> modifyOption(Function1<PART, PART> f) {
    return whole -> getOption(whole).map(p -> replace(f.apply(p)).apply(whole));
  }

  default Function1<WHOLE, Option<WHOLE>> replaceOption(PART part) {
    return modifyOption(_ -> part);
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
