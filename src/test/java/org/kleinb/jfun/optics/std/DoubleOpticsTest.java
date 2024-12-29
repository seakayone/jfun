package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.PrismLaws;

class DoubleOpticsTest {

  @Test
  void testPrismLaws() {
    var prism = DoubleOptics.doubleIntegerPrism();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (double) Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0.0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (double) Integer.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MIN_VALUE)).isTrue();
  }
}
