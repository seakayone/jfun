package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public final class DoubleOptics {

  private DoubleOptics() {}

  public static Prism<Double, Integer> doubleIntegerPrism() {
    return Prism.of(d -> Try.of(d::intValue).toOption(), Integer::doubleValue);
  }
}
