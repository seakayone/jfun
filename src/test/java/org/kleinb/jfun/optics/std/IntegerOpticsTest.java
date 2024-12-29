package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.Prism;
import org.kleinb.jfun.optics.PrismLaws;

class IntegerOpticsTest {

  @Test
  void testPrismLawsIntegerToCharacter() {
    final Prism<Integer, Character> prism = IntegerOptics.integerToCharacter();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (int) Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (int) Character.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (char) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsIntegerToByte() {
    final Prism<Integer, Byte> prism = IntegerOptics.integerToByte();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (int) Byte.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (int) Byte.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsIntegerToBoolean() {
    final Prism<Integer, Boolean> prism = IntegerOptics.integerToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, 1)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, 2)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }
}
