package org.kleinb.jfun.optics.law;

import org.kleinb.jfun.optics.Lens;

public interface LensLaws {
  static <S, A> boolean getReplace(Lens<S, A> l, S s) {
    return l.replace(l.get(s)).apply(s).equals(s);
  }

  static <S, A> boolean replaceGet(Lens<S, A> l, S s, A a) {
    return l.get(l.replace(a).apply(s)).equals(a);
  }
}
