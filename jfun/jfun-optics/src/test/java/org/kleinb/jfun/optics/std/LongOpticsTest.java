package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.law.PrismLaws;

class LongOpticsTest {

  @Test
  void testPrismLawsLongToInteger() {
    final Prism<Long, Integer> prism = LongOptics.longToInteger();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Integer.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsLongToCharacter() {
    final Prism<Long, Character> prism = LongOptics.longToCharacter();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Character.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (char) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsLongToByte() {
    final Prism<Long, Byte> prism = LongOptics.longToByte();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Byte.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (long) Byte.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsLongToBoolean() {
    final Prism<Long, Boolean> prism = LongOptics.longToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, 1L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 2L)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }
}
