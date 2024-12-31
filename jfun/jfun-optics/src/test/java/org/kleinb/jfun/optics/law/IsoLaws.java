package org.kleinb.jfun.optics.law;

import org.kleinb.jfun.optics.Iso;

public interface IsoLaws {
  static <A, B> boolean roundTripOneWay(Iso<A, B> i, A a) {
    return i.reverseGet(i.get(a)).equals(a);
  }

  static <A, B> boolean roundTripOtherWay(Iso<A, B> i, B b) {
    return i.get(i.reverseGet(b)).equals(b);
  }
}
