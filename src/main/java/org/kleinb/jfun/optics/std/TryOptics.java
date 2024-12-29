package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.PartialFunction;
import org.kleinb.jfun.optics.Prism;

public interface TryOptics {
  static <A> Prism<Try<A>, A> success() {
    return Prism.of(PartialFunction.of(Try::get, Try::isSuccess), Try::success);
  }

  static <A> Prism<Try<A>, Throwable> failure() {
    return Prism.of(PartialFunction.of(Try::getFailure, Try::isFailure), Try::failure);
  }
}
