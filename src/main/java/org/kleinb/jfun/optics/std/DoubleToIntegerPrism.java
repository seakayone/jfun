package org.kleinb.jfun.optics.std;

import org.kleinb.jfun.Option;
import org.kleinb.jfun.Try;
import org.kleinb.jfun.optics.Prism;

public class DoubleToIntegerPrism implements Prism<Double, Integer> {

  private DoubleToIntegerPrism() {}

  public static final DoubleToIntegerPrism INSTANCE = new DoubleToIntegerPrism();

  public static DoubleToIntegerPrism get() {
    return INSTANCE;
  }

  @Override
  public Option<Integer> getOption(Double d) {
    return Try.of(d::intValue).toOption();
  }

  @Override
  public Double reverseGet(Integer i) {
    return i.doubleValue();
  }
}
