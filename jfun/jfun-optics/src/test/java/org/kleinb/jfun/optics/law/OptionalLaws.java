package org.kleinb.jfun.optics.law;

import org.kleinb.jfun.Function1;
import org.kleinb.jfun.optics.Optional;

public interface OptionalLaws {

  static <S, A> Boolean getOptionSet(S s, Optional<S, A> optional) {
    return optional
        .getOrModify(s)
        .fold(Function1.identity(), a -> optional.replace(a).apply(s))
        .equals(s);
  }

  static <S, A> Boolean setGetOption(S s, A a, Optional<S, A> optional) {
    return optional
        .getOption(optional.replace(a).apply(s))
        .equals(optional.getOption(s).map(__ -> a));
  }
}
