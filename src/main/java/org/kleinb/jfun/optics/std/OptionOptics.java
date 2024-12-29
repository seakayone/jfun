package org.kleinb.jfun.optics.std;

import java.util.Objects;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.PartialFunction;
import org.kleinb.jfun.optics.Iso;
import org.kleinb.jfun.optics.Prism;

public final class OptionOptics {
  private OptionOptics() {}

  static <A> Prism<Option<A>, A> some() {
    return Prism.of(PartialFunction.of(Option::isSome, Option::get), Option::some);
  }

  static <A> Prism<Option<A>, Void> none() {
    return Prism.of(PartialFunction.of(Option::isNone, _ -> null), Option::none);
  }

  static <A> Iso<Option<A>, A> withDefault(A defaultValue) {
    return Iso.of(
        option -> option.getOrElse(defaultValue),
        value -> Objects.equals(value, defaultValue) ? Option.none() : Option.some(value));
  }
}
