package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Try;
import org.kleinb.jfun.PartialFunction;
import org.kleinb.jfun.optics.Prism;

public final class TryOptics {
  private TryOptics() {}

  static <A> Prism<Try<A>, A> success() {
    return Prism.of(PartialFunction.of(Try::get, Try::isSuccess), Try::success);
  }

  static <A> Prism<Try<A>, Throwable> failure() {
    return Prism.of(PartialFunction.of(Try::getFailure, Try::isFailure), Try::failure);
  }
}
