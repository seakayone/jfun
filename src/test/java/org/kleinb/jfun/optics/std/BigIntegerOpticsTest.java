package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.law.PrismLaws;

class BigIntegerOpticsTest {

  @Test
  void testPrismLawsBigIntegerToLong() {
    var prism = BigIntegerOptics.bigIntegerToLong();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Long.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Long.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Long.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0L)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Long.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsBigIntegerToInteger() {
    var prism = BigIntegerOptics.bigIntegerToInteger();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Integer.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Integer.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Integer.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsBigIntegerToCharacter() {
    var prism = BigIntegerOptics.bigIntegerToCharacter();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Character.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Character.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (char) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Character.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsBigIntegerToByte() {
    var prism = BigIntegerOptics.bigIntegerToByte();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Byte.MAX_VALUE)))
        .isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(Byte.MIN_VALUE)))
        .isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, (byte) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, Byte.MIN_VALUE)).isTrue();
  }

  @Test
  void testPrismLawsBigIntegerToBoolean() {
    var prism = BigIntegerOptics.bigIntegerToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ONE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.ZERO)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, BigInteger.valueOf(2L))).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }
}
