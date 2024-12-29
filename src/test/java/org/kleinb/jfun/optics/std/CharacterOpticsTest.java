package org.kleinb.jfun.optics.std;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.kleinb.jfun.optics.PrismLaws;

class CharacterOpticsTest {

  @Test
  void testPrismLawsCharToBoolean() {
    var prism = CharacterOptics.charToBoolean();

    assertThat(PrismLaws.partialRoundTripOneWay(prism, (char) 0)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, (char) 1)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, Character.MAX_VALUE)).isTrue();
    assertThat(PrismLaws.partialRoundTripOneWay(prism, Character.MIN_VALUE)).isTrue();

    assertThat(PrismLaws.partialRoundTripOtherWay(prism, true)).isTrue();
    assertThat(PrismLaws.partialRoundTripOtherWay(prism, false)).isTrue();
  }
}
