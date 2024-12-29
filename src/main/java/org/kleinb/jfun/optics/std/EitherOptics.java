package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Either;
import org.kleinb.jfun.optics.PartialFunction;
import org.kleinb.jfun.optics.Prism;

public final class EitherOptics {
  private EitherOptics() {}

  static <A, B> Prism<Either<A, B>, A> left() {
    return Prism.of(PartialFunction.of(Either::getLeft, Either::isLeft), Either::left);
  }

  static <A, B> Prism<Either<A, B>, B> right() {
    return Prism.of(PartialFunction.of(Either::get, Either::isRight), Either::right);
  }
}
