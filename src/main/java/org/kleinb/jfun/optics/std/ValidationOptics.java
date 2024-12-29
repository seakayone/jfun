package org.kleinb.jfun.optics.std;

import java.util.List;
import org.kleinb.jfun.Validation;
import org.kleinb.jfun.optics.PartialFunction;
import org.kleinb.jfun.optics.Prism;

public final class ValidationOptics {
  private ValidationOptics() {}

  static <E, A> Prism<Validation<E, A>, List<E>> invalid() {
    return Prism.of(
        PartialFunction.of(Validation::getError, Validation::isInvalid), Validation::invalid);
  }

  static <E, A> Prism<Validation<E, A>, A> valid() {
    return Prism.of(PartialFunction.of(Validation::get, Validation::isValid), Validation::valid);
  }
}
