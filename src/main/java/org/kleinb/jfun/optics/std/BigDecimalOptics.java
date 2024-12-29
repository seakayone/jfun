package org.kleinb.jfun.optics.std;

import java.math.BigDecimal;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public final class BigDecimalOptics {
  private BigDecimalOptics() {}

  public static Prism<BigDecimal, Long> bigDecimalToLong() {
    return Prism.of(bd -> Try.of(bd::longValue).toOption(), BigDecimal::new);
  }

  public static Prism<BigDecimal, Integer> bigDecimalToInteger() {
    return Prism.of(bd -> Try.of(bd::intValue).toOption(), BigDecimal::new);
  }
}
