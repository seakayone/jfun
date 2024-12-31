package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.law.PrismLaws;

class BigDecimalOpticsTest {

  @Test
  void testPrismLawsBigDecimalToBigLong() {
    final Prism<BigDecimal, Long> prism = BigDecimalOptics.bigDecimalToLong();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.valueOf(Long.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.valueOf(Long.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Long.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Long.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsBigDecimalToBigInteger() {
    final Prism<BigDecimal, Integer> prism = BigDecimalOptics.bigDecimalToInteger();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.valueOf(Integer.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigDecimal.valueOf(Integer.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MIN_VALUE)).isTrue();
  }
}
