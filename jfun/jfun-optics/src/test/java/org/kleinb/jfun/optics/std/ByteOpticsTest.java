package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.law.PrismLaws;

class ByteOpticsTest {
  @Test
  void testPrismLawsByteToBoolean() {
    var prism = ByteOptics.byteToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (byte) 1)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (byte) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (byte) 2)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }
}
