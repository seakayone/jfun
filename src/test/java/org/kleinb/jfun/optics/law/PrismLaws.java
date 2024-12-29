package org.kleinb.jfun.optics.law;

import org.kleinb.jfun.None;
import org.kleinb.jfun.Option;
import org.kleinb.jfun.Some;
import org.kleinb.jfun.optics.Prism;

public interface PrismLaws {
  static <S, A> boolean partialRoundTripOneWay(Prism<S, A> p, S s) {
    switch (p.getOption(s)) {
      case Some(A value) -> {
        return p.reverseGet(value).equals(s);
      }
      case None<A> _ -> {
        return true;
      }
    }
  }

  static <A, B> boolean partialRoundTripOtherWay(Prism<A, B> p, B b) {
    return p.getOption(p.reverseGet(b)).equals(Option.some(b));
  }
}
